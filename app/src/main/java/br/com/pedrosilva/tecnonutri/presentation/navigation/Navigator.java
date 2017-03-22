package br.com.pedrosilva.tecnonutri.presentation.navigation;

import android.content.Context;
import android.content.Intent;

import java.util.Date;

import br.com.pedrosilva.tecnonutri.presentation.ui.activities.PostDetailsActivity;
import br.com.pedrosilva.tecnonutri.presentation.ui.activities.ProfileActivity;

/**
 * Created by psilva on 3/17/17.
 */

public abstract class Navigator {
    public static void navigateToProfile(Context context, int profileId, String profileName) {
        if (context != null) {
            Intent intent = ProfileActivity.Companion.getCallingIntent(context, profileId, profileName);
            context.startActivity(intent);
        }
    }

    public static void navigateToFeedItemDetails(Context context, String feedHash, Date itemDate) {
        if (context != null) {
            Intent intent = PostDetailsActivity.Companion.getCallingIntent(context, feedHash, itemDate);
            context.startActivity(intent);
        }
    }
}
