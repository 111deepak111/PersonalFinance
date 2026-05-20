package com.example.personalfinance.ui.dialog

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.personalfinance.data.metadata.TransactionPartyType
import com.example.personalfinance.ui.models.FinanceViewModel
import com.example.personalfinance.ui.models.TransactionFormState

enum class LedgerTxType {EXPENSE, INCOME, TRANSFER};
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionDialog(
    onDismiss: () -> Unit,
    viewModel: FinanceViewModel
) {
    val accounts by viewModel.allAccounts.collectAsState(initial = emptyList());
    var txType by remember { mutableStateOf(LedgerTxType.EXPENSE) };
    var amount by remember { mutableStateOf("") };
    var note by remember {mutableStateOf("")};

    var sourceAccountId by remember { mutableLongStateOf(0L) };
    var destinationAccountId by remember { mutableLongStateOf(0L) };

    var sourceDropDownExpanded by remember {mutableStateOf(false)};
    var destDropDownExpanded by remember {mutableStateOf(false)};
    LaunchedEffect(accounts) {
        if (accounts.isNotEmpty()){
            if(sourceAccountId == 0L) sourceAccountId = accounts.first().id;
            if (destinationAccountId == 0L && accounts.size > 1) {
                destinationAccountId = accounts[1].id
            } else if (destinationAccountId == 0L) {
                destinationAccountId = accounts.first().id
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {Text(text="Record Entry", style = MaterialTheme.typography.titleMedium)},
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)){

                Box(
                    modifier=Modifier.fillMaxWidth().height(48.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement= Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LedgerTxType.values().forEach { type ->
                            val isSelected = txType == type;
                            val displayText = when(type){
                                LedgerTxType.INCOME -> if (isSelected) "Income" else "I";
                                LedgerTxType.EXPENSE -> if (isSelected) "Expense" else "E";
                                LedgerTxType.TRANSFER -> if (isSelected)"Transfer" else "T";
                            }

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.weight(1f).fillMaxHeight()
                                    .animateContentSize(animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioLowBouncy,
                                        stiffness = Spring.StiffnessMedium))
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                                    .clickable{txType=type}
                            ){
                                Text(
                                    text = displayText,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it},
                    label = { Text ("Amount (₹)")},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it},
                    label = { Text ("Remarks")},
                    modifier = Modifier.fillMaxWidth()
                )

                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val sourceName: String = accounts.find {it.id == sourceAccountId}?.name ?: "Select Account";
                    OutlinedButton(
                        onClick = {sourceDropDownExpanded = true},
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text="Source Account: $sourceName")
                    };
                    DropdownMenu(
                        expanded = sourceDropDownExpanded,
                        onDismissRequest = { sourceDropDownExpanded = false},
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        accounts.forEach { account ->
                            DropdownMenuItem(
                                text = {Text(account.name)},
                                onClick = {
                                    sourceAccountId = account.id
                                    sourceDropDownExpanded = false
                                }
                            )
                        }
                    };
                }
                Box(Modifier.fillMaxWidth()){
                    val destName = accounts.find { it.id==destinationAccountId }?.name ?:"Select Destination";
                    val destLabel = when (txType) {
                        LedgerTxType.EXPENSE -> "Destination: [Standard Operating Expense]"
                        LedgerTxType.INCOME -> "Destination (To Asset): $destName"
                        LedgerTxType.TRANSFER -> "Destination (To Asset): $destName"
                    }

                    OutlinedButton(
                        onClick = {
                            if(txType!= LedgerTxType.EXPENSE) destDropDownExpanded = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = txType!= LedgerTxType.EXPENSE
                    ) {
                        Text(text=destLabel)
                    };
                    DropdownMenu(expanded = destDropDownExpanded, onDismissRequest = {destDropDownExpanded=false}) {
                        accounts.forEach { account ->
                            DropdownMenuItem(
                                text= {Text(account.name)},
                                onClick = {
                                    destinationAccountId=account.id;
                                    destDropDownExpanded=false;
                                }
                            );
                        };
                    };
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val parsedAmount = amount.toDoubleOrNull();
                    if (parsedAmount!=null && sourceAccountId!=0L){
                        var finalFormState = when (txType) {
                            LedgerTxType.INCOME -> TransactionFormState(
                                amount = amount,
                                note = note,
                                sourceId = 888L,
                                sourceType = TransactionPartyType.ACCOUNT,
                                destinationId = destinationAccountId,
                                destinationType = TransactionPartyType.ACCOUNT
                            )
                            LedgerTxType.EXPENSE -> TransactionFormState(
                                amount = amount,
                                note = note,
                                sourceId = sourceAccountId,
                                sourceType = TransactionPartyType.ACCOUNT,
                                destinationId = 999L,
                                destinationType = TransactionPartyType.EXPENSES
                            )
                            LedgerTxType.TRANSFER-> TransactionFormState(
                                amount = amount,
                                note = note,
                                sourceId = sourceAccountId,
                                sourceType = TransactionPartyType.ACCOUNT,
                                destinationId = destinationAccountId,
                                destinationType = TransactionPartyType.ACCOUNT
                            )
                        };
                        viewModel.addTransaction(finalFormState);
                        onDismiss();
                    }
                }
            ) {
                Text("Post Ledger Entry");
            }
        },
        dismissButton = {
            TextButton( onClick = onDismiss) {
                Text("Cancel")
            }
        }
    );
}
