package com.example.personalfinance.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.personalfinance.data.metadata.Account
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun ModifyAccountDialog (
    account: Account,
    onDismiss: ()-> Unit,
    onSave: (Account) -> Unit
) {
    var editedName by remember { mutableStateOf(account.name) };
    var isDefaultAccount by remember {mutableStateOf(account.default)};
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {Text("Modify G\\L Account")},
        text = {
            Column (verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = editedName,
                    onValueChange = { editedName=it},
                    label = { Text("Account Alias / Name")},
                    modifier = Modifier.fillMaxWidth()
                );
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column (modifier = Modifier.weight(1f)){
                        Text(text="System Default Account", style = MaterialTheme.typography.bodyLarge)
                        Text(
                            text = "Automates primary settlement logging for fast matching.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = isDefaultAccount,
                        onCheckedChange = {isDefaultAccount = it}
                    )
                };
                Text(
                    text = "Note: Balance fields and ID references are locked to preserve audit integrity.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                );
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updatedPayload = account.copy(name = editedName, default = isDefaultAccount);
                    onSave(updatedPayload);
                    onDismiss();
                }
            ) {
                Text("Update Master Record")
            }
        },
        dismissButton = {
            TextButton( onClick = onDismiss) { Text("Cancel")}
        }
    );
}