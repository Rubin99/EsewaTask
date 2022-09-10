package com.esewa.esewatask.premiumUsers

import com.esewa.esewatask.shared.remoteConfigResponse.UserList
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.hannesdorfmann.mosby3.mvp.MvpView

/**
 * Created by Rubin on 9/10/2022
 */
interface PremiumUserView : MvpView{
    fun displayRemoteConfigUpdate(firebaseRemoteConfig: FirebaseRemoteConfig, userList: UserList?)
    fun remoteConfigError()
}