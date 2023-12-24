package online.bacovsky.trainingmanagement.presentation.add_client_screen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import online.bacovsky.trainingmanagement.util.Routes
import online.bacovsky.trainingmanagement.util.UiEvent
import online.bacovsky.trainingmanagement.R
import online.bacovsky.trainingmanagement.util.UiText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddClientScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: AddClientViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.validationEvents.collect { event ->
            when(event) {
                is AddClientViewModel.ValidationEvent.Success -> {
                    Toast.makeText(
                        context,
                        UiText.StringResource(R.string.saved).asString(context),
                        Toast.LENGTH_LONG
                    ).show()
                    onNavigate(UiEvent.Navigate(Routes.CLIENT_LIST_SCREEN))
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.add_client),
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onNavigate(UiEvent.Navigate(Routes.CLIENT_LIST_SCREEN))
                        }
                    ) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = (paddingValues.calculateStartPadding(LayoutDirection.Ltr) + 10.dp),
                    top = paddingValues.calculateTopPadding(),
                    end = (paddingValues.calculateEndPadding(LayoutDirection.Ltr) + 10.dp),
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.name,
                onValueChange = {
                    viewModel.onEvent(AddClientFormEvent.NameChanged(it))
                },
                isError = state.nameError != null,
                label = { Text(text = stringResource(id = R.string.full_name)) },
                leadingIcon = {
                    Icon(Icons.Outlined.Person, contentDescription = "Person Icon")
                }
            )
            if (state.nameError != null) {
                Text(
                    text =  state.nameError.asString(),
                    color = MaterialTheme.colorScheme.error
                )
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.price,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = {
                    viewModel.onEvent(AddClientFormEvent.PriceChanged(it))
                },
                isError = state.priceError != null,
                label = {
                    Text(text = stringResource(id = R.string.price_for_training))
                },
                leadingIcon = {
                    Icon(Icons.Outlined.Payments, contentDescription = "Payment Icon")
                }
            )
            if (state.priceError != null) {
                Text(
                    text =  state.priceError.asString(),
                    color = MaterialTheme.colorScheme.error
                )
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.funds,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { funds ->
                    viewModel.onEvent(AddClientFormEvent.FundsChanged(
                        funds = funds,
                        fundsNote = UiText.StringResource(R.string.initial_funds).asString(context))
                    )
                },
                isError = state.fundsError != null,
                label = {
                    Text(
                        text = stringResource(id = R.string.account_balance)
                    )
                },
                leadingIcon = {
                    Icon(Icons.Outlined.AccountBalance, contentDescription = "Account Icon")
                }
            )
            if (state.fundsError != null) {
                Text(
                    text =  state.fundsError.asString(),
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    viewModel.onEvent(AddClientFormEvent.Submit)
                }
            ) {
                Text(text = stringResource(id = R.string.save))
            }
        }
    }
}

