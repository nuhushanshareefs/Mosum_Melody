
package com.example.a20mosummelody

import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import com.example.a20mosummelody.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//bff7bfad634500ab67af8bc953ac72e9
class MainActivity : AppCompatActivity() {
    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fetchWeatherData("bangalore")
        SearchCity()
    }

    private fun SearchCity() {
        val searchView =binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

        })
    }

    private fun fetchWeatherData(cityname:String) {
        val retrofit=Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)
        val response=retrofit.getWeatherData(cityname,"bff7bfad634500ab67af8bc953ac72e9","metric")
        response.enqueue(object : Callback<WeatherApp>{
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
            val responseBody=response.body()
                if(response.isSuccessful && responseBody!=null){
                    val temperature=responseBody.main.temp.toString()
//                    Log.d("TAG", "onResponse: $temperature")
                    binding.temp.text="$temperature°C"
                    val humidity =responseBody.main.humidity
                    val windSpeed =responseBody.wind.speed
                    val sunRise=responseBody.sys.sunrise.toLong()
                    val sunSet=responseBody.sys.sunset.toLong()
                    val condition=responseBody.weather.firstOrNull()?.main?:"unknown"
                    binding.weather.text=condition
                    binding.humidityvalueid.text="$humidity %"
                    binding.windvalueid.text="$windSpeed m/s"
                    binding.day.text=dayname(System.currentTimeMillis())
                    binding.date.text=date()
                        binding.citydisplay.text="$cityname"
                    binding.sehrivalueid.text="${time(sunRise)}AM"
                    binding.iftarvalueid.text="${time(sunSet)}PM"

                    changewaetheracdtocond(condition)
                }
            }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })}

    private fun changewaetheracdtocond(conditions: String) {
when(conditions){
   "Partly Clouds","Clouds","Overcast","Mist","Foggy" ->{
        binding.root.setBackgroundResource(R.drawable.colud_background)
        binding.lottieAnimationView.setAnimation(R.raw.cloudy)
       binding.button.setOnClickListener(object : View.OnClickListener{
           override fun onClick(p0: View?) {
              val playlist="https://open.spotify.com/playlist/0AfV8UN6Dc9kTAkr9jCYyo?si=dca76a21b48541a9"
               val intent =Intent(Intent.ACTION_VIEW)
               intent.data=Uri.parse(playlist)
               startActivity(intent)
           }
       })
    }

    "Clear Sky","Sunny","Clear" ->{
        binding.root.setBackgroundResource(R.drawable.mysun)
        binding.lottieAnimationView.setAnimation(R.raw.mysunicon)
        binding.button.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val playlist="https://open.spotify.com/playlist/58RcJJZnOjBTo6tAik6lR2?si=5f919321fbb946a7"
                val intent =Intent(Intent.ACTION_VIEW)
                intent.data=Uri.parse(playlist)
                startActivity(intent)
            }
        })
    }

    "Light Rain","Drizzle","Moderate Rain","Showers","Heavy Rain" ->{
        binding.root.setBackgroundResource(R.drawable.myrain)
        binding.lottieAnimationView.setAnimation(R.raw.rain)
        binding.button.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val playlist="https://open.spotify.com/playlist/7myObzsv5ZfX3Jl0TW8V4E?si=ab173dd255f34dcd"
                val intent =Intent(Intent.ACTION_VIEW)
                intent.data=Uri.parse(playlist)
                startActivity(intent)
            }
        })
    }

    "Light Snow","Moderate Snow","Heavy Snow","Blizzard"->{
        binding.root.setBackgroundResource(R.drawable.mysnow)
        binding.lottieAnimationView.setAnimation(R.raw.snow)
        binding.button.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val playlist="https://open.spotify.com/playlist/52uCIMxaIK3B3CAGqgvvPq?si=fa52af3d4c6149a5"
                val intent =Intent(Intent.ACTION_VIEW)
                intent.data=Uri.parse(playlist)
                startActivity(intent)
            }
        })
    }
}
        binding.lottieAnimationView.playAnimation()
    }

    fun dayname(timestamp:Long) :String{
            val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
            return sdf.format((Date()))


    }
    fun date() :String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format((Date()))
    }
    fun time(timestamp: Long) :String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format((Date(timestamp*1000)))
    }

    }