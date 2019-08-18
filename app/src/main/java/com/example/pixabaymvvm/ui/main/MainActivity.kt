package com.example.pixabaymvvm.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.Observer
import com.example.pixabaymvvm.HIT_EXTRA
import com.example.pixabaymvvm.R
import com.example.pixabaymvvm.models.Hit
import com.example.pixabaymvvm.ui.BaseActivity
import com.example.pixabaymvvm.ui.details.DetailsActivity
import com.example.pixabaymvvm.util.extensions.hideKeyboard

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private val model: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        search.setIconifiedByDefault(false)
        search.queryHint = getString(R.string.search)

        model.getHits("flowers") // to show something to the user

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query!!.length >= 3) {
                    model.getHits(query)
                    hideKeyboard()
                    return true
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.length >= 3) {
                    model.getHits(newText)
                    return true
                }
                return false
            }
        })



        model.getRatesLiveData().observe(this, Observer {
            if (it.isLoading) {
                progress_bar.visibility = View.VISIBLE
                hits_recycler_view.visibility = View.GONE
            } else if (it.isSuccessful) {
                if (it.hits!!.size > 0) {
                    progress_bar.visibility = View.GONE
                    hits_recycler_view.visibility = View.VISIBLE

                    hideBanner()
                    setHitRecyclerView(it.hits)
                } else {
                    progress_bar.visibility = View.GONE
                    hits_recycler_view.visibility = View.GONE
                    showInfoBanner(getString(R.string.no_images_found), getString(R.string.no_images_found_description))
                }
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchItem = menu.findItem(R.id.action_search)
        // Optional: if you want to expand SearchView from icon to edittext view
        searchItem.expandActionView()

        return false
    }

    private fun setHitRecyclerView(hitsList: ArrayList<Hit>) {
        if (hits_recycler_view.adapter == null) {
            hits_recycler_view.adapter = HitsAdapter(hitsList, onItemClickListener = { hit, viewHolder ->
                val pairCreatorImageView =
                    Pair.create<View, String>(viewHolder.hitImageView, getString(R.string.transition_hit_image))
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairCreatorImageView)
                val intent = Intent(this, DetailsActivity::class.java).apply {
                    putExtra(HIT_EXTRA, hit)
                }
                startActivity(intent, options.toBundle())
            })
        } else {
            (hits_recycler_view.adapter as HitsAdapter).setHits(hitsList)
            (hits_recycler_view.adapter as HitsAdapter).notifyDataSetChanged()
        }
    }

}
