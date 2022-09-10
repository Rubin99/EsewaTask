package com.esewa.esewatask

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esewa.esewatask.adapter.NameData
import com.esewa.esewatask.adapter.NameListAdapter
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.common.collect.Collections2
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import java.util.Collections
import com.google.common.base.Predicates
import com.google.common.collect.Lists
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.get
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.graphics.ColorSpace.Model
import com.esewa.esewatask.remoteConfigResponse.UserList


class MainActivity : AppCompatActivity() {

    private lateinit var nameDataList: MutableList<NameData>
    private lateinit var maleDataList: MutableList<NameData>
    var nameListAdapter: NameListAdapter? = null
    var btnSortByAge: Button? = null
    var btnSortByMale: Button? = null
    var btnSortByFemale: Button? = null
    var btnSortByReset: Button? = null
    var lnlNames: LinearLayout? = null
    private var fbAnalytics: FirebaseAnalytics? = null
    lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        initFirestore()
        initRemoteConfig()
//        setUpFirestore(db)
    }

    private fun initRemoteConfig() {
        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        //Default values
//        var defaultData: MutableMap<String?, Any?>? = HashMap()
//        defaultData?.put("btnAgeEnable", true)
//        defaultData?.put("lnlNamesColor", "#5fb648")
//        defaultData?.put("btnFemaleText", "Female")
//        defaultData?.let { firebaseRemoteConfig?.setDefaultsAsync(it) }
        remoteConfig?.setDefaultsAsync(R.xml.remote_config_defaults)

        fetchRemoteConfigData()
    }

    private fun initViews() {
        fbAnalytics = FirebaseAnalytics.getInstance(this)
        btnSortByAge = findViewById(R.id.btnSortByAge)
        btnSortByMale = findViewById(R.id.btnSortByMale)
        btnSortByFemale = findViewById(R.id.btnSortByFemale)
        btnSortByReset = findViewById(R.id.btnSortByReset)
        lnlNames = findViewById(R.id.lnlNames)
        nameDataList = ArrayList()
        maleDataList = ArrayList()
        val rcvName: RecyclerView = findViewById(R.id.rcvNames)
        rcvName.layoutManager = LinearLayoutManager(this)
        nameListAdapter = NameListAdapter(this, nameDataList)
        rcvName.adapter = nameListAdapter

        btnSortByMale?.setOnClickListener(View.OnClickListener {
            sortByMale()
            var bundle: Bundle = Bundle()
            bundle.putString("male", "Sort by Male")
            fbAnalytics?.logEvent("btnSortByMale", bundle)
        })

        btnSortByAge?.setOnClickListener(View.OnClickListener {
            nameDataList.reverse()
            var bundle: Bundle = Bundle()
            bundle.putString("age", "Sort by Age")
            fbAnalytics?.logEvent("btnSortByAge", bundle)
        })

        btnSortByFemale?.setOnClickListener(View.OnClickListener {
            sortByFemale()
            var bundle: Bundle = Bundle()
            bundle.putString("female", "Sort by Female")
            fbAnalytics?.logEvent("btnSortByFemale", bundle)
        })

        btnSortByReset?.setOnClickListener(View.OnClickListener {
            resetList()
            fetchRemoteConfigData()
        })
    }

    private fun resetList() {
        val unFilteredList: ArrayList<NameData> = ArrayList() // Create Filter List
        val oldList: MutableList<NameData> = nameDataList //This is Old llist

        for (i in oldList) {
            unFilteredList.add(i)
        }

        nameListAdapter?.filterList(unFilteredList) // Send new List to Adapter
    }

    private fun sortByFemale() {
        val filteredFemaleList: ArrayList<NameData> = ArrayList() // Create Filter List
        val oldList: MutableList<NameData> = nameDataList //This is Old llist

        for (i in oldList) { // Filter list using for loop
            if (i.sex!!.contains("Female")
            ) {
                filteredFemaleList.add(i)
            }
        }

        nameListAdapter?.filterList(filteredFemaleList) // Send new List to Adapter
    }

    private fun sortByMale() {
        val filteredMaleList: ArrayList<NameData> = ArrayList() // Create Filter List
        val oldList: MutableList<NameData> = nameDataList //This is Old llist

        for (i in oldList) { // Filter list using for loop
            if (i.sex!!.contains("Male")
            ) {
                filteredMaleList.add(i)
            }
        }

        nameListAdapter?.filterList(filteredMaleList) // Send new List to Adapter
    }

    private fun initFirestore() {
        val db = Firebase.firestore
        fbAnalytics = FirebaseAnalytics.getInstance(this)
        checkFirestore(db)
        fetchFirestoreData()
        eventChangeListener(db)
    }

    private fun fetchRemoteConfigData() {
        remoteConfig?.fetchAndActivate()
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d("RemoteConfig", "Config params updated: $updated")
                    Toast.makeText(this, "Fetch and activate succeeded",
                        Toast.LENGTH_SHORT).show()

                    var userList: UserList = Gson().fromJson<UserList>(
                        remoteConfig.getString("userList"),
                        object : TypeToken<UserList?>() {}.type
                    )

                    if (userList != null){
                        Log.d("NameData", userList.toString())

                    }
                    remoteConfig?.getBoolean("btnAgeEnable")?.let { btnSortByMale?.setEnabled(it) }
                    lnlNames?.setBackgroundColor(Color.parseColor(remoteConfig?.getString("lnlNamesColor")))
                    btnSortByFemale?.text = remoteConfig?.getString("btnFemaleText")
                } else {
                    Toast.makeText(this, "Fetch failed",
                        Toast.LENGTH_SHORT).show()
                }
            }
//        firebaseRemoteConfig?.fetchAndActivate()?.addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//
//                firebaseRemoteConfig?.getBoolean("btnAgeEnable")?.let { btnSortByAge?.setEnabled(it) }
//                lnlNames?.setBackgroundColor(Color.parseColor(firebaseRemoteConfig?.getString("lnlNamesColor")))
//                btnSortByFemale?.text = firebaseRemoteConfig?.getString("btnFemaleText")
//
//            } else {
//                Log.d("RemoteConfigError", task.exception?.message.toString())
//            }
//        }
    }


    private fun eventChangeListener(db: FirebaseFirestore) {
        db.collection("users_list").orderBy("age", Query.Direction.ASCENDING)
            .addSnapshotListener(object : EventListener<QuerySnapshot?> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.d("FireStoreError", error.message.toString())
                        return
                    }
                    if (value != null) {
                        for (document: DocumentChange in value.documentChanges) {
                            if (document.type == DocumentChange.Type.ADDED) {
                                nameDataList.add(document.document.toObject(NameData::class.java))
                            }
                        }
                        nameListAdapter?.notifyDataSetChanged()
                    }
                }
            })
    }

    private fun fetchFirestoreData() {
    }

    private fun checkFirestore(db: FirebaseFirestore) {
        db.collection("users_list")
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

    private fun setUpFirestore(db: FirebaseFirestore) {


        // Create a new user with a first and last name
        val user = hashMapOf(
            "name" to "Rubin Shrestha",
            "age" to 23,
            "sex" to "Male"
        )

// Add a new document with a generated ID
        db.collection("users_list")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d("Firestore", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding document", e)
            }

        // Create a new user with a first, middle, and last name
        val user2 = hashMapOf(
            "name" to "Test Name",
            "age" to 50,
            "sex" to "Female"
        )

// Add a new document with a generated ID
        db.collection("users_list")
            .add(user2)
            .addOnSuccessListener { documentReference ->
                Log.d("Firestore", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding document", e)
            }
    }

}