package ipca.examples.dailynews.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ipca.examples.dailynews.Screen
import ipca.examples.dailynews.encodeURL
import ipca.examples.dailynews.models.Article
import ipca.examples.dailynews.ui.theme.DailyNewsTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.unit.dp

@Composable
fun HomeView(
    searchBar: String,
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    onArticleClick: () -> Unit) {

    val viewModel : HomeViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()



    HomeViewContent(modifier = modifier,
        navController = navController,
        uiState = uiState,
        onArticleClick = onArticleClick)

    LaunchedEffect(key1 = searchBar) {
        viewModel.searchText(searchBar)
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeViewContent(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    uiState: ArticlesState,
    onArticleClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            uiState.isLoading -> {
                Text("Loading articles...")
            }
            uiState.error != null -> {
                Text("Error: ${uiState.error}")
            }
            uiState.articles.isEmpty() -> {
                Text("No articles found!")
            }
            else -> {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(15.dp)
                ) {


                    if (uiState.articles.isEmpty()) {
                        item {
                            Text("No matching articles found!")
                        }
                    } else {
                        itemsIndexed(uiState.articles) { index, article ->
                            RowArticle(
                                modifier = Modifier
                                    .clickable {
                                        onArticleClick()
                                        Log.d("dailynews", article.url ?: "none")
                                        navController.navigate(
                                            Screen.ArticleDetail.route
                                                .replace("{articleUrl}", article.url?.encodeURL() ?: "")
                                        )
                                    },
                                article = article
                            )
                        }
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun HomeViewPreview() {

    val articles = arrayListOf(
        Article("Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin lobortis augue in erat scelerisque, vitae fringilla nisi tempus. Sed finibus tellus porttitor dignissim eleifend. Etiam sed neque libero. Integer auctor turpis est. Nunc ac auctor velit. Nunc et mi sollicitudin, iaculis nunc et, congue neque. Suspendisse potenti. Vestibulum finibus justo sed eleifend commodo. Phasellus vestibulum ligula nisi, convallis rhoncus quam placerat id. Donec eu lobortis lacus, quis porta tortor. Suspendisse quis dolor sapien. Maecenas finibus purus at orci aliquam eleifend. Nam venenatis sapien ac enim efficitur pretium. Praesent sagittis risus vitae feugiat blandit. Etiam non neque arcu. Cras a mauris eu erat sodales iaculis non a lorem.",
            urlToImage = "https://media.istockphoto.com/id/1166633394/pt/foto/victorian-british-army-gymnastic-team-aldershot-19th-century.jpg?s=1024x1024&w=is&k=20&c=fIfqysdzOinu8hNJG6ZXOhl8ghQHA7ySl8BZZYWrxyQ="),
        Article("Lorem Ipsum is simply dummy text of the printing", "description"))

    //val articles = arrayListOf<Article>()
    DailyNewsTheme {
        HomeViewContent(uiState = ArticlesState(
            articles = articles,
            isLoading = false,
            searchText = "",
            error = null
        ),
            onArticleClick = {})
    }
}