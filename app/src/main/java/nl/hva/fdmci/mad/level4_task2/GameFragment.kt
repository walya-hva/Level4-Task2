package nl.hva.fdmci.mad.level4_task2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        btn_rock.setOnClickListener{ fight(Weapon.ROCK) }
        btn_paper.setOnClickListener { fight(Weapon.PAPER) }
        btn_scissors.setOnClickListener { fight(Weapon.SCISSORS) }

        gameRepository = GameRepository(requireContext())
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

        val game = Game(computerWeapon = computerWeapon, playerWeapon = playerWeapon,
            date = Date.from(Instant.now()), winner = winner
        )

        mainScope.launch {
            withContext(Dispatchers.IO) {
                gameRepository.insertGame(game)
            }
        }
    }
}