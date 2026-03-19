/**
 * Этот файл описывает Compose-экран слоя presentation.
 *
 * Здесь собирается интерфейс, отображается состояние экрана и обрабатываются действия пользователя.
 */
package com.example.sneakerstoreapp.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.sneakerstoreapp.R
import com.example.sneakerstoreapp.domain.model.HomeCategory
import com.example.sneakerstoreapp.domain.model.HomeProduct
import com.example.sneakerstoreapp.presentation.sidemenu.SideMenuDrawer
import com.example.sneakerstoreapp.ui.theme.InputBackground
import com.example.sneakerstoreapp.ui.theme.InputText
import com.example.sneakerstoreapp.ui.theme.SecondaryText
import com.example.sneakerstoreapp.ui.theme.SneakerBlue
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onSearchClick: () -> Unit = {},
    onCategoryClick: (String?) -> Unit = {},
    onShowAllPopularClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isMenuVisible by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(InputBackground)
    ) {
        when {
            // Во время загрузки показываем только индикатор.
            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = SneakerBlue
                )
            }

            // Если есть ошибка, показываем ее вместо контента.
            uiState.message != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.message.orEmpty(),
                        color = InputText,
                        fontSize = 16.sp
                    )
                }
            }

            else -> {
                // В нормальном состоянии рисуем основной контент главного экрана.
                HomeContent(
                    categories = uiState.categories,
                    products = uiState.products,
                    onMenuClick = { isMenuVisible = true },
                    onSearchClick = onSearchClick,
                    onCategoryClick = onCategoryClick,
                    onShowAllPopularClick = onShowAllPopularClick,
                    onFavoriteClick = onFavoriteClick,
                    onProfileClick = onProfileClick,
                    onToggleFavorite = viewModel::toggleFavorite
                )
            }
        }

        if (isMenuVisible) {
            // Сайд-меню рисуем поверх экрана как отдельный слой.
            SideMenuDrawer(
                onDismiss = { isMenuVisible = false },
                onProfileClick = {
                    isMenuVisible = false
                    onProfileClick()
                },
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
private fun HomeContent(
    categories: List<HomeCategory>,
    products: List<HomeProduct>,
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit,
    onCategoryClick: (String?) -> Unit,
    onShowAllPopularClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onProfileClick: () -> Unit,
    onToggleFavorite: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                // Главный экран можно прокручивать, если контент не помещается по высоте.
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 16.dp, bottom = 118.dp)
        ) {
            HomeTopBar(onMenuClick = onMenuClick)

            Spacer(modifier = Modifier.height(26.dp))

            SearchAndFilterRow(onSearchClick = onSearchClick)

            Spacer(modifier = Modifier.height(24.dp))

            HomeSectionTitle(title = "Категории")

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                // Категории располагаются горизонтально и могут прокручиваться.
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CategoryChip(
                    title = "Все",
                    isSelected = true,
                    onClick = { onCategoryClick(null) }
                )

                categories.forEach { category ->
                    CategoryChip(
                        title = category.title,
                        isSelected = false,
                        onClick = { onCategoryClick(category.id) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            HomeSectionHeader(
                title = "Популярное",
                onAllClick = onShowAllPopularClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                // Популярные товары на главной показываем в горизонтальном списке.
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                products.take(10).forEachIndexed { index, product ->
                    ProductCard(
                        product = product,
                        showBagIcon = index % 2 == 1,
                        onFavoriteClick = { onToggleFavorite(product.id) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            HomeSectionHeader(title = "Акции")

            Spacer(modifier = Modifier.height(16.dp))

            PromotionCard()
        }

        HomeBottomBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 18.dp),
            onFavoriteClick = onFavoriteClick,
            onProfileClick = onProfileClick
        )
    }
}

@Composable
private fun HomeTopBar(onMenuClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Главная",
            fontSize = 30.sp,
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
                    .size(width = 27.dp, height = 20.dp)
                    .clickable(onClick = onMenuClick)
            )

            Box {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_bag_outline),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Icon(
                    painter = painterResource(id = R.drawable.ic_notification_dot),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 1.dp, y = 1.dp)
                        .size(8.dp),
                    tint = Color.Unspecified
                )
            }
        }
    }
}

@Composable
private fun SearchAndFilterRow(onSearchClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier
                .weight(1f)
                .height(52.dp)
                .clickable(onClick = onSearchClick),
            shape = RoundedCornerShape(14.dp),
            color = Color.White,
            shadowElevation = 10.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Поиск",
                    fontSize = 14.sp,
                    color = SecondaryText
                )
            }
        }

        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(SneakerBlue),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_sliders),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun HomeSectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = InputText
    )
}

@Composable
private fun HomeSectionHeader(
    title: String,
    onAllClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HomeSectionTitle(title = title)

        Text(
            text = "Все",
            modifier = if (onAllClick != null) Modifier.clickable(onClick = onAllClick) else Modifier,
            fontSize = 14.sp,
            color = SneakerBlue
        )
    }
}

@Composable
private fun CategoryChip(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(108.dp)
            .height(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
            color = InputText
        )
    }
}

@Composable
private fun ProductCard(
    product: HomeProduct,
    showBagIcon: Boolean,
    onFavoriteClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(164.dp)
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
                .height(102.dp)
                .padding(top = 8.dp, end = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp),
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
            fontSize = 18.sp,
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
                if (showBagIcon) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_bag_small),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(12.dp)
                    )
                } else {
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
}

@Composable
private fun PromotionCard() {
    Image(
        painter = painterResource(id = R.drawable.promo_banner),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        contentScale = ContentScale.FillWidth
    )
}

@Composable
private fun HomeBottomBar(
    modifier: Modifier = Modifier,
    onFavoriteClick: () -> Unit,
    onProfileClick: () -> Unit
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
            BottomNavIcon(
                icon = R.drawable.ic_nav_home,
                selected = true
            )
            BottomNavIcon(
                icon = R.drawable.ic_nav_heart,
                onClick = onFavoriteClick
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

            BottomNavIcon(icon = R.drawable.ic_nav_bell)
            BottomNavIcon(
                icon = R.drawable.ic_nav_profile,
                onClick = onProfileClick
            )
        }
    }
}

@Composable
private fun BottomNavIcon(
    icon: Int,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Icon(
        painter = painterResource(id = icon),
        contentDescription = null,
        tint = if (selected) SneakerBlue else SecondaryText,
        modifier = Modifier
            .size(22.dp)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
    )
}

private fun formatPrice(price: Double): String {
    return NumberFormat
        .getCurrencyInstance(Locale.forLanguageTag("ru-RU"))
        .format(price)
}
