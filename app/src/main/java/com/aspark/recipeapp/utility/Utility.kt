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

fun parseSummary(summary: String): Map<String, String> {
    val map = mutableMapOf<String, String>()

    val boldPattern = "<b>(.*?)</b>".toRegex()
    val boldMatches = boldPattern.findAll(summary).forEach { matchResult ->
        when {
            matchResult.value.contains("protein") ->
                map["protein"] = matchResult.groupValues[1].removeSuffix(" of protein")

            matchResult.value.contains("calories") ->
                map["calories"] = matchResult.groupValues[1].removeSuffix(" calories")

            matchResult.value.contains("fat") ->
                map["fat"] = matchResult.groupValues[1].removeSuffix(" of fat")

            matchResult.value.contains("covers") ->
                map["nutritionCoverage"] = matchResult.groupValues[1].removePrefix("covers ")

            matchResult.value.contains("spoonacular score") -> {
                val lastIndexWhiteSpace = matchResult
                    .groupValues[1]
                    .lastIndexOf(" ")

                map["score"] = matchResult.groupValues[1].removeRange(0, lastIndexWhiteSpace)
            }
        }
    }
//
//        // Extract the dish name (assuming it's the first sentence)
//        val dishName = summary.split(".").firstOrNull()?.trim()
//        if (dishName != null) {
//            map["dishName"] = dishName
//        }
//
//        // Extract the number of servings
//        val servingsPattern = "serves (\\d+)".toRegex()
//        servingsPattern.find(summary)?.let {
//            map["servings"] = it.groupValues[1]
//        }

    return map
}