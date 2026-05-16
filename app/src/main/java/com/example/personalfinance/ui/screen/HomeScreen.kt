package com.example.personalfinance.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun HomeScreen() {
    var showCashFlowChart by remember{ mutableStateOf(true) }
    var showBalanceSheetChart by remember{ mutableStateOf(true) }
    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item{
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton( onClick = { showCashFlowChart = !showCashFlowChart} ) {
                    Text(if(showCashFlowChart) "Hide Cashflow" else "Show Cashflow")
                }

                TextButton( onClick = { showBalanceSheetChart = !showBalanceSheetChart} ) {
                    Text(if(showBalanceSheetChart) "Hide Balance sheet" else "Show Balance sheet")
                }
            }
        }

        item {
            AnimatedVisibility(visible = showCashFlowChart) {
                OutlinedChartCard(title = "Income vs Expenses")
            }
        }

        item {
            AnimatedVisibility(visible = showBalanceSheetChart) {
                OutlinedChartCard(title = "Assets vs Liability")
            }
        }

        item {
            Text(
                text = "G\\L Accounts",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        items (2) { index ->
            ExpandingAccountCard(accountName = if (index==0)"HDFC Bank" else "Groww Wallet")
        }
    }
}

@Composable
fun OutlinedChartCard(title: String){
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            Text(text="$title\n Outline Chart", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable

fun ExpandingAccountCard(accountName: String){
    var isExpanded by remember { mutableStateOf(false) }
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column (modifier= Modifier.padding(16.dp)) {
            Text(text = accountName, style = MaterialTheme.typography.titleMedium)
            AnimatedVisibility(visible = isExpanded) {
                Column (
                    modifier= Modifier.padding(top=16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Current Balance: ₹0.00",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Default: Yes",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(onClick = { }) {
                        Text("Modify Parameters")
                    }
                }
            }
        }
    }
}