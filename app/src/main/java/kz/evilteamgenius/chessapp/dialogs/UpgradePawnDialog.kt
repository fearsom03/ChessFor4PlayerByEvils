package kz.evilteamgenius.chessapp.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.dialog_upgrade_pawn.*
import kz.evilteamgenius.chessapp.R

class UpgradePawnDialog(var listener: ListenerOfChoose, context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_upgrade_pawn)
        setCanceledOnTouchOutside(false)
        setClickers()
    }

    private fun setClickers() {
        pawn_bishop.setOnClickListener {
            listener.onClickButton(0)
            dismiss()
        }

        pawn_knight.setOnClickListener {
            listener.onClickButton(1)
            dismiss()
        }

        pawn_queen.setOnClickListener {
            listener.onClickButton(2)
            dismiss()
        }

        pawn_rook.setOnClickListener {
            listener.onClickButton(3)
            dismiss()
        }
    }

    interface ListenerOfChoose {
        fun onClickButton(position: Int)
    }
}