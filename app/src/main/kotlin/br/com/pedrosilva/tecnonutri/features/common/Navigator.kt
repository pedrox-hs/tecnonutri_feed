package br.com.pedrosilva.tecnonutri.features.common

import android.content.Context
import br.com.pedrosilva.tecnonutri.features.feed.detail.FeedDetailActivity
import br.com.pedrosilva.tecnonutri.features.profile.ProfileActivity
import java.util.Date

object Navigator {

    fun navigateToProfile(context: Context, profileId: Int, profileName: String) {
        val intent = ProfileActivity.getCallingIntent(context, profileId, profileName)
        context.startActivity(intent)
    }

    fun navigateToFeedItemDetails(context: Context, feedId: String, feedDate: Date) {
        val intent = FeedDetailActivity.getCallingIntent(context, feedId, feedDate)
        context.startActivity(intent)
    }
}