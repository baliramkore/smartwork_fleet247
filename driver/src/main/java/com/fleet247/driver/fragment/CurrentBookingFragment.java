package com.fleet247.driver.fragment;


import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fleet247.driver.R;
import com.fleet247.driver.data.models.upcomingbooking.Booking;
import com.fleet247.driver.data.models.upcomingbooking.Passenger;
import com.fleet247.driver.utility.GsonStringConvertor;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentBookingFragment extends Fragment implements View.OnClickListener {

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
    LinearLayout passengerLay1;
    LinearLayout passengerLay2;
    LinearLayout passengerLay3;
    LinearLayout passengerLay4;
    LinearLayout passengerLay5;
    LinearLayout passengerLay6;
    boolean isExpended=false;

    Booking booking;

    FloatingActionButton floatingActionButton;

    View itemView;


    public CurrentBookingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        itemView= inflater.inflate(R.layout.fragment_current_booking, container, false);
        date=itemView.findViewById(R.id.travel_date);
        bookingId=itemView.findViewById(R.id.booking_id);
        pickupLocation=itemView.findViewById(R.id.pickup_location);
        dropLocation=itemView.findViewById(R.id.drop_location);
        noOfPassenger=itemView.findViewById(R.id.no_of_passenger);
        tripType=itemView.findViewById(R.id.trip_type);
        paymentType=itemView.findViewById(R.id.payment_status);
        floatingActionButton=itemView.findViewById(R.id.cancel_trip_floating_button);
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

        passengerLay1.setVisibility(View.GONE);
        passengerLay2.setVisibility(View.GONE);
        passengerLay3.setVisibility(View.GONE);
        passengerLay4.setVisibility(View.GONE);
        passengerLay5.setVisibility(View.GONE);
        passengerLay6.setVisibility(View.GONE);
        passengerDetails.setOnClickListener(this);
        floatingActionButton.setVisibility(View.GONE);

        booking= GsonStringConvertor.stringToGson(getArguments().getString("bookingInfo","n"),Booking.class);
        setData();
        return itemView;
    }

    void setData(){
        date.setText(booking.getPickupDatetime());
        bookingId.setText(booking.getBookingId());
        pickupLocation.setText(booking.getPickupLocation());
        dropLocation.setText(booking.getDropLocation());
        tripType.setText(booking.getTourType());
        noOfPassenger.setText(booking.getPassengers().size()+"");
        paymentType.setText("Prepaid");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.passenger_details:
                if (!isExpended) {
                    List<Passenger> passengers=new ArrayList<>();
                    passengers=booking.getPassengers();
                    isExpended = true;
                    switch (booking.getPassengers().size()) {
                        case 1:
                            passengerLay1.setVisibility(View.VISIBLE);
                            passengerName1.setText(passengers.get(0).peopleName);
                            passengerContact1.setText(passengers.get(0).peopleContact);
                            break;
                        case 2:
                            passengerLay1.setVisibility(View.VISIBLE);
                            passengerLay2.setVisibility(View.VISIBLE);
                            passengerName1.setText(passengers.get(0).peopleName);
                            passengerContact1.setText(passengers.get(0).peopleContact);
                            passengerName2.setText(passengers.get(1).peopleName);
                            passengerContact2.setText(passengers.get(1).peopleContact);
                            break;
                        case 3:
                            passengerLay1.setVisibility(View.VISIBLE);
                            passengerLay2.setVisibility(View.VISIBLE);
                            passengerLay3.setVisibility(View.VISIBLE);
                            passengerName1.setText(passengers.get(0).peopleName);
                            passengerContact1.setText(passengers.get(0).peopleContact);
                            passengerName2.setText(passengers.get(1).peopleName);
                            passengerContact2.setText(passengers.get(1).peopleContact);
                            passengerName3.setText(passengers.get(2).peopleName);
                            passengerContact3.setText(passengers.get(2).peopleContact);
                            break;
                        case 4:
                            passengerLay1.setVisibility(View.VISIBLE);
                            passengerLay2.setVisibility(View.VISIBLE);
                            passengerLay3.setVisibility(View.VISIBLE);
                            passengerLay4.setVisibility(View.VISIBLE);
                            passengerLay1.setVisibility(View.VISIBLE);
                            passengerLay2.setVisibility(View.VISIBLE);
                            passengerLay3.setVisibility(View.VISIBLE);
                            passengerName1.setText(passengers.get(0).peopleName);
                            passengerContact1.setText(passengers.get(0).peopleContact);
                            passengerName2.setText(passengers.get(1).peopleName);
                            passengerContact2.setText(passengers.get(1).peopleContact);
                            passengerName3.setText(passengers.get(2).peopleName);
                            passengerName4.setText(passengers.get(3).peopleName);
                            passengerContact4.setText(passengers.get(3).peopleContact);

                            break;
                        case 5:
                            passengerLay1.setVisibility(View.VISIBLE);
                            passengerLay2.setVisibility(View.VISIBLE);
                            passengerLay3.setVisibility(View.VISIBLE);
                            passengerLay4.setVisibility(View.VISIBLE);
                            passengerLay5.setVisibility(View.VISIBLE);

                            passengerName1.setText(passengers.get(0).peopleName);
                            passengerContact1.setText(passengers.get(0).peopleContact);
                            passengerName2.setText(passengers.get(1).peopleName);
                            passengerContact2.setText(passengers.get(1).peopleContact);
                            passengerName3.setText(passengers.get(2).peopleName);
                            passengerName4.setText(passengers.get(3).peopleName);
                            passengerContact4.setText(passengers.get(3).peopleContact);
                            passengerName5.setText(passengers.get(4).peopleName);
                            passengerContact5.setText(passengers.get(4).peopleContact);
                            break;
                        case 6:
                            passengerLay1.setVisibility(View.VISIBLE);
                            passengerLay2.setVisibility(View.VISIBLE);
                            passengerLay3.setVisibility(View.VISIBLE);
                            passengerLay4.setVisibility(View.VISIBLE);
                            passengerLay5.setVisibility(View.VISIBLE);
                            passengerLay6.setVisibility(View.VISIBLE);

                            passengerName1.setText(passengers.get(0).peopleName);
                            passengerContact1.setText(passengers.get(0).peopleContact);
                            passengerName2.setText(passengers.get(1).peopleName);
                            passengerContact2.setText(passengers.get(1).peopleContact);
                            passengerName3.setText(passengers.get(2).peopleName);
                            passengerName4.setText(passengers.get(3).peopleName);
                            passengerContact4.setText(passengers.get(3).peopleContact);
                            passengerName5.setText(passengers.get(4).peopleName);
                            passengerContact5.setText(passengers.get(4).peopleContact);
                            passengerName6.setText(passengers.get(5).peopleName);
                            passengerContact6.setText(passengers.get(5).peopleContact);
                            break;
                        default:
                            break;
                    }
                    passengerDetails.setText("Collapse");
                    passengerDetails.setCompoundDrawablesWithIntrinsicBounds(null, view.getContext().getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24px), null, null);
                } else {
                    isExpended = false;
                    passengerDetails.setText("Passenger Details");
                    passengerDetails.setCompoundDrawablesWithIntrinsicBounds(null, null, null, view.getContext().getDrawable(R.drawable.ic_keyboard_arrow_down_black_24px));
                    passengerLay1.setVisibility(View.GONE);
                    passengerLay2.setVisibility(View.GONE);
                    passengerLay3.setVisibility(View.GONE);
                    passengerLay4.setVisibility(View.GONE);
                    passengerLay5.setVisibility(View.GONE);
                    passengerLay6.setVisibility(View.GONE);
                }
                break;
            /*case R.id.start_trip_floating_button:
                Calendar calendar=Calendar.getInstance();
                Date currentDate=new Date(System.currentTimeMillis());
                Log.d("PickUpDateTime",booking.getPickupDatetime().trim());
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                try {
                    Date date=simpleDateFormat.parse(booking.getPickupDatetime());
                    Log.d("FormatedDate",simpleDateFormat.format(date));
                    if (date.after(currentDate)){
                        long diff=date.getTime()-currentDate.getTime();
                        long hours=diff/(60*60*1000);
                        int basehour;
                        Log.d("Tour Type",booking.getTourType());
                        if (booking.getTourType().equals("Local")){
                            basehour=4;
                        }
                        else {
                            basehour=24;
                        }
                        if (hours<=basehour){
                            Log.d("Start Booking","True");
                        }else {
                            Snackbar.make(itemView,"You can start trip "+basehour+" hours before Pickup Time",Snackbar.LENGTH_LONG).show();
                            Log.d("Start Booking","False");
                        }
                    }
                    else if (date.before(currentDate)){
                        Log.d("Start Booking","True");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }*/
        }
    }
}
