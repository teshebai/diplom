package kz.sdk.shina.models

data class User(
    var name: String? = null,
    var lastname: String?= null,
    var pictureUrl: String? = null,
    var favorites: Map<String, Product> = emptyMap(),
    var favoritesRent: Map<String, Product> = emptyMap(),
)
