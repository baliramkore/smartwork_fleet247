package com.fleet247.driver.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fleet247.driver.R;
import com.fleet247.driver.adapter.CallUserAdapter;
import com.fleet247.driver.data.models.upcomingbooking.Booking;
import com.fleet247.driver.utility.GsonStringConvertor;

/**
 * Created by sandeep on 29/1/18.
 */

public class PhoneBottomSheet extends BottomSheetDialogFragment implements CallUserAdapter.CallUserEventListener,
        View.OnClickListener{

    View view;
    RecyclerView recyclerView;
    CallUserAdapter callUserAdapter;
    Booking booking;
    TextView bookingId;
    TextView pickupLocation;
    TextView tripType;
    TextView vehicleType;
    TextView paymentType;

    TextView contactSupport;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.bottomsheet_phone,container,false);
        recyclerView=view.findViewById(R.id.phone_bottomsheet_recyclerview);
        bookingId=view.findViewById(R.id.booking_id_value);
        pickupLocation=view.findViewById(R.id.pickup_location_value);
        tripType=view.findViewById(R.id.package_type_value);
        vehicleType=view.findViewById(R.id.vehicle_type_value);
        paymentType=view.findViewById(R.id.payment_type_value);

        contactSupport=view.findViewById(R.id.contact_support);
        Bundle bundle=getArguments();
        booking= GsonStringConvertor.stringToGson(bundle.getString("bookingInfo"),Booking.class);
        callUserAdapter=new CallUserAdapter(this,booking.getPassengers());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(callUserAdapter);

        contactSupport.setOnClickListener(this);


        bookingId.setText(booking.getReferenceNo());
        pickupLocation.setText(booking.getPickupLocation());
        tripType.setText(booking.getTourType()+"|"+booking.getPackageName());
        vehicleType.setText(booking.getTaxiModel());
        paymentType.setText(booking.getPaymentType());
        return view;
    }

    @Override
    public void onCallClicked(String contactNo) {
      Log.d("Event",contactNo);
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+91"+contactNo));
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.contact_support:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+918030656503"));
                startActivity(intent);
        }
    }
}
