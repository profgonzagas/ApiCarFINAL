package com.example.myapitest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapitest.databinding.ActivityCarDetailBinding
import com.example.myapitest.service.RetrofitClient
import com.example.myapitest.service.safeApiCall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.myapitest.service.Result
import com.example.myapitest.model.Car
import com.example.myapitest.ui.loadUrl

class CarDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCarDetailBinding
    private lateinit var car: Car
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
       // enableEdgeToEdge()
      //  setContentView(R.layout.activity_car_detail)
       /* ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
        setupView()


       // loadCar()
        retrieveCar()
    }

    private fun setupView() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        binding.destroyCar.setOnClickListener{
            deleteCar()
        }
    }




    private fun retrieveCar() { //loadCar
        val carId = intent.getStringExtra(ARG_ID) ?: ""

        CoroutineScope(Dispatchers.IO).launch {
            val result = safeApiCall { RetrofitClient.apiService.retrieveCar(carId) }

            withContext(Dispatchers.Main) {

                when (result) {
                    is Result.Error -> {
                        Toast.makeText(
                            this@CarDetailActivity,
                            getString(R.string.could_not_retrieve_vehicle_data),
                            Toast.LENGTH_LONG
                        ).show()

                    }

                    is Result.Success -> {
                        car = result.data.value
                        handleSuccess()
                    }
                }
            }
        }
    }

    private fun deleteCar(){
        CoroutineScope(Dispatchers.IO).launch{
            val result = safeApiCall { RetrofitClient.apiService.deleteCar(car.id) }
            withContext(Dispatchers.Main){
                when (result){
                    is Result.Error -> {
                        Toast.makeText(this@CarDetailActivity,"Erro ao deletar", Toast.LENGTH_LONG).show()
                    }
                    is Result.Success -> {
                        Toast.makeText(this@CarDetailActivity,"Car deletado", Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
            }
        }
    }

   private fun handleSuccess() {
       binding.etName.setText(car.name)
       binding.etLicense.setText( car.license)
       binding.etYear.setText(car.year)
      /* binding.place.text = getString(
           R.string.lat_long_detail,
           car.place.lat.toString(),
           car.place.long.toString()
       )*/
       binding.imageView.loadUrl(car.imageUrl)
   }

    companion object {
        private const val ARG_ID = "ARG_ID"

        fun newIntent(
            context: Context,
            carId: String
        ) = Intent(
            context,
            CarDetailActivity::class.java
        ).apply {
            putExtra(ARG_ID, carId)
            Log.d("MainActivity", "Putextra: $carId")
            Log.d("MainActivity", "Putextra arg: $ARG_ID")
        }
    }
}
