package online.bacovsky.trainingmanagement.presentation.client_add_funds_screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import online.bacovsky.trainingmanagement.R
import online.bacovsky.trainingmanagement.presentation.client_payment_history_screen.PaymentItem
import online.bacovsky.trainingmanagement.util.UiEvent
import online.bacovsky.trainingmanagement.util.UiText
import online.bacovsky.trainingmanagement.util.toLocalizedDateTimeFormat
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientPaymentListScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: ClientAddFundsViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val state = viewModel.state
    val snackbarHostState = remember { SnackbarHostState() }
    val payments = viewModel.payments.collectAsState(initial = emptyList())

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect {event ->
            when(event) {
                is UiEvent.Navigate -> {
                    onNavigate(event)
                }
                else -> {}
            }
        }
    }
    LaunchedEffect(key1 = true) {
        viewModel.validationEvents.collect {event ->
            when(event) {
                is ClientAddFundsViewModel.ValidationEvent.Success -> {
                    Toast.makeText(
                        context,
                        UiText.StringResource(R.string.saved).asString(context),
                        Toast.LENGTH_LONG
                    ).show()
//                    viewModel.onEvent(ClientPaymentListEvent.OnBackButtonClick)
//                    onNavigate(UiEvent.Navigate(Routes.CLIENT_LIST_SCREEN))
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
                        text = stringResource(id = R.string.add_funds_screen_title)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            viewModel.onEvent(ClientAddFundsEvent.OnBackButtonClick)
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
                .padding(
                    start = (paddingValues.calculateStartPadding(LayoutDirection.Ltr) + 10.dp),
                    top = paddingValues.calculateTopPadding(),
                    end = (paddingValues.calculateEndPadding(LayoutDirection.Ltr) + 10.dp),
                    bottom = paddingValues.calculateBottomPadding()
                )
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {
                    Text(
                        text = stringResource(id = R.string.add_funds_placeholder)
                    )
                },
                value = state.funds,
                onValueChange = { newValue ->
                    viewModel.onEvent(ClientAddFundsEvent.OnFundsChanged(
                        funds = newValue,
                        fundsNote = UiText.StringResource(
                            resourceId = R.string.funds_added_at,
                            args = arrayOf(LocalDateTime.now().toLocalizedDateTimeFormat())
                        ).asString(context)
                    ))
                },
                isError = state.fundsError != null
            )

            if (state.fundsError != null) {
                Text(
                    text =  state.fundsError.asString(),
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier
                .height(8.dp)
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                          viewModel.onEvent(ClientAddFundsEvent.OnSaveClick)
                },
            ) {
                Text(text = stringResource(id = R.string.save))
            }

            Spacer(modifier = Modifier.size(30.dp))

            Text(
                text = stringResource(id = R.string.payment_history),
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.size(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
//            Text(
//                text = "Amount",
//                style = MaterialTheme.typography.headlineSmall
//            )
//            Text(
//                text = "Note",
//                style = MaterialTheme.typography.headlineSmall
//            )
            }
            Row(
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(payments.value) { payment ->
                        PaymentItem(payment)
                    }
                }
            }
        }
    }
}