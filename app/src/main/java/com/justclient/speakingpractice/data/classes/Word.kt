package com.justclient.speakingpractice.data.classes

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Word(
    val id: String = UUID.randomUUID().toString(),
    var enWord: String,
    var spWord: String,
    var type: Int
)
