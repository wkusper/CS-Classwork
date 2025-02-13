package com.example.newsapp

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import org.chromium.net.CronetEngine
import org.chromium.net.CronetException
import org.chromium.net.UrlRequest
import org.chromium.net.UrlResponseInfo
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.ByteBuffer
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {

    private val gson = Gson()
    private val cacheFileName = "cached_article.txt"
    private val cacheImageName = "cached_image.png"
    private val timestampFileName = "timestamp.txt"
    private val expirationTimeMillis = 10 * 1000
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("Lifecycle", "MainActivity onCreate called")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        val articleRecyclerView: RecyclerView = findViewById(R.id.articleRecycler)
        articleRecyclerView.layoutManager = LinearLayoutManager(this)
        newsAdapter = NewsAdapter(emptyList())
        articleRecyclerView.adapter = newsAdapter

        if (isCacheValid()){
            loadCachedContent()
        } else{
            makeNetworkRequest(this)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Checks if the cached content has expired
    private fun isCacheValid(): Boolean {
        val timestampFile = File(filesDir, timestampFileName)
        if (!timestampFile.exists()) return false

        val lastRetrieved = timestampFile.readText().toLongOrNull() ?: return false
        val currentTime = System.currentTimeMillis()
        return currentTime - lastRetrieved < expirationTimeMillis
    }

    // Saves the current timestamp
    private fun saveTimestamp() {
        val timestampFile = File(filesDir, timestampFileName)
        timestampFile.writeText(System.currentTimeMillis().toString())
    }

    // Caches content into a text file
    private fun saveArticleContent(text: String) {
        val file = File(filesDir, cacheFileName)
        file.writeText(text)
    }

    // Loads the cached content
    private fun loadCachedContent() {
        val textView: TextView = findViewById(R.id.newsTitle)
        val imageView: ImageView = findViewById(R.id.newsImage)

        // Load text
        val file = File(filesDir, cacheFileName)
        val cachedText = file.readText()
        textView.text = cachedText

        // Load image
        val imageFile = File(filesDir, cacheImageName)
        val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
        imageView.setImageBitmap(bitmap)
    }

    fun makeNetworkRequest(context: Context) {
        Log.d("NetworkRequest", "makeNetworkRequest called")
        val apiKey = "dbabe1ffbb144781a0b2a8191a46699e"
        val query = "NFL"
        val url = "https://newsapi.org/v2/everything?apiKey=$apiKey&q=$query"

        Log.d("NetworkRequest", "Starting network request to URL: $url")

        if(isCacheValid()){
            loadCachedContent()
            return
        }
        // Create and configure CronetEngine
        val myBuilder = CronetEngine.Builder(context)
        val cronetEngine: CronetEngine = myBuilder.build()

        // Create an Executor for network tasks
        val executor: Executor = Executors.newSingleThreadExecutor()


        val myUrlRequestCallback = MyUrlRequestCallback(false) { newsTitles, imageUrl ->
            runOnUiThread {
                Log.d("NetworkRequest", "Data received, updating UI")
                if (newsTitles.isNotEmpty()) {
                    val randomTitle = newsTitles.random()
                    saveTimestamp()
                    saveArticleContent(randomTitle)
                    displayArticleDetails(listOf(randomTitle),imageUrl)
                }
            }
        }

        // Create the UrlRequest
        val requestBuilder = cronetEngine.newUrlRequestBuilder(
            url,
            myUrlRequestCallback,
            executor
        )
        val request: UrlRequest = requestBuilder.build()

        // Start the request
        request.start()
    }
    // Updates the RecyclerView with a single article
    private fun displayArticleDetails(articleList: List<String>, imageUrl: String?) {
        newsAdapter.updateData(articleList, imageUrl)
    }

}

class MyUrlRequestCallback(private val shouldFollow: Boolean, private val onDataReceived: (List<String>, String?) -> Unit) : UrlRequest.Callback() {

    private val responseStream = ByteArrayOutputStream()
    private val gson = Gson()

    override fun onResponseStarted(request: UrlRequest, info: UrlResponseInfo) {
        val httpStatusCode = info.httpStatusCode

        when (httpStatusCode) {
            200 -> {
                // Successful response, start reading the response
                request.read(ByteBuffer.allocateDirect(1024))
            }
            503 -> {
                // Handle service unavailable
                Log.e("RequestError", "Service unavailable (503)")
            }
            else -> {
                Log.e("RequestError", "Unexpected HTTP status code: $httpStatusCode")
            }
        }
    }

    override fun onSucceeded(request: UrlRequest, info: UrlResponseInfo) {
        // This is called when the request completes successfully
        Log.d("RequestSuccess", "Request completed successfully")
        //turn reciecved data into string
        val responseString = responseStream.toString("UTF-8")
        // parse the article
        val newsResponse = parseResponse(responseString)
        // pass titles to handler
        val articleTitles = newsResponse.articles.map { it.title }
        // pass image too
        val imageUrl = newsResponse.articles.firstOrNull()?.urlToImage
        onDataReceived(articleTitles,imageUrl)
    }

    override fun onFailed(request: UrlRequest, info: UrlResponseInfo, error: CronetException) {
        // Handle the error with CronetException
        Log.e("RequestError", "Request failed: ${error.message}")
    }

    override fun onCanceled(request: UrlRequest, info: UrlResponseInfo) {
        // Clean up resources if the request is canceled
        Log.d("RequestCanceled", "Request was canceled")
    }

    override fun onRedirectReceived(request: UrlRequest, info: UrlResponseInfo, newLocationUrl: String) {
        // Determine whether to follow the redirect or not
        if (shouldFollow) {
            request.followRedirect()
        } else {
            request.cancel()
        }
    }

    override fun onReadCompleted(request: UrlRequest, info: UrlResponseInfo, buffer: ByteBuffer) {
        buffer.flip()
        val responseBytes = ByteArray(buffer.remaining())
        buffer.get(responseBytes)
        responseStream.write(responseBytes)
        buffer.clear()
        request.read(buffer)
    }

    private fun parseResponse(jsonResponse: String): NewsResponse {
        return gson.fromJson(jsonResponse, NewsResponse::class.java)
    }
}