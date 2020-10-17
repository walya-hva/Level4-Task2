package nl.hva.fdmci.mad.level4_task2.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.hva.fdmci.mad.level4_task2.R
import nl.hva.fdmci.mad.level4_task2.model.Game
import nl.hva.fdmci.mad.level4_task2.repository.GameRepository


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class HistoryFragment : Fragment() {

    private var gameList = arrayListOf<Game>()
    private val gameAdapter = GameAdapter(gameList)
    private lateinit var gameRepository: GameRepository
    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        gameRepository = GameRepository(requireContext())

        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.action_history).setIcon(R.drawable.ic_delete_white_24dp)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_history -> {
                mainScope.launch {
                    withContext(Dispatchers.IO) {
                        gameRepository.deleteAllGames()
                    }
                    gameList.clear()
                    gameAdapter.notifyDataSetChanged()
                }

                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.title_history)

        (activity as AppCompatActivity).toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        (activity as AppCompatActivity).toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_HistoryFragment_to_GameFragment)
        }

        mainScope.launch {
            gameList.clear()
            val games = withContext(Dispatchers.IO) {
                gameRepository.getAllGames()
            }

            gameList.addAll(games);
            gameAdapter.notifyDataSetChanged()
        }

        initRv()
    }



    private fun initRv() {
        rv_games.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )

        createItemTouchHelper().attachToRecyclerView(rv_games)

        rv_games.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = gameAdapter
        }
    }

    private fun createItemTouchHelper(): ItemTouchHelper {

        // Callback which is used to create the ItemTouch helper. Only enables left swipe.
        // Use ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) to also enable right swipe.
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // Enables or Disables the ability to move items up and down.
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // Callback triggered when a user swiped an item.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val gameToDelete = gameList[position]
                mainScope.launch {
                    withContext(Dispatchers.IO) {
                        gameRepository.deleteGame(gameToDelete)
                    }
                    gameList.remove(gameToDelete)
                }
            }
        }
        return ItemTouchHelper(callback)
    }
}