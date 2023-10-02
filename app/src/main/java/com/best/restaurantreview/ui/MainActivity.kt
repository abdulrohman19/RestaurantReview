package com.best.restaurantreview.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.best.restaurantreview.data.response.CustomerReviewsItem
import com.best.restaurantreview.data.response.Restaurant
import com.best.restaurantreview.databinding.ActivityMainBinding
import com.bumptech.glide.Glide


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    //    companion object {
//        private const val TAG = "MainActivity"
//        private const val RESTAURANT_ID = "uewq1zg2zlskfw1e867"
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        mainViewModel.restaurant.observe(this) {restaurant ->
            setRestaurantData(restaurant)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvReview.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvReview.addItemDecoration(itemDecoration)

        mainViewModel.listReview.observe(this) {consumerReviews->
            setReviewData(consumerReviews)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

//        findRestaurant()

        binding.btnSend.setOnClickListener { view ->
//            postReview(binding.edReview.text.toString())
            mainViewModel.postReview(binding.edReview.text.toString())
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

//    private fun findRestaurant() {
//        showLoading(true)
//        val client = ApiConfig.getApiService().getRestaurant(RESTAURANT_ID)
//        client.enqueue(object : Callback<RestaurantResponse> {
//            override fun onResponse(
//                call: Call<RestaurantResponse>,
//                response: Response<RestaurantResponse>
//            ) {
//                showLoading(false)
//                if (response.isSuccessful) {
//                    val responseBody = response.body()
//                    if (responseBody != null) {
//                        setRestaurantData(responseBody.restaurant)
//                        setReviewData(responseBody.restaurant.customerReviews)
//                    }
//                } else {
//                    Log.e(TAG, "onFailure: ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<RestaurantResponse>, t: Throwable) {
//                showLoading(false)
//                Log.e(TAG, "onFailure: ${t.message}")
//            }
//        })
//    }

    private fun setRestaurantData(restaurant: Restaurant) {
        binding.tvTitle.text = restaurant.name
        binding.tvDescription.text = restaurant.description
        Glide.with(this)
            .load("https://restaurant-api.dicoding.dev/images/large/${restaurant.pictureId}")
            .into(binding.ivPicture)
    }

//    private fun postReview(review: String) {
//        showLoading(true)
//        val client = ApiConfig.getApiService().postReview(RESTAURANT_ID, "Dicoding", review)
//        client.enqueue(object : Callback<PostReviewResponse> {
//            override fun onResponse(
//                call: Call<PostReviewResponse>,
//                response: Response<PostReviewResponse>
//            ) {
//                showLoading(false)
//                val responseBody = response.body()
//                if (response.isSuccessful && responseBody != null) {
//                    setReviewData(responseBody.customerReviews)
//                } else {
//                    Log.e(TAG, "onFailure: ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<PostReviewResponse>, t: Throwable) {
//                showLoading(false)
//                Log.e(TAG, "onFailure: ${t.message}")
//            }
//        })
//    }

    private fun setReviewData(consumerReviews: List<CustomerReviewsItem>) {
        val adapter = ReviewAdapter()
        adapter.submitList(consumerReviews)
        binding.rvReview.adapter = adapter
        binding.edReview.setText("")
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
//        if (isLoading) {
//            binding.progressBar.visibility = View.VISIBLE
//        } else {
//            binding.progressBar.visibility = View.GONE
//        }
    }

}