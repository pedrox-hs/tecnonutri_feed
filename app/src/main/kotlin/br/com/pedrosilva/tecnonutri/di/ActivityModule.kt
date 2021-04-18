package br.com.pedrosilva.tecnonutri.di

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import br.com.pedrosilva.tecnonutri.features.feed.detail.FeedDetailActivity
import br.com.pedrosilva.tecnonutri.features.feed.list.FeedListActivity
import br.com.pedrosilva.tecnonutri.features.profile.ProfileActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import java.security.InvalidParameterException

@Module
@InstallIn(ActivityComponent::class)
internal class ActivityModule {
    @Provides
    fun provideFeedListActivity(
        @ActivityContext context: Context
    ): FeedListActivity = context.asActivity()

    @Provides
    fun provideFeedDetailActivity(
        @ActivityContext context: Context
    ): FeedDetailActivity = context.asActivity()

    @Provides
    fun provideProfileActivity(
        @ActivityContext context: Context
    ): ProfileActivity = context.asActivity()
}

@Suppress("UNCHECKED_CAST")
private fun <T : AppCompatActivity> Context.asActivity() =
    this as? T ?: throw InvalidParameterException() // WorkAround to provide activity instance