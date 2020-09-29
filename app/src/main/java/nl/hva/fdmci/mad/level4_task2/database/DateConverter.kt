package nl.hva.fdmci.mad.level4_task2.database

import androidx.room.TypeConverter
import java.util.*


class DateConverter {

    companion object {
        @TypeConverter
        @JvmStatic
        fun fromTimestamp(value: Long?): Date? = if (null == value) null else Date(value)

        @TypeConverter
        @JvmStatic
        fun dateToTimestamp(date: Date?): Long? = date?.time
    }

}