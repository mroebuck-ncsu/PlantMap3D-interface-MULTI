package edu.ncsu.biomap.dashboard

sealed class TopAppBarActionType(open val title: String) {
    sealed class DataCollection(title: String) : TopAppBarActionType(title) {
        data class CollectImage(override val title: String = "Collect Image") : DataCollection(title)
        data class Save(override val title: String = "Save") : DataCollection(title)
        data class Delete(override val title: String = "Delete") : DataCollection(title)

        companion object {
            fun values(): Array<DataCollection> = arrayOf(
                CollectImage(),
                Save(),
                Delete())
        }
    }

    sealed class DataReview(override val title: String) : TopAppBarActionType(title) {
        data class Upload(override val title: String = "Upload") : DataReview(title)
        data class Delete(override val title: String = "Delete") : DataReview(title)

        companion object {
            fun values(): Array<DataReview> = arrayOf(Upload(), Delete())
        }
    }
}