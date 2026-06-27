package com.example.personalfinance.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.personalfinance.data.metadata.ChartSliceData
import com.example.personalfinance.data.metadata.StatusCategory
import com.example.personalfinance.data.metadata.TransactionPartyType
import kotlinx.coroutines.flow.Flow
@Dao
interface CategoryDao {
    @Query("""
        Select SUM(amount) from transactions
        where destinationType = :categoryType and destinationId = :categoryId
    """)
    fun getIncomingSumForSpecificCategory(categoryType: TransactionPartyType, categoryId: Long): Double

    @Query("""
        Select SUM(amount) from transactions
        where sourceType = :categoryType and sourceId = :categoryId
    """)
    fun getOutgoingSumForSpecificCategory(categoryType: TransactionPartyType, categoryId: Long): Double

    @Query("""
        SELECT COALESCE(SUM(amount), 0.0) FROM Transactions 
        WHERE destinationType = :categoryType 
        AND timestamp BETWEEN :startDate AND :endDate
    """)
    fun getTotalForTypeOverTime(categoryType: TransactionPartyType, startDate: Long, endDate: Long): Flow<Double>

    @Query("""
        SELECT destinationId as categoryId, SUM(amount) as totalAmount 
        FROM Transactions 
        WHERE destinationType = :categoryType 
        AND timestamp BETWEEN :startDate AND :endDate 
        GROUP BY destinationId
        HAVING totalAmount > 0
    """)
    fun getCategoryBreakdownOverTime(categoryType: TransactionPartyType, startDate: Long, endDate: Long): Flow<List<ChartSliceData>>

    @Insert
    suspend fun insertStatusCategory(category: StatusCategory):Long;

    @Query("SELECT * FROM StatusCategory")
    fun getAllStatusCategories(): Flow<List<StatusCategory>>
}
