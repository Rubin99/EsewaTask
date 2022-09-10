package com.esewa.esewatask.freeUsers

import android.app.Activity
import android.util.Log
import com.google.firebase.firestore.*
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter

/**
 * Created by Rubin on 9/10/2022
 */
class FreeUserPresenter : MvpBasePresenter<FreeUserView>() {

    fun fetchRemoteConfigData(remoteConfig: FirebaseRemoteConfig, activity: Activity) {
        ifViewAttached { view ->
            remoteConfig?.fetchAndActivate()
                ?.addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        val updated = task.result
                        Log.d("RemoteConfig", "Config params updated: $updated")
                        view.displayRemoteConfigUpdate(remoteConfig)
                    } else {
                        view.remoteConfigError()
                    }
                }
        }
    }

    fun checkFirestore(fireStore: FirebaseFirestore) {
        fireStore.collection("users_list")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("FirestoreRead", document.data.toString())
                }
            }
            .addOnFailureListener { exception ->
                Log.w("FirestoreRead", "Error getting documents.", exception)
            }
    }

    fun eventChangeListener(fireStore: FirebaseFirestore) {
        ifViewAttached { view ->
            fireStore.collection("users_list")
                .addSnapshotListener(object : EventListener<QuerySnapshot?> {
                    override fun onEvent(
                        value: QuerySnapshot?,
                        error: FirebaseFirestoreException?
                    ) {
                        if (error != null) {
                            Log.d("FireStoreError", error.message.toString())
                            return
                        }
                        if (value != null) {
                            view.fireStoreChangeListener(value)
                        }
                    }
                })
        }
    }
}