// Pacote onde a classe está localizada
package com.example.netflixretrofit.adapter

// Importações necessárias para o funcionamento do adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.netflixretrofit.databinding.ItemRvBinding
import com.example.netflixretrofit.model.Filme
import com.example.netflixretrofit.util.Const
import com.squareup.picasso.Picasso

// Classe FilmeAdapter que herda de RecyclerView.Adapter. Ela é responsável por adaptar a lista de filmes para ser exibida em um RecyclerView.
class FilmeAdapter(val onclick: (Filme) -> Unit) : RecyclerView.Adapter<FilmeAdapter.FilmeViewHolder>() {

    // Lista que vai armazenar os filmes a serem exibidos no RecyclerView
    private var listaFilmes = mutableListOf<Filme>()

    // Método para adicionar uma lista de filmes à lista atual e notificar o adapter que os dados mudaram
    fun adicionarLista(lista: List<Filme>) {
        listaFilmes.addAll(lista)
        // Notifica que os dados mudaram para atualizar a interface
        notifyDataSetChanged()
    }

    // Classe interna que representa o ViewHolder, ou seja, a representação visual de um item na lista
    inner class FilmeViewHolder(val binding: ItemRvBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Método que vincula os dados de um filme à interface
        fun bind(filme: Filme) {
            // Recupera o caminho da imagem do filme
            val nomeFilme = filme.backdrop_path

            // Define o tamanho da imagem a ser carregada
            val tamanho = "w780"

            // Base da URL para carregar a imagem
            val urlBase = Const.BASE_URL_IMAGE

            // Concatena a URL base com o tamanho e o caminho do filme
            val url = urlBase + tamanho + nomeFilme

            // Usa o Picasso para carregar a imagem na ImageView correspondente
            Picasso.get().load(url).into(binding.imgItemFilme)

            // Define o título do filme na TextView correspondente
            binding.textTitulo.text = filme.title

            // Configura um listener de clique no item para acionar a função passada por parâmetro (onclick) com o filme atual
            binding.clItem.setOnClickListener {
                onclick(filme)
            }
        }
    }

    // Método que cria e retorna um novo ViewHolder quando necessário
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmeViewHolder {
        // Infla o layout do item da lista usando o ItemRvBinding
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = ItemRvBinding.inflate(layoutInflater, parent, false)

        // Retorna uma nova instância de FilmeViewHolder
        return FilmeViewHolder(view)
    }

    // Método que vincula os dados ao ViewHolder correspondente a uma posição na lista
    override fun onBindViewHolder(holder: FilmeViewHolder, position: Int) {
        // Recupera o filme da lista de acordo com a posição
        val filme = listaFilmes[position]

        // Chama o método bind para associar os dados ao ViewHolder
        holder.bind(filme)
    }

    // Retorna o número total de itens na lista
    override fun getItemCount(): Int = listaFilmes.size
}
