package com.example.sanja.weatherapp;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by sanja on 4/7/2017.
 */

public class DisplayCityAdapter extends RecyclerView.Adapter<DisplayCityAdapter.EpisodeHolder> {
    List<SaveCityObject> mData;
    LayoutInflater inflater;
    Context mContext;

    DatabaseReference rootRef;
    DatabaseReference sRef;

    public ItemClickListener itemClickListener;
    public ItemLongClickListener itemLongClickListener;

    public DisplayCityAdapter(List<SaveCityObject> list , Context context){
        this.mData = list;
        this.inflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    @Override
    public EpisodeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_vertical_layout , parent , false);
        return new EpisodeHolder(view);
    }

    @Override
    public void onBindViewHolder(EpisodeHolder holder, int position) {

        SaveCityObject object = mData.get(position);
        String city = object.getCityName();
        String country = object.getCountry();
        holder.cityCountry.setText(city+", "+country);
        String DEGREE  = "\u00b0";
        holder.tempVertical.setText("Temperature : "+object.getTempInC()+DEGREE+"C");
        if(object.isFavorite()){
            holder.imageButton.setImageResource(R.drawable.star_gold);
        }
        else{
            holder.imageButton.setImageResource(R.drawable.star_gray);
        }

        String prettyTimeString = object.getTime();
        PrettyTime p = new PrettyTime();
       /* SimpleDateFormat df = new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy");
        Log.d("demo","df "+df);
        Date date = null;
        try {
            date = df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        *//*String epoch = String.valueOf(date.getTime());*//*
        Log.d("demo","date "+date);*/

        holder.updatedVertical.setText("Last Updated : "+p.format(new Date(Long.parseLong(prettyTimeString))));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }




    public class EpisodeHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        ImageButton imageButton;
        TextView cityCountry;
        TextView tempVertical;
        TextView updatedVertical;
        View container;

        public EpisodeHolder(final View itemView) {
            super(itemView);

            imageButton = (ImageButton) itemView.findViewById(R.id.imageButtonVertical);
            cityCountry = (TextView)itemView.findViewById(R.id.textViewCityCountry);
            tempVertical = (TextView)itemView.findViewById(R.id.textViewTempVertical);
            updatedVertical = (TextView)itemView.findViewById(R.id.textViewUpdatedVertical);
            container = itemView.findViewById(R.id.root_saved_city_layout);

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(MainActivity.savedCityObjectArrayList.get(getAdapterPosition()).isFavorite()){
                        Log.d("demo","onImageButtonClick "+getAdapterPosition());
                        MainActivity.recyclerView.getLayoutManager().moveView(getAdapterPosition(),0);
                        imageButton.setImageResource(R.drawable.star_gray);
                        SaveCityObject object =  mData.get(getAdapterPosition());
                        LayoutInflater inflater1 = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View layout = inflater1.inflate(R.layout.custom_toast,
                                (ViewGroup)itemView.findViewById(R.id.custom_toast_container));

                        TextView text = (TextView) layout.findViewById(R.id.text_toast);
                        text.setText(object.cityName+" removed from Favorites");

                        Toast toast = new Toast(mContext);
                        toast.setGravity(Gravity.BOTTOM, -20, 100);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setView(layout);
                        toast.show();
                        object.setFavorite(false);
                        Log.d("demo","savedCityObjectArrayList.get(postion) "+object.toString());
                        rootRef= FirebaseDatabase.getInstance().getReference();
                        sRef=rootRef.child("Cities");
                        DatabaseReference reference = sRef.child(object.getUid1());
                        reference.setValue(object);
                    }
                    else{
                        Log.d("demo","onImageButtonClick "+getAdapterPosition());

                        imageButton.setImageResource(R.drawable.star_gold);
                        SaveCityObject object =  mData.get(getAdapterPosition());
                        //Toast.makeText(mContext, object.cityName+" marked as Favorites", Toast.LENGTH_SHORT).show();
                        LayoutInflater inflater1 = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View layout = inflater1.inflate(R.layout.custom_toast,
                                (ViewGroup)itemView.findViewById(R.id.custom_toast_container));

                        TextView text = (TextView) layout.findViewById(R.id.text_toast);
                        text.setText(object.cityName+" added to Favorites");

                        Toast toast = new Toast(mContext);
                        toast.setGravity(Gravity.BOTTOM, -20, 100);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setView(layout);
                        toast.show();
                        object.setFavorite(true);
                        Log.d("demo","savedCityObjectArrayList.get(postion) "+object.toString());
                        rootRef= FirebaseDatabase.getInstance().getReference();
                        sRef=rootRef.child("Cities");
                        DatabaseReference reference = sRef.child(object.getUid1());
                        reference.setValue(object);
                        /*mData.remove(getAdapterPosition());
                        notifyDataSetChanged();
                        mData.add(0,object);
                        Log.d("demo"," ?? "+mData.toString());
                        notifyDataSetChanged();*/


                    }


                }
            });
            //container.setOnClickListener(this);

            container.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if(v.getId() == R.id.root_saved_city_layout){
                itemClickListener.onOtherItemClick(getAdapterPosition());
            }else{
                /*itemClickListener.onImageButtonClick(getAdapterPosition());*/
            }

        }


        @Override
        public boolean onLongClick(View v) {

            itemLongClickListener.onItemLongClick(getAdapterPosition());
            return false;
        }
    }


    static interface ItemClickListener {

        public void onOtherItemClick(int position);
        public void onImageButtonClick(int postion);
    }

    static interface ItemLongClickListener {

        public void onItemLongClick(int position);

    }
    public void setItemLongClickListener(ItemLongClickListener itemLongClickListener){

        this.itemLongClickListener = itemLongClickListener;
    }


    public void setItemClickListener(ItemClickListener itemClickListener){

        this.itemClickListener = itemClickListener;
    }

}
