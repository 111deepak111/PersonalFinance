package com.example.personalfinance.ui.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.personalfinance.ui.models.AccountFormState
import com.example.personalfinance.ui.models.FinanceViewModel

@Composable
fun AddAccountDialog (
    onDismiss: () -> Unit,
    viewModel: FinanceViewModel
) {
    var accountName by remember { mutableStateOf("") };
    var initialBalance by remember { mutableStateOf("") };
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {Text(text = "New Account", style= MaterialTheme.typography.titleMedium)},
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = accountName,
                    onValueChange = { accountName = it },
                    label = { Text("Account Name (e.g., HDFC Bank)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = initialBalance,
                    onValueChange = { initialBalance = it },
                    label = { Text("Opening Balance (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}