package edu.ncsu.biomap

import java.io.Serializable

sealed class SupportedUserType(val resId: Int) : Serializable {
    class Calibrator(resId: Int = R.string.calibrator) : SupportedUserType(resId = resId)
    class Researcher(resId: Int = R.string.researcher) : SupportedUserType(resId = resId)
    class Farmer(resId: Int = R.string.farmer) : SupportedUserType(resId = resId)
}
