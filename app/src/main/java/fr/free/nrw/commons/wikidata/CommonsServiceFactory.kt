package fr.free.nrw.commons.wikidata

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class CommonsServiceFactory(
    private val okHttpClient: OkHttpClient,
) {
    val builder: Retrofit.Builder by lazy {
        // All instances of retrofit share this configuration, but create it lazily
        Retrofit.Builder()
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonUtil.defaultGson))
    }

    val retrofitCache: MutableMap<String, Retrofit> = mutableMapOf()

    inline fun <reified T: Any> create(baseUrl: String): T =
        retrofitCache.getOrPut(baseUrl) {
            // Cache instances of retrofit based on API backend
            builder.baseUrl(baseUrl).build()
        }.create(T::class.java)
}
