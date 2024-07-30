package com.hitec.presentation.login

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.hitec.domain.usecase.FindLocalSiteNameUseCase
import com.hitec.domain.usecase.GetLocalSiteUseCase
import com.hitec.domain.usecase.LoginScreenInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    @ApplicationContext private val context: Context,
    private val getLocalSiteUseCase: GetLocalSiteUseCase,
    private val loginScreenInfoUseCase: LoginScreenInfoUseCase,
    private val findLocalSiteNameUseCase: FindLocalSiteNameUseCase
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
        getAndroidDeviceId()
        getLoginScreenInfo()
    }

    fun getLocalSite() = intent {
        getLocalSiteUseCase(
            userId = state.id,
            password = state.password,
            mobileId = state.id,
            bluetoothId = state.androidDeviceId,
        ).getOrThrow()
    }

    fun onIdChange(id: String) = blockingIntent {
        reduce { state.copy(id = id) }
    }

    fun onPasswordChange(password: String) = blockingIntent {
        reduce { state.copy(password = password) }
    }

    fun onLocalSiteChange(localSite: String) = blockingIntent {
        reduce { state.copy(localSite = localSite) }
    }

    fun onSwitchChange(isSwitchOn: Boolean) = intent {
        reduce { state.copy(isSwitchOn = isSwitchOn) }
    }

    private fun setIsLocalSiteWarningVisible(isLocalSiteWarningVisible: Boolean) = intent {
        reduce { state.copy(isLocalSiteWarningVisible = isLocalSiteWarningVisible) }
    }

    private fun setLocalSiteEngWrittenByUser(localSiteEngWrittenByUser: String) = intent {
        reduce { state.copy(localSiteEngWrittenByUser = localSiteEngWrittenByUser) }
    }

    fun onLoginClick() = intent {
        val foundLocalSiteName = findLocalSiteNameUseCase(state.localSite).getOrThrow()
        setLocalSiteEngWrittenByUser(foundLocalSiteName.first().siteId)
        saveLoginScreenInfo()

        if (foundLocalSiteName.isNotEmpty()) {
            setIsLocalSiteWarningVisible(false)

            postSideEffect(LoginSideEffect.NavigateToMainActivity)
        } else {
            setIsLocalSiteWarningVisible(true)
        }
    }

    private fun saveLoginScreenInfo() = intent {
        loginScreenInfoUseCase.saveLoginScreenInfo(
            state.id,
            state.password,
            state.localSite,
            state.androidDeviceId,
            state.isSwitchOn,
            state.localSiteEngWrittenByUser
        ).getOrThrow()
    }

    private fun getLoginScreenInfo() = intent {
        val loginScreenInfo = loginScreenInfoUseCase.getLoginScreenInfo().getOrThrow()

        if (loginScreenInfo.isSwitchOn) {
            reduce {
                state.copy(
                    id = loginScreenInfo.id,
                    password = loginScreenInfo.password,
                    localSite = loginScreenInfo.localSite,
                    isSwitchOn = loginScreenInfo.isSwitchOn
                )
            }
        }
    }

    //해당 함수는 사용할 일이 없을 수 있음.
    private fun clearLoginScreenInfo() = intent {
        loginScreenInfoUseCase.clearLoginScreenInfo().getOrThrow()
    }

    @SuppressLint("HardwareIds")
    private fun getAndroidDeviceId() = intent {
        val rawId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        val macAddress = rawId?.uppercase()?.chunked(2)?.joinToString(":") ?: ""

        reduce { state.copy(androidDeviceId = macAddress) }
    }
}

@Immutable
data class LoginState(
    val id: String = "",
    val password: String = "",
    val localSite: String = "",
    val androidDeviceId: String = "",
    val isSwitchOn: Boolean = false,
    var isLocalSiteWarningVisible: Boolean = false,
    var localSiteEngWrittenByUser: String = "",
)

sealed interface LoginSideEffect {
    class Toast(val message: String) : LoginSideEffect
    object NavigateToMainActivity : LoginSideEffect
}