package edu.ncsu.biomap.dashboard

sealed class TopAppBarActionType {
    enum class DataCollection(val title: String) {
        CollectImage("Collect Image"),
        Save("Save"),
        Delete("Delete")
    }
    enum class DataReview(val title: String) {
        Upload("Upload"),
        Delete("Delete")
    }
}