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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.hitec.presentation.component.button.HorizontalScrollUnderlinedTextButton
import com.hitec.presentation.component.button.UnderlinedTextButton
import com.hitec.presentation.theme.Paddings
import com.hitec.presentation.theme.primaryBlue1

@Composable
fun NfcMenu(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    onNfcTagDetected: () -> Unit,
    onUpdateClick: () -> Unit
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
                .clip(RoundedCornerShape(8.dp))
                .background(primaryBlue1.copy(alpha = 0.5f)),
            verticalArrangement = Arrangement.Bottom
        ) {
            HorizontalScrollUnderlinedTextButton(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(horizontal = Paddings.small),
                text = "change serial",
                onClick = {
                    onNfcTagDetected()
                    onUpdateClick()
                }
            )
        }
    }
}