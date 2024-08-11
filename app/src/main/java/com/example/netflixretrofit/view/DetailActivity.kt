// Pacote onde a classe está localizada
package com.example.netflixretrofit.view

// Importações necessárias para a funcionalidade da DetailActivity
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.netflixretrofit.databinding.ActivityDetailBinding
import com.example.netflixretrofit.model.Filme
import com.example.netflixretrofit.util.Const
import com.squareup.picasso.Picasso

// DetailActivity, a tela de detalhes que exibe informações específicas sobre um filme selecionado
class DetailActivity : AppCompatActivity() {

    // Instância do binding para acessar as views no layout da Activity
    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }

    // Método chamado ao criar a Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilita o modo Edge-to-Edge, que permite que o conteúdo ocupe toda a tela, incluindo áreas sob as barras de status e navegação
        enableEdgeToEdge()

        // Define o layout da Activity com base no binding
        setContentView(binding.root)

        // Recupera os dados passados pela Intent que iniciou esta Activity
        val bundle = intent.extras
        if (bundle != null) {
            // Verifica a versão do Android para recuperar o objeto Parcelable (no caso, um Filme)
            val filme = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Para versões mais recentes do Android (Tiramisu e posteriores)
                bundle.getParcelable("Filme", Filme::class.java)
            } else {
                // Para versões anteriores do Android
                bundle.getParcelable("Filme") as? Filme
            }

            // Se o filme foi recuperado com sucesso, exibe as informações na interface
            if (filme != null) {
                val nomeFilme = filme.backdrop_path // Caminho da imagem do filme
                val tamanho = "w780" // Define o tamanho da imagem a ser carregada
                val urlBase = Const.BASE_URL_IMAGE // URL base para carregar a imagem
                val url = urlBase + tamanho + nomeFilme // Constrói a URL completa da imagem

                // Usa o Picasso para carregar a imagem do filme na ImageView
                Picasso.get().load(url).into(binding.imgPoster)

                // Define o título do filme na TextView correspondente
                binding.textFilmeTitulo.text = filme.title
            }
        }
    }
}
