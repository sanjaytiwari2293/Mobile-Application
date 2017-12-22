package com.example.sanja.podcastapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sanja on 3/17/2017.
 */

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.EpisodeHolder> {

    List<Episodes> mData;
    LayoutInflater inflator;
    Context mContext;

    public ItemClickListener itemClickListener;

    public EpisodeAdapter(List<Episodes> list , Context context){
        this.mData = list;
        this.inflator = LayoutInflater.from(context);
        this.mContext = context;

    }

    @Override
    public EpisodeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.row_item_layout , parent , false);
        return new EpisodeHolder(view);
    }

    @Override
    public void onBindViewHolder(EpisodeHolder holder, int position) {
        Episodes episode = mData.get(position);
        holder.title.setText(episode.getTitle());

        holder.pub_date.setText(episode.getRel_date());

        String imgUrl = episode.getImg_url();
        Picasso.with(mContext).load(imgUrl).into(holder.image);


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class EpisodeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title;
        TextView pub_date;
        ImageButton play;
        ImageView image;
        View container;

        public EpisodeHolder(View itemView) {
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.textViewTitle);
            pub_date = (TextView)itemView.findViewById(R.id.textViewRelDate);
            play = (ImageButton)itemView.findViewById(R.id.imageButton_row_item);
            image = (ImageView)itemView.findViewById(R.id.imageView);
            container = itemView.findViewById(R.id.row_item_root);

            play.setOnClickListener(this);
            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if(v.getId() == R.id.row_item_root){
                itemClickListener.onOtherItemClick(getAdapterPosition());
            }else{
                itemClickListener.onPlayButtonClick(getAdapterPosition());
            }

        }


    }

    static interface ItemClickListener {

        public void onOtherItemClick(int position);
        public void onPlayButtonClick(int postion);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){

        this.itemClickListener = itemClickListener;
    }
}
