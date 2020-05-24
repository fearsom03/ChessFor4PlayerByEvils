package kz.evilteamgenius.chessapp.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.api.load
import kotlinx.android.synthetic.main.fragment_update_your_application.*
import kz.evilteamgenius.chessapp.R


class UpdateYourApplicationFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_your_application, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        imageOfUpdate.load("https://www.theriverstonegroup.com/wordpress/wp-content/uploads/2014/09/Update.jpg")
        linearMain.setOnClickListener {
            val appPackageName = requireActivity().packageName
            try {
                startActivity(Intent(Intent.ACTION_VIEW
                        , Uri.parse("market://details?id="
                        + appPackageName)))
            } catch (anfe: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW
                        , Uri.parse("https://play.google.com/store/apps/details?id="
                        + appPackageName)))
            }
        }
    }
}
