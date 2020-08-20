package com.example.githubuserapi.main


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuserapi.R
import com.example.githubuserapi.adapter.SectionPagerAdapter
import com.example.githubuserapi.api.ApiConfig
import com.example.githubuserapi.model.GithubUser
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NAME = "extra_name"
    }

    private lateinit var sectionPagerAdapter: SectionPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val username: String = intent.getStringExtra(EXTRA_NAME)
        setActionBar()
        getDetailApi(username)
        initSectionPager()
        sectionPagerAdapter.username = username
    }

    private fun initSectionPager(){
        sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager)
        view_pager.adapter = sectionPagerAdapter
        tab_layout.setupWithViewPager(view_pager)
    }

    private fun getDetailApi(username : String?){
        val client = username?.let {
            ApiConfig.getApiServices().getDetailUser(it)
        }
        client?.enqueue(object : Callback<GithubUser>{

            override fun onFailure(call: Call<GithubUser>, t: Throwable) {
                Toast.makeText(this@DetailActivity, "Failed load", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
                progressBar.visibility = View.INVISIBLE
            }

            override fun onResponse(call: Call<GithubUser>, response: Response<GithubUser>) {
                try {
                    val data = response.body()
                    //progressBarDetail.visibility = View.VISIBLE

                    Glide.with(this@DetailActivity)
                        .load(data?.avatar)
                        .apply(RequestOptions())
                        .into(img_detail_photo)

                    tv_detail_fullname.text = if (data?.name != null) data.name else "-"
                    tv_detail_username.text = if (data?.username != null) data.username else "-"
                    tv_detail_company.text = if (data?.company != null) data.company else "-"
                    tv_detail_location.text = if (data?.location != null) data.location else "-"
                    tv_detail_repo.text = if (data?.repository != null) data.repository else "-"
                    tv_detail_followers.text = if (data?.follower != null) data.follower else "-"
                    tv_detail_following.text = if (data?.following != null) data.following else "-"
                    progressBarDetail.visibility = View.INVISIBLE
                } catch (e : Exception){
                    Toast.makeText(this@DetailActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

        })
    }
    private fun setActionBar(){
        supportActionBar?.title = resources.getString(R.string.title_bar_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0f
    }
}