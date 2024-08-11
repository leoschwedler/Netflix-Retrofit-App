// Pacote onde a classe está localizada
package com.example.netflixretrofit.remote

// Importações necessárias para configurar o Retrofit e as dependências do HTTP client
import com.example.netflixretrofit.util.Const
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// Objeto singleton que encapsula a configuração do Retrofit e do OkHttpClient
object RetrofitService {

    // Configuração do cliente HTTP (OkHttpClient)
    val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .writeTimeout(10, TimeUnit.SECONDS) // Tempo máximo para escrita em uma conexão
        .readTimeout(20, TimeUnit.SECONDS)  // Tempo máximo para leitura em uma conexão
        .connectTimeout(20, TimeUnit.SECONDS) // Tempo máximo para estabelecer uma conexão
        .addInterceptor(AuthInterceptor()) // Adiciona um interceptor para autenticação
        .build()

    // Configuração do Retrofit para fazer requisições HTTPS
    val TheMovieDBAPI = Retrofit.Builder()
        .baseUrl(Const.BASE_URL) // URL base da API
        .addConverterFactory(GsonConverterFactory.create()) // Converte respostas JSON em objetos Kotlin usando Gson
        .client(okHttpClient) // Utiliza o cliente HTTP configurado acima
        .build()
        .create(TheMovieDBAPI::class.java) // Cria uma implementação da interface da API
}
