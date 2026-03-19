/**
 * Этот файл описывает Compose-экран слоя presentation.
 *
 * Здесь собирается интерфейс, отображается состояние экрана и обрабатываются действия пользователя.
 */
package com.example.sneakerstoreapp.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sneakerstoreapp.presentation.auth.BackButton
import com.example.sneakerstoreapp.ui.theme.InputBackground
import com.example.sneakerstoreapp.ui.theme.InputText

@Composable
fun LoyaltyCardScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(InputBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Карта лояльности",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = InputText
                )

                Box(modifier = Modifier.align(Alignment.CenterStart)) {
                    BackButton(onClick = onBackClick)
                }
            }

            Spacer(modifier = Modifier.height(44.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 46.dp),
                shape = RoundedCornerShape(14.dp),
                color = Color.White
            ) {
                LoyaltyBarcodeView(
                    value = uiState.profile?.userId.orEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(462.dp)
                        .padding(horizontal = 18.dp, vertical = 28.dp)
                )
            }
        }
    }
}

@Composable
private fun LoyaltyBarcodeView(
    value: String,
    modifier: Modifier = Modifier
) {
    val normalized = value.ifBlank { "000000000000000000000000" }

    BarcodeView(
        value = normalized + normalized.reversed(),
        modifier = modifier.graphicsLayer {
            rotationZ = 90f
        }
    )
}
