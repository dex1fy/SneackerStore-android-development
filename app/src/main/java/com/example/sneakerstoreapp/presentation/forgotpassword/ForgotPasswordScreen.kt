package com.example.sneakerstoreapp.presentation.forgotpassword

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sneakerstoreapp.R
import com.example.sneakerstoreapp.presentation.auth.AuthHeader
import com.example.sneakerstoreapp.presentation.auth.BackButton
import com.example.sneakerstoreapp.presentation.auth.SimpleInputField
import com.example.sneakerstoreapp.ui.theme.ScreenBackground
import com.example.sneakerstoreapp.ui.theme.SneakerBlue
import com.example.sneakerstoreapp.ui.theme.SneakerStoreAppTheme

@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        BackButton(onClick = onBackClick)

        Spacer(modifier = Modifier.height(54.dp))

        AuthHeader(
            title = stringResource(R.string.forgot_password_title),
            subtitle = stringResource(R.string.forgot_password_subtitle)
        )

        Spacer(modifier = Modifier.height(56.dp))

        SimpleInputField(
            value = email,
            onValueChange = { email = it },
            placeholder = stringResource(R.string.email_placeholder),
            keyboardType = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SneakerBlue,
                contentColor = Color.White
            )
        ) {
            Text(
                text = stringResource(R.string.send_button),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ForgotPasswordScreenPreview() {
    SneakerStoreAppTheme {
        ForgotPasswordScreen(onBackClick = { })
    }
}
