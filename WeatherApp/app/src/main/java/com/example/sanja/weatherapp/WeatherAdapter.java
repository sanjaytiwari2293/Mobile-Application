package com.example.sanja.weatherapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by sanja on 4/6/2017.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.EpisodeHolder> {

    List<FiveDaysWeatherObject> mData;
    LayoutInflater inflater;
    Context mContext;

    public ItemClickListener itemClickListener;

    public WeatherAdapter(List<FiveDaysWeatherObject> list , Context context){
        this.mData = list;
        this.inflater = LayoutInflater.from(context);
        this.mContext = context;

    }

    @Override
    public EpisodeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_horizontal_layout , parent , false);
        return new EpisodeHolder(view);

    }

    @Override
    public void onBindViewHolder(EpisodeHolder holder, int position) {

        FiveDaysWeatherObject weatherObject = mData.get(position);
        //2017-04-06T07:00:00-04:00
        String date = weatherObject.getDate();
        String dateD = date.substring(0,2);
        String dateR = date.substring(3,9);
        dateD = dateD.concat("th");

        int iconId = weatherObject.getDayIcon();
        if(iconId<10){
            /*new DisplayCurrentConditionsAsync().execute("http://developer.accuweather.com/sites/default/files/0"+wId+"-s.png");*/
            String url = "http://developer.accuweather.com/sites/default/files/0"+iconId+"-s.png";
            Picasso.with(mContext).load(url).fit().centerCrop().into(holder.image);
        }
        else {
            // new DisplayCurrentConditionsAsync().execute("http://developer.accuweather.com/sites/default/files/"+wId+"-s.png");
            String url = "http://developer.accuweather.com/sites/default/files/"+iconId+"-s.png";
            Picasso.with(mContext).load(url).fit().centerCrop().into(holder.image);
        }

        holder.dateShow.setText(dateD+" "+dateR);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class EpisodeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView image;
        TextView dateShow;
        View container;

        public EpisodeHolder(View itemView) {
            super(itemView);

            image = (ImageView)itemView.findViewById(R.id.imageViewIconDisplay);
            dateShow = (TextView)itemView.findViewById(R.id.textViewDateShow);
            container = itemView.findViewById(R.id.root_linear_layout);

            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if(v.getId() == R.id.root_linear_layout){
                itemClickListener.onOtherItemClick(getAdapterPosition());
            }

        }


    }

    static interface ItemClickListener {

        public void onOtherItemClick(int position);

    }

    public void setItemClickListener(ItemClickListener itemClickListener){

        this.itemClickListener = itemClickListener;
    }
}
