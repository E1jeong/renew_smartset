package com.hitec.presentation.main

import android.widget.Toast
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun MainScreen(
    viewModel: MainViewModel
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MainSideEffect.Toast -> {
                Toast.makeText(
                    context,
                    sideEffect.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    MainScreen(state.localSiteEngWrittenByUser)
}

@Composable
private fun MainScreen(test: String) {
    Text(text = test)
}
