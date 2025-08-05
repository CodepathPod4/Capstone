package com.example.codepathcapstoneproject

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestHeaders
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import kotlin.random.Random

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

    private fun getAlbumArt(track: String, artist: String){
        val client = AsyncHttpClient()
        val rheaders = RequestHeaders()
        val ai: ApplicationInfo = application.packageManager
            .getApplicationInfo(application.packageName, PackageManager.GET_META_DATA)
        val spotifyKey = ai.metaData.getString("spotifyKey")
        val url = "https://api.spotify.com/v1/search?q=track:$track+$artist&type=track"
        rheaders["Authorization"] = spotifyKey
        client[url, rheaders, null, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, rheaders: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("APItest", "art response successful")
                val data = json.jsonObject
                Log.d("apidata", data. toString())
                Log.d("apitest artist", artist)
                Log.d("apitest track", track)
                val total = data.getJSONObject("tracks").getInt("total")
                if (total > 0) {
                    val art = data.getJSONObject("tracks").getJSONArray("items").getJSONObject(0)
                        .getJSONObject("album").getJSONArray("images").getJSONObject(0)
                        .getString("url")
                    Log.d("Apitest art", art.toString())
                    trackList.add(track)
                    artistList.add(artist)
                    imageList.add(art)
                    // send to recycler view here
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Apitest album art error", errorResponse)
            }
        }]
    }

    private fun fetchData(mood: String) {
        val client = AsyncHttpClient()
        val tag = mood
        val ai: ApplicationInfo = application.packageManager
            .getApplicationInfo(application.packageName, PackageManager.GET_META_DATA)
        val lastfmKey = ai.metaData.getString("lastFMKey")
        if (lastfmKey != null) {
            Log.d("apikey", lastfmKey)
        }
        val url = "https://ws.audioscrobbler.com/2.0/?method=tag.gettoptracks&tag=$tag&api_key=$lastfmKey&format=json"
        client[url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("APItest", "mood response successful")
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
            val partyTags = arrayOf("party", "dance", "fun")
            val sadTags = arrayOf("sad", "melancholy", "acoustic")
            val workoutTags = arrayOf("workout", "exercise", "upbeat")
            val chillTags = arrayOf("chillout", "relaxing", "mellow")
            val studyTags = arrayOf("piano", "lo-fi", "classical")
            var arr = emptyArray<String>()
            if (mood.equals("party")){
                arr = partyTags
            }
            if (mood.equals("sad")) {
                arr = sadTags
            }
            if (mood.equals("workout")) {
                arr = workoutTags
            }
            if (mood.equals("chill")) {
                arr = chillTags
            }
            if (mood.equals("study")){
                arr = studyTags
            }
            val num = Random.nextInt(arr.size)
            var finalMood = arr[num]
            Log.d("arrayindex", num.toString())
            Log.d("finalMood", finalMood)
            fetchData(finalMood)
        }
    }
}