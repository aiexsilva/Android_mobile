package com.example.myshoppinglist.ui.profile

import android.widget.Button
import android.widget.Space
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myshoppinglist.ui.theme.ShoppingListTheme

@Composable
fun ProfileView(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    onLogout: () -> Unit = {},
){

    val viewModel : ProfileViewModel = viewModel()
    val state = viewModel.state.value

    Column(modifier = modifier.fillMaxSize()) {

        Spacer(modifier = Modifier.padding(100.dp))

        Text(state.user?.name?:"Username:", textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.padding(10.dp))

        Text(state.user?.email?:"Email:", textAlign = TextAlign.Center)

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = {
                Text("change username")
            },
            value = state.name?:"",
            onValueChange = viewModel::onNameChange
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onClick = {
                viewModel.saveUser()
                navController.popBackStack()
            }) {
            Text("Save")
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onClick = (onLogout)
            ) {
            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
        }
    }

    LaunchedEffect (key1 = true){
        viewModel.getUser()
    }

}

@Preview(showBackground = true)
@Composable
fun ProfileViewPreview(){
    ShoppingListTheme {
        ProfileView()
    }
}