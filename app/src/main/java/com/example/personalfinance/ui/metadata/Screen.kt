package com.example.personalfinance.ui.metadata

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.ui.graphics.vector.ImageVector

enum class Screen(val title: String, val icon: ImageVector) {
    ASSETS("Assets", Icons.Default.KeyboardArrowUp),
    LIABILITIES("Liabilities", Icons.Default.KeyboardArrowDown),
    INCOME("Income", Icons.Default.KeyboardArrowUp),
    EXPENSES("Expenses", Icons.Default.KeyboardArrowDown),
    HOME(title = "Home", Icons.Default.Home)
}