package com.example.weatherapp
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.models.WeatcherClass
import com.google.gson.Gson
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var requestQueue: RequestQueue
    private var HTTP="http://api.weatherstack.com/current?access_key="
    private var KEY="0043577cebf84fee95299025907f9fd2"
    private var QUERYMY="&query="
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestQueue=Volley.newRequestQueue(this)
        binding.editText.queryHint="Search..."
        binding.editText.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val jsonObjectRequest = JsonObjectRequest(Request.Method.GET,
                        HTTP + KEY + QUERYMY + query, null,
                        { response ->
                            val weatherClass = Gson().fromJson(response.toString(), WeatcherClass::class.java)
                            binding.city.text = weatherClass.location.name
                            binding.country.text = weatherClass.location.country
                            binding.description.text = weatherClass.current.weather_descriptions[0]
                            binding.temperature.text = weatherClass.current.temperature.toString() + "°C"
                            binding.time.text = "Time: " + weatherClass.location.localtime
                            binding.visibility.text = weatherClass.current.visibility.toString()
                            binding.pressure.text = weatherClass.current.pressure.toString()
                            binding.cloudCover.text = weatherClass.current.cloudcover.toString()
                            binding.windSpeed.text = weatherClass.current.wind_speed.toString()
                            binding.humidity.text = weatherClass.current.humidity.toString()
                            val  imageRequest = ImageRequest(weatherClass.current.weather_icons[0], object : Response.Listener<Bitmap> {
                                override fun onResponse(response: Bitmap?) {
                                    binding.iconImg.setImageBitmap(response)
                                }
                            }, 0, 0, ImageView.ScaleType.CENTER_CROP,
                                    Bitmap.Config.ARGB_8888, object : Response.ErrorListener {
                                override fun onErrorResponse(error: VolleyError?) {
                                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
                                }
                            })
                            requestQueue.add(imageRequest)
                        }) { }
                requestQueue.add(jsonObjectRequest)
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }
}