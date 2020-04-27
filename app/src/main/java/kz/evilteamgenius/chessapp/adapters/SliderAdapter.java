package kz.evilteamgenius.chessapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

import kz.evilteamgenius.chessapp.R;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private Context context;
    private List<String> allImages = new ArrayList<>();
    private ImageView mImageView;

    public SliderAdapter(Context context) {
        this.context = context;
    }


    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_advertisement, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        viewHolder.bindItem(allImages.get(position));
    }

    @Override
    public int getCount() {
        return allImages.size();
    }

    public void setImages(ArrayList<String> arrayList){
        allImages.clear();
        allImages.addAll(arrayList);
        notifyDataSetChanged();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        public SliderAdapterVH(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageOfAdvertisement);
            mImageView.setClipToOutline(true);
        }
        void bindItem(String url){
            Glide.with(context)
                    .load(url)
                    .into(mImageView);

        }
    }
}
