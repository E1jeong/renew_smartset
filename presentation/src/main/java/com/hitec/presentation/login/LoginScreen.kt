package com.hitec.presentation.login

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hitec.presentation.R
import com.hitec.presentation.theme.RenewSmartSetTheme
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

const val TAG = "LoginScreen"

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is LoginSideEffect.Toast -> Toast.makeText(
                context,
                sideEffect.message,
                Toast.LENGTH_SHORT
            ).show()

            is LoginSideEffect.NavigateToMainActivity -> {}
        }
    }

    LoginScreen(
        id = state.id,
        password = state.password,
        localSite = state.localSite,
        isSwitchOn = state.isSwitchOn,
        onIdChange = viewModel::onIdChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLocalSiteChange = viewModel::onLocalSiteChange,
        onSwitchChange = viewModel::onSwitchChange,
        onLoginClick = viewModel::onLoginClick
    )
}

@Composable
private fun LoginScreen(
    id: String,
    password: String,
    localSite: String,
    isSwitchOn: Boolean,
    onIdChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLocalSiteChange: (String) -> Unit,
    onSwitchChange: (Boolean) -> Unit,
    onLoginClick: () -> Unit
) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.login_title),
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Text(
                        text = stringResource(R.string.login_sub_title),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 28.dp, end = 12.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Switch(
                        checked = isSwitchOn,
                        onCheckedChange = { onSwitchChange(it) },
                    )
                    Text(
                        text = "Remember me",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, start = 12.dp, end = 12.dp)
                ) {
                    Text(text = "ID")
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = id,
                        onValueChange = onIdChange,
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        )
                    )
                    Spacer(modifier = Modifier.size(20.dp))
                    Text(text = "Password")
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = password,
                        onValueChange = onPasswordChange,
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        )
                    )
                    Spacer(modifier = Modifier.size(20.dp))
                    Text(text = "LocalSite")
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth(0.5f),
                        value = localSite,
                        onValueChange = onLocalSiteChange,
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        )
                    )
                }
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp, start = 12.dp, end = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(modifier = Modifier.fillMaxWidth(), onClick = onLoginClick) {
                    Text(text = "Login")
                }
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = stringResource(id = R.string.login_bottom_copyright),
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    RenewSmartSetTheme {
        LoginScreen(
            id = "",
            password = "",
            localSite = "",
            isSwitchOn = false,
            onIdChange = {},
            onPasswordChange = {},
            onLocalSiteChange = {},
            onSwitchChange = {},
            onLoginClick = {}
        )
    }
}