package br.com.alura.helloapp.ui.userDialog

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.alura.helloapp.data.Usuario
import br.com.alura.helloapp.data.UsuarioPOJO
import br.com.alura.helloapp.database.UsuarioDao
import br.com.alura.helloapp.preferences.PreferencesKey
import br.com.alura.helloapp.util.ID_USUARIO_ATUAL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FormularioUsuarioViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val dataStore: DataStore<Preferences>,
    private val usuarioDao: UsuarioDao
) : ViewModel() {

    private val nomeUsuario = savedStateHandle.get<String>(ID_USUARIO_ATUAL)

    private val _uiState = MutableStateFlow(FormularioUsuarioUiState())
    val uiState: StateFlow<FormularioUsuarioUiState>
        get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            nomeUsuario?.let {
                usuarioDao.buscaPorId(it).first()?.let { usuarioBuscado ->
                    _uiState.value = _uiState.value.copy(
                        nomeUsuario = usuarioBuscado.idUsuario,
                        nome = usuarioBuscado.nome,
                        senha = usuarioBuscado.senha
                    )
                }
            }
        }

        _uiState.update { state ->
            state.copy(onNomeMudou = {
                _uiState.value = _uiState.value.copy(
                    nome = it
                )
            })
        }
    }

    fun onClickMostraMensagemExclusao() {
        _uiState.value = _uiState.value.copy(
            mostraMensagemExclusao = true
        )
    }

    suspend fun atualiza() {
        usuarioDao.atualiza(
            UsuarioPOJO(
                idUsuario = _uiState.value.nomeUsuario,
                nome = _uiState.value.nome,
            )
        )
    }

    suspend fun apaga() {
        usuarioDao.apaga(Usuario(idUsuario = _uiState.value.nomeUsuario))
        if (nomeUsuario.equals(dataStore.data.first()[PreferencesKey.USUARIO_ATUAL])) {
            dataStore.edit { it.remove(PreferencesKey.USUARIO_ATUAL) }
        }
    }
}