package com.example.personalfinance.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountsDao {

    @Query("Select * from Account")
    fun getAllAccounts(): Flow<List<Account>>;

    @Query("Select * from account where id = :accountId Limit 1")
    suspend fun getAccountById(accountId :Long): Account?;

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAccount (account : Account): Long;

    @Update
    suspend fun updateAccount (account: Account)

    @Query("Update account set balance = balance + :amount where id = :accountId")
    suspend fun updateAccountBalance(amount :Double, accountId: Long)
}