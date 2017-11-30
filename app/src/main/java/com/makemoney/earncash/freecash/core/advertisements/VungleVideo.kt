package com.makemoney.earncash.freecash.core.advertisements

import android.app.Activity
import android.widget.TextView
import com.makemoney.earncash.freecash.core.analytics.Analytics
import com.makemoney.earncash.freecash.core.managers.CoinsManager
import com.makemoney.earncash.freecash.core.managers.PreferencesManager
import com.makemoney.earncash.freecash.screens.BaseActivity
import com.vungle.publisher.EventListener
import com.vungle.publisher.VunglePub

class VungleVideo {

    private val vungle = VunglePub.getInstance()

    private var coinsText: TextView? = null
    var Coins: CoinsManager
    var prefernceManager: PreferencesManager

    constructor(Preferences: PreferencesManager, Coins: CoinsManager) {
        this.Coins = Coins
        this.prefernceManager = Preferences
    }

    fun init(activity: Activity) {
        vungle.init(activity, "59e65a5e7f2ced5b1700ea9c")

        vungle.setEventListeners(object : EventListener {
            override fun onAdEnd(b: Boolean, b1: Boolean) {
                if (b) {
                    if (!prefernceManager.get(PreferencesManager.ADDITIONAL_LIFE, false)) {
                        Coins.addCoins(0.01f)
                        Analytics.report(Analytics.VIDEO, Analytics.VUNGLE, Analytics.REWARD)
                        coinsText?.text = BaseActivity.format(Coins.getCoins())
                    } else {
                        prefernceManager.put(PreferencesManager.ADDITIONAL_LIFE, false)
                        prefernceManager.put(PreferencesManager.TICKETS_LIFES, 1)
                        Analytics.report(Analytics.VIDEO, Analytics.VUNGLE, Analytics.GAME_BOOST)
                    }
                }
            }

            override fun onAdStart() {
            }

            override fun onAdUnavailable(s: String) {}

            override fun onAdPlayableChanged(b: Boolean) {}

            override fun onVideoView(isCompletedView: Boolean, watchedMillis: Int, videoDurationMillis: Int) {
            }
        })
    }

    fun showVideo(coinView: TextView): Boolean {
        this.coinsText = coinView
        if (vungle.isAdPlayable) {
            vungle.playAd()
            Analytics.report(Analytics.VIDEO, Analytics.VUNGLE, Analytics.OPEN)
            return true
        }
        return false
    }

    fun onResume() {
        vungle.onResume()
    }
}
