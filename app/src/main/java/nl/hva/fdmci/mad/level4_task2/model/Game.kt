package nl.hva.fdmci.mad.level4_task2.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "game_table")
data class Game (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null,

    @ColumnInfo(name = "computer_weapon")
    var computerWeapon: String,

    @ColumnInfo(name = "player_weapon")
    var playerWeapon: String,

    @ColumnInfo(name = "game_time")
    var date: Date,

    @ColumnInfo(name = "winner")
    var winner: String
): Parcelable