/**
 * Этот файл относится к слою presentation.
 *
 * Он помогает экрану отображать данные, обрабатывать действия пользователя или синхронизировать состояние.
 */
package com.example.sneakerstoreapp.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sneakerstoreapp.R
import com.example.sneakerstoreapp.ui.theme.DividerGray
import com.example.sneakerstoreapp.ui.theme.InputBackground
import com.example.sneakerstoreapp.ui.theme.InputHint
import com.example.sneakerstoreapp.ui.theme.InputText
import com.example.sneakerstoreapp.ui.theme.SecondaryText
import com.example.sneakerstoreapp.ui.theme.SneakerBlue

@Composable
fun BackButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(InputBackground),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = onClick) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_left),
                contentDescription = stringResource(R.string.back_button),
                tint = InputText
            )
        }
    }
}

@Composable
fun AuthHeader(
    title: String,
    subtitle: String
) {
    Text(
        text = title,
        modifier = Modifier.fillMaxWidth(),
        fontSize = 32.sp,
        fontWeight = FontWeight.Medium,
        color = InputText,
        textAlign = TextAlign.Center
    )

    Text(
        text = subtitle,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        fontSize = 16.sp,
        color = SecondaryText,
        textAlign = TextAlign.Center
    )
}

@Composable
fun SimpleLabel(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        color = InputText
    )
}

@Composable
fun SimpleInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: (@Composable (() -> Unit))? = null
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        singleLine = true,
        textStyle = TextStyle(
            fontSize = 15.sp,
            color = InputText
        ),
        placeholder = {
            Text(
                text = placeholder,
                fontSize = 15.sp,
                color = InputHint
            )
        },
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = visualTransformation,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = InputBackground,
            unfocusedContainerColor = InputBackground,
            disabledContainerColor = InputBackground,
            errorContainerColor = InputBackground,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            focusedTextColor = InputText,
            unfocusedTextColor = InputText,
            focusedPlaceholderColor = InputHint,
            unfocusedPlaceholderColor = InputHint,
            focusedTrailingIconColor = SecondaryText,
            unfocusedTrailingIconColor = SecondaryText
        )
    )
}

@Composable
fun PasswordInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPasswordVisible: Boolean,
    onPasswordVisibilityChange: () -> Unit
) {
    SimpleInputField(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        keyboardType = KeyboardType.Password,
        visualTransformation = if (isPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            val eyeIconRes = if (isPasswordVisible) {
                R.drawable.ic_eye_open
            } else {
                R.drawable.ic_eye
            }

            Icon(
                painter = painterResource(id = eyeIconRes),
                contentDescription = stringResource(R.string.toggle_password_visibility),
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onPasswordVisibilityChange
                ),
                tint = SecondaryText
            )
        }
    )
}

@Composable
fun AgreementRow(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onCheckedChange(!isChecked) }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AgreementIndicator(isChecked = isChecked)

        Text(
            text = stringResource(R.string.privacy_agreement),
            modifier = Modifier.padding(start = 12.dp),
            fontSize = 14.sp,
            lineHeight = 18.sp,
            color = SecondaryText,
            textDecoration = TextDecoration.Underline
        )
    }
}

@Composable
private fun AgreementIndicator(isChecked: Boolean) {
    val indicatorShape = RoundedCornerShape(6.dp)
    val indicatorColor = if (isChecked) SneakerBlue else Color.Transparent
    val borderColor = if (isChecked) SneakerBlue else DividerGray

    Box(
        modifier = Modifier
            .size(18.dp)
            .clip(indicatorShape)
            .background(indicatorColor)
            .border(width = 1.dp, color = borderColor, shape = indicatorShape),
        contentAlignment = Alignment.Center
    ) {
        if (isChecked) {
            Icon(
                painter = painterResource(id = R.drawable.ic_agreement_check),
                contentDescription = null,
                modifier = Modifier.size(8.dp),
                tint = Color.Unspecified
            )
        }
    }
}

@Composable
fun AuthFooter(
    text: String,
    actionText: String,
    onActionClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = SecondaryText
        )

        Text(
            text = " $actionText",
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onActionClick
            ),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = InputText
        )
    }
}
