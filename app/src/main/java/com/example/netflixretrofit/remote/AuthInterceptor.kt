package com.example.netflixretrofit.remote

import com.example.netflixretrofit.util.Const
import okhttp3.Interceptor
import okhttp3.Response

// Esta classe implementa o Interceptor, que é uma interface do OkHttp
class AuthInterceptor : Interceptor {

    // Este é o método obrigatório que deve ser implementado para interceptar as requisições
    override fun intercept(chain: Interceptor.Chain): Response {
        // Obtemos a requisição original e começamos a construir uma nova a partir dela
        val construtorRequisicao = chain.request().newBuilder()

        // Pega a URL da requisição atual
        val urlAtual = chain.request().url()

        // Cria uma nova URL a partir da URL atual, adicionando parâmetros de consulta (query parameters)
        val novaUrl = urlAtual.newBuilder()

        // Adiciona um parâmetro de consulta chamado "api_key" com o valor definido em Const.API_KEY
        novaUrl.addQueryParameter("api_key", Const.API_KEY)

        // Atualiza a URL da requisição com a nova URL que contém o parâmetro de consulta
        construtorRequisicao.url(novaUrl.build())

        // Prossegue com a requisição, agora com a URL modificada
        return chain.proceed(construtorRequisicao.build())
    }
}
