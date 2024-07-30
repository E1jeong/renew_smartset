package com.hitec.presentation.login

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hitec.presentation.R
import com.hitec.presentation.component.DefaultTextField
import com.hitec.presentation.main.MainActivity
import com.hitec.presentation.theme.RenewSmartSetTheme
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

const val TAG = "LoginScreen"
const val LOCAL_SITE_ENG = "localSiteEng"

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

            is LoginSideEffect.NavigateToMainActivity -> {
                context.startActivity(
                    Intent(context, MainActivity::class.java).apply {
                        putExtra(LOCAL_SITE_ENG, state.localSiteEngWrittenByUser)
                    }
                )
            }
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
        onLoginClick = viewModel::onLoginClick,
        onDownloadLocalSiteClick = viewModel::getLocalSite,
        isLocalSiteWarningVisible = state.isLocalSiteWarningVisible,
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
    onLoginClick: () -> Unit,
    onDownloadLocalSiteClick: () -> Unit,
    isLocalSiteWarningVisible: Boolean,
) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoginHeader()
            LoginForm(
                id = id,
                password = password,
                localSite = localSite,
                isSwitchOn = isSwitchOn,
                onIdChange = onIdChange,
                onPasswordChange = onPasswordChange,
                onLocalSiteChange = onLocalSiteChange,
                onSwitchChange = onSwitchChange,
                onDownloadLocalSiteClick = onDownloadLocalSiteClick,
                isLocalSiteWarningVisible = isLocalSiteWarningVisible
            )
            Spacer(modifier = Modifier.weight(1f))
            LoginFooter(onLoginClick = onLoginClick)
        }
    }
}

@Composable
private fun LoginHeader() {
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
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun LoginForm(
    id: String,
    password: String,
    localSite: String,
    isSwitchOn: Boolean,
    onIdChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLocalSiteChange: (String) -> Unit,
    onSwitchChange: (Boolean) -> Unit,
    onDownloadLocalSiteClick: () -> Unit,
    isLocalSiteWarningVisible: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        LoginSwitch(isSwitchOn = isSwitchOn, onSwitchChange = onSwitchChange)
        LoginTextField(label = R.string.id, value = id, onValueChange = onIdChange)
        Spacer(modifier = Modifier.height(20.dp))
        LoginTextField(
            label = R.string.password,
            value = password,
            onValueChange = onPasswordChange,
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(20.dp))
        LocalSiteSection(
            localSite = localSite,
            onLocalSiteChange = onLocalSiteChange,
            isLocalSiteWarningVisible = isLocalSiteWarningVisible,
            onDownloadLocalSiteClick = onDownloadLocalSiteClick
        )
    }
}

@Composable
private fun LoginSwitch(
    isSwitchOn: Boolean,
    onSwitchChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Switch(
                checked = isSwitchOn,
                onCheckedChange = onSwitchChange,
            )
            Text(
                text = stringResource(R.string.login_switch_text),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
private fun LoginTextField(
    label: Int, // R.string resource ID
    value: String,
    onValueChange: (String) -> Unit,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = stringResource(label))
        DefaultTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            visualTransformation = visualTransformation
        )
    }
}

@Composable
private fun LocalSiteSection(
    localSite: String,
    onLocalSiteChange: (String) -> Unit,
    isLocalSiteWarningVisible: Boolean,
    onDownloadLocalSiteClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = stringResource(R.string.local_site))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            DefaultTextField(
                modifier = Modifier.fillMaxWidth(0.5f),
                value = localSite,
                onValueChange = onLocalSiteChange
            )
            Spacer(modifier = Modifier.width(10.dp))
            if (isLocalSiteWarningVisible) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onDownloadLocalSiteClick
                ) {
                    Text(
                        text = stringResource(R.string.login_button_download_local_site),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
        if (isLocalSiteWarningVisible) {
            Text(
                text = stringResource(R.string.login_text_local_site_warning),
                color = Color.Red,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
private fun LoginFooter(onLoginClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp, start = 12.dp, end = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onLoginClick
        ) {
            Text(text = stringResource(R.string.login))
        }
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = stringResource(id = R.string.login_bottom_copyright),
            style = MaterialTheme.typography.labelMedium,
        )
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
            onLoginClick = {},
            onDownloadLocalSiteClick = {},
            isLocalSiteWarningVisible = false,
        )
    }
}