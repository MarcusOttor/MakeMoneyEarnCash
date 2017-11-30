package com.makemoney.earncash.freecash.inject

import com.makemoney.earncash.freecash.core.MyApplication
import com.makemoney.earncash.freecash.core.services.ClaimService
import com.makemoney.earncash.freecash.screens.BaseActivity
import com.makemoney.earncash.freecash.screens.dialogs.LoginDialog
import com.makemoney.earncash.freecash.screens.dialogs.PromocodeDialog
import com.makemoney.earncash.freecash.screens.dialogs.RedeemDialog
import com.makemoney.earncash.freecash.screens.dialogs.SignupDialog
import dagger.Component

@Component(modules = arrayOf(AppModule::class, MainModule::class))
interface AppComponent {

    fun inject(screen: BaseActivity)
    fun inject(app: MyApplication)
    fun inject(dialog: LoginDialog)
    fun inject(dialog: SignupDialog)
    fun inject(dialog: PromocodeDialog)
    fun inject(dialog: RedeemDialog)
    fun inject(service: ClaimService)
}
