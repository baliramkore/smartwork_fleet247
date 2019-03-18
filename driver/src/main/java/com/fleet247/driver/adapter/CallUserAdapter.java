package com.fleet247.driver.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fleet247.driver.R;
import com.fleet247.driver.data.models.upcomingbooking.Passenger;

import java.util.List;

/**
 * Created by sandeep on 27/11/17.
 */

public class CallUserAdapter extends RecyclerView.Adapter<CallUserAdapter.ViewHolder> {

    CallUserEventListener callUserEventListener;
    List<Passenger> passenger;

    public CallUserAdapter(CallUserEventListener callUserEventListener,List<Passenger> passenger) {
        this.callUserEventListener=callUserEventListener;
        this.passenger=passenger;
        Log.d("Status","Initiated");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.call_contact_bottom_view,parent,false);
        Log.d("Status","Created");
        return new CallUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.userName.setText(passenger.get(position).peopleName);
        holder.userContactNo.setText(passenger.get(position).peopleContact);
        Log.d("Status","Binded");

    }

    @Override
    public int getItemCount() {
        return passenger.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView userName;
        TextView userContactNo;
        ImageView callButton;

        public ViewHolder(View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.call_user_name);
            userContactNo=itemView.findViewById(R.id.call_user_no);
            callButton=itemView.findViewById(R.id.call_user_button);
            callButton.setOnClickListener(this);
            Log.d("Status","View Created");
        }

        @Override
        public void onClick(View view) {
            int pos=getAdapterPosition();
            callUserEventListener.onCallClicked(passenger.get(pos).peopleContact);
        }
    }

    public interface CallUserEventListener{
        void onCallClicked(String contactNo);
    }
}
