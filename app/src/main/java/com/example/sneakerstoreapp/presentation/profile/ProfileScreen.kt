/**
 * Этот файл описывает Compose-экран слоя presentation.
 *
 * Здесь собирается интерфейс, отображается состояние экрана и обрабатываются действия пользователя.
 */
package com.example.sneakerstoreapp.presentation.profile

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.sneakerstoreapp.R
import com.example.sneakerstoreapp.presentation.sidemenu.SideMenuDrawer
import com.example.sneakerstoreapp.ui.theme.InputBackground
import com.example.sneakerstoreapp.ui.theme.InputHint
import com.example.sneakerstoreapp.ui.theme.InputText
import com.example.sneakerstoreapp.ui.theme.SecondaryText
import com.example.sneakerstoreapp.ui.theme.SneakerBlue

@Composable
fun ProfileScreen(
    onHomeClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onBarcodeClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isMenuVisible by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        when {
            // Пока профиль загружается, показываем progress
            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = SneakerBlue
                )
            }

            // Если профиль не найден, показываем текстовое сообщение
            uiState.message != null && uiState.profile == null -> {
                Text(
                    text = uiState.message.orEmpty(),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 24.dp),
                    color = InputText,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }

            uiState.profile != null -> {
                // Когда данные загружены, рисуем полный экран профиля
                ProfileContent(
                    uiState = uiState,
                    onMenuClick = { isMenuVisible = true },
                    onEditClick = viewModel::startEditing,
                    onCancelEditClick = viewModel::cancelEditing,
                    onSaveClick = viewModel::saveProfile,
                    onFirstNameChange = viewModel::onFirstNameChange,
                    onLastNameChange = viewModel::onLastNameChange,
                    onAddressChange = viewModel::onAddressChange,
                    onPhoneChange = viewModel::onPhoneChange,
                    onHomeClick = onHomeClick,
                    onFavoriteClick = onFavoriteClick,
                    onBarcodeClick = onBarcodeClick
                )
            }
        }

        if (isMenuVisible) {
            // Сайд-меню открывается поверх экрана профиля
            SideMenuDrawer(
                onDismiss = { isMenuVisible = false },
                onProfileClick = { isMenuVisible = false },
                onFavoriteClick = {
                    isMenuVisible = false
                    onFavoriteClick()
                },
                onLogoutClick = {
                    isMenuVisible = false
                    onLogoutClick()
                }
            )
        }
    }
}

@Composable
private fun ProfileContent(
    uiState: ProfileUiState,
    onMenuClick: () -> Unit,
    onEditClick: () -> Unit,
    onCancelEditClick: () -> Unit,
    onSaveClick: () -> Unit,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onHomeClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onBarcodeClick: () -> Unit
) {
    val profile = uiState.profile ?: return

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                // Экран можно прокручивать, чтобы все поля точно помещались.
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .padding(bottom = 132.dp)
        ) {
            ProfileTopBar(
                isEditing = uiState.isEditing,
                isSaving = uiState.isSaving,
                onMenuClick = onMenuClick,
                onEditClick = onEditClick,
                onCancelEditClick = onCancelEditClick,
                onSaveClick = onSaveClick
            )

            Spacer(modifier = Modifier.height(26.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfilePhoto(photoUrl = profile.photoUrl)

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = buildProfileName(uiState),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = InputText,
                    textAlign = TextAlign.Center
                )

                if (uiState.isEditing) {
                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Изменить фото профиля",
                        fontSize = 12.sp,
                        color = SneakerBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            BarcodeCard(
                userId = profile.userId,
                onClick = onBarcodeClick
            )

            Spacer(modifier = Modifier.height(22.dp))

            if (uiState.message != null) {
                Text(
                    text = uiState.message,
                    fontSize = 13.sp,
                    color = SneakerBlue
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            ProfileField(
                label = "Имя",
                value = uiState.firstName,
                isEditing = uiState.isEditing,
                onValueChange = onFirstNameChange,
                keyboardType = KeyboardType.Text
            )

            Spacer(modifier = Modifier.height(14.dp))

            ProfileField(
                label = "Фамилия",
                value = uiState.lastName,
                isEditing = uiState.isEditing,
                onValueChange = onLastNameChange,
                keyboardType = KeyboardType.Text
            )

            Spacer(modifier = Modifier.height(14.dp))

            ProfileField(
                label = "Адрес",
                value = uiState.address,
                isEditing = uiState.isEditing,
                onValueChange = onAddressChange,
                keyboardType = KeyboardType.Text
            )

            Spacer(modifier = Modifier.height(14.dp))

            ProfileField(
                label = "Телефон",
                value = uiState.phone,
                isEditing = uiState.isEditing,
                onValueChange = onPhoneChange,
                keyboardType = KeyboardType.Phone
            )
        }

        ProfileBottomBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 18.dp),
            onHomeClick = onHomeClick,
            onFavoriteClick = onFavoriteClick
        )
    }
}

@Composable
private fun ProfileTopBar(
    isEditing: Boolean,
    isSaving: Boolean,
    onMenuClick: () -> Unit,
    onEditClick: () -> Unit,
    onCancelEditClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        if (isEditing) {
            Button(
                onClick = onSaveClick,
                enabled = !isSaving,
                modifier = Modifier
                    .fillMaxWidth(0.64f)
                    .height(40.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SneakerBlue,
                    disabledContainerColor = SneakerBlue.copy(alpha = 0.65f)
                )
            ) {
                Text(
                    text = if (isSaving) "Сохранение..." else "Сохранить",
                    fontSize = 14.sp,
                    color = Color.White
                )
            }

            Text(
                text = "Отмена",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable(onClick = onCancelEditClick),
                fontSize = 13.sp,
                color = SecondaryText
            )
        } else {
            Text(
                text = "Профиль",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = InputText
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_hamburger),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(width = 26.dp, height = 18.dp)
                        .clickable(onClick = onMenuClick)
                )

                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(SneakerBlue)
                        .clickable(onClick = onEditClick),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_profile_edit),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(9.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfilePhoto(photoUrl: String?) {
    Box(
        modifier = Modifier
            .size(96.dp)
            .clip(CircleShape)
            .background(InputBackground),
        contentAlignment = Alignment.Center
    ) {
        if (photoUrl.isNullOrBlank()) {
            Canvas(modifier = Modifier.size(84.dp)) {
                drawCircle(
                    color = SneakerBlue.copy(alpha = 0.16f),
                    style = Stroke(width = 5f)
                )
                drawCircle(color = SneakerBlue.copy(alpha = 0.08f))
            }
        } else {
            AsyncImage(
                model = photoUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun BarcodeCard(
    userId: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(InputBackground)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        VerticalOpenLabel()

        Surface(
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color.White
        ) {
            BarcodeView(
                value = userId,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .padding(horizontal = 10.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun VerticalOpenLabel() {
    Column(
        modifier = Modifier.width(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        listOf("О", "т", "к", "р", "ы", "т", "ь").forEach { letter ->
            Text(
                text = letter,
                fontSize = 10.sp,
                color = SecondaryText,
                lineHeight = 10.sp
            )
        }
    }
}

@Composable
internal fun BarcodeView(
    value: String,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val units = value.map { char ->
            1f + (char.code % 4)
        }
        val gap = 2f
        val totalUnits = units.sum() + gap * (units.size - 1).coerceAtLeast(0)
        val scale = if (totalUnits == 0f) 1f else size.width / totalUnits
        var currentX = 0f

        units.forEachIndexed { index, unit ->
            val width = unit * scale
            val heightRatio = if (index % 3 == 0) 0.92f else 0.78f
            val top = (size.height - (size.height * heightRatio)) / 2f

            drawLine(
                color = Color.Black,
                start = androidx.compose.ui.geometry.Offset(currentX + (width / 2f), top),
                end = androidx.compose.ui.geometry.Offset(currentX + (width / 2f), size.height - top),
                strokeWidth = width,
                cap = StrokeCap.Butt
            )

            currentX += width + (gap * scale)
        }
    }
}

@Composable
private fun ProfileField(
    label: String,
    value: String,
    isEditing: Boolean,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType
) {
    Column {
        Text(
            text = label,
            fontSize = 16.sp,
            color = InputText
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (isEditing) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    color = InputText
                ),
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_profile_field_check),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(width = 12.dp, height = 9.dp)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                shape = RoundedCornerShape(14.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = InputBackground,
                    unfocusedContainerColor = InputBackground,
                    disabledContainerColor = InputBackground,
                    errorContainerColor = InputBackground,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    focusedTextColor = InputText,
                    unfocusedTextColor = InputText,
                    focusedPlaceholderColor = InputHint,
                    unfocusedPlaceholderColor = InputHint
                )
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(InputBackground)
                    .padding(horizontal = 14.dp, vertical = 16.dp)
            ) {
                Text(
                    text = value.ifBlank { "Не указано" },
                    fontSize = 14.sp,
                    color = if (value.isBlank()) InputHint else InputText
                )
            }
        }
    }
}

@Composable
private fun ProfileBottomBar(
    modifier: Modifier = Modifier,
    onHomeClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 18.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_nav_home),
                contentDescription = null,
                tint = InputText.copy(alpha = 0.55f),
                modifier = Modifier
                    .size(22.dp)
                    .clickable(onClick = onHomeClick)
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_nav_heart),
                contentDescription = null,
                tint = InputText.copy(alpha = 0.55f),
                modifier = Modifier
                    .size(22.dp)
                    .clickable(onClick = onFavoriteClick)
            )

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(SneakerBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bag_outline),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }

            Icon(
                painter = painterResource(id = R.drawable.ic_nav_bell),
                contentDescription = null,
                tint = InputText.copy(alpha = 0.55f),
                modifier = Modifier.size(22.dp)
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_nav_profile),
                contentDescription = null,
                tint = SneakerBlue,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

private fun buildProfileName(uiState: ProfileUiState): String {
    val firstName = if (uiState.isEditing) uiState.firstName else uiState.profile?.firstName.orEmpty()
    val lastName = if (uiState.isEditing) uiState.lastName else uiState.profile?.lastName.orEmpty()

    return listOf(firstName, lastName)
        .filter { it.isNotBlank() }
        .joinToString(" ")
        .ifBlank { "Ваш профиль" }
}
