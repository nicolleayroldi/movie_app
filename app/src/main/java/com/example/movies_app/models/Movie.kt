package com.example.movies_app.model

data class Movie(
    val id: Int,
    val name: String,
    val premiered: String,
    val rating: Rating,
    val genres: List<String>,
    val summary: String,
    val image: Image
)

data class Rating(
    val average: Double
)

data class Image(
    val medium: String
)




