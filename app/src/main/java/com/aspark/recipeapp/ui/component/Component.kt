@file:OptIn(ExperimentalMaterial3Api::class)

package com.aspark.recipeapp.ui.component

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import com.aspark.recipeapp.ui.Screen
import kotlinx.coroutines.delay

@Composable
fun BottomNavigationBar(
    navController: NavController,
    selected: Screen,
    onItemSelected: (Screen) -> Unit
) {

    Log.i("Component", "BottomNavigationBar: called")

    NavigationBar {
        NavigationBarItem(
            selected = selected == Screen.Home,
            onClick = {
                navController.navigate(Screen.Home.route)
                onItemSelected(Screen.Home)
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
                onItemSelected(Screen.Favorites)
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
    url: String?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageRequest = ImageRequest.Builder(context)
        .data(url ?: "")
        .crossfade(true)
//        .size(100, 100)
        .build()

    AsyncImage(
        model = imageRequest,
        contentDescription = "",
        imageLoader = context.imageLoader,
        modifier = modifier,
        contentScale = ContentScale.Crop,
//        placeholder = painterResource(id = R.drawable.ic_launcher_background)
    )
}

@Composable
fun BoldTitle(title: String) {
    Text(
        text = title, fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
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

    var query by rememberSaveable { mutableStateOf("") }
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
            IconButton(
                onClick = { onBack() },
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

@Composable
fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    val transition = rememberInfiniteTransition(label = "infinite")
    val startOffsetX by transition.animateFloat(
        initialValue = -3 * size.width.toFloat(),
        targetValue = 3 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
            )
        ), label = "shimmer"
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFD9D7D7),
                Color(0xFFB7B7B7),
                Color(0xFFD9D7D7),
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    ).onGloballyPositioned {
        size = it.size
    }
}

@Composable
fun AnnotatedText(boldText: String, normalText: String) {
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(fontWeight = FontWeight.Bold)
            ) {
                append("$boldText ")
            }
            append(normalText)
        }
    )
}

@Composable
fun BulletText(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = "•",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(text = text)
    }
}

@Preview
@Composable
private fun PreviewComponent() {
    MyAsyncImage(
        url = "",
        modifier = Modifier
            .size(160.dp)
            .shimmerEffect()
    )
}


