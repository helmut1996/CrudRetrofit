package com.example.crudretrofit.api

import com.example.crudretrofit.model.Usuario
import com.example.crudretrofit.model.UsuariosResponse
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

object ApiConstantes {
    const val BASE_URL= "http://192.168.0.9:4000/"
    var okHttpClient = OkHttpClient.Builder()
        .connectTimeout(2, TimeUnit.MINUTES)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()
}
interface WebService {

    @GET("usuarios")
    suspend fun obtenerUsuarios(): Response<UsuariosResponse>

    @GET("usuario/{idUsuario}")
    suspend fun obtenerUsuario(
        @Path("idUsuario") idUsuario:Int
    ): Response<Usuario>

    @POST("usuario/add")
    suspend fun agregarUsuario(
        @Body usuario: Usuario
    ): Response<String>

    @PUT("usuario/update/{idUsuario}")
    suspend fun actualizarUsuario(
        @Path("idUsuario") idUsuario:Int,
        @Body usuario: Usuario
    ):Response<String>

    @DELETE("usuario/delete/{idUsuario}")
    suspend fun borrarUsuario(
        @Path("idUsuario") idUsuario: Int,

    ):Response<String>
}

object RetrofitCliente {
    val webService:WebService by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConstantes.BASE_URL)
            .client(ApiConstantes.okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build().create(WebService::class.java)
    }


}