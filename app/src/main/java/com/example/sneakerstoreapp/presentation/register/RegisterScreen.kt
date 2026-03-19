package com.example.sneakerstoreapp.presentation.register

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
import com.example.sneakerstoreapp.presentation.auth.AgreementRow
import com.example.sneakerstoreapp.presentation.auth.AuthFooter
import com.example.sneakerstoreapp.presentation.auth.AuthHeader
import com.example.sneakerstoreapp.presentation.auth.BackButton
import com.example.sneakerstoreapp.presentation.auth.PasswordInputField
import com.example.sneakerstoreapp.presentation.auth.SimpleInputField
import com.example.sneakerstoreapp.presentation.auth.SimpleLabel
import com.example.sneakerstoreapp.ui.theme.InactiveButtonBlue
import com.example.sneakerstoreapp.ui.theme.InactiveButtonText
import com.example.sneakerstoreapp.ui.theme.ScreenBackground
import com.example.sneakerstoreapp.ui.theme.SneakerBlue
import com.example.sneakerstoreapp.ui.theme.SneakerStoreAppTheme

@Composable
fun RegisterScreen(
    onBackClick: () -> Unit,
    onSignInClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var isAgreementChecked by rememberSaveable { mutableStateOf(false) }

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
            onValueChange = { name = it },
            placeholder = stringResource(R.string.name_placeholder),
            keyboardType = KeyboardType.Text
        )

        Spacer(modifier = Modifier.height(20.dp))

        SimpleLabel(text = stringResource(R.string.email_label))
        Spacer(modifier = Modifier.height(12.dp))
        SimpleInputField(
            value = email,
            onValueChange = { email = it },
            placeholder = stringResource(R.string.email_placeholder),
            keyboardType = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(20.dp))

        SimpleLabel(text = stringResource(R.string.password_label))
        Spacer(modifier = Modifier.height(12.dp))
        PasswordInputField(
            value = password,
            onValueChange = { password = it },
            placeholder = stringResource(R.string.password_placeholder),
            isPasswordVisible = isPasswordVisible,
            onPasswordVisibilityChange = { isPasswordVisible = !isPasswordVisible }
        )

        Spacer(modifier = Modifier.height(18.dp))

        AgreementRow(
            isChecked = isAgreementChecked,
            onCheckedChange = { isAgreementChecked = it }
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            enabled = isAgreementChecked,
            colors = ButtonDefaults.buttonColors(
                containerColor = SneakerBlue,
                contentColor = Color.White,
                disabledContainerColor = InactiveButtonBlue,
                disabledContentColor = InactiveButtonText
            )
        ) {
            Text(
                text = stringResource(R.string.register_button),
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RegisterScreenPreview() {
    SneakerStoreAppTheme {
        RegisterScreen(
            onBackClick = { },
            onSignInClick = { }
        )
    }
}
