package nl.hva.fdmci.mad.level4_task2.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import nl.hva.fdmci.mad.level4_task2.model.Game

@Dao
interface GameDao {

    @Query("SELECT * FROM game_table")
    suspend fun getAllGames(): List<Game>

    @Insert
    suspend fun insertGame(game: Game)

    @Delete
    suspend fun deleteGame(game: Game)

    @Query("DELETE FROM game_table")
    suspend fun deleteAllGames()

    @Query("SELECT COUNT(id) AS win_count FROM game_table WHERE winner = \"You\"")
    suspend fun getWinCount() : Int

    @Query("SELECT COUNT(id) AS draw_count FROM game_table WHERE winner = \"Draw\"")
    suspend fun getDrawCount() : Int

    @Query("SELECT COUNT(id) AS draw_count FROM game_table WHERE winner = \"Computer\"")
    suspend fun getLoseCount() : Int
}