package com.hitec.presentation.login

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.hitec.domain.usecase.GetLocalSiteUseCase
import com.hitec.domain.usecase.LoginScreenInfoUseCase
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
class LoginViewModel @Inject constructor(
    private val getLocalSiteUseCase: GetLocalSiteUseCase,
    private val loginScreenInfoUseCase: LoginScreenInfoUseCase
) : ViewModel(), ContainerHost<LoginState, LoginSideEffect> {

    override val container: Container<LoginState, LoginSideEffect> =
        container(
            initialState = LoginState(),
            buildSettings = {
                this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                    intent { postSideEffect(LoginSideEffect.Toast(throwable.message.toString())) }
                }
            }
        )

    init {
        getLoginScreenInfo()
        getLocalSite()
    }

    private fun getLocalSite() = intent {
        getLocalSiteUseCase().getOrThrow()
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

    fun onSwitchChange(isSwitchOn: Boolean) = intent {
        reduce {
            state.copy(isSwitchOn = isSwitchOn)
        }
        postSideEffect(LoginSideEffect.Toast(isSwitchOn.toString()))
    }

    fun onLoginClick() = intent {
        val id = state.id
        val password = state.password
        val localSite = state.localSite
        val isSwitchOn = state.isSwitchOn

        if (isSwitchOn) {
            loginScreenInfoUseCase.saveLoginScreenInfo(id, password, localSite, isSwitchOn)
                .getOrThrow()
        } else {
            clearLoginScreenInfo()
        }

        postSideEffect(LoginSideEffect.Toast("로그인 성공"))
        postSideEffect(LoginSideEffect.NavigateToMainActivity)
    }

    private fun getLoginScreenInfo() = intent {
        val loginScreenInfo = loginScreenInfoUseCase.getLoginScreenInfo().getOrThrow()

        reduce {
            state.copy(
                id = loginScreenInfo.id,
                password = loginScreenInfo.password,
                localSite = loginScreenInfo.localSite,
                isSwitchOn = loginScreenInfo.isSwitchOn
            )
        }
    }

    private fun clearLoginScreenInfo() = intent {
        loginScreenInfoUseCase.clearLoginScreenInfo().getOrThrow()
    }
}

@Immutable
data class LoginState(
    val id: String = "",
    val password: String = "",
    val localSite: String = "",
    var isSwitchOn: Boolean = false
)

sealed interface LoginSideEffect {
    class Toast(val message: String) : LoginSideEffect
    object NavigateToMainActivity : LoginSideEffect
}
