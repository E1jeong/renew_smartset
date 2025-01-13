package com.hitec.presentation.main.photo_upload

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.hitec.domain.model.InstallDevice
import com.hitec.domain.model.LoginScreenInfo
import com.hitec.domain.usecase.login.LoginScreenInfoUseCase
import com.hitec.domain.usecase.main.device_detail.GetInstallDeviceFromImeiUseCase
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
class PhotoUploadViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val loginScreenInfoUseCase: LoginScreenInfoUseCase,
    private val postDownloadableImageListUseCase: PostDownloadableImageListUseCase,
    private val postDownloadDeviceImageUseCase: PostDownloadDeviceImageUseCase,
    private val getInstallDeviceFromImeiUseCase: GetInstallDeviceFromImeiUseCase,
) : ViewModel(), ContainerHost<PhotoUploadState, PhotoUploadSideEffect> {

    override val container: Container<PhotoUploadState, PhotoUploadSideEffect> = container(
        initialState = PhotoUploadState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {
                    postSideEffect(PhotoUploadSideEffect.Toast(throwable.message.toString()))
                    Log.e(TAG, "error handler: ${throwable.message}")
                }
            }
        },
        onCreate = {
            getLoginScreenInfo()
        }
    )

    private fun getLoginScreenInfo() = intent {
        val loginScreenInfo = loginScreenInfoUseCase.getLoginScreenInfo().getOrThrow()
        reduce { state.copy(loginScreenInfo = loginScreenInfo) }
    }

    fun initialize(deviceImei: String) = intent {
        getInstallDeviceFromImei(deviceImei)
        getDownloadableImageList()

        if (state.downloadableImageList.isNotEmpty()) {
            setImageSaveDir(context = context)

            for (photoTypeCd in state.downloadableImageList) {
                getDeviceImages(photoTypeCd)
            }
        }
    }

    private fun getInstallDeviceFromImei(deviceImei: String) = blockingIntent {
        val installDevice = getInstallDeviceFromImeiUseCase(deviceImei).getOrThrow()
        reduce { state.copy(installDevice = installDevice) }
    }

    private fun getDownloadableImageList() = blockingIntent {
        val response = postDownloadableImageListUseCase(
            userId = state.loginScreenInfo.id,
            password = state.loginScreenInfo.password,
            mobileId = state.loginScreenInfo.id,
            bluetoothId = state.loginScreenInfo.androidDeviceId,
            localSite = state.loginScreenInfo.localSite,
            meterDeviceId = state.installDevice.meterDeviceId,
            deviceTypeCd = state.installDevice.deviceTypeCd ?: "",
        ).getOrDefault(emptyList())

        val resultCode = response.first().resultCd
        if (resultCode != -1) {
            reduce { state.copy(downloadableImageList = response.map { it.photoTypeCd }.toSet().toSortedSet()) }
        }
    }

    private fun getDeviceImages(photoTypeCd: Int) = intent {
        val response = postDownloadDeviceImageUseCase(
            userId = state.loginScreenInfo.id,
            password = state.loginScreenInfo.password,
            mobileId = state.loginScreenInfo.id,
            bluetoothId = state.loginScreenInfo.androidDeviceId,
            localSite = state.loginScreenInfo.localSite,
            meterDeviceId = state.installDevice.meterDeviceId,
            deviceTypeCd = state.installDevice.deviceTypeCd ?: "",
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

    private fun setImageSaveDir(context: Context) = intent {
        val appName = context.getString(R.string.app_name)
        val photoDirName = context.getString(R.string.directory_photo)
        val path = "$appName/$photoDirName/${state.loginScreenInfo.localSite}/${state.installDevice.consumeHouseNo}"
        PathHelper.deleteDir(path) // delete old image files
        PathHelper.isExistDir(path)

        reduce { state.copy(imagePath = path) }
    }


    companion object {
        const val TAG = "PhotoUploadViewModel"
    }
}

@Immutable
data class PhotoUploadState(
    val receivedImei: String = "",
    val loginScreenInfo: LoginScreenInfo = LoginScreenInfo(
        id = "",
        password = "",
        localSite = "",
        androidDeviceId = "",
        isSwitchOn = false,
        localSiteEngWrittenByUser = ""
    ),
    val installDevice: InstallDevice = InstallDevice(meterDeviceId = "HT-T-012345"),
    val downloadableImageList: Set<Int> = emptySet(),
    val deviceImageList: List<Pair<Int, Any?>> = emptyList(),
    val imagePath: String = "",
)

sealed interface PhotoUploadSideEffect {
    data class Toast(val message: String) : PhotoUploadSideEffect
}