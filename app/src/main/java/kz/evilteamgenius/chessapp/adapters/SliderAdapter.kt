package kz.evilteamgenius.chessapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import coil.api.load
import com.smarteist.autoimageslider.SliderViewAdapter
import kz.evilteamgenius.chessapp.R
import kz.evilteamgenius.chessapp.adapters.SliderAdapter.SliderAdapterVH
import kz.evilteamgenius.chessapp.models.customAdds.AdObj
import java.util.*

class SliderAdapter(private val context: Context,
                    private val listener: myListener)
    : SliderViewAdapter<SliderAdapterVH>() {
    private val allImages: MutableList<AdObj> = ArrayList()
    private var mImageView: ImageView? = null
    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.item_advertisement, null)
        return SliderAdapterVH(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
        viewHolder.bindItem(allImages[position])
    }

    override fun getCount(): Int {
        return allImages.size
    }

    fun setImages(arrayList: ArrayList<AdObj>?) {
        allImages.clear()
        allImages.addAll(arrayList!!)
        notifyDataSetChanged()
    }

    inner class SliderAdapterVH(itemView: View) : ViewHolder(itemView) {
        fun bindItem(adObj: AdObj) {
            mImageView?.load(adObj.urlImage)
            mImageView?.setOnClickListener {
                listener.goToWebPage(adObj.urlOfAdvertisement)
            }
        }

        init {
            mImageView = itemView.findViewById(R.id.imageOfAdvertisement)
        }
    }

    interface myListener {
        fun goToWebPage(webPage: String)
    }

}