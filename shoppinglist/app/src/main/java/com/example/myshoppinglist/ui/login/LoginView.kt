package com.example.myshoppinglist.ui.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myshoppinglist.ui.theme.ShoppingListTheme

@Composable
fun LoginView(modifier: Modifier = Modifier,
              onLoginSuccess : ()->Unit = {}) {

    val viewModel : LoginViewModel = viewModel()
    val state = viewModel.state.value
    var isPasswordVisible by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center)
    {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            OutlinedTextField(value = state.email,
                onValueChange = {
                    viewModel.onEmailChange(it)
                },
                placeholder = {
                    Text("email")
                },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = "email")
                },
                maxLines = 1,
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = state.password,
                onValueChange = {
                    viewModel.onPasswordChange(it)
                },
                placeholder = {
                    Text("password")
                },
                visualTransformation = if(isPasswordVisible){
                    VisualTransformation.None
                }else{
                    PasswordVisualTransformation()
                },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = "pass")
                },
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        if(isPasswordVisible){
                            Icon(Icons.Default.Visibility, contentDescription = "Password Visible")
                        }else{
                            Icon(Icons.Default.VisibilityOff, contentDescription = "Password Not Visible")
                        }
                    }
                },
                maxLines = 1,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.onLoginClick {
                        onLoginSuccess()
                    }

                },
                content = {
                    Text("Login")
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (state.error != null)
                Text(state.error?:"")
            if (state.isLoading)
                CircularProgressIndicator()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginViewPreview() {
    ShoppingListTheme {
        LoginView()
    }
}