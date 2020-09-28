package nl.hva.fdmci.mad.level4_task2.repository

import android.content.Context
import nl.hva.fdmci.mad.level4_task2.dao.GameDao
import nl.hva.fdmci.mad.level4_task2.database.GameRoomDatabase
import nl.hva.fdmci.mad.level4_task2.model.Game

class GameRepository(context: Context) {

    private val gameDao: GameDao

    init {
        val database = GameRoomDatabase.getDatabase(context)
        gameDao = database!!.gameDao()
    }

    suspend fun getAllGames(): List<Game> {
        return gameDao.getAllGames()
    }

    suspend fun insertGame(game: Game) {
        gameDao.deleteGame(game)
    }

    suspend fun deleteGame(game: Game) {
        gameDao.deleteGame(game)
    }

    suspend fun deleteAllGames() {
        gameDao.deleteAllGames()
    }

}
