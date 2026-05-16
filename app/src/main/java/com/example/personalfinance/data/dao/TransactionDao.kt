package com.example.personalfinance.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.personalfinance.data.metadata.Transactions
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("Select * from Transactions")
    fun getAllTransactions(): Flow<List<Transactions>>

    @Insert
    suspend fun insertRawTransaction(transaction: Transactions): Long

    @Delete
    suspend fun deleteRawTransaction(transaction: Transactions)
}