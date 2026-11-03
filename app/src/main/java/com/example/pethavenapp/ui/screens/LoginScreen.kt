package com.example.pethavenapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pethavenapp.R
import com.example.pethavenapp.data.model.Session
import com.example.pethavenapp.ui.components.EmailField
import com.example.pethavenapp.ui.components.LoadingButton
import com.example.pethavenapp.ui.components.PasswordField
import com.example.pethavenapp.ui.components.RunningDog
import com.example.pethavenapp.ui.theme.PetHavenAppTheme
import com.example.pethavenapp.ui.theme.brandBackground
import com.example.pethavenapp.util.LocalScreenMetrics
import com.example.pethavenapp.util.ProvideScreenMetrics
import com.example.pethavenapp.util.ScreenMetrics
import com.example.pethavenapp.viewmodels.LoginEvent
import com.example.pethavenapp.viewmodels.LoginUiState
import com.example.pethavenapp.viewmodels.LoginViewModel

private class LoginActions(
    val onEmailChange: (String) -> Unit,
    val onPasswordChange: (String) -> Unit,
    val onTogglePasswordVisibility: () -> Unit,
    val onLogin: () -> Unit
)

@Composable
fun LoginScreen(
    onLoginSuccess: (Session) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentOnLoginSuccess by rememberUpdatedState(onLoginSuccess)

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is LoginEvent.LoggedIn -> currentOnLoginSuccess(event.session)
            }
        }
    }

    LoginContent(
        uiState = uiState,
        actions = LoginActions(
            onEmailChange = viewModel::onEmailChange,
            onPasswordChange = viewModel::onPasswordChange,
            onTogglePasswordVisibility = viewModel::onTogglePasswordVisibility,
            onLogin = viewModel::onLoginClick
        ),
        modifier = modifier
    )
}

@Composable
private fun LoginContent(
    uiState: LoginUiState,
    actions: LoginActions,
    modifier: Modifier = Modifier
) {
    ProvideScreenMetrics(modifier = modifier.background(brandBackground())) { metrics ->
        Scaffold(containerColor = Color.Transparent) { innerPadding ->
            val layoutModifier = Modifier
                .padding(innerPadding)
                .imePadding()
            if (metrics.isWide) {
                WideLayout(uiState, actions, metrics, layoutModifier)
            } else {
                SinglePaneLayout(uiState, actions, metrics, layoutModifier)
            }
        }
    }
}

@Composable
private fun SinglePaneLayout(
    uiState: LoginUiState,
    actions: LoginActions,
    metrics: ScreenMetrics,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = metrics.unit(0.04f)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LoginHeader(modifier = Modifier.fillMaxWidth(metrics.contentFraction))
        Spacer(Modifier.height(metrics.unit(0.06f)))
        LoginForm(uiState, actions, modifier = Modifier.fillMaxWidth(metrics.contentFraction))
    }
}

@Composable
private fun WideLayout(
    uiState: LoginUiState,
    actions: LoginActions,
    metrics: ScreenMetrics,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = metrics.unit(0.08f)),
        horizontalArrangement = Arrangement.spacedBy(metrics.unit(0.08f)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LoginHeader(modifier = Modifier.fillMaxWidth())
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(vertical = metrics.unit(0.04f)),
            verticalArrangement = Arrangement.Center
        ) {
            LoginForm(uiState, actions, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun LoginHeader(modifier: Modifier = Modifier) {
    val metrics = LocalScreenMetrics.current
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RunningDog(modifier = Modifier.fillMaxWidth(0.75f))
        Spacer(Modifier.height(metrics.unit(0.03f)))
        Text(
            text = stringResource(R.string.login_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.tertiary,
            textAlign = TextAlign.Center,
            modifier = Modifier.semantics { heading() }
        )
        Spacer(Modifier.height(metrics.unit(0.02f)))
        Text(
            text = stringResource(R.string.login_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun LoginForm(
    uiState: LoginUiState,
    actions: LoginActions,
    modifier: Modifier = Modifier
) {
    val metrics = LocalScreenMetrics.current
    val focusManager = LocalFocusManager.current
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        border = BorderStroke(Dp.Hairline, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(metrics.unit(0.05f)),
            verticalArrangement = Arrangement.spacedBy(metrics.unit(0.02f))
        ) {
            EmailField(
                value = uiState.email,
                onValueChange = actions.onEmailChange,
                onNext = { focusManager.moveFocus(FocusDirection.Down) },
                error = uiState.emailError
            )
            PasswordField(
                value = uiState.password,
                onValueChange = actions.onPasswordChange,
                isVisible = uiState.isPasswordVisible,
                onToggleVisibility = actions.onTogglePasswordVisibility,
                onDone = {
                    focusManager.clearFocus()
                    actions.onLogin()
                },
                error = uiState.passwordError
            )
            uiState.authError?.let { AuthErrorMessage(it) }
            LoadingButton(
                text = stringResource(R.string.login_button),
                loadingText = stringResource(R.string.login_loading),
                isLoading = uiState.isLoading,
                onClick = {
                    focusManager.clearFocus()
                    actions.onLogin()
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun AuthErrorMessage(messageRes: Int, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(messageRes),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.error,
        modifier = modifier.semantics { liveRegion = LiveRegionMode.Polite }
    )
}

private val previewActions = LoginActions({}, {}, {}, {})

@Preview(name = "Teléfono", device = "spec:width=411dp,height=891dp", locale = "es")
@Composable
private fun LoginPhonePreview() {
    PetHavenAppTheme { LoginContent(LoginUiState(), previewActions) }
}

@Preview(name = "Con errores", device = "spec:width=411dp,height=891dp", locale = "es")
@Composable
private fun LoginErrorPreview() {
    PetHavenAppTheme {
        LoginContent(
            LoginUiState(
                email = "correo",
                emailError = R.string.login_error_email_invalid,
                authError = R.string.auth_error_invalid_credentials
            ),
            previewActions
        )
    }
}

@Preview(name = "Teléfono horizontal", device = "spec:width=891dp,height=411dp", locale = "es")
@Composable
private fun LoginLandscapePreview() {
    PetHavenAppTheme { LoginContent(LoginUiState(isLoading = true), previewActions) }
}

@Preview(name = "Tableta vertical", device = "spec:width=800dp,height=1280dp", locale = "es")
@Composable
private fun LoginTabletPreview() {
    PetHavenAppTheme { LoginContent(LoginUiState(), previewActions) }
}
