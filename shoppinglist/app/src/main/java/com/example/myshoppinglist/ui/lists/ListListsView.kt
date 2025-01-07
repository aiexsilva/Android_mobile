package com.example.myshoppinglist.ui.lists

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.Screen
import com.example.myshoppinglist.repositories.ListItemsRepository
import com.example.myshoppinglist.ui.theme.ShoppingListTheme
import kotlin.math.log

@Composable
fun ListListsView(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    listId : String,
){

    val viewModel : ListListsViewModel = viewModel()
    val state = viewModel.state.value
    var repository = ListItemsRepository

    Box(modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd,) {

        LazyColumn(modifier = modifier.fillMaxSize()) {

            itemsIndexed(
                items = state.listItemsList
            ){  index, item ->
                Card(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = {
                                Log.d("lol", "${item.docId}")
                                Log.d("lol", "${item.name}")
                                item.docId?.let { it1 -> repository.deleteList(it1) }
                                viewModel.getLists()
                            },
                            onTap = {
                                navController.navigate(
                                    Screen.ListItems.route
                                        .replace("{listId}", item.docId!!)
                                        .replace("{name}", item.name?:"")
                                )
                            }
                        )
                    },){
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        text = item.name?:""
                    )
                }
            }
        }

        Column {
            Button(
                modifier = Modifier
                    .padding(16.dp)
                    .size(64.dp),
                onClick = {
                    navController.navigate(Screen.AddList.route)
                }) {
                Image(
                    modifier = Modifier
                        .scale(2.0f)
                        .size(64.dp),
                    colorFilter = ColorFilter.tint(Color.White),
                    painter = painterResource(R.drawable.baseline_add_24),
                    contentDescription = "add"
                )
            }

            Button(
                modifier = Modifier
                    .padding(16.dp)
                    .size(64.dp),
                onClick = {
                    navController.navigate(Screen.Profile.route)
                }) {
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = "User Profile Icon",
                    tint = Color.White
                )
            }
        }
    }

    LaunchedEffect (key1 = true){
        viewModel.getLists()
    }

}

@Preview(showBackground = true)
@Composable
fun ListListViewPreview(){
    ShoppingListTheme {
        ListListsView(listId = "")
    }
}