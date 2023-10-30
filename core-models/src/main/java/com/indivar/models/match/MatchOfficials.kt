package com.indivar.models.match

data class MatchOfficials (
    val firstUmpire: String,
    val secondUmpire: String,
    val thirdUmpire: String,
    val referee: String,
    val reserveUmpire: String?,
)