/**
 * Этот файл описывает Compose-экран слоя presentation.
 *
 * Здесь собирается интерфейс, отображается состояние экрана и обрабатываются действия пользователя.
 */
package com.example.sneakerstoreapp.presentation.register

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sneakerstoreapp.R
import com.example.sneakerstoreapp.presentation.auth.AgreementRow
import com.example.sneakerstoreapp.presentation.auth.AuthFooter
import com.example.sneakerstoreapp.presentation.auth.AuthHeader
import com.example.sneakerstoreapp.presentation.auth.BackButton
import com.example.sneakerstoreapp.presentation.auth.PasswordInputField
import com.example.sneakerstoreapp.presentation.auth.SimpleInputField
import com.example.sneakerstoreapp.presentation.auth.SimpleLabel
import com.example.sneakerstoreapp.ui.theme.InputText
import com.example.sneakerstoreapp.ui.theme.InactiveButtonBlue
import com.example.sneakerstoreapp.ui.theme.InactiveButtonText
import com.example.sneakerstoreapp.ui.theme.ScreenBackground
import com.example.sneakerstoreapp.ui.theme.SecondaryText
import com.example.sneakerstoreapp.ui.theme.SneakerBlue
import com.example.sneakerstoreapp.ui.theme.SneakerStoreAppTheme

@Composable
fun RegisterScreen(
    onBackClick: () -> Unit,
    onSignInClick: () -> Unit,
    onRegisterSuccess: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val viewModel: RegisterViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var isAgreementChecked by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(uiState.message) {
        val message = uiState.message ?: return@LaunchedEffect
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

        if (uiState.isSuccess) {
            onRegisterSuccess()
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
            title = stringResource(R.string.register_title),
            subtitle = stringResource(R.string.register_subtitle)
        )

        Spacer(modifier = Modifier.height(56.dp))

        SimpleLabel(text = stringResource(R.string.name_label))
        Spacer(modifier = Modifier.height(12.dp))
        SimpleInputField(
            value = name,
            onValueChange = {
                name = it
                viewModel.clearNameError()
            },
            placeholder = stringResource(R.string.name_placeholder),
            keyboardType = KeyboardType.Text
        )
        ValidationText(uiState.nameError)

        Spacer(modifier = Modifier.height(20.dp))

        SimpleLabel(text = stringResource(R.string.email_label))
        Spacer(modifier = Modifier.height(12.dp))
        SimpleInputField(
            value = email,
            onValueChange = {
                email = it
                viewModel.clearEmailError()
            },
            placeholder = stringResource(R.string.email_placeholder),
            keyboardType = KeyboardType.Email
        )
        ValidationText(uiState.emailError)

        Spacer(modifier = Modifier.height(20.dp))

        SimpleLabel(text = stringResource(R.string.password_label))
        Spacer(modifier = Modifier.height(12.dp))
        PasswordInputField(
            value = password,
            onValueChange = {
                password = it
                viewModel.clearPasswordError()
            },
            placeholder = stringResource(R.string.password_placeholder),
            isPasswordVisible = isPasswordVisible,
            onPasswordVisibilityChange = { isPasswordVisible = !isPasswordVisible }
        )
        ValidationText(uiState.passwordError)

        Spacer(modifier = Modifier.height(18.dp))

        AgreementRow(
            isChecked = isAgreementChecked,
            onCheckedChange = { isAgreementChecked = it }
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = {
                viewModel.signUp(
                    name = name,
                    email = email,
                    password = password
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            enabled = isAgreementChecked && !uiState.isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = SneakerBlue,
                contentColor = Color.White,
                disabledContainerColor = InactiveButtonBlue,
                disabledContentColor = InactiveButtonText
            )
        ) {
            Text(
                text = if (uiState.isLoading) {
                    stringResource(R.string.loading_button)
                } else {
                    stringResource(R.string.register_button)
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        AuthFooter(
            text = stringResource(R.string.have_account),
            actionText = stringResource(R.string.sign_in),
            onActionClick = onSignInClick
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ValidationText(error: String?) {
    if (error.isNullOrBlank()) return

    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = error,
        fontSize = 13.sp,
        color = Color(0xFFE74C3C)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RegisterScreenPreview() {
    SneakerStoreAppTheme {
        RegisterScreen(
            onBackClick = { },
            onSignInClick = { },
            onRegisterSuccess = { }
        )
    }
}
