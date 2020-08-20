package com.example.githubuserapi.api

import com.example.githubuserapi.model.GithubUser
import com.example.githubuserapi.model.ResponseUser
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices  {

    @GET("users")
    fun getApi() : Call<List<GithubUser>>

    @GET("search/users")
    @Headers("Authorization: token fa685b4eb7654f16ea5ecd548c5e6514e976eb19")
    fun getSearchUser(@Query("q") query : String?) : Call<ResponseUser>

    @GET("users/{username}")
    @Headers("Authorization: token fa685b4eb7654f16ea5ecd548c5e6514e976eb19")
    fun getDetailUser(@Path("username") username : String? ) : Call<GithubUser>

    @GET("users/{username}/{follow}")
    @Headers("Authorization: token fa685b4eb7654f16ea5ecd548c5e6514e976eb19")
    fun getDetailFollow(@Path("username") username: String, @Path("follow") follow : String) : Call<ArrayList<GithubUser>>
}