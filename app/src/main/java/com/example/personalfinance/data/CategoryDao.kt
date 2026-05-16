package com.example.personalfinance.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface CategoryDao {
    @Query("""
        Select SUM(amount) from transactions
        where destinationType = :categoryType and destinationId = :categoryId
    """)
    fun getIncomingSumForSpecificCategory(categoryType: TransactionPartyType, categoryId: Long): Double;

    @Query("""
        Select SUM(amount) from transactions
        where sourceType = :categoryType and sourceId = :categoryId
    """)
    fun getOutgoingSumForSpecificCategory(categoryType: TransactionPartyType, categoryId: Long): Double;

}