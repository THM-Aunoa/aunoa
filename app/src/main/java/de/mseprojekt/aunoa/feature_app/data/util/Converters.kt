package de.mseprojekt.aunoa.feature_app.data.util

import androidx.room.TypeConverter
import de.mseprojekt.aunoa.feature_app.domain.model.StatusType


class Converters {

    @TypeConverter
    fun fromStatusType(status : StatusType): Int{
        return when(status) {
            is StatusType.Success ->{
                1;
            }
            else -> 2;
            }
        }
    @TypeConverter
    fun toStatusType(number: Int): StatusType{
        return if (number==1)
            StatusType.Success
        else
            StatusType.Failure

    }
}