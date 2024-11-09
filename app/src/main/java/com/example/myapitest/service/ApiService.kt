package com.example.myapitest.service
import retrofit2.http.GET
import com.example.myapitest.model.Car
import com.example.myapitest.model.RetrieveCar
import com.google.android.gms.common.api.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("car")
    suspend fun getCars(): List<Car>

    @GET("car/{id}")
    suspend fun retrieveCar(@Path("id") id: String): RetrieveCar

    @DELETE("car/{id}")
    suspend fun deleteCar(@Path("id") id: String)

    @POST("car")
    suspend fun createCar(@Body car: Car): Car
}