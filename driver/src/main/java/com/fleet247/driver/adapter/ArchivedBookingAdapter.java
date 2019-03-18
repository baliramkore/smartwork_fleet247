package com.fleet247.driver.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fleet247.driver.R;
import com.fleet247.driver.data.models.archivedbooking.Booking;
import com.fleet247.driver.data.models.archivedbooking.Passenger;

import java.util.List;

/**
 * Created by sandeep on 7/2/18.
 */

public class ArchivedBookingAdapter extends RecyclerView.Adapter<ArchivedBookingAdapter.ViewHolder>{
    List<Booking> bookingList;
    RecyclerViewEventListener recyclerViewEventListener;
    Context context;

    public ArchivedBookingAdapter(RecyclerViewEventListener recyclerViewEventListener, List<Booking> bookingList){
        this.bookingList=bookingList;
        this.recyclerViewEventListener=recyclerViewEventListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.completed_booking_lay,parent,false);
        return new ArchivedBookingAdapter.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.date.setText(bookingList.get(position).getPickupDatetime());
        holder.bookingId.setText(bookingList.get(position).getReferenceNo());
        holder.pickupLocation.setText(bookingList.get(position).getPickupLocation());
        holder.noOfPassenger.setText(bookingList.get(position).getPassengers().size()+"");
        if(bookingList.get(position).getDropLocation()!=null && bookingList.get(position).getDropLocation().length()>1){
            holder.dropLocation.setText(bookingList.get(position).getDropLocation());
            holder.dropLocationLay.setVisibility(View.VISIBLE);
        }
        else{
            holder.dropLocationLay.setVisibility(View.GONE);
        }

        holder.paymentType.setText(bookingList.get(position).getPaymentTxype());
        holder.tripType.setText(bookingList.get(position).getTourType()+" "+bookingList.get(position).getPackageName());
        List<Passenger> passengers=bookingList.get(position).getPassengers();
        switch (bookingList.get(position).getPassengers().size()){
            case 1:
                holder.passengerName1.setText(passengers.get(0).peopleName);
                holder.passengerContact1.setText(passengers.get(0).peopleContact);
                break;
            case 2:
                holder.passengerName1.setText(passengers.get(0).peopleName);
                holder.passengerContact1.setText(passengers.get(0).peopleContact);
                holder.passengerName2.setText(passengers.get(1).peopleName);
                holder.passengerContact2.setText(passengers.get(1).peopleContact);
                break;
            case 3:
                holder.passengerName1.setText(passengers.get(0).peopleName);
                holder.passengerContact1.setText(passengers.get(0).peopleContact);
                holder.passengerName2.setText(passengers.get(1).peopleName);
                holder.passengerContact2.setText(passengers.get(1).peopleContact);
                holder.passengerName3.setText(passengers.get(2).peopleName);
                holder.passengerContact3.setText(passengers.get(2).peopleContact);
                break;
            case 4:
                holder.passengerName1.setText(passengers.get(0).peopleName);
                holder.passengerContact1.setText(passengers.get(0).peopleContact);
                holder.passengerName2.setText(passengers.get(1).peopleName);
                holder.passengerContact2.setText(passengers.get(1).peopleContact);
                holder.passengerName3.setText(passengers.get(2).peopleName);
                holder.passengerContact3.setText(passengers.get(2).peopleContact);
                holder.passengerName4.setText(passengers.get(3).peopleName);
                holder.passengerContact4.setText(passengers.get(3).peopleContact);
                break;
            case 5:
                holder.passengerName1.setText(passengers.get(0).peopleName);
                holder.passengerContact1.setText(passengers.get(0).peopleContact);
                holder.passengerName2.setText(passengers.get(1).peopleName);
                holder.passengerContact2.setText(passengers.get(1).peopleContact);
                holder.passengerName3.setText(passengers.get(2).peopleName);
                holder.passengerContact3.setText(passengers.get(2).peopleContact);
                holder.passengerName4.setText(passengers.get(3).peopleName);
                holder.passengerContact4.setText(passengers.get(3).peopleContact);
                holder.passengerName5.setText(passengers.get(4).peopleName);
                holder.passengerContact5.setText(passengers.get(4).peopleContact);
                break;
            case 6:
                holder.passengerName1.setText(passengers.get(0).peopleName);
                holder.passengerContact1.setText(passengers.get(0).peopleContact);
                holder.passengerName2.setText(passengers.get(1).peopleName);
                holder.passengerContact2.setText(passengers.get(1).peopleContact);
                holder.passengerName3.setText(passengers.get(2).peopleName);
                holder.passengerContact3.setText(passengers.get(2).peopleContact);
                holder.passengerName4.setText(passengers.get(3).peopleName);
                holder.passengerContact4.setText(passengers.get(3).peopleContact);
                holder.passengerName5.setText(passengers.get(4).peopleName);
                holder.passengerContact5.setText(passengers.get(4).peopleContact);
                holder.passengerName6.setText(passengers.get(5).peopleName);
                holder.passengerContact6.setText(passengers.get(5).peopleContact);
                break;

            default:break;

        }
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView date;
        TextView time;
        TextView bookingId;
        TextView pickupLocation;
        TextView dropLocation;
        TextView noOfPassenger;
        TextView tripType;
        TextView paymentType;
        TextView passengerDetails;
        TextView passengerName1;
        TextView passengerContact1;
        TextView passengerName2;
        TextView passengerContact2;
        TextView passengerName3;
        TextView passengerContact3;
        TextView passengerName4;
        TextView passengerContact4;
        TextView passengerName5;
        TextView passengerContact5;
        TextView passengerName6;
        TextView passengerContact6;
        LinearLayout dropLocationLay;
        LinearLayout passengerLay1;
        LinearLayout passengerLay2;
        LinearLayout passengerLay3;
        LinearLayout passengerLay4;
        LinearLayout passengerLay5;
        LinearLayout passengerLay6;
        LinearLayout paymentDetails;
        boolean isExpended=false;

        public ViewHolder(View itemView) {
            super(itemView);

            date=itemView.findViewById(R.id.travel_date);
            bookingId=itemView.findViewById(R.id.booking_id);
            pickupLocation=itemView.findViewById(R.id.pickup_location);
            dropLocation=itemView.findViewById(R.id.drop_location);
            dropLocationLay=itemView.findViewById(R.id.archived_drop_location_lay);
            noOfPassenger=itemView.findViewById(R.id.no_of_passenger);
            tripType=itemView.findViewById(R.id.trip_type);
            paymentType=itemView.findViewById(R.id.payment_status);
            passengerDetails=itemView.findViewById(R.id.passenger_details);
            passengerName1=itemView.findViewById(R.id.passenger_name_1);
            passengerContact1=itemView.findViewById(R.id.passenger_contact_1);
            passengerName2=itemView.findViewById(R.id.passenger_name_2);
            passengerContact2=itemView.findViewById(R.id.passenger_contact_2);
            passengerName3=itemView.findViewById(R.id.passenger_name_3);
            passengerContact3=itemView.findViewById(R.id.passenger_contact_3);
            passengerName4=itemView.findViewById(R.id.passenger_name_4);
            passengerContact4=itemView.findViewById(R.id.passenger_contact_4);
            passengerName5=itemView.findViewById(R.id.passenger_name_5);
            passengerContact5=itemView.findViewById(R.id.passenger_contact_5);
            passengerName6=itemView.findViewById(R.id.passenger_name_6);
            passengerContact6=itemView.findViewById(R.id.passenger_contact_6);
            passengerLay1=itemView.findViewById(R.id.passenger_lay_1);
            passengerLay2=itemView.findViewById(R.id.passenger_lay_2);
            passengerLay3=itemView.findViewById(R.id.passenger_lay_3);
            passengerLay4=itemView.findViewById(R.id.passenger_lay_4);
            passengerLay5=itemView.findViewById(R.id.passenger_lay_5);
            passengerLay6=itemView.findViewById(R.id.passenger_lay_6);
            paymentDetails=itemView.findViewById(R.id.payment_details);

            passengerLay1.setVisibility(View.GONE);
            passengerLay2.setVisibility(View.GONE);
            passengerLay3.setVisibility(View.GONE);
            passengerLay4.setVisibility(View.GONE);
            passengerLay5.setVisibility(View.GONE);
            passengerLay6.setVisibility(View.GONE);
            itemView.requestFocus();

            passengerDetails.setOnClickListener(this);
            paymentType.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            android.util.Log.d("Clicked","True");
            int pos=getAdapterPosition();
            switch (view.getId()){
                case R.id.payment_status:
                    recyclerViewEventListener.onDetailsClicked(bookingList.get(pos));
                    break;
                case R.id.passenger_details:
                    if (!isExpended) {
                        isExpended=true;
                        Booking booking=bookingList.get(pos);
                        switch (bookingList.get(pos).getPassengers().size()) {
                            case 1:
                                passengerLay1.setVisibility(View.VISIBLE);
                                break;
                            case 2:
                                passengerLay1.setVisibility(View.VISIBLE);
                                passengerLay2.setVisibility(View.VISIBLE);
                                break;
                            case 3:
                                passengerLay1.setVisibility(View.VISIBLE);
                                passengerLay2.setVisibility(View.VISIBLE);
                                passengerLay3.setVisibility(View.VISIBLE);
                                break;
                            case 4:
                                passengerLay1.setVisibility(View.VISIBLE);
                                passengerLay2.setVisibility(View.VISIBLE);
                                passengerLay3.setVisibility(View.VISIBLE);
                                passengerLay4.setVisibility(View.VISIBLE);
                                break;
                            case 5:
                                passengerLay1.setVisibility(View.VISIBLE);
                                passengerLay2.setVisibility(View.VISIBLE);
                                passengerLay3.setVisibility(View.VISIBLE);
                                passengerLay4.setVisibility(View.VISIBLE);
                                passengerLay5.setVisibility(View.VISIBLE);
                                break;
                            case 6:
                                passengerLay1.setVisibility(View.VISIBLE);
                                passengerLay2.setVisibility(View.VISIBLE);
                                passengerLay3.setVisibility(View.VISIBLE);
                                passengerLay4.setVisibility(View.VISIBLE);
                                passengerLay5.setVisibility(View.VISIBLE);
                                passengerLay6.setVisibility(View.VISIBLE);
                                break;
                            default:
                                break;
                        }
                        passengerDetails.setText(context.getString(R.string.collapse_text));
                        passengerDetails.setCompoundDrawablesWithIntrinsicBounds(null,view.getContext().getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24px),null,null);
                    }
                    else {
                        isExpended=false;
                        passengerDetails.setText(context.getString(R.string.passenger_details));
                        passengerDetails.setCompoundDrawablesWithIntrinsicBounds(null,null,null,view.getContext().getDrawable(R.drawable.ic_keyboard_arrow_down_black_24px));
                        passengerLay1.setVisibility(View.GONE);
                        passengerLay2.setVisibility(View.GONE);
                        passengerLay3.setVisibility(View.GONE);
                        passengerLay4.setVisibility(View.GONE);
                        passengerLay5.setVisibility(View.GONE);
                        passengerLay6.setVisibility(View.GONE);
                    }
                    break;
            }

        }
    }

    public interface RecyclerViewEventListener{
        void onDetailsClicked(Booking booking);
    }
}
