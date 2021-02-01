package com.chstudios.myfoodapp.di

import android.content.Context
import androidx.room.Room
import com.chstudios.myfoodapp.data.local.FoodDao
import com.chstudios.myfoodapp.data.local.FoodDatabase
import com.chstudios.myfoodapp.data.remote.spoonacularapi.SpoonacularAPI
import com.chstudios.myfoodapp.data.remote.spoonacularapi.SpoonacularApiHelper
import com.chstudios.myfoodapp.data.remote.spoonacularapi.SpoonacularApiHelperImpl
import com.chstudios.myfoodapp.data.repository.FoodRepository
import com.chstudios.myfoodapp.data.repository.Repository
import com.chstudios.myfoodapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, FoodDatabase::class.java, Constants.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideWordDao(db: FoodDatabase) = db.foodDao()

    @Singleton
    @Provides
    fun provideFoodRepository(
        dao: FoodDao,
        api: SpoonacularApiHelper
    ) = FoodRepository(dao, api) as Repository







    @Singleton
    @Provides
    fun providesSpoonacularOkhttpInterceptor() : Interceptor {
        return  Interceptor { chain: Interceptor.Chain ->
            val original: Request = chain.request()
            val requestBuilder: Request.Builder = original.newBuilder()
                .addHeader("x-rapidapi-key", Constants.API_KEY)
                .addHeader("x-rapidapi-host", Constants.SPOONACULAR_API_HOST)
            val request: Request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    @Singleton
    @Provides
    fun providesNutritionIxOkhttpInterceptor() : Interceptor {
        return  Interceptor { chain: Interceptor.Chain ->
            val original: Request = chain.request()
            val requestBuilder: Request.Builder = original.newBuilder()
                .addHeader("x-rapidapi-key", Constants.API_KEY)
                .addHeader("x-rapidapi-host", Constants.NUTRIONIX_API_HOST)
            val request: Request = requestBuilder.build()
            chain.proceed(request)
        }
    }




    @Singleton
    @Provides
    @Named("Spoonacular")
    fun provideSpoonacularOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(providesSpoonacularOkhttpInterceptor())
            .addInterceptor(loggingInterceptor)
            .build()
    }



    @Singleton
    @Provides
    @Named("NutritionIx")
    fun provideNutritionIxOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(providesNutritionIxOkhttpInterceptor())
            .addInterceptor(loggingInterceptor)
            .build()
    }




    @Singleton
    @Provides
    @Named("Spoonacular")
    fun provideSpoonacularRetrofit(@Named("Spoonacular")okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Constants.SPOONACULAR_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    @Named("NutritionIx")
    fun provideNutritionIxRetrofit(@Named("NutritionIx")okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Constants.NUTRITIONIX_URL)
        .client(okHttpClient)
        .build()





    @Singleton
    @Provides
    @Named("Spoonacular")
    fun provideSpoonacularApiService(@Named("Spoonacular")retrofit: Retrofit) = retrofit.create(SpoonacularAPI::class.java)

    @Singleton
    @Provides
    @Named("NutritionIx")
    fun provideNutritionIxApiService(@Named("NutritionIx")retrofit: Retrofit) = retrofit.create(SpoonacularAPI::class.java)





    @Singleton
    @Provides
    fun provideApiHelper(apiHelper: SpoonacularApiHelperImpl): SpoonacularApiHelper = apiHelper






















}