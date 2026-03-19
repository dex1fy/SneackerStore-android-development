package com.example.sneakerstoreapp.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sneakerstoreapp.R
import com.example.sneakerstoreapp.ui.theme.SneakerBlue
import com.example.sneakerstoreapp.ui.theme.SneakerStoreAppTheme

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SneakerBlue),
        contentAlignment = Alignment.Center
    ) {
        // Center icon for the splash screen.
        Image(
            painter = painterResource(id = R.drawable.ic_splash_logo),
            contentDescription = null,
            modifier = Modifier.size(129.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SplashScreenPreview() {
    SneakerStoreAppTheme {
        SplashScreen()
    }
}
