package edu.ncsu.biomap.model

import java.io.Serializable

data class AttributeModel(
    var attribute: String,
    var value: String
) : Serializable
