package com.hitec.presentation.main.device_detail.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hitec.presentation.R
import com.hitec.presentation.component.button.HorizontalScrollUnderlinedTextButton
import com.hitec.presentation.theme.Paddings
import com.hitec.presentation.theme.RenewSmartSetTheme
import com.hitec.presentation.theme.primaryBlue1

@Composable
fun NfcMenu(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    onChangeSerialClick: () -> Unit,
    onReadConfigClick: () -> Unit,
    onWriteConfigClick: () -> Unit,
    onSetSleepClick: () -> Unit,
    onSetActiveClick: () -> Unit,
    onResetDeviceClick: () -> Unit,
    onReadMeterClick: () -> Unit,
    onReqCommClick: () -> Unit,
    onCheckCommClick: () -> Unit,
    onUpdateFirmwareBslClick: () -> Unit,
    onUpdateFirmwareFotaClick: () -> Unit,
    onChangeRiHourToMinuteClick: () -> Unit,
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = isVisible,
        enter = slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) + fadeIn(animationSpec = tween(300)), // 페이드 인 효과 추가 (선택적),
        exit = slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }) + fadeOut(animationSpec = tween(300)) // 페이드 아웃 효과 추가 (선택적)
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .clip(RoundedCornerShape(size = 16.dp))
                .background(primaryBlue1.copy(alpha = 0.5f)),
            verticalArrangement = Arrangement.Bottom
        ) {
            HorizontalScrollUnderlinedTextButton(
                modifier = Modifier.padding(horizontal = Paddings.small),
                text = stringResource(id = R.string.change_serial),
                onClick = onChangeSerialClick
            )
            HorizontalScrollUnderlinedTextButton(
                modifier = Modifier.padding(horizontal = Paddings.small),
                text = stringResource(id = R.string.read_config),
                onClick = onReadConfigClick
            )
            HorizontalScrollUnderlinedTextButton(
                modifier = Modifier.padding(horizontal = Paddings.small),
                text = stringResource(id = R.string.write_config),
                onClick = onWriteConfigClick
            )
            HorizontalScrollUnderlinedTextButton(
                modifier = Modifier.padding(horizontal = Paddings.small),
                text = stringResource(id = R.string.set_sleep),
                onClick = onSetSleepClick
            )
            HorizontalScrollUnderlinedTextButton(
                modifier = Modifier.padding(horizontal = Paddings.small),
                text = stringResource(id = R.string.set_active),
                onClick = onSetActiveClick
            )
            HorizontalScrollUnderlinedTextButton(
                modifier = Modifier.padding(horizontal = Paddings.small),
                text = stringResource(id = R.string.reset_device),
                onClick = onResetDeviceClick
            )
            HorizontalScrollUnderlinedTextButton(
                modifier = Modifier.padding(horizontal = Paddings.small),
                text = stringResource(id = R.string.read_meter),
                onClick = onReadMeterClick
            )
            HorizontalScrollUnderlinedTextButton(
                modifier = Modifier.padding(horizontal = Paddings.small),
                text = stringResource(id = R.string.request_communication),
                onClick = onReqCommClick
            )
            HorizontalScrollUnderlinedTextButton(
                modifier = Modifier.padding(horizontal = Paddings.small),
                text = stringResource(id = R.string.check_communication),
                onClick = onCheckCommClick
            )
            HorizontalScrollUnderlinedTextButton(
                modifier = Modifier.padding(horizontal = Paddings.small),
                text = stringResource(id = R.string.update_firmware_bsl),
                onClick = onUpdateFirmwareBslClick
            )
            HorizontalScrollUnderlinedTextButton(
                modifier = Modifier.padding(horizontal = Paddings.small),
                text = stringResource(id = R.string.update_firmware_fota),
                onClick = onUpdateFirmwareFotaClick
            )
            HorizontalScrollUnderlinedTextButton(
                modifier = Modifier.padding(horizontal = Paddings.small),
                text = stringResource(id = R.string.change_ri_hour_to_minute),
                onClick = onChangeRiHourToMinuteClick
            )
        }
    }
}

@Preview
@Composable
fun NfcMenuPreview() {
    RenewSmartSetTheme {
        NfcMenu(
            isVisible = true,
            onChangeSerialClick = {},
            onReadConfigClick = {},
            onWriteConfigClick = {},
            onSetSleepClick = {},
            onSetActiveClick = {},
            onResetDeviceClick = {},
            onReadMeterClick = {},
            onReqCommClick = {},
            onCheckCommClick = {},
            onUpdateFirmwareBslClick = {},
            onUpdateFirmwareFotaClick = {},
            onChangeRiHourToMinuteClick = {},
        )
    }
}