package br.com.alura.helloapp.ui.userDialog

import br.com.alura.helloapp.data.Usuario
import br.com.alura.helloapp.database.UsuarioDao

data class ListaUsuariosUiState(
    val nomeDeUsuario: String? = null,
    val nome: String? = null,
    val outrasContas: List<Usuario> = emptyList()
)