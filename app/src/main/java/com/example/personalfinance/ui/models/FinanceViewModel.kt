package com.example.personalfinance.ui.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.personalfinance.data.metadata.Account
import com.example.personalfinance.data.database.AppDatabase
import com.example.personalfinance.data.repository.LedgerRepository
import com.example.personalfinance.data.metadata.Transactions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FinanceViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application, viewModelScope);
    private val accountsDao = database.accountsDao();
    private val transactionDao = database.transactionDao();
    private val ledgerRepository = LedgerRepository(database,accountsDao, transactionDao);

    val allAccounts: Flow<List<Account>> = accountsDao.getAllAccounts();
    val allTransaction: Flow<List<Transactions>> = transactionDao.getAllTransactions();

    fun addTransaction(formState: TransactionFormState) {
        val parsedAmount = formState.amount.toDoubleOrNull() ?: 0.0
        if (parsedAmount <= 0.0) return

        viewModelScope.launch(Dispatchers.IO) {
            val transactionRecord = Transactions(
                amount = parsedAmount,
                timestamp = System.currentTimeMillis(),
                note = formState.note.ifBlank { null },
                sourceId = formState.sourceId,
                sourceType = formState.sourceType,
                destinationId = formState.destinationId,
                destinationType = formState.destinationType
            )
            ledgerRepository.executeDoubleEntry(transactionRecord)
        }
    }

    fun createNewAccount(formState: AccountFormState) {
        val name = formState.accountName.trim()
        val parsedBalance = formState.initialBalance.toDoubleOrNull() ?: 0.0
        if (name.isEmpty()) return

        viewModelScope.launch(Dispatchers.IO) {
            val newAccount = Account(
                name = name,
                balance = parsedBalance,
                default = false,
                statusCategoryId = 0L
            )
            accountsDao.insertAccount(newAccount)
        }
    }

    fun setAccountInactive(accountId: Long, onResult: (DeactivationResult)->Unit){
        viewModelScope.launch(Dispatchers.IO){
            val account = accountsDao.getAccountById(accountId);
            if (account == null) {
                withContext(Dispatchers.Main){onResult(DeactivationResult.NOT_FOUND)}
                return@launch;
            }
            if(account.balance!=0.0) {
                withContext(Dispatchers.Main) { onResult(DeactivationResult.ERR_NON_ZERO_BALANCE) }
                return@launch;
            }
            if(account.default) {
                withContext(Dispatchers.Main) { onResult(DeactivationResult.ERR_IS_DEFAULT) }
                return@launch;
            }
            val transactionHistoryCount = transactionDao.getTransactionCountForAccount(accountId);
            if(transactionHistoryCount>0) {
                withContext(Dispatchers.Main) { onResult(DeactivationResult.ERR_HAS_HISTORY) };
                return@launch;
            }
            val rowsUpdated = accountsDao.updateToInactiveAccount(accountId);

            withContext(Dispatchers.Main) {
                if (rowsUpdated > 0) {
                    onResult(DeactivationResult.SUCCESS)
                } else {
                    onResult(DeactivationResult.FAILURE)
                }
            }
        }
    }

    fun updateAccountDetails(updatedAccount: Account){
        viewModelScope.launch(Dispatchers.IO) {
            if (updatedAccount.default)
                accountsDao.clearOtherDefaults(updatedAccount.id);
            accountsDao.updateAccount(updatedAccount);
        }
    }

}
enum class DeactivationResult{
    NOT_FOUND,
    FAILURE,
    SUCCESS,
    ERR_IS_DEFAULT,
    ERR_NON_ZERO_BALANCE,
    ERR_HAS_HISTORY
}