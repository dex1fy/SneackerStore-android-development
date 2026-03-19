/**
 * Этот файл описывает Compose-экран слоя presentation.
 *
 * Здесь собирается интерфейс, отображается состояние экрана и обрабатываются действия пользователя.
 */
package com.example.sneakerstoreapp.presentation.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.sneakerstoreapp.R
import com.example.sneakerstoreapp.domain.model.HomeCategory
import com.example.sneakerstoreapp.domain.model.HomeProduct
import com.example.sneakerstoreapp.presentation.auth.BackButton
import com.example.sneakerstoreapp.ui.theme.InputHint
import com.example.sneakerstoreapp.ui.theme.InputBackground
import com.example.sneakerstoreapp.ui.theme.InputText
import com.example.sneakerstoreapp.ui.theme.SecondaryText
import com.example.sneakerstoreapp.ui.theme.SneakerBlue
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CatalogScreen(
    selectedCategoryId: String?,
    initialQuery: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CatalogViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val products = viewModel.filteredProducts()

    LaunchedEffect(selectedCategoryId, initialQuery) {
        viewModel.loadData(
            selectedCategoryId = selectedCategoryId,
            initialQuery = initialQuery
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(InputBackground)
    ) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = SneakerBlue
                )
            }

            uiState.message != null -> {
                Text(
                    text = uiState.message.orEmpty(),
                    modifier = Modifier.align(Alignment.Center),
                    color = InputText,
                    fontSize = 16.sp
                )
            }

            else -> {
                CatalogContent(
                    title = uiState.title,
                    categories = uiState.categories,
                    selectedCategoryId = uiState.selectedCategoryId,
                    searchQuery = uiState.searchQuery,
                    products = products,
                    onBackClick = onBackClick,
                    onCategoryClick = viewModel::onCategoryClick,
                    onSearchQueryChange = viewModel::onSearchQueryChange,
                    onToggleFavorite = viewModel::toggleFavorite
                )
            }
        }
    }
}

@Composable
private fun CatalogContent(
    title: String,
    categories: List<HomeCategory>,
    selectedCategoryId: String?,
    searchQuery: String,
    products: List<HomeProduct>,
    onBackClick: () -> Unit,
    onCategoryClick: (String?) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onToggleFavorite: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.width(44.dp)) {
                BackButton(onClick = onBackClick)
            }

            Text(
                text = title,
                modifier = Modifier.weight(1f),
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = InputText
            )

            Spacer(modifier = Modifier.width(44.dp))
        }

        if (selectedCategoryId == null) {
            Spacer(modifier = Modifier.height(20.dp))

            SearchField(
                query = searchQuery,
                onQueryChange = onSearchQueryChange
            )

            Spacer(modifier = Modifier.height(24.dp))
        } else {
            Spacer(modifier = Modifier.height(28.dp))
        }

        Text(
            text = "Категории",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = InputText
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CatalogCategoryChip(
                title = "Все",
                isSelected = selectedCategoryId == null,
                onClick = { onCategoryClick(null) }
            )

            categories.forEach { category ->
                CatalogCategoryChip(
                    title = category.title,
                    isSelected = selectedCategoryId == category.id,
                    onClick = { onCategoryClick(category.id) }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(products, key = { it.id }) { product ->
                CatalogProductCard(
                    product = product,
                    onFavoriteClick = { onToggleFavorite(product.id) }
                )
            }
        }
    }
}

@Composable
private fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        shadowElevation = 10.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            TextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    fontSize = 15.sp,
                    color = InputText
                ),
                placeholder = {
                    Text(
                        text = "Поиск",
                        fontSize = 15.sp,
                        color = InputHint
                    )
                },
                singleLine = true,
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    errorContainerColor = Color.White,
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
        }
    }
}

@Composable
private fun CatalogCategoryChip(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(100.dp)
            .height(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) SneakerBlue else Color.White)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = if (isSelected) Color.White else InputText
        )
    }
}

@Composable
private fun CatalogProductCard(
    product: HomeProduct,
    onFavoriteClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(start = 12.dp, top = 12.dp, end = 0.dp, bottom = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(InputBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_nav_heart),
                    contentDescription = null,
                    tint = if (product.isFavorite) Color(0xFFFF6B6B) else SecondaryText,
                    modifier = Modifier
                        .size(14.dp)
                        .clickable(onClick = onFavoriteClick)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(112.dp)
                .padding(top = 8.dp, end = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(104.dp)
                    .aspectRatio(1.65f),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "BEST SELLER",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = SneakerBlue
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = product.title,
            modifier = Modifier.padding(end = 12.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = InputText,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = formatPrice(product.price),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = InputText,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Box(
                modifier = Modifier
                    .size(width = 42.dp, height = 38.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp))
                    .background(SneakerBlue),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+",
                    fontSize = 28.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}

private fun formatPrice(price: Double): String {
    return NumberFormat
        .getCurrencyInstance(Locale.forLanguageTag("ru-RU"))
        .format(price)
}
