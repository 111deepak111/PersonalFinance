package com.example.personalfinance.data

import androidx.room.TypeConverter

class Convertors {
    @TypeConverter
    fun fromPartyType(value: TransactionPartyType): String{
        return value.name;
    }

    @TypeConverter
    fun toPartyType(value: String): TransactionPartyType{
        return TransactionPartyType.valueOf(value);
    }
}