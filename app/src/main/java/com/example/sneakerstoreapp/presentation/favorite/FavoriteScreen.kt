/**
 * Этот файл описывает Compose-экран слоя presentation.
 *
 * Здесь собирается интерфейс, отображается состояние экрана и обрабатываются действия пользователя.
 */
package com.example.sneakerstoreapp.presentation.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.sneakerstoreapp.domain.model.HomeProduct
import com.example.sneakerstoreapp.presentation.auth.BackButton
import com.example.sneakerstoreapp.ui.theme.InputBackground
import com.example.sneakerstoreapp.ui.theme.InputText
import com.example.sneakerstoreapp.ui.theme.SecondaryText
import com.example.sneakerstoreapp.ui.theme.SneakerBlue
import java.text.NumberFormat
import java.util.Locale

@Composable
fun FavoriteScreen(
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoriteViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
                FavoriteContent(
                    products = uiState.products,
                    onBackClick = onBackClick,
                    onHomeClick = onHomeClick,
                    onProfileClick = onProfileClick,
                    onToggleFavorite = viewModel::toggleFavorite
                )
            }
        }
    }
}

@Composable
private fun FavoriteContent(
    products: List<HomeProduct>,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    onToggleFavorite: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
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
                    text = "Избранное",
                    modifier = Modifier.weight(1f),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = InputText
                )

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_nav_heart),
                        contentDescription = null,
                        tint = Color(0xFFFF6B6B),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(products, key = { it.id }) { product ->
                    FavoriteProductCard(
                        product = product,
                        onFavoriteClick = { onToggleFavorite(product.id) }
                    )
                }
            }
        }

        FavoriteBottomBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 18.dp),
            onHomeClick = onHomeClick,
            onProfileClick = onProfileClick
        )
    }
}

@Composable
private fun FavoriteProductCard(
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
                    tint = Color(0xFFFF6B6B),
                    modifier = Modifier
                        .size(14.dp)
                        .clickable(onClick = onFavoriteClick)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(104.dp)
                .padding(top = 8.dp, end = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

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
            fontSize = 16.sp,
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

@Composable
private fun FavoriteBottomBar(
    modifier: Modifier = Modifier,
    onHomeClick: () -> Unit,
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
            Icon(
                painter = painterResource(id = R.drawable.ic_nav_home),
                contentDescription = null,
                tint = SecondaryText,
                modifier = Modifier
                    .size(22.dp)
                    .clickable(onClick = onHomeClick)
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_nav_heart),
                contentDescription = null,
                tint = SneakerBlue,
                modifier = Modifier.size(22.dp)
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
                tint = SecondaryText,
                modifier = Modifier.size(22.dp)
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_nav_profile),
                contentDescription = null,
                tint = SecondaryText,
                modifier = Modifier
                    .size(22.dp)
                    .clickable(onClick = onProfileClick)
            )
        }
    }
}

private fun formatPrice(price: Double): String {
    return NumberFormat
        .getCurrencyInstance(Locale.forLanguageTag("ru-RU"))
        .format(price)
}
