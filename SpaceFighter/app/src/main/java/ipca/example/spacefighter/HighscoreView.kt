package ipca.example.spacefighter

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.File
import java.nio.charset.Charset

fun getHighscoreFile(context: Context): File {
    return File(context.filesDir, "highscore.txt")
}

fun readText(context: Context): String {
    val file = getHighscoreFile(context)
    return if (file.exists()) file.readText(Charset.defaultCharset()) else "0"
}

@Composable
fun HighscoreView(
    modifier: Modifier = Modifier,
    onClickButton: () -> Unit = {},
) {
    val context = LocalContext.current
    val highscore = readText(context)
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(painter = painterResource(id = R.drawable.highscore),
                contentDescription = "high score" ,
                modifier = Modifier.width(160.dp).height(80.dp),
                contentScale = ContentScale.FillBounds)
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = highscore,
                fontSize = 50.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onClickButton,
                modifier = Modifier
                    .width(200.dp)
                    .height(60.dp)
            ) {
                Text("Main Screen")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HighscoreViewPreview() {
    HighscoreView()
}