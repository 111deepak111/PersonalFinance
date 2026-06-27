package com.example.personalfinance.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.personalfinance.ui.models.FinanceViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.personalfinance.data.metadata.TransactionPartyType

@Composable
fun AddStatusCategoryDialog ( onDismiss: () -> Unit, viewModel: FinanceViewModel) {
    var categoryName by remember { mutableStateOf("") }
    var isAsset by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Asset / Liability Category", style = MaterialTheme.typography.titleMedium) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = categoryName,
                    onValueChange = { categoryName = it },
                    label = { Text("Category Name (e.g., Real Estate)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // A clean segmented control for selecting Asset vs Liability
                Text("Classification", style = MaterialTheme.typography.labelMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = isAsset,
                        onClick = { isAsset = true },
                        label = { Text("Asset (+)") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = !isAsset,
                        onClick = { isAsset = false },
                        label = { Text("Liability (-)") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (categoryName.isNotBlank()) {
                        val partyType = if (isAsset) TransactionPartyType.ASSETS else TransactionPartyType.LIABILITY
                        viewModel.createStatusCategory(name = categoryName, type = partyType)
                        onDismiss()
                    }
                }
            ) {
                Text("Create Category")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    );
}