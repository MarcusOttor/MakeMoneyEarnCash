package com.makemoney.earncash.freecash.screens.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import butterknife.ButterKnife
import butterknife.OnClick
import com.makemoney.earncash.freecash.AppTools
import com.makemoney.earncash.freecash.R
import com.makemoney.earncash.freecash.core.MyApplication
import com.makemoney.earncash.freecash.core.analytics.Analytics
import com.makemoney.earncash.freecash.core.managers.AnimationsManager
import com.makemoney.earncash.freecash.core.managers.CoinsManager
import com.makemoney.earncash.freecash.core.managers.DialogsManager
import com.makemoney.earncash.freecash.core.managers.PreferencesManager
import com.makemoney.earncash.freecash.inject.AppModule
import com.makemoney.earncash.freecash.inject.DaggerAppComponent
import com.makemoney.earncash.freecash.screens.BaseActivity
import com.makemoney.earncash.freecash.screens.MainActivity
import kotlinx.android.synthetic.main.dialog_redeem.view.*
import javax.inject.Inject
import kotlin.concurrent.thread

class RedeemDialog : DialogFragment() {

    @Inject lateinit var preferencesManager: PreferencesManager
    @Inject lateinit var coinsManager: CoinsManager
    @Inject lateinit var dialogsManager: DialogsManager
    @Inject lateinit var animations: AnimationsManager

    private var currentTab: Int = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        DaggerAppComponent.builder()
                .appModule(AppModule(context))
                .mainModule((activity.application as MyApplication).mainModule)
                .build().inject(this)

        var view = inflater?.inflate(R.layout.dialog_redeem, container, false)

        view?.redeemMoneyText?.text = "Cash out $${BaseActivity.format(coinsManager.getCoins())}"

        ButterKnife.bind(this, view!!)

        return view
    }

    @OnClick(R.id.paypal, R.id.visa)
    fun tabsClick(v: View) {
        when (v.id) {
            R.id.paypal -> {
                if (currentTab != 0) {
                    currentTab = 0
                    animations.scale(view?.rootView?.paypal!!, 1f, 400L, {}, {})
                    animations.scale(view?.rootView?.visa!!, 0.8f, 400L, {}, {})
                    view?.rootView?.emailText?.hint = "Paypal email address"
                }
            }
            R.id.visa -> {
                if (currentTab != 1) {
                    currentTab = 1
                    animations.scale(view?.rootView?.visa!!, 1.1f, 400L, {}, {})
                    animations.scale(view?.rootView?.paypal!!, 0.6f, 400L, {}, {})
                    view?.rootView?.emailText?.hint = "Email address"
                }
            }
        }
    }

    @OnClick(R.id.redeemBtn)
    fun redeem() {
        if (AppTools.isNetworkAvaliable(activity)) {
            if (coinsManager.getCoins() >= 100f) {
                if (AppTools.isEmailAdressCorrect(view?.rootView?.emailText?.text.toString())) {
                    var dismisser = dialogsManager.showProgressDialog(activity.supportFragmentManager)
                    thread {
                        Thread.sleep(3000)
                        Analytics.report(Analytics.WITHDRAW, Analytics.AMOUNT, coinsManager.getCoins().toString())
                        activity.runOnUiThread {
                            dismisser.dismiss()
                            coinsManager.subtractCoins(coinsManager.getCoins())
                            view?.rootView?.redeemMoneyText?.text = "Cash out $${BaseActivity.format(coinsManager.getCoins())}"
                            (activity as MainActivity).updateCoins()
                            dialogsManager.showAlertDialog(activity.supportFragmentManager,
                                    "You will receive your money in 3 - 7 days!", {
                                dismiss()
                            })
                        }
                    }
                } else {
                    dialogsManager.showAlertDialog(activity.supportFragmentManager,
                            "Email is not valid!", {
                        (activity as MainActivity).admobInterstitial?.show { }
                    })
                }
            } else {
                dialogsManager.showAlertDialog(activity.supportFragmentManager,
                        "Not enough money! You need at least $100", {
                    (activity as MainActivity).admobInterstitial?.show { }
                })
            }
        } else {
            dialogsManager.showAlertDialog(activity.supportFragmentManager,
                    "No internet connection!", {
                (activity as MainActivity).admobInterstitial?.show { }
            })
        }

    }
}