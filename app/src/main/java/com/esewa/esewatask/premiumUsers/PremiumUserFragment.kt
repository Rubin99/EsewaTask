package com.esewa.esewatask.premiumUsers

import android.R.attr
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esewa.esewatask.R
import com.esewa.esewatask.adapter.firestoredata.NameListAdapter
import com.esewa.esewatask.shared.NameData
import com.esewa.esewatask.shared.remoteConfigResponse.UserList
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.hannesdorfmann.mosby3.mvp.MvpFragment
import android.R.attr.data
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList
import android.R.attr.data
import android.R.attr.data








class PremiumUserFragment : MvpFragment<PremiumUserView, PremiumUserPresenter>(), PremiumUserView {

    private lateinit var nameDataList: MutableList<NameData>
    var nameListAdapter: NameListAdapter? = null
    var btnSortByAge: Button? = null
    var btnSortByMale: Button? = null
    var btnSortByFemale: Button? = null
    var btnSortByReset: Button? = null
    var lnlNames: LinearLayout? = null
    private var fbAnalytics: FirebaseAnalytics? = null
    lateinit var remoteConfig: FirebaseRemoteConfig

    var count: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_premium_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initRemoteConfig()
    }

    private fun initViews(view: View) {
        fbAnalytics = FirebaseAnalytics.getInstance(requireContext())
        btnSortByAge = view.findViewById(R.id.btnSortByAgeP)
        btnSortByMale = view.findViewById(R.id.btnSortByMaleP)
        btnSortByFemale = view.findViewById(R.id.btnSortByFemaleP)
        btnSortByReset = view.findViewById(R.id.btnSortByResetP)
        lnlNames = view.findViewById(R.id.lnlNamesP)
        nameDataList = ArrayList()
        val rcvName: RecyclerView = view.findViewById(R.id.rcvNamesP)
        rcvName.layoutManager = LinearLayoutManager(requireContext())
        nameListAdapter = NameListAdapter(requireContext(), nameDataList)
        rcvName.adapter = nameListAdapter

        initClickListeners()

    }

    private fun initRemoteConfig() {
        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig?.setDefaultsAsync(R.xml.remote_config_defaults)

        presenter.fetchRemoteConfigData(remoteConfig, requireActivity())
    }

    private fun initClickListeners() {
        btnSortByMale?.setOnClickListener(View.OnClickListener {
            sortByMale()
            var bundle: Bundle = Bundle()
            bundle.putString("male", "Sort by Male")
            fbAnalytics?.logEvent("btnSortByMale_Premium", bundle)
        })

        btnSortByAge?.setOnClickListener(View.OnClickListener {
            sortByAge()
            var bundle: Bundle = Bundle()
            bundle.putString("age", "Sort by Age")
            fbAnalytics?.logEvent("btnSortByAge_Premium", bundle)
        })

        btnSortByFemale?.setOnClickListener(View.OnClickListener {
            sortByFemale()
            var bundle: Bundle = Bundle()
            bundle.putString("female", "Sort by Female")
            fbAnalytics?.logEvent("btnSortByFemale_Premium", bundle)
        })

        btnSortByReset?.setOnClickListener(View.OnClickListener {
            resetList()
            count = 0
            presenter.fetchRemoteConfigData(remoteConfig, requireActivity())
        })
    }

    private fun sortByAge() {
        if (count % 2 == 0){
            Collections.sort(nameDataList,
                Comparator<NameData> { o1, o2 -> o1.age!!.compareTo(o2.age!!) })
            count++
        } else{
            Collections.sort(nameDataList,
                Comparator<NameData> { o1, o2 -> o2.age!!.compareTo(o1.age!!) })
            count++
        }
        nameListAdapter?.notifyDataSetChanged()
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

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PremiumUserFragment().apply {
            }
    }

    override fun createPresenter() = PremiumUserPresenter()

    override fun displayRemoteConfigUpdate(
        firebaseRemoteConfig: FirebaseRemoteConfig,
        userList: UserList?
    ) {

        if (userList != null){
            Log.d("NameData", userList.toString())
            for (i in userList){
                var nameData = NameData(name = i.name, age = i.age, sex = i.sex)
                nameDataList.add(nameData)
            }
            nameListAdapter?.notifyDataSetChanged()
        }
    }

    override fun remoteConfigError() {
        TODO("Not yet implemented")
    }
}