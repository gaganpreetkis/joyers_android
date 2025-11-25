package com.synapse.joyers.apiData.response

data class TitlesApiResponse(
    val success: Boolean? = null,
    val message: String? = null,
    val data: TitlesDataWrapper? = null
)

data class TitlesDataWrapper(
    val data: MutableList<Title>? = null,
    val pagination: Pagination? = null,
    val filters: Filters? = null,
    val sort: Sort? = null
)

data class Title(
    val _id: String? = null,
    val title: String? = null,
    val decriptionTitle: String? = null,
    var selected: Boolean = false,
    val subtitles: MutableList<Subtitle>? = null
)

data class Subtitle(
    val _id: String? = null,
    val uuid: String? = null,
    val name: String? = null,
    val description: String? = null,
    var selected: Boolean = false,
    val subtitles: MutableList<Subtitle>? = null
)

data class Pagination(
    val currentPage: Int? = null,
    val totalPages: Int? = null,
    val totalItems: Int? = null,
    val itemsPerPage: Int? = null
)

data class Filters(
    val search: String? = null,
    val status: String? = null,
    val joyerStatus: String? = null
)

data class Sort(
    val field: String? = null,
    val order: String? = null
)
