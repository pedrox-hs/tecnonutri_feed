package br.com.pedrosilva.tecnonutri.presentation.ui.listeners

import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem
import br.com.pedrosilva.tecnonutri.domain.entities.Profile

typealias ChangeLikeListener = (feedHash: String, liked: Boolean) -> Unit

typealias FeedItemClickListener = (feedItem: FeedItem) -> Unit

typealias ProfileClickListener = (profile: Profile) -> Unit