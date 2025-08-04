package com.example.codepathcapstoneproject

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

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

        fetchData()
    }

    private fun fetchData() {
        val client = AsyncHttpClient()
        val tag = "lo-fi"
        val url = "https://ws.audioscrobbler.com/2.0/?method=tag.gettoptracks&tag=$tag&api_key=1c35e3e663d6cce6a5b0b42b239aa5ac&format=json"
        client[url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("APItest", "response successful")
                val data = json.jsonObject
                val track = data.getJSONObject("tracks").getJSONArray("track").getJSONObject(0).getString("name")
                Log.d("APItest", track)
                val artist = data.getJSONObject("tracks").getJSONArray("track").getJSONObject(0).getJSONObject("artist").getString("name")
                //val check = data.getString("snapshot_id")
                Log.d("APItest", artist)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("APItest Error", errorResponse)
            }
        }]

    }

}