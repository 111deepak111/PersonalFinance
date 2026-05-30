package com.example.personalfinance.ui.chart

import androidx.compose.ui.graphics.Color

data class ChartSlice(
    val label: String,
    val amount: Double,
    val color: Color,
    val id: Long
)

sealed class ChartDrillDownState {
    object Macro: ChartDrillDownState();

    data class Micro(val tappedCategoryId: Long, val categoryName: String): ChartDrillDownState();
}