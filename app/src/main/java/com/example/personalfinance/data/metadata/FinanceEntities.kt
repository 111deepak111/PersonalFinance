package com.example.personalfinance.data.metadata

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Account")
data class Account (
    @PrimaryKey(autoGenerate = true)val id: Long = 0,
    val name: String,
    val balance: Double,
    val default: Boolean = false
)

@Entity(tableName = "FlowCategory")
data class FlowCategory (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: TransactionPartyType
)

@Entity(tableName = "StatusCategory")
data class StatusCategory (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: TransactionPartyType
)

@Entity(tableName = "Transactions")
data class Transactions (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val timestamp: Long,
    val note: String?,
    val sourceId: Long,
    val sourceType: TransactionPartyType,
    val destinationId: Long,
    val destinationType: TransactionPartyType
)