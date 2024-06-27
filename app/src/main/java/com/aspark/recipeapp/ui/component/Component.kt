package com.aspark.recipeapp.ui.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.aspark.recipeapp.R
import com.aspark.recipeapp.ui.Screen

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
                    if (selected == Screen.Home) Screen.Home.selectedIcon
                    else Screen.Home.icon,
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
                    if (selected == Screen.Favorites) Screen.Favorites.selectedIcon
                    else Screen.Favorites.icon,
                    contentDescription = ""
                )
            }
        )
    }
}

@Composable
fun rememberMyAsyncPainter(
    url: String
) = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current).data(data = url)
            .apply(block = fun ImageRequest.Builder.() {
                crossfade(true)
                placeholder(R.drawable.ic_launcher_background)
            }).build()
    )

@Composable
fun BoldTitle(title: String) {
    Text(text = title, fontWeight = FontWeight.Bold,
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
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
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

