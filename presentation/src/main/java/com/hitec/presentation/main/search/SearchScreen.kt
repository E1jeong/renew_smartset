package com.hitec.presentation.main.search

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.hitec.domain.model.AsDevice
import com.hitec.domain.model.InstallDevice
import com.hitec.presentation.R
import com.hitec.presentation.main.asdevice.component.AsDeviceCard
import com.hitec.presentation.main.installdevice.component.InstallDeviceCard
import com.hitec.presentation.navigation.RouteName
import com.hitec.presentation.theme.Paddings
import com.hitec.presentation.theme.backgroundGray0
import com.hitec.presentation.theme.textFieldDefaultColor
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current

    val previousRoute = navController.previousBackStackEntry?.destination?.route
    when (previousRoute) {
        RouteName.INSTALL_DEVICE -> viewModel.setPreviousRoute(RouteName.INSTALL_DEVICE)
        RouteName.AS_DEVICE -> viewModel.setPreviousRoute(RouteName.AS_DEVICE)
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is SearchSideEffect.Toast -> {
                Toast.makeText(
                    context,
                    sideEffect.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    SearchContent(
        state = state,
        onChipSelected = viewModel::onChipSelected,
        onSearch = viewModel::search,
        onInstallDeviceClick = { installDevice ->
            viewModel.openDeviceDetailScreen(navController, installDevice)
        },
        onAsDeviceClick = { asDevice ->
            viewModel.openAsReportScreen(navController, asDevice)
        }
    )
}

@Composable
private fun SearchContent(
    state: SearchState,
    onChipSelected: (String) -> Unit,
    onSearch: (String) -> Unit,
    onInstallDeviceClick: (InstallDevice) -> Unit,
    onAsDeviceClick: (AsDevice) -> Unit,
) {
    var userInput by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column {
        SearchBox(
            isInstallDevice = state.previousRoute == RouteName.INSTALL_DEVICE,
            keyword = userInput,
            onValueChange = { userInput = it },
            searchAction = {
                onSearch(userInput)
                keyboardController?.hide()
            },
            onClear = { userInput = "" }
        )
        SubAreaChipGroup(
            subAreaList = state.subAreaList,
            selectedChip = state.selectedChip,
            onChipSelected = onChipSelected
        )

        Box(modifier = Modifier.fillMaxSize()) {
            if (state.isNetworkLoading) {
                Text(stringResource(R.string.loading))
            } else {
                SearchResult(
                    isInstallDevice = state.previousRoute == RouteName.INSTALL_DEVICE,
                    installDeviceList = state.searchedInstallDeviceList,
                    onInstallDeviceClick = onInstallDeviceClick,
                    asDeviceList = state.searchedAsDeviceList,
                    onAsDeviceClick = onAsDeviceClick
                )
            }
        }
    }
}


@Composable
private fun SearchBox(
    isInstallDevice: Boolean,
    keyword: String,
    onValueChange: (String) -> Unit,
    searchAction: () -> Unit,
    onClear: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Paddings.medium),
            value = keyword,
            onValueChange = onValueChange,
            placeholder = { Text(text = stringResource(R.string.search_placeholder_imei)) },
            shape = RoundedCornerShape(size = 8.dp),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                autoCorrectEnabled = true,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search,
            ),
            keyboardActions = KeyboardActions(
                onSearch = { searchAction() }
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = if (isInstallDevice) textFieldDefaultColor else backgroundGray0,
                unfocusedContainerColor = if (isInstallDevice) textFieldDefaultColor else backgroundGray0,
                disabledContainerColor = if (isInstallDevice) textFieldDefaultColor else backgroundGray0,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            maxLines = 1,
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(id = R.string.search)
                )
            },
            trailingIcon = {
                if (keyword.isNotEmpty()) {
                    Icon(
                        modifier = Modifier.clickable { onClear() },
                        imageVector = Icons.Filled.Cancel,
                        contentDescription = stringResource(id = R.string.cancel)
                    )
                }
            }
        )
    }
}

@Composable
private fun SubAreaChipGroup(
    subAreaList: List<String>,
    selectedChip: String,
    onChipSelected: (String) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = Paddings.medium)) {
        if (subAreaList.isNotEmpty()) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(subAreaList.size) { index ->
                    val subArea = subAreaList[index]
                    val isSelected = subArea == selectedChip
                    FilterChip(
                        selected = isSelected,
                        onClick = { onChipSelected(subArea) },
                        label = { Text(subArea) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = true,
                            borderWidth = 1.dp,
                            selectedBorderWidth = 1.dp,
                            selectedBorderColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    )
                }
            }
        } else {
            Text(stringResource(id = R.string.search_empty_chip_group_warning))
        }
    }
}

@Composable
private fun SearchResult(
    isInstallDevice: Boolean,
    installDeviceList: List<InstallDevice>,
    onInstallDeviceClick: (InstallDevice) -> Unit,
    asDeviceList: List<AsDevice>,
    onAsDeviceClick: (AsDevice) -> Unit,
) {
    if (isInstallDevice) {
        LazyColumn {
            items(installDeviceList.size) { index ->
                InstallDeviceCard(
                    installDevice = installDeviceList[index],
                    onClick = { onInstallDeviceClick(installDeviceList[index]) }
                )
            }
        }
    } else {
        LazyColumn {
            items(asDeviceList.size) { index ->
                AsDeviceCard(
                    asDevice = asDeviceList[index],
                    onClick = { onAsDeviceClick(asDeviceList[index]) }
                )
            }
        }
    }
}