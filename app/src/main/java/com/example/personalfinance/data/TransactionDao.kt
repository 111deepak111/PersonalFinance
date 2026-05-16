package com.example.personalfinance.data

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

interface TransactionDao {
    @Query("Select * from Transactions")
    fun getAllTransactions(): Flow<List<Transactions>>;

    @Insert
    suspend fun insertRawTransaction(transaction: Transactions): Long;

    @Delete
    suspend fun deleteRawTransaction(transaction: Transactions);
}