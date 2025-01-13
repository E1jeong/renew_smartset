package com.hitec.presentation.main.photo_upload

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class PhotoUploadViewModel @Inject constructor(
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
        }
    )


    companion object {
        const val TAG = "PhotoUploadViewModel"
    }
}

@Immutable
data class PhotoUploadState(
    val receivedImei: String = "",
)

sealed interface PhotoUploadSideEffect {
    data class Toast(val message: String) : PhotoUploadSideEffect
}