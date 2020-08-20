package com.example.githubuserapi.main.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapi.R
import com.example.githubuserapi.adapter.UserAdapter
import com.example.githubuserapi.api.ApiConfig
import com.example.githubuserapi.model.GithubUser
import kotlinx.android.synthetic.main.fragment_detail_follow.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class DetailFollowFragment : Fragment() {

    companion object {
        private val ARG_USERNAME = "arg username"
        private val ARG_TAB = "arg tab"

        fun newInstance(username : String?, tab : String?) : DetailFollowFragment {
            val fragment =
                DetailFollowFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            bundle.putString(ARG_TAB, tab)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var githubUser = arrayListOf<GithubUser>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail_follow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var username = "username"
        var tab = "tab"
        if (arguments != null){
            username = arguments?.getString(ARG_USERNAME, "username") as String
            tab = arguments?.getString(ARG_TAB, "tab") as String
        }
        initRecyclerView()
        getFollowApi(username, tab)
    }

    private fun initRecyclerView(){
        recycler_view_fragment.layoutManager = LinearLayoutManager(activity)
        recycler_view_fragment.setHasFixedSize(true)
        val listUserAdapter = UserAdapter(githubUser)
        recycler_view_fragment.adapter = listUserAdapter
    }

    private fun getFollowApi(username : String, follow : String){

        val client = ApiConfig.getApiServices().getDetailFollow(username, follow)
        client.enqueue(object : Callback<ArrayList<GithubUser>>{

            override fun onFailure(call: Call<ArrayList<GithubUser>>, t: Throwable) {
                Toast.makeText(activity, "Failed load", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }

            override fun onResponse(call: Call<ArrayList<GithubUser>>, response: Response<ArrayList<GithubUser>>) {
                try {
                    val dataArray = response.body() as ArrayList
                    for (data in dataArray){
                        githubUser.add(data)
                    }
                    initRecyclerView()
                } catch (e : Exception){
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

        })
    }
}