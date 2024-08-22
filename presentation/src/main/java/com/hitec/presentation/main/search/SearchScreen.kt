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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.hitec.domain.model.InstallDevice
import com.hitec.presentation.R
import com.hitec.presentation.main.component.InstallDeviceCard
import com.hitec.presentation.theme.Paddings
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current

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
        onItemClick = { installDevice ->
            viewModel.openDeviceDetailScreen(navController, installDevice)
        }
    )
}

@Composable
private fun SearchContent(
    state: SearchState,
    onChipSelected: (String) -> Unit,
    onSearch: (String) -> Unit,
    onItemClick: (InstallDevice) -> Unit,
) {
    var userInput by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column {
        SearchBox(
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
                    installDeviceList = state.searchedInstallDeviceList,
                    onItemClick = onItemClick
                )
            }
        }
    }
}


@Composable
private fun SearchBox(
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
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search,
                autoCorrect = true,
                capitalization = KeyboardCapitalization.Words
            ),
            keyboardActions = KeyboardActions(
                onSearch = { searchAction() }
            ),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            maxLines = 1,
            singleLine = true,
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "SearchIcon")
            },
            trailingIcon = {
                if (keyword.isNotEmpty()) {
                    Icon(
                        modifier = Modifier.clickable { onClear() },
                        imageVector = Icons.Filled.Cancel,
                        contentDescription = "Cancel"
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
                            borderWidth = 1.dp,
                            selectedBorderWidth = 1.dp,
                            selectedBorderColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    )
                }
            }
        } else {
            Text("No options available")
        }
    }
}

@Composable
private fun SearchResult(
    installDeviceList: List<InstallDevice>,
    onItemClick: (InstallDevice) -> Unit
) {
    LazyColumn {
        items(installDeviceList.size) { index ->
            InstallDeviceCard(
                installDevice = installDeviceList[index],
                onClick = { onItemClick(installDeviceList[index]) }
            )
        }
    }
}