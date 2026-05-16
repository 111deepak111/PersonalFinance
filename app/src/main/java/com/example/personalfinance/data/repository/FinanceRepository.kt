package com.example.personalfinance.data.repository
import com.example.personalfinance.data.database.AppDatabase
import com.example.personalfinance.data.metadata.TransactionPartyType
import com.example.personalfinance.data.metadata.Transactions
import com.example.personalfinance.data.dao.AccountsDao
import com.example.personalfinance.data.dao.TransactionDao

class LedgerRepository (
    private val database: AppDatabase,
    private val accountsDao: AccountsDao,
    private val transactionDao: TransactionDao
) {
    suspend fun executeDoubleEntry(transaction: Transactions){
        transactionDao.insertRawTransaction(transaction)
        if (transaction.sourceType == TransactionPartyType.ACCOUNT){
            accountsDao.updateAccountBalance(-transaction.amount, transaction.sourceId)
        }
        if (transaction.destinationType == TransactionPartyType.ACCOUNT){
            accountsDao.updateAccountBalance(transaction.amount, transaction.destinationId)
        }
    }
}