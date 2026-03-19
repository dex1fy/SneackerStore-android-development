/**
 * Этот файл относится к слою presentation.
 *
 * Он помогает экрану отображать данные, обрабатывать действия пользователя или синхронизировать состояние.
 */
package com.example.sneakerstoreapp.presentation.sidemenu

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.sneakerstoreapp.R
import com.example.sneakerstoreapp.domain.model.ProfileData
import com.example.sneakerstoreapp.ui.theme.SneakerBlue

@Composable
fun SideMenuDrawer(
    onDismiss: () -> Unit,
    onProfileClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SideMenuViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.22f))
            .pointerInput(Unit) {
                detectTapGestures(onTap = { onDismiss() })
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.86f)
                .clip(RoundedCornerShape(topEnd = 28.dp, bottomEnd = 28.dp))
                .background(SneakerBlue)
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 18.dp)
                .pointerInput(Unit) {}
        ) {
            SideMenuProfileHeader(profile = uiState.profile)

            Spacer(modifier = Modifier.height(28.dp))

            SideMenuItem(R.drawable.ic_nav_profile, "Профиль", onProfileClick)
            SideMenuItem(R.drawable.ic_bag_outline, "Корзина", onDismiss)
            SideMenuItem(R.drawable.ic_nav_heart, "Избранное", onFavoriteClick)
            SideMenuItem(R.drawable.ic_side_orders, "Заказы", onDismiss)
            SideMenuItem(
                iconRes = R.drawable.ic_nav_bell,
                title = "Уведомления",
                onClick = onDismiss,
                showNotificationDot = true
            )
            SideMenuItem(R.drawable.ic_side_settings, "Настройки", onDismiss)

            Spacer(modifier = Modifier.height(22.dp))

            HorizontalDivider(
                thickness = 1.dp,
                color = Color.White.copy(alpha = 0.22f)
            )

            Spacer(modifier = Modifier.height(22.dp))

            SideMenuItem(R.drawable.ic_side_logout, "Выйти", onLogoutClick)
        }
    }
}

@Composable
private fun SideMenuProfileHeader(profile: ProfileData?) {
    Column {
        SideMenuPhoto(photoUrl = profile?.photoUrl)

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = buildSideMenuName(profile),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}

@Composable
private fun SideMenuPhoto(photoUrl: String?) {
    Box(
        modifier = Modifier
            .size(88.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.18f)),
        contentAlignment = Alignment.Center
    ) {
        if (photoUrl.isNullOrBlank()) {
            Canvas(modifier = Modifier.size(78.dp)) {
                drawCircle(
                    color = Color.White.copy(alpha = 0.25f),
                    style = Stroke(width = 5f)
                )
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
private fun SideMenuItem(
    iconRes: Int,
    title: String,
    onClick: () -> Unit,
    showNotificationDot: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )

                if (showNotificationDot) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFF6B6B))
                    )
                }
            }

            Spacer(modifier = Modifier.width(24.dp))

            Text(
                text = title,
                fontSize = 17.sp,
                color = Color.White
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_side_chevron),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(width = 8.dp, height = 14.dp)
        )
    }
}

private fun buildSideMenuName(profile: ProfileData?): String {
    return listOf(profile?.firstName.orEmpty(), profile?.lastName.orEmpty())
        .filter { it.isNotBlank() }
        .joinToString(" ")
        .ifBlank { "Профиль" }
}
