package com.example.retrofit

import retrofit2.Response
import retrofit2.http.GET

interface AlbumService {
    @GET(value = "/albums")
    suspend fun getAlbums() : Response<Albums>
}