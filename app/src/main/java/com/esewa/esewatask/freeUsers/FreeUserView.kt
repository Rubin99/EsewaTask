package com.esewa.esewatask.freeUsers

import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.hannesdorfmann.mosby3.mvp.MvpView

/**
 * Created by Rubin on 9/10/2022
 */
interface FreeUserView: MvpView {
fun displayRemoteConfigUpdate(firebaseRemoteConfig: FirebaseRemoteConfig)
fun remoteConfigError()
fun fireStoreChangeListener(value: QuerySnapshot?)
}