package com.example.personalfinance.data

import androidx.room.Dao
import androidx.room.Transaction


@Dao
abstract class LedgerRepository (
    private val accountsDao: AccountsDao,
    private val transactionDao: TransactionDao
) {
    @Transaction
    open suspend fun executeDoubleEntry (transaction : Transactions){
        transactionDao.insertRawTransaction(transaction);
        if (transaction.sourceType == TransactionPartyType.ACCOUNT){
            accountsDao.updateAccountBalance(-transaction.amount, transaction.sourceId);
        }
        if (transaction.destinationType == TransactionPartyType.ACCOUNT){
            accountsDao.updateAccountBalance(transaction.amount, transaction.destinationId);
        }
    }
}