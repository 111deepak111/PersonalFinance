package com.example.personalfinance.data.metadata

enum class ChartType (val label: String) {
    ASSETS_VS_LIABILITIES("Assets vs Liabilities"),
    INCOME_VS_EXPENSES("Income vs Expenses"),
    ACCOUNT_BALANCES("Account Balances")
};

data class ChartSliceData(
    val categoryId: Long,
    val totalAmount: Double
)