package com.example.news_main_features

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.news_main_features.models.ArticleUi


/**
 * Делаем обертку что бы не светить наружу класс
 */

@Composable
fun NewsMain() {
NewsMain(vm = viewModel())
}

@Composable
internal fun NewsMain(vm: MainNewsViewModel) {
    val state by vm.state.collectAsState()
    when(val cureState = state) {
        is State.Success -> ArticlesDraw(cureState)
        is State.Error -> TODO()
        is State.Loading -> TODO()
        State.None -> TODO()
    }
}

@Composable
private fun ArticlesDraw(state: State.Success) {
   LazyColumn {
       items(state.articleUis) {
           articleUi ->  key(articleUi.id) {
               ArticleDraw(articleUi)
       }
       }
       }
   }

/**
 * Добавление превью элементов
 */
@Preview
@Composable
fun ArticleDraw(@PreviewParameter(ArticlePreviewProvider::class) articleUi: ArticleUi) {
    Column {
        Text(text = articleUi.tittle, style = MaterialTheme.typography.headlineMedium, maxLines = 1)
        Text(text = articleUi.description, style = MaterialTheme.typography.bodyMedium, maxLines = 3)
    }
}

private abstract class ArticlePreviewProvider: PreviewParameterProvider<ArticleUi> {
    override val values = sequenceOf(
       ArticleUi(
            1,
            "Tramp is fake presedent",
            "Donald Tramp is not legal presedent. He stay is Russian Federation",
            imageUrl = null,
            url = "",
        ),
        ArticleUi(
            2,
            "Greenland will not fight!",
            "Greenland did not agree to join the American War of Independence.",
            imageUrl = null,
            url = "",
        ),
        ArticleUi(
            3,
            "Israel strikes back.",
            "Israel again connected its satellites to the offensive in Europe.",
            imageUrl = null,
            url = "",
        )

    )
}

