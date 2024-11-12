package ipca.examples.dailynews.ui

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ipca.examples.dailynews.models.Article
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


data class ArticlesState (
    val articles: ArrayList<Article> = arrayListOf(),
    var searchText : String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ArticlesState())
    val uiState : StateFlow<ArticlesState> = _uiState.asStateFlow()


    fun searchText(searchText: String?){

        _uiState.value = ArticlesState(
            searchText = searchText)
        fetchArticles()
    }


    fun fetchArticles() {

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null)

        val client = OkHttpClient()

        var searchQuery = ""
        if (!(_uiState.value.searchText?.isNullOrBlank() == true)){
            searchQuery = "q=${_uiState.value.searchText}&"
        }

        val request = Request.Builder()
            .url("https://content.guardianapis.com/search?${searchQuery}api-key=${sharedData.key}&show-fields=thumbnail")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                _uiState.value = ArticlesState(
                    isLoading = false,
                    error = e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    val articlesResult = arrayListOf<Article>()
                    val result = response.body!!.string()

                    val jsonResult = JSONObject(result)
                    val responseJson = jsonResult.getJSONObject("response")
                    val status = responseJson.getString("status")

                    if (status == "ok") {
                        val resultsJson = responseJson.getJSONArray("results")

                        for (index in 0 until resultsJson.length()) {
                            val articleJson = resultsJson.getJSONObject(index)
                            val fields = articleJson.optJSONObject("fields")
                            val imageUrl = fields?.optString("thumbnail") ?: ""
                            val article = Article(
                                title = articleJson.getString("webTitle"),
                                description = articleJson.optString("sectionName", "No description available"),
                                url = articleJson.getString("webUrl"),
                                urlToImage = imageUrl
                            )
                            articlesResult.add(article)
                        }
                    }
                    _uiState.value = ArticlesState(
                        articles = articlesResult,
                        isLoading = false,
                        error = null)
                }
            }
        })
    }

}