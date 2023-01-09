package edu.ncsu.biomap.util

import android.os.Build
import android.os.Bundle
import edu.ncsu.biomap.model.AttributeModel

fun Bundle?.serializable(key: String, clazz: Class<AttributeModel>) =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this?.getSerializable(key, clazz)
    } else {
        this?.getSerializable(key)
    }