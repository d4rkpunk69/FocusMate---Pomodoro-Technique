package com.example.retrofit

import  android.os.Bundle
import android.util.Log
import android.widget.TextView 
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val retService : AlbumService = RetrofitInstance
            .getRetrofitInstance()
            .create(AlbumService::class.java)
        val responseLiveData: LiveData<Response<Albums>> = liveData {
            val response: Response<Albums> = retService.getAlbums()
            emit(response)
        }
        responseLiveData.observe(this, Observer {
            val albumsList: MutableListIterator<AlbumsItem>? = it.body()?.listIterator()
            if(albumsList!=null) {
                while (albumsList.hasNext()) {
                    val albumsItem: AlbumsItem = albumsList.next()
                    Log.i("MyTag", albumsItem.title)
                    val result: String = " " +"Album id: ${albumsItem.id}"+"/n"
                    " "+"Album title: ${albumsItem.title}"+"/n" + " " +"Album url: ${albumsItem.completed}"
" +"
                }
            }
        })
    }
}