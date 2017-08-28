package it.centotrenta.expridge.Utilities

data class Item(
    var name: String = "",
    var notificationStatus: Boolean = false,
    var date: Long = 0,
    var ownerId: Long = 0,
    var imageId: Int = 0,
    var id: Long = 0
    )
