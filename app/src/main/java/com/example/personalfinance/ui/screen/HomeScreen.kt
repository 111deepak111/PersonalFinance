package com.example.personalfinance.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.example.personalfinance.data.metadata.Account
import com.example.personalfinance.data.metadata.ChartType
import com.example.personalfinance.ui.chart.LedgerPieChart
import com.example.personalfinance.ui.dialog.ModifyAccountDialog
import com.example.personalfinance.ui.models.DeactivationResult
import com.example.personalfinance.ui.models.FinanceViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(financeViewModel: FinanceViewModel) {
    val dbAccounts by financeViewModel.allAccounts.collectAsState(initial = emptyList());
    val snackBarHostState = remember { SnackbarHostState() };
    val scope = rememberCoroutineScope();
    var accountToModify by remember { mutableStateOf<Account?>(null) }
    var selectedChartTab by remember { mutableStateOf(ChartType.ACCOUNT_BALANCES) }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState) { data ->
                val isSuccess = data.visuals.actionLabel == "SUCCESS"
                Snackbar(
                    containerColor = if (isSuccess) Color(0xFF2E7D32) else Color(0xFFC62828), // Dark Green vs Dark Red
                    contentColor = Color.White,
                    snackbarData = data
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Analytics Viewport",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
            )

            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                ChartType.values().forEachIndexed { index, type ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = ChartType.values().size
                        ),
                        onClick = { selectedChartTab = type },
                        selected = selectedChartTab == type
                    ) {
                        val icon = when(type){
                            ChartType.ASSETS_VS_LIABILITIES -> Icons.Filled.Balance
                            ChartType.INCOME_VS_EXPENSES -> Icons.AutoMirrored.Filled.TrendingUp
                            ChartType.ACCOUNT_BALANCES -> Icons.Filled.AccountBalanceWallet
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = type.label,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                        alpha = 0.3f
                    )
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = selectedChartTab.label + " Analytical Matrix",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                when(selectedChartTab){
                    ChartType.ACCOUNT_BALANCES -> {
                        LedgerPieChart(accounts = dbAccounts)
                    }
                    ChartType.INCOME_VS_EXPENSES -> {
                        LedgerPieChart(accounts = dbAccounts)
                    }
                    ChartType.ASSETS_VS_LIABILITIES -> {
                        LedgerPieChart(accounts = dbAccounts)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {


                item {
                    Text(
                        text = "G\\L Accounts",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(dbAccounts) { account ->
                    if (account.isActive)
                        ExpandingAccountCard(
                            account = account, financeViewModel, snackBarHostState, scope,
                            onModifyClick = { accountToModify = account },
                            onDeactivateResult = { resultMessage, successState ->
                                scope.launch {
                                    snackBarHostState.showSnackbar(
                                        message = resultMessage,
                                        actionLabel = if (successState) "SUCCESS" else "ERROR"
                                    )
                                }
                            });
                }
            }
        }
    }
    accountToModify?.let { targetAccount ->
        ModifyAccountDialog(
            account = targetAccount,
            onDismiss = { accountToModify = null }, // Clear state to close modal window cleanly
            onSave = { updatedAccount ->
                financeViewModel.updateAccountDetails(updatedAccount)
                scope.launch {
                    snackBarHostState.showSnackbar(
                        message = "Account alterations successfully written to master records.",
                        actionLabel = "SUCCESS"
                    )
                }
            }
        )
    }
}

@Composable
fun OutlinedChartCard(title: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "$title\n Outline Chart", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpandingAccountCard(
    account: Account,
    financeViewModel: FinanceViewModel,
    snackBarHostState: SnackbarHostState,
    scope: CoroutineScope,
    onModifyClick: () -> Unit,
    onDeactivateResult: (String, Boolean) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) };
    var isMenuExpanded by remember { mutableStateOf(false) };
    var accountToModify by remember { mutableStateOf<Account?>(null) }
    Box {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = { isExpanded = !isExpanded },
                    onLongClick = { isMenuExpanded = true }
                ),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = account.name, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "₹${String.format("%.2f", account.balance)}",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (account.balance >= 0) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.error
                    )
                }
                AnimatedVisibility(visible = isExpanded) {
                    Column(
                        modifier = Modifier.padding(top = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Internal Account Code Reference ID: #00${account.id}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = if (account.default) "Default Account: Yes" else "Default Account: No",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (account.default) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            DropdownMenu(expanded = isMenuExpanded, onDismissRequest = { isMenuExpanded = false }) {
                DropdownMenuItem(
                    text = { Text("Modify Account") },
                    onClick = {
                        isMenuExpanded = false;
                        onModifyClick();
                    }
                );
                DropdownMenuItem(
                    text = { Text("Delete Account") },
                    onClick = {
                        isMenuExpanded = false;
                        financeViewModel.setAccountInactive(accountId = account.id) { result ->
                            scope.launch {
                                when (result) {
                                    DeactivationResult.FAILURE -> {
                                        snackBarHostState.showSnackbar(
                                            message = "System Failure: Internal ledger transaction commit execution error.",
                                            actionLabel = "ERROR"
                                        )
                                    }

                                    DeactivationResult.SUCCESS -> {
                                        snackBarHostState.showSnackbar(
                                            message = "Ledger Account '${account.name}' successfully archived and deactivated.",
                                            actionLabel = "SUCCESS" // Mapped to our green color rule above
                                        )
                                    }

                                    DeactivationResult.NOT_FOUND -> {
                                        snackBarHostState.showSnackbar(
                                            message = "Database Error: The target ledger account could not be found.",
                                            actionLabel = "ERROR"
                                        )
                                    }

                                    DeactivationResult.ERR_NON_ZERO_BALANCE -> {
                                        snackBarHostState.showSnackbar(
                                            message = "Deactivation Rejected: Account balance must be exactly ₹0.00 (Current: ₹${account.balance}).",
                                            actionLabel = "ERROR" // Mapped to our red color rule above
                                        )
                                    }

                                    DeactivationResult.ERR_HAS_HISTORY -> {
                                        snackBarHostState.showSnackbar(
                                            message = "Security Block: This account contains historical transaction logs and cannot be deactivated.",
                                            actionLabel = "ERROR"
                                        )
                                    }

                                    DeactivationResult.ERR_IS_DEFAULT -> {
                                        snackBarHostState.showSnackbar(
                                            message = "System Block: Reassign your default settlement account settings before disabling this account branch.",
                                            actionLabel = "ERROR"
                                        )
                                    }
                                }
                            }
                        }
                    }
                );
            }
        }
    }
}