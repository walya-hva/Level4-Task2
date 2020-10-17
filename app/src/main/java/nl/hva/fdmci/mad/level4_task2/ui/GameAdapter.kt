package nl.hva.fdmci.mad.level4_task2.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import kotlinx.android.synthetic.main.game_history_card.view.*
import nl.hva.fdmci.mad.level4_task2.R
import nl.hva.fdmci.mad.level4_task2.model.Game
import nl.hva.fdmci.mad.level4_task2.model.Weapon

class GameAdapter(private val games: List<Game>) : Adapter<GameAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.game_history_card, parent, false)
        )
    }

    override fun getItemCount(): Int = games.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(games[position])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(game: Game) {

            when(game.winner) {
                "Draw" -> {
                    itemView.tv_winner.setText(R.string.msg_draw_win)
                }
                "Computer" -> {
                    itemView.tv_winner.setText(R.string.msg_computer_win)
                }
                "You" -> {
                    itemView.tv_winner.setText(R.string.msg_player_win)
                }
            }

            itemView.tv_game_date.text = game.date.toString()
            val computerWeapon = Weapon.valueOf(game.computerWeapon)
            itemView.img_computer_history.setImageResource(computerWeapon.resourceId)
            val playerWeapon = Weapon.valueOf(game.playerWeapon)
            itemView.img_player_history.setImageResource(playerWeapon.resourceId)
        }
    }

}