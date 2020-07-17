package br.com.pedrosilva.tecnonutri.presentation.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import br.com.pedrosilva.tecnonutri.R
import br.com.pedrosilva.tecnonutri.data.repositories.FeedRepositoryImpl
import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem
import br.com.pedrosilva.tecnonutri.domain.entities.Profile
import br.com.pedrosilva.tecnonutri.domain.executor.impl.ThreadExecutor
import br.com.pedrosilva.tecnonutri.presentation.navigation.Navigator
import br.com.pedrosilva.tecnonutri.presentation.presenters.FeedItemPresenter
import br.com.pedrosilva.tecnonutri.presentation.presenters.impl.FeedItemPresenterImpl
import br.com.pedrosilva.tecnonutri.presentation.ui.adapters.FoodAdapter
import br.com.pedrosilva.tecnonutri.presentation.ui.components.CircleTransformation
import br.com.pedrosilva.tecnonutri.threading.MainThreadImpl
import br.com.pedrosilva.tecnonutri.util.AppUtil
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception
import java.util.*

class PostDetailsActivity : BaseActivity(), FeedItemPresenter.View {

    private var feedHash = ""
    private var itemDate: Date? = null
    private var profile: Profile? = null
    private var feedItem: FeedItem? = null

    private var feedItemPresenter: FeedItemPresenterImpl? = null
    private var foodAdapter: FoodAdapter? = null

    private var rlHeader: RelativeLayout? = null
    private var ivProfileImage: ImageView? = null
    private var tvProfileName: TextView? = null
    private var tvGeneralGoal: TextView? = null
    private var ivMeal: ImageView? = null
    private var cbLike: CheckBox? = null

    private var rvFood: RecyclerView? = null
    private var swipeRefresh: SwipeRefreshLayout? = null
    private var pbLoadingImage: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        bindElements()
        bindProgress()
        showProgress()

        init()
    }

    override fun onResume() {
        super.onResume()
        if (feedItem != null) {
            cbLike!!.isChecked = feedItemPresenter!!.isLiked(feedHash)
        }
    }

    private fun bindElements() {
        rlHeader = findViewById(R.id.rl_header)
        ivProfileImage = findViewById(R.id.iv_profile_image)
        tvProfileName = findViewById(R.id.tv_profile_name)
        tvGeneralGoal = findViewById(R.id.tv_profile_general_goal)
        ivMeal = findViewById(R.id.iv_meal)
        pbLoadingImage = findViewById(R.id.pb_loading_image)
        swipeRefresh = findViewById(R.id.swipe_refresh)
        rvFood = findViewById(R.id.rv_food)
        cbLike = findViewById(R.id.cb_like)
    }

    private fun init() {
        setupRecyclerView();

        feedHash = intent.getStringExtra(EXTRA_FEED_ITEM_HASH) ?: ""
        itemDate = intent.getSerializableExtra(EXTRA_FEED_ITEM_DATE) as Date

        val dateFormatted = AppUtil.formatDate(itemDate)
        title = getString(R.string.meal_date, dateFormatted)


        rlHeader!!.setOnClickListener {
            if (profile != null) {
                Navigator.navigateToProfile(this, profile!!.id, profile!!.name)
            }
        }

        swipeRefresh!!.setOnRefreshListener { feedItemPresenter!!.reload() }

        cbLike!!.setOnClickListener { view ->
            feedItemPresenter!!.changeLike(feedHash, (view as CheckBox).isChecked)
        }

        feedItemPresenter = FeedItemPresenterImpl(
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(),
                this,
                FeedRepositoryImpl(this),
                feedHash
        )
    }

    override fun onLoadFeedItem(item: FeedItem) {
        profile = item.profile
        feedItem = item

        Picasso.get()
                .load(item.profile.imageUrl)
                .placeholder(R.drawable.profile_image_placeholder)
                .error(R.drawable.profile_image_placeholder)
                .transform(CircleTransformation())
                .fit()
                .centerCrop()
                .into(ivProfileImage)

        Picasso.get()
                .load(item.imageUrl)
                .fit()
                .into(ivMeal, object : Callback {
                    override fun onSuccess() {
                        pbLoadingImage?.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        // do nothing
                    }

                })

        cbLike!!.isChecked = item.isLiked
        tvProfileName!!.text = item.profile.name
        tvGeneralGoal!!.text = item.profile.generalGoal
        foodAdapter!!.setFoodItem(feedItem)

        swipeRefresh!!.isRefreshing = false
    }

    override fun onLoadFail(t: Throwable?) {
        swipeRefresh!!.isRefreshing = false
        showError(getString(R.string.fail_to_load_try_again))
    }

    private fun setupRecyclerView() {
        foodAdapter = FoodAdapter(this)
        rvFood!!.layoutManager = LinearLayoutManager(this);
        rvFood!!.adapter = foodAdapter
    }

    override fun onChangeLike(feedHash: String, like: Boolean) {

    }

    override fun reloadAll() {
        feedItemPresenter!!.load()
    }

    companion object {

        private const val EXTRA_FEED_ITEM_HASH = "EXTRA_FEED_ITEM_HASH"
        private const val EXTRA_FEED_ITEM_DATE = "EXTRA_FEED_ITEM_DATE"

        fun getCallingIntent(context: Context, feedHash: String, date: Date): Intent {
            val intent = Intent(context, PostDetailsActivity::class.java)
            intent.putExtra(EXTRA_FEED_ITEM_HASH, feedHash)
            intent.putExtra(EXTRA_FEED_ITEM_DATE, date)
            return intent
        }
    }
}
