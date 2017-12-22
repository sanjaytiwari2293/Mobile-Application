package com.example.sanja.tripbookfinal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sanja on 5/24/2017.
 */

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder>{

    List<Deals> mData;
    LayoutInflater inflater;
    Context mContext;
    int numOfPeople;
    /*int currentPos = -1;
    private RadioButton lastCheckedRB = null;*/

    public ItemClickListener itemClickListener;

    public PlacesAdapter(List<Deals> mData, Context mContext, int people) {
        this.mData = mData;
        this.inflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        numOfPeople = people;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_item_places, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Deals user = mData.get(position);
        holder.textViewPlaceName.setText(user.getplace());
        holder.cost.setText("Cost : $"+String.valueOf(user.getcost())+"*"+numOfPeople+" = $"+numOfPeople*user.getcost());
        holder.duration.setText(user.getduration());

        //maybe shared preference will work or arraylist!!
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textViewPlaceName;
        TextView cost;
        TextView duration;
        Button viewMaps;
        //RadioButton radioButton;
        RadioGroup radioGroup;
        View container;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewPlaceName = (TextView)itemView.findViewById(R.id.textViewPlace);
            cost = (TextView) itemView.findViewById(R.id.textViewCost);
            duration = (TextView) itemView.findViewById(R.id.textViewDuration);
            container = itemView.findViewById(R.id.rootLinear);
            container.setOnClickListener(this);
            //radioButton = (RadioButton)itemView.findViewById(R.id.radioButtonSelect);
            //radioGroup = (RadioGroup)itemView.findViewById(R.id.radioGroup);
            viewMaps = (Button)itemView.findViewById(R.id.buttonViewMap);
            viewMaps.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if(v.getId()==R.id.buttonViewMap){
                itemClickListener.onViewMapClick(getAdapterPosition());
            }
            else{
                Log.d("demo","container clicked click");
                itemClickListener.onContainerClick(getAdapterPosition());
            }

        }
    }

    static interface ItemClickListener {

        public void onViewMapClick(int position);

        void onContainerClick(int adapterPosition);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){

        this.itemClickListener = itemClickListener;
    }
}
