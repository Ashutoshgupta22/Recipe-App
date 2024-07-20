@file:OptIn(ExperimentalMaterial3Api::class)

package com.aspark.recipeapp.ui.component

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import coil.request.ImageRequest
import coil.size.Size
import com.aspark.recipeapp.MyApplication
import com.aspark.recipeapp.R
import com.aspark.recipeapp.ui.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BottomNavigationBar(navController: NavController) {

    NavigationBar {

        var selected by remember { mutableStateOf<Screen>(Screen.Home) }

        NavigationBarItem(
            selected = selected == Screen.Home,
            onClick = {
                navController.navigate(Screen.Home.route)
                selected = Screen.Home
            },
            label = { Text(text = "Home") },
            icon = {
                Icon(
                    imageVector =
                    if (selected == Screen.Home) Screen.Home.selectedIcon!!
                    else Screen.Home.icon!!,
                    contentDescription = ""
                )
            }
        )

        NavigationBarItem(
            selected = selected == Screen.Favorites,
            onClick = {
                navController.navigate(Screen.Favorites.route)
                selected = Screen.Favorites
            },
            label = { Text(text = "Favourite") },
            icon = {
                Icon(
                    imageVector =
                    if (selected == Screen.Favorites) Screen.Favorites.selectedIcon!!
                    else Screen.Favorites.icon!!,
                    contentDescription = ""
                )
            }
        )
    }
}

@Composable
fun MyAsyncImage(
    url: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageRequest = ImageRequest.Builder(context)
        .data(url)
        .crossfade(true)
//        .size(100, 100)
        .build()

    AsyncImage(
        model = imageRequest,
        contentDescription = "",
        imageLoader = context.imageLoader,
        modifier = modifier,
        contentScale = ContentScale.Crop,
        placeholder = painterResource(id = R.drawable.ic_launcher_background)
    )
}

@Composable
fun BoldTitle(title: String) {
    Text(
        text = title, fontWeight = FontWeight.Bold,
        color = Color.Black
    )
}

@Composable
fun ExpandableCard(
    title: String,
    description: String
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RectangleShape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize() // This ensures smooth animation when expanding/collapsing
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp
                        else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                }
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun MySearchBar(
    onBack: () -> Unit,
    onClear: () -> Unit,
    onSearch: (String) -> Unit,
    content: @Composable (ColumnScope.() -> Unit)
) {

    var query by remember { mutableStateOf("") }
    val active by remember { mutableStateOf(true) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = Unit) {
        delay(100)
        focusRequester.requestFocus()
    }

    SearchBar(
        query = query,
        onQueryChange = {
            query = it
            onSearch(query)
        },
        onSearch = {
            onSearch(it)
        },
        active = active,
        onActiveChange = {
            if (active && !it) onBack()
//            else onActiveChange()
        },
        placeholder = {
            Text(text = "Search any recipe")
        },
        leadingIcon = {
                IconButton(onClick = { onBack() },
                    modifier = Modifier

                    ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
        },
        trailingIcon = {
            if (query.isNotEmpty())
                IconButton(onClick = {
                    query = ""
                    onClear()
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = ""
                    )
                }
        },
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .focusRequester(focusRequester)
    ) {
        content()
    }
}

