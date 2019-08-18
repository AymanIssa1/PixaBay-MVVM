package com.example.pixabaymvvm.models


import com.google.gson.annotations.SerializedName

data class Hits(
    @SerializedName("hits") var hits: ArrayList<Hit>,
    @SerializedName("total") var total: Int,
    @SerializedName("totalHits") var totalHits: Int
)