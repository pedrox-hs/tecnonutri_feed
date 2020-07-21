package br.com.pedrosilva.tecnonutri.presentation.ui.listeners

import com.pedrenrique.tecnonutri.domain.FeedItem
import com.pedrenrique.tecnonutri.domain.Profile

typealias ChangeLikeListener = (feedHash: String, liked: Boolean) -> Unit

typealias FeedItemClickListener = (feedItem: FeedItem) -> Unit

typealias ProfileClickListener = (profile: Profile) -> Unit