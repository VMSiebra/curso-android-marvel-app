package com.example.core.data.network.response

data class CharacterResponse(
    val id: Int,
    val name: String,
    val originResponse: OriginResponse,
    val image: String
)
