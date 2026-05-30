package com.example.personalfinance.ui.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.personalfinance.data.metadata.Account


val ChartColors = listOf(
    Color(0xFF0D47A1), // Deep Blue
    Color(0xFF1B5E20), // Emerald Green
    Color(0xFFE65100), // Safety Orange
    Color(0xFF4A148C), // Corporate Purple
    Color(0xFF006064), // Deep Teal
    Color(0xFFF57F17)  // Amber Gold
);

@Composable
fun LedgerPieChart(accounts:List<Account>, modifier: Modifier = Modifier) {
    val activeAssetsAccounts = accounts.filter { account -> account.isActive && account.balance > 0.0 };
    val totalBalance = activeAssetsAccounts.sumOf { it-> it.balance };
    if (activeAssetsAccounts.isEmpty() || totalBalance == 0.0){
        Box( modifier = modifier, contentAlignment = Alignment.Center){
            Text("No positive asset balances available to aggregate", style = MaterialTheme.typography.bodyMedium);
        }
        return;
    }
    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Canvas ( modifier = Modifier.size(200.dp)) {
            var currentStartAngle = 0f;
            activeAssetsAccounts.forEachIndexed { index, account ->
                val accountWeight = (account.balance/totalBalance).toFloat();
                val sweepAngle = accountWeight * 360f;
                val color = ChartColors[index%ChartColors.size];
                drawArc(
                    color = color,
                    startAngle = currentStartAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    size = Size(size.width, size.height)
                );
                currentStartAngle += sweepAngle;
            }
        }
        LazyRow (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
        ) {
            items(activeAssetsAccounts.mapIndexed { index,account -> Pair(account, ChartColors[index%ChartColors.size])})
            {
                (account, color)->
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Box(modifier = Modifier.size(12.dp).background(color, CircleShape));
                    Text(
                        text = "${account.name} (₹${String.format("%.0f", account.balance)})",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}