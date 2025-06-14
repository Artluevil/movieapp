package com.kbak.moviesapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.LoadState
import com.kbak.moviesapp.ui.viewmodel.MovieViewModel
import com.kbak.moviesapp.ui.viewmodel.GenreViewModel
import com.kbak.moviesapp.ui.components.MovieListCardContainer

@Composable
fun MovieListScreen(
    navController: NavController,
    viewModel: MovieViewModel = hiltViewModel(),
    genreViewModel: GenreViewModel
) {
    val movies = viewModel.movies.collectAsLazyPagingItems()
    val listState = rememberLazyGridState()
    val hasScrolledToTop = remember { mutableStateOf(false) }

    LaunchedEffect(movies.loadState.refresh) {
        if (movies.loadState.refresh is LoadState.Loading) {
            hasScrolledToTop.value = false
        }

        if (
            movies.loadState.refresh is LoadState.NotLoading &&
            movies.itemCount > 0 &&
            !hasScrolledToTop.value
        ) {
            listState.scrollToItem(0)
            hasScrolledToTop.value = true
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = movies.loadState.refresh) {
            is LoadState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is LoadState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Error: ${state.error.localizedMessage ?: "Unknown Error"}",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            is LoadState.NotLoading -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    item(span = { GridItemSpan(2) }) {
                        Spacer(modifier = Modifier.height(1.dp))
                    }

                    items(
                        count = movies.itemCount,
                        key = { index -> movies[index]?.localId ?: index }
                    ) { index ->
                        val movie = movies[index]
                        if (movie != null) {
                            MovieListCardContainer(
                                modifier = Modifier,
                                movie = movie,
                                navController = navController,
                                genreViewModel = genreViewModel
                            )
                        }
                    }

                    if (movies.loadState.append is LoadState.Loading) {
                        item(span = { GridItemSpan(2) }) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}
