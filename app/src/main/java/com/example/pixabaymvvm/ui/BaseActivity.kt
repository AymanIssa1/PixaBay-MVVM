package com.example.pixabaymvvm.ui

import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.pixabaymvvm.R
import com.example.pixabaymvvm.views.BannerInfoView
import com.google.android.material.appbar.AppBarLayout
import com.zplesac.connectionbuddy.ConnectionBuddy
import com.zplesac.connectionbuddy.interfaces.ConnectivityChangeListener
import com.zplesac.connectionbuddy.models.ConnectivityEvent
import com.zplesac.connectionbuddy.models.ConnectivityState
import org.jetbrains.anko.childrenRecursiveSequence

abstract class BaseActivity : AppCompatActivity(), ConnectivityChangeListener {

    private var bannerInfoView: BannerInfoView? = null

    override fun onStart() {
        super.onStart()
        ConnectionBuddy.getInstance().registerForConnectivityEvents(this, this)
    }

    override fun onStop() {
        super.onStop()
        ConnectionBuddy.getInstance().unregisterFromConnectivityEvents(this)
    }

    override fun onResume() {
        super.onResume()
        if (connectivityState != null)
            if (connectivityState!!.value == ConnectivityState.CONNECTED)
                hideBanner()

    }

    override fun onConnectionChange(event: ConnectivityEvent) {
        connectivityState = event.state
        if (event.state.value == ConnectivityState.CONNECTED) {
            if (!isInternetConnectedViewShowed)
                showInternetConnectedBanner()
        } else if (event.state.value == ConnectivityState.DISCONNECTED) {
            showNoInternetBanner()
            isInternetConnectedViewShowed = false
        }
    }

    fun showInfoBanner(title: String, description: String) {
        hideBanner()
        showBanner(
            title,
            description,
            BannerInfoView.BannerState.INFO
        )

    }

    private fun showInternetConnectedBanner() {
        hideBanner()
        showBanner(
            getString(R.string.internet_connected_title),
            getString(R.string.internet_connected_description),
            BannerInfoView.BannerState.INTERNET_CONNECTED
        )

        val handler = Handler()
        handler.postDelayed({
            hideBanner()
        }, 3000)
    }

    private fun showNoInternetBanner() {
        hideBanner()
        showBanner(
            getString(R.string.no_internet_title),
            getString(R.string.no_internet_description),
            BannerInfoView.BannerState.NO_INTERNET
        )
    }

    private fun showBanner(title: String, description: String, bannerState: BannerInfoView.BannerState) {
        if (bannerInfoView == null) {
            addBannerToActivity(title, description, bannerState)
        } else if (bannerInfoView!!.bannerState != bannerState) {
            bannerInfoView!!.titleText = title
            bannerInfoView!!.descriptionText = description
            bannerInfoView!!.setStyle(bannerState)
            bannerInfoView!!.show()
        } else if (!bannerInfoView!!.isShown) {
            bannerInfoView!!.titleText = title
            bannerInfoView!!.descriptionText = description
            bannerInfoView!!.setStyle(bannerState)
            bannerInfoView!!.show()
        }
    }

    fun hideBanner() {
        bannerInfoView?.dismiss()
    }

    private fun addBannerToActivity(title: String, description: String, bannerState: BannerInfoView.BannerState) {
        for (view in window.decorView.rootView.childrenRecursiveSequence()) {
            if (view is AppBarLayout) {
                bannerInfoView = BannerInfoView(this)
                bannerInfoView!!.titleText = title
                bannerInfoView!!.descriptionText = description
                bannerInfoView!!.setStyle(bannerState)
                view.addView(bannerInfoView, 1)
                bannerInfoView!!.show()
            }
        }
    }


    companion object {
        private var isInternetConnectedViewShowed = true
        private var connectivityState: ConnectivityState? = null
    }


}