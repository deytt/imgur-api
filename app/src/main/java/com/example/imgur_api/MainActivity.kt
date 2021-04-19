package com.example.imgur_api

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imgur_api.model.Imgur
import com.example.imgur_api.model.ImgurResponse
import com.example.imgur_api.retrofit.Client
import com.example.imgur_api.retrofit.service.Service
import com.example.imgur_api.utils.PaginationAdapter
import com.example.imgur_api.utils.PaginationScrollListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    lateinit var rv: RecyclerView
    lateinit var imgurService: Service
    lateinit var progressBar: ProgressBar
    lateinit var adapter: PaginationAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    private val TOTAL_PAGES = 5
    private val PAGE_START = 1
    private var isLoading = false
    private var isLastPage = false
    val totalPageCount = 5 //limiting to 5, since total pages in actual API is very large.
    private var currentPage = PAGE_START

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        setCompatActivityTitle()

        initializeComponents()

        recyclerViewAddListener()

        imgurService = Client.getService()

        loadApiData()
    }

    private fun loadApiData() {
        callTopImgurApi().enqueue(object : Callback<ImgurResponse> {
            override fun onResponse(call: Call<ImgurResponse>, response: Response<ImgurResponse>) {
                val results: List<Imgur> = fetchResults(response)
                progressBar.visibility = View.GONE
                adapter.addAll(results)
                if (currentPage <= totalPageCount) adapter.isLoadingAdded = true else isLastPage = true
            }

            override fun onFailure(call: Call<ImgurResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun fetchResults(response: Response<ImgurResponse>): List<Imgur> {
        val responseBody: ImgurResponse = response.body()
        return responseBody.data ?: emptyList()
    }

    private fun loadNextPage() {
        callTopImgurApi().enqueue(object : Callback<ImgurResponse> {
            override fun onResponse(call: Call<ImgurResponse>, response: Response<ImgurResponse>) {
                adapter.removeLoadingFooter()
                isLoading = false
                val results: List<Imgur> = fetchResults(response)
                adapter.addAll(results)
                if (currentPage != totalPageCount) adapter.isLoadingAdded = true else isLastPage = true
            }

            override fun onFailure(call: Call<ImgurResponse>, t: Throwable) {
                t.printStackTrace()
                // TODO: handle failure
            }
        })
    }

    private fun callTopImgurApi(): Call<ImgurResponse> {
        Log.d(TAG, "Loading Page: $currentPage")
        return imgurService.findTopWeekly(currentPage)
    }

    private fun initializeComponents() {
        adapter = PaginationAdapter(this)
        progressBar = findViewById(R.id.main_progress)
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv = findViewById(R.id.main_recycler)
        rv.layoutManager = linearLayoutManager
        rv.itemAnimator = DefaultItemAnimator()
        rv.adapter = adapter
    }

    private fun recyclerViewAddListener() {
        rv.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
            override fun loadMoreItems() {
                isLoading = true
                currentPage += 1

                Handler(Looper.getMainLooper()).postDelayed({
                    loadNextPage()
                }, 1000) // mocking network delay for API call
            }

            override fun getTotalPageCount(): Int {
                return TOTAL_PAGES
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }
        })
    }

    private fun setCompatActivityTitle() {
        title = resources.getString(R.string.top_weekly)
    }
}