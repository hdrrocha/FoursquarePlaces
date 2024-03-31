package com.example.foursquareplaces.injection

import com.example.foursquareplaces.BuildConfig
import com.example.foursquareplaces.data.api.FoursquareApi
import com.example.foursquareplaces.data.repository.SearchPlacesRepositoryImp
import com.example.foursquareplaces.domain.mapper.abs.SearchPlacesMapper
import com.example.foursquareplaces.domain.mapper.abs.SearchPlacesMapperImp
import com.example.foursquareplaces.domain.repository.SearchPlacesRepository
import com.example.foursquareplaces.domain.usecase.SearchPlacesUseCaseImp
import com.example.foursquareplaces.domain.usecase.abs.SearchPlacesUseCase
import com.example.foursquareplaces.ui.searchplaces.viewmodel.SearchPlacesViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Modules {

    private val network = module {
        single {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

    private val api = module {
        single {
            val retrofit: Retrofit = get()
            retrofit.create(FoursquareApi::class.java)
        }
    }

    private val repository = module {
        single<SearchPlacesRepository> {
            SearchPlacesRepositoryImp(api = get())
        }
    }

    private val mapper = module {
        single<SearchPlacesMapper> {
            SearchPlacesMapperImp()
        }
    }

    private val useCase = module {
        single<SearchPlacesUseCase> {
            SearchPlacesUseCaseImp(
                mapper = get(),
                repository = get()
            )
        }
    }

    private val viewModel = module {
        viewModel {
            SearchPlacesViewModel(
                useCase = get()
            )
        }

    }


    var all = listOf(
        network,
        api,
        repository,
        mapper,
        useCase,
        viewModel,

    )
}

