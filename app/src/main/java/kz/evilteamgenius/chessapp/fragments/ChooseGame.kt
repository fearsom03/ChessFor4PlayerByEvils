package kz.evilteamgenius.chessapp.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_choose_game.*
import kz.evilteamgenius.chessapp.R
import kz.evilteamgenius.chessapp.api.loaders.LastMoveLoader
import kz.evilteamgenius.chessapp.api.loaders.LastMoveLoader.LastMoveCallback
import kz.evilteamgenius.chessapp.api.loaders.MakeNewGameLoader
import kz.evilteamgenius.chessapp.api.loaders.MakeNewGameLoader.GetMakeNewGameLoaderCallback
import kz.evilteamgenius.chessapp.engine.GameEngine
import kz.evilteamgenius.chessapp.engine.Match
import kz.evilteamgenius.chessapp.extensions.*
import kz.evilteamgenius.chessapp.models.Game
import kz.evilteamgenius.chessapp.viewModels.GameViewModel
import timber.log.Timber
import java.util.*


private const val ARG_PARAM1 = "param1"

class ChooseGame : Fragment(), View.OnClickListener {
    private var isOnlineGame: Boolean = false
    private val handler: Handler? = Handler()
    private val timer = Timer()
    private var runnable: Runnable? = Runnable { getLastMove() }
    private var mode: Int = 1
    var viewModel: GameViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isOnlineGame = it.getBoolean(ARG_PARAM1)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(GameViewModel::class.java)
        initClickers()
    }

    private fun initClickers() {
        noTeamForFour.setOnClickListener(this)
        withTeamForFour.setOnClickListener(this)
        regularForTwo.setOnClickListener(this)
        extendedForTwo.setOnClickListener(this)
    }

    companion object {
        @JvmStatic
        fun newInstance(isOnlineGame: Boolean) =
                ChooseGame().apply {
                    arguments = Bundle().apply {
                        putBoolean(ARG_PARAM1, isOnlineGame)
                    }
                }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.noTeamForFour -> {
                mode = 4
                if (!isOnlineGame) {
                    startLocalMatch(mode)
                } else {
                    viewModel?.getInternetCheck()?.observe(requireActivity(),
                            androidx.lifecycle.Observer { aBoolean: Boolean ->
                                if (aBoolean) {
                                    makeNewGame()
                                } else {
                                    toastKt(requireContext(), getString(R.string.needToBeOnline))
                                }
                            })
                }

            }
            R.id.withTeamForFour -> {
                mode = 3
                if (!isOnlineGame) {
                    startLocalMatch(mode)
                } else {
                    viewModel?.getInternetCheck()?.observe(requireActivity(),
                            androidx.lifecycle.Observer { aBoolean: Boolean ->
                                if (aBoolean) {
                                    makeNewGame()
                                } else {
                                    toastKt(requireContext(), getString(R.string.needToBeOnline))
                                }
                            })
                }

            }
            R.id.regularForTwo -> {
                mode = 1
                if (!isOnlineGame) {
                    startLocalMatch(mode)
                } else {
                    viewModel?.getInternetCheck()?.observe(requireActivity(),
                            androidx.lifecycle.Observer { aBoolean: Boolean ->
                                if (aBoolean) {
                                    makeNewGame()
                                } else {
                                    toastKt(requireContext(), getString(R.string.needToBeOnline))
                                }
                            })
                }

            }
            R.id.extendedForTwo -> {
                mode = 2
                if (!isOnlineGame) {
                    startLocalMatch(mode)
                } else {
                    viewModel?.getInternetCheck()?.observe(requireActivity(),
                            androidx.lifecycle.Observer { aBoolean: Boolean ->
                                if (aBoolean) {
                                    makeNewGame()
                                } else {
                                    toastKt(requireContext(), getString(R.string.needToBeOnline))
                                }
                            })
                }

            }
        }


    }

    private fun startLocalMatch(mode: Int) {
        val match = Match(System.currentTimeMillis().toString(), mode, true)
        GameEngine.newGame(match, null, null, null)
        startGame(match.id)
    }

    private fun startGame(matchID: String) {
        Timber.d("startGame")
        val fragment = GameFragment()
        val b = Bundle()
        b.putString("matchID", matchID)
        fragment.arguments = b
        replaceFragment(fragment)
    }

    private fun makeNewGame() {
        val loader = MakeNewGameLoader(object : GetMakeNewGameLoaderCallback {
            override fun onGetGoodsLoaded(game: Game) {
                GameEngine.game = game
                requireContext().toast(game.toString())
                if (!checkIfMatched(game)) callAsynchronousTask()
            }

            override fun onResponseFailed(errorMessage: String) {
                requireContext().toast(errorMessage)
            }
        })
        loader.loadMakeNew2PGame(requireActivity().getToken(), mode)
    }

    private fun checkIfMatched(game: Game): Boolean {
        return if (mode == 1 || mode == 2) {
            if (!game.player1.isEmpty() && !game.player2.isEmpty()) {
                timer.cancel() // Terminates this timer, discarding any currently scheduled tasks.
                timer.purge() // Removes all cancelled tasks from this timer's task queue.
                val match = Match(System.currentTimeMillis().toString(),
                        mode, false)
                val players = arrayOf(game.player1, game.player2)
                GameEngine.game = game
                GameEngine.newGame(match, players, requireContext().getUsername(), game.id.toString())
                startGame(match.id)
                return true
            }
            false
        } else {
            if (!game.player1.isEmpty() && !game.player2.isEmpty() && !game.player3.isEmpty() && !game.player4.isEmpty()) {
                timer.cancel()
                timer.purge()
                val match = Match(System.currentTimeMillis().toString(),
                        mode, false)
                val players = arrayOf(game.player1, game.player2, game.player3, game.player4)
                GameEngine.game = game
                GameEngine.newGame(match, players, requireContext().getUsername(), game.id.toString())
                startGame(match.id)
                return true
            }
            false
        }
    }

    private fun callAsynchronousTask() {
        val doAsynchronousTask: TimerTask = object : TimerTask() {
            override fun run() {
                handler?.post(runnable)
            }
        }
        timer.schedule(doAsynchronousTask, 0, 6000) //execute in every 50000 ms
    }

    private fun getLastMove() {
        val token = requireActivity().getToken()
        //        toast(getContext(),token);
        val lastMoveLoader = LastMoveLoader(object : LastMoveCallback {
            override fun onMoveLoaded(game: Game) {
                checkIfMatched(game)
                context!!.toast(game.toString())
            }

            override fun onResponseFailed(errorMessage: String) {
                context!!.toast(errorMessage)
            }
        })
        lastMoveLoader.getLastMove(token, GameEngine.game.id)
    }


}
