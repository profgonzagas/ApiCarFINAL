package com.example.myapitest


import androidx.activity.enableEdgeToEdge
import com.example.myapitest.databinding.ActivityCarDetailBinding
import com.example.myapitest.databinding.ActivityNewCarBinding

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.CAMERA
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import com.example.myapitest.model.Car
//import com.example.myapitest.model.Place
import com.example.myapitest.service.Result
import com.example.myapitest.service.RetrofitClient
import com.example.myapitest.service.safeApiCall
//import com.example.myapitest.utils.firebaseLogout
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

class NewCarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewCarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewCarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
       // FirebaseAuth.getInstance().currentUser
    }
    private fun setupView() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        binding.btConfirm.setOnClickListener {
            btConfirmOnClick()
        }

    }

    private fun btConfirmOnClick() {
        val modelName = binding.etCreateName.text.toString().trim()
        val modelYear = binding.etCreateYear.text.toString().trim()
        val modelLicense = binding.etCreateLicense.text.toString().trim()
        val modelURL = binding.imageUrl.text.toString().trim()

        if (modelName.isEmpty()) {
            binding.tlCreateName.error = "Campo obrigatório"
            return
        } else {
            binding.tlCreateName.error = null
        }

        if (modelYear.isEmpty()) {
            binding.tlCreateYear.error = "Campo obrigatório"
            return
        } else {
            binding.tlCreateYear.error = null
        }

        if (modelLicense.isEmpty()) {
            binding.tlCreateLicense.error = "Campo obrigatório"
            return
        } else {
            binding.tlCreateLicense.error = null
        }
       /* if (modelURL.isEmpty()) {
            binding.createImage.er = "Campo obrigatório"
            return
        } else {
            binding.createImage.error = null
        }*/

        // Verifique se a imagem foi tirada
      /*  if (imageFile == null) {
            Toast.makeText(
                this,
                getString("Desenho não selecionado"),
                Toast.LENGTH_LONG
            ).show()
            return
        }*/
       // getLastLocation()
        val car = Car(
            id= UUID.randomUUID().toString(),
            name= binding.etCreateName.text.toString(),
            year = binding.etCreateYear.text.toString(),
            license = binding.etCreateLicense.text.toString(),
            imageUrl = binding.imageUrl.text.toString()
           // imageUrl = imageFirebaseUri ?: ""

        )

        Log.d("CAR1", car.toString())
        CoroutineScope(Dispatchers.IO).launch {
            val result = safeApiCall { RetrofitClient.apiService.createCar(car) }
            Log.d("CAR2", car.toString())
            Log.d("CARTUDO", "Car details: Name: ${car.name}, Year: ${car.year}, License: ${car.license}, Image URL: ${car.imageUrl}")

            withContext(Dispatchers.Main) {

                Log.d("Car3", "Result: $result")

                when (result) {
                    is Result.Error -> {
                        Toast.makeText(
                            this@NewCarActivity,
                            "Erro não conhecido",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.e("POST", result.message)
                    }
                    is Result.Success -> {
                        Toast.makeText(
                            this@NewCarActivity,
                            getString(R.string.success_create, result.data.id),
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                        startActivity(CarDetailActivity.newIntent(this@NewCarActivity, car.id))
                    }
                }
            }
        }
    }


    companion object {
        fun newIntent(context: Context) =
            Intent(context, NewCarActivity::class.java)
    }
}