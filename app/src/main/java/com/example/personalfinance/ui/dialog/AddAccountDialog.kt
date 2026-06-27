package com.example.personalfinance.ui.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.personalfinance.data.metadata.StatusCategory
import com.example.personalfinance.ui.models.AccountFormState
import com.example.personalfinance.ui.models.FinanceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAccountDialog (
    onDismiss: () -> Unit,
    viewModel: FinanceViewModel,
    availableCategories: List<StatusCategory>
) {
    var accountName by remember { mutableStateOf("") };
    var initialBalance by remember { mutableStateOf("") };
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<StatusCategory?>(null) }
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

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedCategory?.name ?: "Select Classification",
                        onValueChange = {},
                        readOnly = true, // Prevents typing in the box
                        label = { Text("Asset / Liability Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    );
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        if (availableCategories.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text("No categories found. Create one first!") },
                                onClick = { expanded = false }
                            )
                        } else {
                            availableCategories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category.name) },
                                    onClick = {
                                        selectedCategory = category
                                        expanded = false
                                    }
                                );
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if(accountName.isNotBlank() && selectedCategory!=null){
                        viewModel.createNewAccount(AccountFormState(
                                accountName = accountName,
                                initialBalance = initialBalance,
                                statusCategoryId = selectedCategory!!.id
                            )
                        );
                        onDismiss();
                    }
                },
                enabled = accountName.isNotBlank() && selectedCategory != null
            ) {
                Text("Provision Account");
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel");
            }
        }
    )
}