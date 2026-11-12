package com.example.pethavenapp

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.example.pethavenapp.data.AppContainer
import com.example.pethavenapp.data.remote.NetworkConfig
import okhttp3.OkHttpClient

/**
 * Configura Coil para que las fotos de las mascotas, que la API sirve solo con sesión, viajen con el token.
 * La cabecera se agrega únicamente a las URL de la propia API, para no filtrar el token a otros hosts.
 */
class PetHavenApplication : Application(), ImageLoaderFactory {

    override fun newImageLoader(): ImageLoader = ImageLoader.Builder(this)
        .okHttpClient {
            OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request()
                    val token = AppContainer.session?.token
                    if (token != null && request.url.toString().startsWith(NetworkConfig.BASE_URL)) {
                        chain.proceed(request.newBuilder().header("Authorization", "Bearer $token").build())
                    } else {
                        chain.proceed(request)
                    }
                }
                .build()
        }
        .build()
}
