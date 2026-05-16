package com.example.personalfinance.data
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Account::class, FlowCategory::class, StatusCategory::class, Transactions::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Convertors::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun accountsDao(): AccountsDao
    abstract fun transactionDao(): TransactionDao
    abstract fun CategoryDao(): CategoryDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null;

        fun getDatabaseO(context : Context, scope: CoroutineScope): AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "PersonalFinanceDB"
                )
                .addCallback(AppDatabaseCallback(scope))
                .build()
                INSTANCE = instance;
                instance
            }
        }

        private class AppDatabaseCallback( private val scope: CoroutineScope): RoomDatabase.Callback(){
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db);
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO){
                        populateInitialData(database)
                    }
                }
            }

            suspend fun populateInitialData(database: AppDatabase){
                val accountsDao = database.accountsDao();
                accountsDao.insertAccount(Account(name = "Primary Bank", balance = 0.0, default = true));
            }
        }
    }
}