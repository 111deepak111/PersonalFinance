package com.example.personalfinance.ui.models

import com.example.personalfinance.data.metadata.StatusCategory
import com.example.personalfinance.data.metadata.TransactionPartyType

data class TransactionFormState (
    val amount: String = "",
    val note: String = "",
    val sourceId: Long = 0L,
    val sourceType: TransactionPartyType = TransactionPartyType.ACCOUNT,
    val destinationId: Long = 0L,
    val destinationType: TransactionPartyType = TransactionPartyType.EXPENSES
)

data class AccountFormState (
    val accountName: String = "",
    val initialBalance: String = "",
    val statusCategoryId: Long = 999L
)
