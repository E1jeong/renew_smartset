package com.hitec.presentation.main.as_report

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.hitec.domain.model.AsDevice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class AsReportViewModel @Inject constructor() : ViewModel(), ContainerHost<AsReportState, AsReportSideEffect> {

    override val container: Container<AsReportState, AsReportSideEffect> = container(
        initialState = AsReportState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {
                    postSideEffect(AsReportSideEffect.Toast(throwable.message.toString()))
                    Log.e(TAG, "error handler: ${throwable.message}")
                }
            }
        }
    )

    companion object {
        const val TAG = "AsReportViewModel"
    }

    fun asReportViewModelInit(asDevice: AsDevice) = intent {
        reduce { state.copy(asDevice = asDevice) }
    }
}

@Immutable
data class AsReportState(
    val asDevice: AsDevice = AsDevice(meterDeviceId = "HT-T-012345")// this is init value, don't mind
)

sealed interface AsReportSideEffect {
    class Toast(val message: String) : AsReportSideEffect
}