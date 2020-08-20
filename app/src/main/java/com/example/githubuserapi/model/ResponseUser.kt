package com.example.githubuserapi.model

import com.google.gson.annotations.SerializedName

data class ResponseUser (

    @field:SerializedName("items")
    val items: List<GithubUser>? = null
)