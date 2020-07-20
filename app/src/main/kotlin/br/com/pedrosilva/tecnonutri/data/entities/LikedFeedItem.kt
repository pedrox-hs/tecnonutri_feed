package br.com.pedrosilva.tecnonutri.data.entities

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

internal open class LikedFeedItem(
    @PrimaryKey
    var feedHash: String? = null
) : RealmObject()