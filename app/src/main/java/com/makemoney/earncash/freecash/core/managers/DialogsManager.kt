package com.makemoney.earncash.freecash.core.managers

import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import com.makemoney.earncash.freecash.screens.dialogs.*

class DialogsManager {

    companion object {
        var PROGRESS = "PROGRESS"
        var SIGNUP = "SIGNUP"
        var LOGIN = "LOGIN"
        var ALERT = "ALERT"
        var ADV_ALERT = "ADV_ALERT"
        var PROMO = "PROMO"
        var REDEEM = "REDEEM"
    }

    fun showLoginDialog(fm: FragmentManager, onLogin: () -> Unit) {
        var dialog = LoginDialog(onLogin)
        dialog.isCancelable = false
        show(fm, dialog, LOGIN)
    }

    fun showSignupDialog(fm: FragmentManager, onSignup: () -> Unit) {
        var dialog = SignupDialog(onSignup)
        dialog.isCancelable = false
        show(fm, dialog, SIGNUP)
    }

    fun showProgressDialog(fm: FragmentManager) : ProgressDialog {
        var dialog = ProgressDialog()
        dialog.isCancelable = false
        show(fm, dialog, PROGRESS)
        return dialog
    }

    fun showAlertDialog(fm: FragmentManager, alert: String, onOk: () -> Unit) {
        var dialog = AlertDialog(alert, onOk)
        dialog.isCancelable = false
        show(fm, dialog, ALERT)
    }

    fun showAdvAlertDialog(fm: FragmentManager, alert: String, onOk: () -> Unit, onCancel: () -> Unit) {
        var dialog = AdvAlertDialog(alert, onOk, onCancel)
        dialog.isCancelable = false
        show(fm, dialog, ADV_ALERT)
    }

    fun showPromoDialog(fm: FragmentManager, done: () -> Unit) {
        var dialog = PromocodeDialog(done)
        dialog.isCancelable = false
        show(fm, dialog, PROMO)
    }

    fun showRedeemDialog(fm: FragmentManager) {
        var dialog = RedeemDialog()
        dialog.isCancelable = true
        show(fm, dialog, REDEEM)
    }

    private fun show (fm: FragmentManager, dialog: DialogFragment, tag: String) {
        dialog.show(fm, tag)
    }
}
