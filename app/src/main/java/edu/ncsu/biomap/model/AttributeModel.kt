package edu.ncsu.biomap.model

import java.io.Serializable

data class AttributeModel(
    var attribute: String,
    var value: String,
    var options: List<String> = listOf(),
) : Serializable
