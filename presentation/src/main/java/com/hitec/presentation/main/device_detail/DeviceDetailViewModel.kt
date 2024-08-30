package com.hitec.presentation.main.device_detail

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.hitec.domain.model.InstallDevice
import com.hitec.domain.usecase.LoginScreenInfoUseCase
import com.hitec.domain.usecase.main.device_detail.PostDownloadDeviceImageUseCase
import com.hitec.domain.usecase.main.device_detail.PostDownloadableImageListUseCase
import com.hitec.presentation.R
import com.hitec.presentation.util.PathHelper
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
class DeviceDetailViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val loginScreenInfoUseCase: LoginScreenInfoUseCase,
    private val postDownloadableImageListUseCase: PostDownloadableImageListUseCase,
    private val postDownloadDeviceImageUseCase: PostDownloadDeviceImageUseCase,
) : ViewModel(), ContainerHost<DeviceDetailState, DeviceDetailSideEffect> {

    override val container: Container<DeviceDetailState, DeviceDetailSideEffect> = container(
        initialState = DeviceDetailState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {
                    postSideEffect(DeviceDetailSideEffect.Toast(throwable.message.toString()))
                    Log.e(TAG, "error handler: ${throwable.message}")
                }
            }
        }
    )

    //From DeviceDetailScreen navController, init InstallDevice
    fun initialize(installDevice: InstallDevice) = intent {
        reduce { state.copy(installDevice = installDevice) }

        getLoginScreenInfo()
        getDownloadableImageList()

        if (state.downloadableImageList.isNotEmpty()) {
            setImageSaveDir(context = context)

            for (photoTypeCd in state.downloadableImageList) {
                getDeviceImages(photoTypeCd)
            }
        }
    }

    private fun setImageSaveDir(context: Context) = intent {
        val appName = context.getString(R.string.app_name)
        val photoDirName = context.getString(R.string.directory_photo)
        val path = "$appName/$photoDirName/${state.localSite}/${state.installDevice?.consumeHouseNo}"
        PathHelper.deleteDir(path) // delete old image files
        PathHelper.isExistDir(path)

        reduce { state.copy(imagePath = path) }
    }

    private fun getLoginScreenInfo() = blockingIntent {
        val loginScreenInfo = loginScreenInfoUseCase.getLoginScreenInfo().getOrThrow()

        reduce {
            state.copy(
                id = loginScreenInfo.id,
                password = loginScreenInfo.password,
                localSite = loginScreenInfo.localSiteEngWrittenByUser, // if loginScreenInfo.localSite == Korean, error
                androidDeviceId = loginScreenInfo.androidDeviceId
            )
        }
    }

    private fun getDownloadableImageList() = blockingIntent {
        val response = postDownloadableImageListUseCase(
            userId = state.id,
            password = state.password,
            mobileId = state.id,
            bluetoothId = state.androidDeviceId,
            localSite = state.localSite,
            meterDeviceId = state.installDevice?.meterDeviceId ?: "",
            deviceTypeCd = state.installDevice?.deviceTypeCd ?: "",
        ).getOrDefault(emptyList())

        val resultCode = response.first().resultCd
        if (resultCode != -1) {
            reduce {
                state.copy(
                    downloadableImageList = response.map { it.photoTypeCd }.toSet().toSortedSet()
                )
            }
        }
    }

    private fun getDeviceImages(photoTypeCd: Int) = intent {
        val response = postDownloadDeviceImageUseCase(
            userId = state.id,
            password = state.password,
            mobileId = state.id,
            bluetoothId = state.androidDeviceId,
            localSite = state.localSite,
            meterDeviceId = state.installDevice?.meterDeviceId ?: "",
            deviceTypeCd = state.installDevice?.deviceTypeCd ?: "",
            meterCd = "", // check empty string value (not null), because always get a response
            photoTypeCd = photoTypeCd
        ).getOrThrow()

        val image = ImageManager.saveBase64ToImage(
            context = context,
            base64Str = response.imageData,
            fileName = response.imageName,
            imagePath = state.imagePath
        )

        reduce { state.copy(deviceImageList = state.deviceImageList + Pair(photoTypeCd, image)) }
    }

    companion object {
        const val TAG = "DeviceDetailViewModel"
    }
}

@Immutable
data class DeviceDetailState(
    val installDevice: InstallDevice? = null,
    val id: String = "",
    val password: String = "",
    val localSite: String = "",
    val androidDeviceId: String = "",
    val downloadableImageList: Set<Int> = emptySet(),
    val deviceImageList: List<Pair<Int, Any?>> = emptyList(),
    val imagePath: String = "",
)

sealed interface DeviceDetailSideEffect {
    class Toast(val message: String) : DeviceDetailSideEffect
}