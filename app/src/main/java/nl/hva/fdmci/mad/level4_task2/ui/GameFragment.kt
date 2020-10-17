package nl.hva.fdmci.mad.level4_task2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
    private var winCount = 0
    private var drawCount = 0
    private var loseCount = 0

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

        mainScope.launch {
            withContext(Dispatchers.IO) {
                gameRepository = GameRepository(requireContext())
                setStatistics()
            }
        }

        (activity as AppCompatActivity).toolbar.navigationIcon = null

        btn_rock.setOnClickListener { fight(Weapon.ROCK) }
        btn_paper.setOnClickListener { fight(Weapon.PAPER) }
        btn_scissors.setOnClickListener { fight(Weapon.SCISSORS) }
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

        when (playerWeapon) {
            Weapon.ROCK -> {
                winner = when (computerWeapon) {
                    Weapon.PAPER -> {
                        "Computer"
                    }
                    Weapon.SCISSORS -> {
                        "You"
                    }
                    Weapon.ROCK -> {
                        "Draw"
                    }
                }
            }
            Weapon.PAPER -> {
                winner = when (computerWeapon) {
                    Weapon.PAPER -> {
                        "Draw"
                    }
                    Weapon.SCISSORS -> {
                        "Computer"
                    }
                    Weapon.ROCK -> {
                        "You"
                    }
                }
            }
            Weapon.SCISSORS -> {
                winner = when (computerWeapon) {
                    Weapon.PAPER -> {
                        "You"
                    }
                    Weapon.SCISSORS -> {
                        "Draw"
                    }
                    Weapon.ROCK -> {
                        "Computer"
                    }
                }
            }
        }

        val game = Game(
            computerWeapon = computerWeapon.toString(), playerWeapon = playerWeapon.toString(),
            date = Date.from(Instant.now()), winner = winner
        )

        mainScope.launch {
            withContext(Dispatchers.IO) {
                gameRepository.insertGame(game)
                setStatistics()
            }
        }

        winnerDisplay(winner)

    }

    private fun winnerDisplay(winner: String) {
        when(winner){
            "You" -> {
                tv_result.setText(R.string.msg_player_win)
            }
            "Computer" -> {
                tv_result.setText(R.string.msg_computer_win)
            }
            "Draw" -> {
                tv_result.setText(R.string.msg_draw_win)
            }
        }
    }

    private suspend fun setStatistics() {

        winCount = gameRepository.getWinCount()
        drawCount = gameRepository.getDrawCount()
        loseCount = gameRepository.getLoseCount()

        mainScope.launch {
            withContext(Dispatchers.Main){
                tv_win.setText(R.string.tv_win)
                tv_win.append(" $winCount")

                tv_draw.setText(R.string.tv_draw)
                tv_draw.append(" $drawCount")

                tv_lose.setText(R.string.tv_lose)
                tv_lose.append(" $loseCount")
            }
        }

    }

}