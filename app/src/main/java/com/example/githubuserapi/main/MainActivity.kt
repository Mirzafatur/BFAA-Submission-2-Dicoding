package com.example.githubuserapi.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuserapi.R
import com.example.githubuserapi.adapter.UserAdapter
import com.example.githubuserapi.api.ApiConfig
import com.example.githubuserapi.model.GithubUser
import com.example.githubuserapi.model.ResponseUser
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var rvGithubUser : RecyclerView
    private lateinit var userAdapter : UserAdapter
    private var githubUser = arrayListOf<GithubUser>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvGithubUser = recycler_view
        getUserApi()
        initRecyclerView()
    }

    private fun initRecyclerView(){
        recycler_view.layoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter(githubUser)
        rvGithubUser.adapter = userAdapter
        rvGithubUser.setHasFixedSize(true)
    }

    private fun getUserApi(){
        val client = ApiConfig.getApiServices().getApi()
        client.enqueue(object : Callback<List<GithubUser>>{

            override fun onFailure(call: Call<List<GithubUser>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed load", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
                progressBar.visibility = View.INVISIBLE
            }

            override fun onResponse(call: Call<List<GithubUser>>, response: Response<List<GithubUser>>) {
                val githubUser = response.body()
                userAdapter.setData(githubUser as ArrayList<GithubUser>)
                progressBar.visibility = View.INVISIBLE
            }

        })
    }

    private fun searchUser(username : String?){

        val client = ApiConfig.getApiServices().getSearchUser(username)
        client.enqueue(object : Callback<ResponseUser>{

            override fun onFailure(call: Call<ResponseUser>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed load", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
                progressBar.visibility = View.INVISIBLE
            }

            override fun onResponse(call: Call<ResponseUser>, response: Response<ResponseUser>) {
                try {
                    val dataArray = response.body()?.items as ArrayList<GithubUser>
                    for (data in dataArray){
                        githubUser.add(data)
                    }
                    recycler_view.visibility = View.VISIBLE
                    progressBar.visibility = View.INVISIBLE
                    initRecyclerView()
                } catch (e : Exception){
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.query_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null){
                    recycler_view.visibility = View.INVISIBLE
                    progressBar.visibility = View.VISIBLE
                    githubUser.clear()
                    searchUser(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_language){
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}