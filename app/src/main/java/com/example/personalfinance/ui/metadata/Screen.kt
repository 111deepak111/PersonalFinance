package com.example.personalfinance.ui.metadata

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.ui.graphics.vector.ImageVector

enum class Screen(val title: String, val icon: ImageVector) {
    ASSETS("Assets", Icons.Filled.AccountBalance),
    LIABILITIES("Liabilities", Icons.Filled.CreditCard),
    INCOME("Income", Icons.Filled.CurrencyRupee),
    EXPENSES("Expenses", Icons.Filled.TrendingDown),
    HOME(title = "Home", Icons.Default.Home)
}