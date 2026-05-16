package com.example.personalfinance.data.convertors

import androidx.room.TypeConverter
import com.example.personalfinance.data.metadata.TransactionPartyType

class Convertors {
    @TypeConverter
    fun fromPartyType(value: TransactionPartyType): String{
        return value.name
    }

    @TypeConverter
    fun toPartyType(value: String): TransactionPartyType {
        return TransactionPartyType.valueOf(value)
    }
}