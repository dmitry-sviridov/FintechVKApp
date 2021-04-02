package ru.sviridov.vkclient.network.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class City(
    val id: Int,
    val title: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Country(
    val id: Int,
    val title: String
)
