package com.example.personalfinance.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.personalfinance.ui.dialog.AddAccountDialog
import com.example.personalfinance.ui.dialog.AddTransactionDialog
import com.example.personalfinance.ui.metadata.Screen
import com.example.personalfinance.ui.models.FinanceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val financeViewModel: FinanceViewModel = viewModel();
    var currentScreen by remember { mutableStateOf<Screen>(Screen.HOME) }
    var isFabExpanded by remember { mutableStateOf(false) }

    var showAddAccountsDialog by remember { mutableStateOf(false)};
    var showAddTransactionsDialog by remember { mutableStateOf(false)};

    if (showAddAccountsDialog){
        AddAccountDialog(
            onDismiss = {showAddAccountsDialog = false},
            viewModel = financeViewModel
        )
    }
    if (showAddTransactionsDialog){
        AddTransactionDialog(
            onDismiss = {showAddTransactionsDialog = false},
            viewModel = financeViewModel
        )
    }
    Scaffold (
        topBar = {
            TopAppBar(
                title = {Text(text = "Personal Finance", fontSize = 20.sp)},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                actions = {
                    IconButton(onClick = { currentScreen = Screen.HOME}) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home",
                            tint = if (currentScreen == Screen.HOME) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                Screen.values().forEach { screen ->
                     if (screen != Screen.HOME) NavigationBarItem (
                        selected = currentScreen == screen,
                        onClick = {currentScreen = screen},
                        label = {Text(screen.title)},
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.title
                            )
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            Column (
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(bottom = 16.dp, end = 8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AnimatedVisibility(visible = isFabExpanded) {
                        FloatingActionButton(
                            onClick = {
                                isFabExpanded = false;
                                showAddAccountsDialog = true;
                            },
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Icon(Icons.Default.AccountCircle, contentDescription = "Add Account")
                        }
                    }
                    AnimatedVisibility(visible = isFabExpanded) {
                        FloatingActionButton(
                            onClick = {
                                isFabExpanded = false;
                                showAddTransactionsDialog = true;
                            },
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add Transaction")
                        }
                    }
                    FloatingActionButton(
                        onClick = { isFabExpanded = !isFabExpanded }
                    ) {
                        Icon(Icons.Default.AddCircle, contentDescription = "Expand Options")
                    }
                }
            }
        }
    ){ innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentAlignment = Alignment.Center
        ){
            when (currentScreen){
                Screen.ASSETS -> PlaceholderView(title = "Assets Ledger")
                Screen.LIABILITIES -> PlaceholderView(title = "Liabilities Ledger")
                Screen.INCOME -> PlaceholderView(title = "Income Statement")
                Screen.EXPENSES -> PlaceholderView(title = "Expenses Ledger")
                Screen.HOME -> HomeScreen(financeViewModel)
            }
        }
    }
}

@Composable
fun PlaceholderView(title: String){
    Text(
        text = title,
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onBackground
    )
}