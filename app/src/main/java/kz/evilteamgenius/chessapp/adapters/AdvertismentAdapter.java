package kz.evilteamgenius.chessapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import kz.evilteamgenius.chessapp.R;

public class AdvertismentAdapter extends RecyclerView.Adapter<AdvertismentAdapter.ViewHolder> {
    private List<String> allImages = new ArrayList<>();
    private ImageView mImageView;


    public AdvertismentAdapter() {
        super();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_advertisement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindItem(allImages.get(position));
    }

    @Override
    public int getItemCount() {
        return allImages.size();
    }

    public void setImages(ArrayList<String> arrayList){
        allImages.clear();
        allImages.addAll(arrayList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageOfAdvertisement);
            mImageView.setClipToOutline(true);
        }
        void bindItem(String url){
            Glide.with(itemView.getContext())
                    .load(url)
                    .into(mImageView);

        }
    }

}
