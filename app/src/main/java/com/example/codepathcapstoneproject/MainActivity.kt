package com.example.codepathcapstoneproject

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

private lateinit var trackList: MutableList<String>
private lateinit var artistList: MutableList<String>
private lateinit var imageList: MutableList<String>

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
        trackList = mutableListOf()
        artistList = mutableListOf()
        imageList = mutableListOf()
        val party_button = findViewById<Button>(R.id.PartyButton)
        val sad_button = findViewById<Button>(R.id.SadButton)
        val workout_button = findViewById<Button>(R.id.WorkoutButton)
        val chill_button = findViewById<Button>(R.id.ChillButton)
        val study_button = findViewById<Button>(R.id.StudyButton)
        setupButton(party_button, "party")
        setupButton(sad_button, "sad")
        setupButton(workout_button,"workout")
        setupButton(chill_button, "chill")
        setupButton(study_button, "study")
    }
    private fun getAlbumArt(track: String, artist: String) {
        val client = AsyncHttpClient()
        val url = "https://ws.audioscrobbler.com/2.0/?method=track.getInfo&api_key=1c35e3e663d6cce6a5b0b42b239aa5ac&artist=$artist&track=$track&format=json"
        client[url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("APItest", "response successful")
                val data = json.jsonObject
                val art = data.getJSONObject("track").getJSONObject("album").getJSONArray("image").getJSONObject(0).getString("#text")
                Log.d("APItest", art)
                trackList.add(track)
                artistList.add(artist)
                imageList.add(art)
                // send to recycler view here
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("APIArt Error", errorResponse)
            }
        }]

    }

    private fun fetchData(mood: String) {
        val client = AsyncHttpClient()
        val tag = mood
        val url = "https://ws.audioscrobbler.com/2.0/?method=tag.gettoptracks&tag=$tag&api_key=1c35e3e663d6cce6a5b0b42b239aa5ac&format=json"
        client[url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("APItest", "response successful")
                for (i in 0 until 20) {
                    val data = json.jsonObject
                    val track = data.getJSONObject("tracks").getJSONArray("track").getJSONObject(i)
                        .getString("name")
                    Log.d("APItest", track)
                    val artist = data.getJSONObject("tracks").getJSONArray("track").getJSONObject(i)
                        .getJSONObject("artist").getString("name")
                    Log.d("APItest", artist)
                    getAlbumArt(track, artist)
                }
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

    private fun setupButton (button: Button, mood: String) {
        button.setOnClickListener{
            fetchData(mood)
        }
    }
}