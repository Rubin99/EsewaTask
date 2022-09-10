package com.esewa.esewatask.premiumUsers

import android.app.Activity
import android.util.Log
import com.esewa.esewatask.shared.remoteConfigResponse.UserList
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter

/**
 * Created by Rubin on 9/10/2022
 */
class PremiumUserPresenter: MvpBasePresenter<PremiumUserView>() {

    fun fetchRemoteConfigData(remoteConfig: FirebaseRemoteConfig, activity: Activity) {
        ifViewAttached { view ->
            remoteConfig?.fetchAndActivate()
                ?.addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        val updated = task.result
                        Log.d("RemoteConfig", "Config params updated: $updated")
                        var userList: UserList = Gson().fromJson<UserList>(
                            remoteConfig.getString("userList"),
                            object : TypeToken<UserList?>() {}.type
                        )
                        view.displayRemoteConfigUpdate(remoteConfig, userList)
                    } else {
                        view.remoteConfigError()
                    }
                }
        }
    }

}