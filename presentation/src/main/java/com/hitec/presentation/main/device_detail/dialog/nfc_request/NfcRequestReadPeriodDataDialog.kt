package com.hitec.presentation.main.device_detail.dialog.nfc_request

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.SendToMobile
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.hitec.presentation.R
import com.hitec.presentation.component.button.PrimaryButton
import com.hitec.presentation.component.icon.LeadingIcon
import com.hitec.presentation.theme.Paddings
import com.hitec.presentation.theme.RenewSmartSetTheme
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Composable
fun NfcRequestReadPeriodDataDialog(
    visible: Boolean = false,
    startDate: LocalDate?,
    endDate: LocalDate?,
    onStartDateSelected: (LocalDate) -> Unit,
    onEndDateSelected: (LocalDate) -> Unit,
    onTagButtonClick: () -> Unit,
    onResultDialogVisible: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    if (visible) {
        Dialog(onDismissRequest = onDismissRequest) {
            val screenHeight = LocalConfiguration.current.screenHeightDp.dp

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = screenHeight * 0.8f),
                elevation = CardDefaults.cardElevation(defaultElevation = Paddings.none),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                shape = MaterialTheme.shapes.large
            ) {
                Header()
                Content(
                    startDate = startDate,
                    endDate = endDate,
                    onStartDateSelected = onStartDateSelected,
                    onEndDateSelected = onEndDateSelected
                )
                Footer(
                    onTagButtonClick = onTagButtonClick,
                    onResultDialogVisible = onResultDialogVisible,
                    onDismissRequest = onDismissRequest
                )
            }
        }
    }
}

@Composable
private fun Header() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(Paddings.large),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.read_period_data),
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayMedium.copy(
                color = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}

@Composable
private fun Content(
    startDate: LocalDate?,
    endDate: LocalDate?,
    onStartDateSelected: (LocalDate) -> Unit,
    onEndDateSelected: (LocalDate) -> Unit
) {
    // 날짜 차이 계산
    val daysDifference = if (startDate != null && endDate != null) {
        ChronoUnit.DAYS.between(startDate, endDate)
    } else null

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Paddings.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DateSelectionRow(
            label = "시작 날짜",
            selectedDate = startDate,
            onSelectDate = onStartDateSelected
        )

        Spacer(modifier = Modifier.height(Paddings.small))

        if (daysDifference != null) {
            when {
                daysDifference > 0 -> {
                    Text("~\t\t(${daysDifference}일 차이)", style = MaterialTheme.typography.bodyLarge)
                }

                daysDifference == 0L -> {
                    Text("~\t\t(같은 날짜)", style = MaterialTheme.typography.bodyLarge)
                }

                else -> {
                    Text(
                        "~\t\t(종료 날짜가 시작 날짜보다 이전입니다)",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Red
                    )
                }
            }
        } else {
            Text("~", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(Paddings.small))

        DateSelectionRow(
            label = "종료 날짜",
            selectedDate = endDate,
            onSelectDate = onEndDateSelected
        )
    }
}

@Composable
private fun DateSelectionRow(
    label: String,
    selectedDate: LocalDate?,
    onSelectDate: (LocalDate) -> Unit
) {
    var openDialog by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = selectedDate?.toString() ?: label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(Paddings.small)
        )
        Button(onClick = { openDialog = true }) {
            Text(text = "날짜 선택")
        }
        if (openDialog) {
            DatePickerDialogDemo(
                initialDate = selectedDate ?: LocalDate.now(),
                onDismissRequest = { openDialog = false },
                onDateSelected = { selectedMillis ->
                    val date = LocalDate.ofEpochDay(selectedMillis / (24 * 60 * 60 * 1000))
                    onSelectDate(date)
                    openDialog = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerDialogDemo(
    initialDate: LocalDate,
    onDismissRequest: () -> Unit,
    onDateSelected: (Long) -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate.toEpochDay() * 24 * 60 * 60 * 1000
    )
    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { onDateSelected(it) }
                }
            ) {
                Text("확인")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("취소")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
private fun Footer(
    onTagButtonClick: () -> Unit,
    onDismissRequest: () -> Unit,
    onResultDialogVisible: () -> Unit
) {
    Row(
        modifier = Modifier.padding(Paddings.large),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PrimaryButton(
            modifier = Modifier.weight(1f),
            text = "Tag",
            leadingIcon = LeadingIcon(Icons.Filled.SendToMobile, null),
            onClick = {
                onTagButtonClick()
                onResultDialogVisible()
                onDismissRequest()
            }
        )
        Spacer(modifier = Modifier.width(Paddings.large))
        PrimaryButton(
            modifier = Modifier.weight(1f),
            text = "Close",
            leadingIcon = LeadingIcon(Icons.Filled.Close, null),
            onClick = onDismissRequest
        )
    }
}


@Preview
@Composable
fun NfcRequestPeriodDataDialogPreview() {
    RenewSmartSetTheme {
        NfcRequestReadPeriodDataDialog(
            visible = true,
            startDate = null,
            endDate = null,
            onStartDateSelected = {},
            onEndDateSelected = {},
            onTagButtonClick = {},
            onResultDialogVisible = {},
            onDismissRequest = {},
        )
    }
}