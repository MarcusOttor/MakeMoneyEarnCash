package com.makemoney.earncash.freecash.core.advertisements

import android.app.Activity
import android.widget.TextView
import com.adcolony.sdk.AdColony
import com.adcolony.sdk.AdColonyInterstitial
import com.adcolony.sdk.AdColonyInterstitialListener
import com.adcolony.sdk.AdColonyZone
import com.makemoney.earncash.freecash.core.analytics.Analytics
import com.makemoney.earncash.freecash.core.managers.CoinsManager
import com.makemoney.earncash.freecash.core.managers.PreferencesManager
import com.makemoney.earncash.freecash.screens.BaseActivity

class AdcolonyVideo {

    private lateinit var coins: TextView
    private var prefernceManager: PreferencesManager
    private var adcolonyVideo: AdColonyInterstitial? = null
    private var coinManager: CoinsManager

    constructor(preferencesManager: PreferencesManager, coinManager: CoinsManager) {
        this.coinManager = coinManager
        this.prefernceManager = preferencesManager
    }

    fun init(activity: Activity) {
        AdColony.configure(activity, "app563aa7f0921545838a", "vz70af5c6eb9694401b6")
        AdColony.setRewardListener { adColonyReward ->
            if (!prefernceManager.get(PreferencesManager.ADDITIONAL_LIFE, false)) {
                coinManager.addCoins(adColonyReward.rewardAmount * 0.01f)
                coins.text = BaseActivity.format(coinManager.getCoins())
                Analytics.report(Analytics.VIDEO, Analytics.ADCOLONY, Analytics.REWARD)
            } else {
                prefernceManager.put(PreferencesManager.ADDITIONAL_LIFE, false)
                prefernceManager.put(PreferencesManager.TICKETS_LIFES, 1)
                Analytics.report(Analytics.VIDEO, Analytics.ADCOLONY, Analytics.GAME_BOOST)
            }
        }
        onResume(activity)
    }

    fun showVideo(coins: TextView): Boolean {
        this.coins = coins
        if (adcolonyVideo != null) {
            if (adcolonyVideo!!.show()) {
                Analytics.report(Analytics.VIDEO, Analytics.ADCOLONY, Analytics.OPEN)
                return true
            }
        }
        return false
    }

    fun onResume(activity: Activity) {
        if (adcolonyVideo == null || adcolonyVideo!!.isExpired) {
            AdColony.requestInterstitial("vz70af5c6eb9694401b6",
                    object : AdColonyInterstitialListener() {
                override fun onRequestFilled(ad: AdColonyInterstitial) {
                    adcolonyVideo = ad
                }

                override fun onRequestNotFilled(zone: AdColonyZone?) {
                    super.onRequestNotFilled(zone)
                }

                override fun onExpiring(ad: AdColonyInterstitial?) {
                    super.onExpiring(ad)
                    AdColony.requestInterstitial("vz70af5c6eb9694401b6", this)
                }
            })
        }
    }
}
