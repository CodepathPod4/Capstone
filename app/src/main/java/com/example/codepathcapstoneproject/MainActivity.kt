package com.example.codepathcapstoneproject

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestHeaders
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import kotlin.random.Random

private lateinit var songList: MutableList<Song>
private lateinit var adapter: SongAdapter
private lateinit var recyclerView: RecyclerView

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

        // Initialize RecyclerView and song list
        songList = mutableListOf()
        adapter = SongAdapter(songList)
        recyclerView = findViewById(R.id.rvSongs)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Setup mood buttons
        findViewById<Button>(R.id.PartyButton).setOnClickListener { handleMood("party") }
        findViewById<Button>(R.id.SadButton).setOnClickListener { handleMood("sad") }
        findViewById<Button>(R.id.WorkoutButton).setOnClickListener { handleMood("workout") }
        findViewById<Button>(R.id.ChillButton).setOnClickListener { handleMood("chill") }
        findViewById<Button>(R.id.StudyButton).setOnClickListener { handleMood("study") }

        // Set up search button
        val searchButton = findViewById<Button>(R.id.searchButton)
        setupSearch(searchButton)
    }

    private fun handleMood(mood: String) {
        val tags = mapOf(
            "party" to arrayOf("party", "dance", "fun"),
            "sad" to arrayOf("sad", "melancholy", "acoustic"),
            "workout" to arrayOf("workout", "exercise", "upbeat"),
            "chill" to arrayOf("chillout", "relaxing", "mellow"),
            "study" to arrayOf("piano", "lo-fi", "classical")
        )
        val selectedTag = tags[mood]?.random() ?: mood
        Log.d("finalmood", selectedTag)
        songList.clear()
        adapter.notifyDataSetChanged()
        fetchData(selectedTag)
    }

    private fun fetchData(mood: String) {
        val client = AsyncHttpClient()
        val ai: ApplicationInfo = application.packageManager
            .getApplicationInfo(application.packageName, PackageManager.GET_META_DATA)
        val lastfmKey = ai.metaData.getString("lastFMKey")
        val url = "https://ws.audioscrobbler.com/2.0/?method=tag.gettoptracks&tag=$mood&api_key=$lastfmKey&format=json"

        client[url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                val tracks = json.jsonObject.getJSONObject("tracks").getJSONArray("track")
                val dataSize = tracks.length()
                Log.d("datasize", dataSize.toString())
                if (dataSize != 50){
                    val errorView = findViewById<TextView>(R.id.errorMessage)
                    errorView.setText("Cannot find songs matching chosen mood!")
                } else {
                    val errorView = findViewById<TextView>(R.id.errorMessage)
                    errorView.setText("")
                    for (i in 0 until minOf(20, tracks.length())) {
                        val track = tracks.getJSONObject(i).getString("name")
                        val artist =
                            tracks.getJSONObject(i).getJSONObject("artist").getString("name")
                        Log.d("lastfm artist", artist)
                        Log.d("lastfm track", track)
                        getAlbumArt(track, artist)
                    }
                }
            }

            override fun onFailure(statusCode: Int, headers: Headers?, errorResponse: String, throwable: Throwable?) {
                Log.e("LastFM Error", errorResponse)
            }
        }]
    }

    private fun getAlbumArt(track: String, artist: String) {
        val client = AsyncHttpClient()
        val headers = RequestHeaders()
        val ai: ApplicationInfo = application.packageManager
            .getApplicationInfo(application.packageName, PackageManager.GET_META_DATA)
        val spotifyKey = ai.metaData.getString("spotifyKey")
        val url = "https://api.spotify.com/v1/search?q=track:$track+$artist&type=track"
        headers["Authorization"] = "Bearer $spotifyKey"

        client[url, headers, null, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, rheaders: Headers, json: JsonHttpResponseHandler.JSON) {
                val tracks = json.jsonObject.getJSONObject("tracks")
                val total = tracks.getInt("total")
                if (total > 0) {
                    val imageUrl = tracks.getJSONArray("items")
                        .getJSONObject(0)
                        .getJSONObject("album")
                        .getJSONArray("images")
                        .getJSONObject(0)
                        .getString("url")

                    runOnUiThread {
                        songList.add(Song(track, artist, imageUrl))
                        adapter.notifyItemInserted(songList.size - 1)
                    }
                }
            }

            override fun onFailure(statusCode: Int, headers: Headers?, errorResponse: String, throwable: Throwable?) {
                Log.e("Spotify Art Error", errorResponse)
            }
        }]
    }

    private fun setupSearch(button: Button) {
        button.setOnClickListener {
            val searchMood =findViewById<EditText>(R.id.searchBar).getText().toString()
            Log.d("search input", "search input set to $searchMood")
            fetchData(searchMood)
        }
    }
}
