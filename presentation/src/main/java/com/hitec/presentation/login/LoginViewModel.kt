package com.hitec.presentation.login

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.hitec.domain.usecase.GetLocalSiteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@OptIn(OrbitExperimental::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val getLocalSiteUseCase: GetLocalSiteUseCase,
) : ViewModel(), ContainerHost<MainState, MainSideEffect> {

    override val container: Container<MainState, MainSideEffect> =
        container(
            initialState = MainState(),
            buildSettings = {
                this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                    intent { postSideEffect(MainSideEffect.Toast(throwable.message.toString())) }
                }
            }
        )

    init {
        load()
    }

    fun load() = intent {
        val localSite = getLocalSiteUseCase().getOrThrow()

        Log.d("!!@@", localSite.toString())
    }


    fun onIdChange(id: String) = blockingIntent {
        reduce {
            state.copy(id = id)
        }
    }

    fun onPasswordChange(password: String) = blockingIntent {
        reduce {
            state.copy(password = password)
        }
    }

    fun onLocalSiteChange(localSite: String) = blockingIntent {
        reduce {
            state.copy(localSite = localSite)
        }
    }

    fun onLoginClick() = intent {
        val id = state.id
        val password = state.password
        postSideEffect(MainSideEffect.NavigateToMainActivity)
    }


}

@Immutable
data class MainState(
    val id: String = "",
    val password: String = "",
    val localSite: String = "",
)

sealed interface MainSideEffect {
    class Toast(val message: String) : MainSideEffect
    object NavigateToMainActivity : MainSideEffect
}
