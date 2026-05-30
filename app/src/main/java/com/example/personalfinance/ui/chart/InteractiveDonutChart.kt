package com.example.personalfinance.ui.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun InteractiveDonutChart (
    slices: List<ChartSlice>,
    currentState: ChartDrillDownState,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalAmount = slices.sumOf { it->it.amount };
    Box (
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(16.dp)
    ) {
        if (totalAmount <= 0.0) {
            Text("No data for this period", color = MaterialTheme.colorScheme.onSurfaceVariant)
            return@Box
        }
        Canvas (Modifier.size(200.dp)) {
            var currentStartAngle = -90f;
            val strokeWidth = 40.dp.toPx();
            slices.forEach { slice ->
                val sweepingAngle = ((slice.amount / totalAmount) * 360f).toFloat();
                drawArc(
                    startAngle = currentStartAngle,
                    sweepAngle = sweepingAngle,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                    color = slice.color
                );
                currentStartAngle += sweepingAngle
            }
        };
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable(
                enabled = currentState is ChartDrillDownState.Micro,
                onClick = onBackClicked
            )
        ) {
            if (currentState is ChartDrillDownState.Micro) {
                Text(
                    text = "⬅ Back",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = currentState.categoryName,
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            };
            Text(
                text = "₹${"%,.2f".format(totalAmount)}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        };
    }
}