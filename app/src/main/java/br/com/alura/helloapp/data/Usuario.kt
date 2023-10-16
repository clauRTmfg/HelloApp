package br.com.alura.helloapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Usuario(
    @PrimaryKey
    val idUsuario: String = "",
    val senha: String = "",
    // anotação necessária para a Migration Automatica (2 para 3),
    // por conta das linhas que já existiam no banco, e agora devem ser
    // atualizadas, por conta da criação da coluna nome
    @ColumnInfo(defaultValue = "")
    val nome: String = ""
)

// POJO (Plain Old Java Object)
// classe auxiliar, para que quando fizermos atualização do usuario em FormularioUsuarioViewModel,
// não precisemos passar todos os campos, mas apenas os que interessam, definidos aqui.
data class UsuarioPOJO(
    @PrimaryKey
    val idUsuario: String = "",
    val nome: String?,
)