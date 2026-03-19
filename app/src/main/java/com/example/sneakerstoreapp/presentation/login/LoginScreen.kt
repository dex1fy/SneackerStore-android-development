/**
 * Этот файл описывает Compose-экран слоя presentation.
 *
 * Здесь собирается интерфейс, отображается состояние экрана и обрабатываются действия пользователя.
 */
package com.example.sneakerstoreapp.presentation.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sneakerstoreapp.R
import com.example.sneakerstoreapp.presentation.auth.AuthFooter
import com.example.sneakerstoreapp.presentation.auth.AuthHeader
import com.example.sneakerstoreapp.presentation.auth.BackButton
import com.example.sneakerstoreapp.presentation.auth.PasswordInputField
import com.example.sneakerstoreapp.presentation.auth.SimpleInputField
import com.example.sneakerstoreapp.presentation.auth.SimpleLabel
import com.example.sneakerstoreapp.ui.theme.ScreenBackground
import com.example.sneakerstoreapp.ui.theme.SecondaryText
import com.example.sneakerstoreapp.ui.theme.SneakerBlue
import com.example.sneakerstoreapp.ui.theme.SneakerStoreAppTheme

@Composable
fun LoginScreen(
    onBackClick: () -> Unit,
    onCreateAccountClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onLoginSuccess: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val viewModel: LoginViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(uiState.message) {
        // Любое новое сообщение показываем через Toast.
        val message = uiState.message ?: return@LaunchedEffect
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

        if (uiState.isSuccess) {
            // После успешного входа передаем управление навигации.
            onLoginSuccess()
        }

        viewModel.clearMessage()
    }

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
            title = stringResource(R.string.login_title),
            subtitle = stringResource(R.string.login_subtitle)
        )

        Spacer(modifier = Modifier.height(56.dp))

        SimpleLabel(text = stringResource(R.string.email_label))
        Spacer(modifier = Modifier.height(12.dp))
        SimpleInputField(
            value = email,
            onValueChange = { email = it },
            placeholder = stringResource(R.string.email_placeholder),
            keyboardType = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(24.dp))

        SimpleLabel(text = stringResource(R.string.password_label))
        Spacer(modifier = Modifier.height(12.dp))
        PasswordInputField(
            value = password,
            onValueChange = { password = it },
            placeholder = stringResource(R.string.password_placeholder),
            isPasswordVisible = isPasswordVisible,
            onPasswordVisibilityChange = { isPasswordVisible = !isPasswordVisible }
        )

        Text(
            text = stringResource(R.string.forgot_password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onForgotPasswordClick
                ),
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = SecondaryText,
            textAlign = TextAlign.End
        )

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = {
                // Передаем введенные значения во ViewModel для валидации и авторизации.
                viewModel.signIn(
                    email = email,
                    password = password
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            enabled = !uiState.isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = SneakerBlue,
                contentColor = Color.White
            )
        ) {
            Text(
                text = if (uiState.isLoading) {
                    stringResource(R.string.loading_button)
                } else {
                    stringResource(R.string.sign_in)
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        AuthFooter(
            text = stringResource(R.string.first_time),
            actionText = stringResource(R.string.create_account),
            onActionClick = onCreateAccountClick
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    SneakerStoreAppTheme {
        LoginScreen(
            onBackClick = { },
            onCreateAccountClick = { },
            onForgotPasswordClick = { },
            onLoginSuccess = { }
        )
    }
}
