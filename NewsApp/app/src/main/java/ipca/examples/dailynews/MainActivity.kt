package ipca.examples.dailynews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ipca.examples.dailynews.ui.ArticleDetail
import ipca.examples.dailynews.ui.HomeView
import ipca.examples.dailynews.ui.theme.DailyNewsTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DailyNewsTheme {
                val navController = rememberNavController()
                var showTopBar by remember { mutableStateOf(true) }
                var searchBar by remember { mutableStateOf(TextFieldValue("")) }
                var searchText by remember { mutableStateOf("") }
                var showSearchBar by remember { mutableStateOf(true) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        if (showTopBar) {
                            TopAppBar(
                                title = {
                                    Text("News")
                                    if (showSearchBar) {
                                        OutlinedTextField(
                                            value = searchBar,
                                            onValueChange = {
                                                searchBar = it
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    Icons.Filled.Search,
                                                    contentDescription = "Search"
                                                )
                                            },
                                            placeholder = { Text("Search...") },
                                            modifier = Modifier.fillMaxWidth().padding(5.dp),
                                            singleLine = true,
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedBorderColor = Color.Transparent,
                                                unfocusedBorderColor = Color.Transparent
                                            )
                                        )
                                    }
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(route = Screen.Home.route) {
                            showSearchBar = true
                            HomeView(
                                navController = navController,
                                searchBar = searchBar.text,
                                modifier = Modifier.fillMaxSize(),
                                onArticleClick = {

                                }
                            )
                        }
                        composable(route = Screen.ArticleDetail.route) {
                            val url = it.arguments?.getString("articleUrl")
                            showSearchBar = false
                            ArticleDetail(
                                modifier = Modifier.fillMaxSize(),
                                url = url ?: ""
                            )
                        }
                    }
                }
            }
        }
    }
}

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object ArticleDetail : Screen("article_detail/{articleUrl}")

}