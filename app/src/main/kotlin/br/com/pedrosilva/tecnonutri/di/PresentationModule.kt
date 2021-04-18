package br.com.pedrosilva.tecnonutri.di

import br.com.pedrosilva.tecnonutri.features.feed.detail.FeedDetailActivity
import br.com.pedrosilva.tecnonutri.features.feed.detail.FeedDetailContract
import br.com.pedrosilva.tecnonutri.features.feed.detail.FeedDetailPresenter
import br.com.pedrosilva.tecnonutri.features.feed.list.FeedListActivity
import br.com.pedrosilva.tecnonutri.features.feed.list.FeedListContract
import br.com.pedrosilva.tecnonutri.features.feed.list.FeedListPresenter
import br.com.pedrosilva.tecnonutri.features.profile.ProfileActivity
import br.com.pedrosilva.tecnonutri.features.profile.ProfileContract
import br.com.pedrosilva.tecnonutri.features.profile.ProfilePresenter
import com.pedrenrique.tecnonutri.data.di.DataModule
import com.pedrenrique.tecnonutri.domain.di.DomainModule
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module(includes = [DataModule::class, DomainModule::class])
@InstallIn(ActivityComponent::class)
abstract class PresentationModule {

    @Binds
    abstract fun bindFeedListView(view: FeedListActivity): FeedListContract.View

    @Binds
    abstract fun bindFeedListPresenter(
        presenter: FeedListPresenter
    ): FeedListContract.Presenter

    @Binds
    abstract fun bindFeedDetailView(view: FeedDetailActivity): FeedDetailContract.View

    @Binds
    abstract fun bindFeedDetailPresenter(
        presenter: FeedDetailPresenter
    ): FeedDetailContract.Presenter

    @Binds
    abstract fun bindProfileView(view: ProfileActivity): ProfileContract.View

    @Binds
    abstract fun bindProfilePresenter(
        presenter: ProfilePresenter
    ): ProfileContract.Presenter
}