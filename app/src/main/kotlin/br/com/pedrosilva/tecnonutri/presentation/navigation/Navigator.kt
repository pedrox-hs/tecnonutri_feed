package br.com.pedrosilva.tecnonutri.presentation.navigation

import android.content.Context
import br.com.pedrosilva.tecnonutri.presentation.ui.activities.PostDetailsActivity
import br.com.pedrosilva.tecnonutri.presentation.ui.activities.ProfileActivity
import java.util.Date

object Navigator {

    fun navigateToProfile(context: Context, profileId: Int, profileName: String) {
        val intent = ProfileActivity.getCallingIntent(context, profileId, profileName)
        context.startActivity(intent)
    }

    fun navigateToFeedItemDetails(context: Context, feedId: String, feedDate: Date) {
        val intent = PostDetailsActivity.getCallingIntent(context, feedId, feedDate)
        context.startActivity(intent)
    }
}