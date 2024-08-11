// Pacote onde a classe está localizada
package com.example.netflixretrofit.view

// Importações necessárias para a funcionalidade da MainActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.netflixretrofit.R
import com.example.netflixretrofit.adapter.FilmeAdapter
import com.example.netflixretrofit.databinding.ActivityMainBinding
import com.example.netflixretrofit.model.FilmeRecente
import com.example.netflixretrofit.model.FilmesResposta
import com.example.netflixretrofit.remote.RetrofitService
import com.example.netflixretrofit.util.Const
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

// MainActivity, a tela principal do aplicativo, que exibe filmes populares e recentes
class MainActivity : AppCompatActivity() {

    // Instância da API do TheMovieDB, criada de forma preguiçosa (lazy)
    private val theMovieDBAPI by lazy { RetrofitService.TheMovieDBAPI }

    // Instância do binding para acessar as views no layout da Activity
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    // Adapter para exibir a lista de filmes no RecyclerView
    private lateinit var filmeAdapter: FilmeAdapter

    // Variáveis para gerenciar a paginação dos filmes populares e coroutines
    private var paginaAtual = 1
    var jobFilmeRecente: Job? = null
    var jobFilmesPopulares: Job? = null
    var gridLayoutManager: GridLayoutManager? = null

    // Método chamado ao criar a Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root) // Configura o layout da Activity

        // Configura as margens para respeitar as barras do sistema (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializa as views e configurações do RecyclerView
        inicializarViews()
    }

    // Método para recuperar filmes populares da próxima página (usado na rolagem infinita)
    private fun recuperarFilmesPopularesProximaPagina() {
        if (paginaAtual < 1000) { // Limita a paginação para 1000 páginas
            paginaAtual++
            recuperarFilmesPopulares(paginaAtual)
        }
    }

    // Método para inicializar as views e configurar o RecyclerView
    private fun inicializarViews() {
        // Configura o adapter para o RecyclerView e define o comportamento ao clicar em um filme
        filmeAdapter = FilmeAdapter {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("Filme", it)
            startActivity(intent) // Abre a DetailActivity passando o filme selecionado
        }

        // Configura o RecyclerView com o adapter e o layout manager
        binding.rvPopulares.adapter = filmeAdapter
        gridLayoutManager = GridLayoutManager(this, 2) // 2 colunas na grid
        binding.rvPopulares.layoutManager = gridLayoutManager

        // Adiciona um listener para detectar quando o usuário rola a lista até o final
        binding.rvPopulares.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val podeDescerVerticalmente = recyclerView.canScrollVertically(1) // Verifica se é possível rolar mais
                if (!podeDescerVerticalmente) {
                    recuperarFilmesPopularesProximaPagina() // Recupera a próxima página de filmes populares
                }
            }
        })
    }

    // Método para recuperar o filme mais recente
    private fun recuperarFilmeRecente() {
        // Inicia uma coroutine para fazer a requisição em background
        val jobFilmeRecente = CoroutineScope(Dispatchers.IO).launch {
            var resposta: Response<FilmeRecente>? = null
            try {
                resposta = theMovieDBAPI.recurarFilmeRecente() // Faz a requisição para obter o filme recente
            } catch (e: Exception) {
                exibirMensagem("Erro ao fazer a requisição") // Exibe mensagem de erro se houver uma exceção
            }

            // Verifica a resposta da requisição
            if (resposta != null) {
                if (resposta.isSuccessful) { // Se a resposta for bem-sucedida
                    val filmeRecente = resposta.body() // Obtém o corpo da resposta
                    val nomeImagem = filmeRecente?.poster_path // Obtém o caminho da imagem do filme
                    val url = Const.BASE_URL_IMAGE + "w780" + nomeImagem // Constrói a URL completa da imagem
                    withContext(Dispatchers.Main) {
                        Picasso.get().load(url).error(R.drawable.capa).into(binding.imgCapa) // Carrega a imagem usando Picasso
                    }
                } else {
                    exibirMensagem("Problema ao fazer a requisicao CODIGO: ${resposta.code()}") // Exibe código de erro da requisição
                }
            } else {
                exibirMensagem("Nao foi possivel fazer a requisição") // Mensagem de erro caso a resposta seja nula
            }
        }
    }

    // Método para recuperar os filmes populares de uma página específica
    private fun recuperarFilmesPopulares(pagina: Int = 1) {
        // Inicia uma coroutine para fazer a requisição em background
        val jobFilmesPopulares = CoroutineScope(Dispatchers.IO).launch {
            var resposta: Response<FilmesResposta>? = null
            try {
                resposta = theMovieDBAPI.recurarFilmesPopulares(pagina) // Faz a requisição para obter filmes populares
            } catch (e: Exception) {
                exibirMensagem("Erro ao fazer a requisição") // Exibe mensagem de erro se houver uma exceção
            }

            // Verifica a resposta da requisição
            if (resposta != null) {
                if (resposta.isSuccessful) { // Se a resposta for bem-sucedida
                    val filmeResposta = resposta.body() // Obtém o corpo da resposta
                    val listaFilmes = filmeResposta?.filmes // Obtém a lista de filmes
                    if (listaFilmes != null && listaFilmes.isNotEmpty()) { // Se a lista não for nula e não estiver vazia
                        withContext(Dispatchers.Main) {
                            filmeAdapter.adicionarLista(listaFilmes) // Adiciona os filmes ao adapter
                        }
                    }
                } else {
                    exibirMensagem("Problema ao fazer a requisicao CODIGO: ${resposta.code()}") // Exibe código de erro da requisição
                }
            } else {
                exibirMensagem("Nao foi possivel fazer a requisição") // Mensagem de erro caso a resposta seja nula
            }
        }
    }

    // Método para exibir mensagens de erro ou informações ao usuário
    private fun exibirMensagem(mensagem: String) {
        Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show() // Exibe uma mensagem Toast
    }

    // Chamado quando a Activity é iniciada ou retornada ao foco
    override fun onStart() {
        super.onStart()
        recuperarFilmeRecente() // Recupera o filme recente ao iniciar a Activity
        recuperarFilmesPopulares() // Recupera os filmes populares ao iniciar a Activity
    }

    // Chamado quando a Activity perde o foco ou é encerrada
    override fun onStop() {
        super.onStop()
        jobFilmeRecente?.cancel() // Cancela a coroutine do filme recente, se estiver ativa
        jobFilmesPopulares?.cancel() // Cancela a coroutine dos filmes populares, se estiver ativa
    }
}
