package online.bacovsky.trainingmanagement.presentation.client_detail_edit_screen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import online.bacovsky.trainingmanagement.R
import online.bacovsky.trainingmanagement.ui.YesNoDialog
import online.bacovsky.trainingmanagement.util.Routes
import online.bacovsky.trainingmanagement.util.UiEvent
import online.bacovsky.trainingmanagement.util.UiText
import online.bacovsky.trainingmanagement.util.currencySymbol
import online.bacovsky.trainingmanagement.util.formattedNumber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientDetailEditScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: ClientDetailEditScreenViewModel = hiltViewModel(),
) {
    val state = viewModel.state
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        viewModel.validationEvents.collect { event ->
            when(event) {
                is ClientDetailEditScreenViewModel.ClientDetailEvent.FormIsValid -> {
                    Toast.makeText(
                        context,
                        UiText.StringResource(R.string.changes_saved).asString(context),
                        Toast.LENGTH_LONG
                    ).show()
                    onNavigate(UiEvent.Navigate(Routes.CLIENT_LIST_SCREEN))
                }
                is ClientDetailEditScreenViewModel.ClientDetailEvent.ClientDeleted -> {
                    Toast.makeText(
                        context,
                        UiText.StringResource(R.string.client_deleted).asString(context),
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
                        text = stringResource(id = R.string.client_detail),
                        maxLines = 1
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            onNavigate(
                                UiEvent.Navigate(
                                    Routes.CLIENT_FUNDS_SCREEN
                                        .replace("{clientId}", "${state.id}")
                                )
                            )
                        }
                    ) {
                        Icon(Icons.Outlined.AttachMoney, contentDescription = "Add/Remove Money")
                    }
                    IconButton(onClick = {
//                         delete client
                        showDeleteDialog = true
                    }) {
                        Icon(Icons.Outlined.Delete, contentDescription = "Delete Client")
                    }
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

        if (showDeleteDialog) {
            YesNoDialog(
                showDialog = showDeleteDialog,
                onDismissRequest = {
                    showDeleteDialog = !showDeleteDialog
                },
                onDismissRequestButtonClick = {
                    showDeleteDialog = !showDeleteDialog
                },
                onConfirmation = {
                    viewModel.onEvent(EditClientFormEvent.OnDeleteClick)
                },
                dialogTitle = stringResource(
                    id = R.string.dialog_delete_client_title,
                    formatArgs = arrayOf(state.name)
                ),
                dialogText = stringResource(
                    id = R.string.dialog_delete_client_text,
                    formatArgs = arrayOf(state.name)
                ),
            )
        }

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(
                start = (paddingValues.calculateStartPadding(LayoutDirection.Ltr) + 10.dp),
                top = paddingValues.calculateTopPadding(),
                end = (paddingValues.calculateEndPadding(LayoutDirection.Ltr) + 10.dp),
                bottom = paddingValues.calculateBottomPadding()
            )
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.name,
                label = { Text(text = stringResource(id = R.string.full_name)) },
                leadingIcon = {
                    Icon(Icons.Outlined.Person, contentDescription = "Person Icon")
                },
                onValueChange = { name ->
                    viewModel.onEvent(EditClientFormEvent.OnNameChanged(name))
                },
                isError = state.nameError != null
            )

            if (state.nameError != null) {
                Text(
                    text = state.nameError.asString(),
                    color = MaterialTheme.colorScheme.error
                )
            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.phoneNumber,
                label = { Text(text = stringResource(id = R.string.phone_number)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                leadingIcon = {
                    Icon(Icons.Outlined.Phone, contentDescription = "Phone Icon")
                },
                onValueChange = { phoneNumber ->
                    viewModel.onEvent(EditClientFormEvent.OnPhoneNumberChanged(phoneNumber))
                },
                isError = state.phoneNumberError != null
            )

            if (state.phoneNumberError != null) {
                Text(
                    text = state.phoneNumberError.asString(),
                    color = MaterialTheme.colorScheme.error
                )
            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.price,
                label = { Text(text = stringResource(id = R.string.price_for_training)) },
                leadingIcon = {
                    Icon(Icons.Outlined.Payments, contentDescription = "Person Icon")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { price ->
                    viewModel.onEvent(EditClientFormEvent.OnPricePerTrainingChanged(price))
                },
                isError = state.priceError != null
            )

            if (state.priceError != null) {
                Text(
                    text = state.priceError.asString(),
                    color = MaterialTheme.colorScheme.error
                )
            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = "${state.balance?.formattedNumber()} $currencySymbol",
                readOnly = true,
                onValueChange = {},
                label = {
                    Text(text = stringResource(id = R.string.account_balance))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.AccountBalanceWallet,
                        contentDescription = "Client balance icon"
                    )
                }
            )

            Spacer(modifier = Modifier.padding(8.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.onEvent(EditClientFormEvent.Submit)
                }
            ) {
                Text(text = stringResource(id = R.string.save))
            }
        }
    }
}