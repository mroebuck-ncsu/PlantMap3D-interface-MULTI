package edu.ncsu.biomap.dashboard

enum class DashboardKeys(name: String) {
    Attribute(name = "ATTRIBUTE"),
    Value(name = "VALUE");

    companion object {
        fun toDashboardKeys(name: String): DashboardKeys {
            values().map {
                if(name == it.name) {
                    return it
                }
            }
            throw Throwable("Unknown Dashboard Key=[$name]")
        }
    }
}