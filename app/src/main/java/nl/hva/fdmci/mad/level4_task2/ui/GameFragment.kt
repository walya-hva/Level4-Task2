package nl.hva.fdmci.mad.level4_task2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.hva.fdmci.mad.level4_task2.R
import nl.hva.fdmci.mad.level4_task2.model.Game
import nl.hva.fdmci.mad.level4_task2.model.Weapon
import nl.hva.fdmci.mad.level4_task2.repository.GameRepository
import java.sql.Date
import java.time.Instant

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class GameFragment : Fragment() {

    private lateinit var gameRepository: GameRepository
    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameRepository = GameRepository(requireContext())

        (activity as AppCompatActivity).toolbar.setNavigationIcon(null)
        (activity as AppCompatActivity).toolbar.setNavigationOnClickListener {}

        btn_rock.setOnClickListener{ fight(Weapon.ROCK) }
        btn_paper.setOnClickListener { fight(Weapon.PAPER) }
        btn_scissors.setOnClickListener { fight(Weapon.SCISSORS) }


        setStatistics()

    }

    private fun setStatistics() {
        var winCount = 0;
        var drawCount = 0;
        var lossCount = 0;

        mainScope.launch {
            withContext(Dispatchers.IO) {
                val gamesList = gameRepository.getAllGames()
                gamesList.forEach { game: Game ->
                    when(game.winner){
                        "Computer" -> {lossCount++}
                        "You" -> {winCount++}
                        "Draw" -> {drawCount++}
                    }
                }
            }
        }

        tv_win.setText(R.string.tv_win)
        tv_win.append(" $winCount")

        tv_draw.setText(R.string.tv_draw)
        tv_draw.append(" $drawCount")

        tv_lose.setText(R.string.tv_lose)
        tv_lose.append(" $lossCount")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_history -> {
                findNavController().navigate(R.id.action_GameFragment_to_HistoryFragment)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun fight(playerWeapon: Weapon) {
        img_player.setImageResource(playerWeapon.resourceId)

        val computerWeapon = Weapon.values().random()
        img_computer.setImageResource(computerWeapon.resourceId)


        var winner = ""

        when(playerWeapon){
            Weapon.ROCK -> {
                winner = when (computerWeapon) {
                    Weapon.PAPER -> { "Computer" }
                    Weapon.SCISSORS -> { "You" }
                    Weapon.ROCK -> { "Draw" }
                }
            }
            Weapon.PAPER -> {
                winner = when (computerWeapon) {
                    Weapon.PAPER -> { "Draw" }
                    Weapon.SCISSORS -> { "Computer" }
                    Weapon.ROCK -> { "You" }
                }
            }
            Weapon.SCISSORS -> {
                winner = when (computerWeapon) {
                    Weapon.PAPER -> { "You" }
                    Weapon.SCISSORS -> { "Draw" }
                    Weapon.ROCK -> { "Computer" }
                }
            }
        }

        val game = Game(computerWeapon = computerWeapon.toString(), playerWeapon = playerWeapon.toString(),
            date = Date.from(Instant.now()), winner = winner
        )

        mainScope.launch {
            withContext(Dispatchers.IO) {
                gameRepository.insertGame(game)
            }
        }
        setStatistics()
    }
}