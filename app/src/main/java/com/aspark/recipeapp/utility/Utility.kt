package com.aspark.recipeapp.utility

fun parseHtml(string: String): List<String> {

    val isHtml = string.trim().startsWith("<") && string.trim().endsWith(">")

   return if (isHtml) {
        string
            .removePrefix("<ol>")
            .removeSuffix("</ol>")
            .split("</li>")
            .map { it.removePrefix("<li>").trim() }
            .filter { it.isNotEmpty() }
    }
    else {
            string
                .split(Regex("[\n.]"))
                .map { it.trim() }
                .filter { it.isNotEmpty() }
    }
}