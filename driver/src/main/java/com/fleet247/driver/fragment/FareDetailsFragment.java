package com.fleet247.driver.fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fleet247.driver.R;
import com.fleet247.driver.data.models.archivedbooking.Booking;
import com.fleet247.driver.data.models.archivedbooking.InvoiceDetails;
import com.fleet247.driver.utility.GsonStringConvertor;

/**
 * A simple {@link Fragment} subclass.
 */
public class FareDetailsFragment extends Fragment {

    Booking booking;
    View view;
    TextView bookingId;
    TextView date;
    TextView fromCity;
    TextView toCity;
    TextView distanceTravelled;
    TextView timeTaken;
    TextView others;
    TextView packageCost;
    TextView extraKmCharges;
    TextView extraTimeCharges;
    TextView parking;
    TextView tollTax;
    TextView stateTax;
    TextView gst;
    TextView total;
    TextView advanceTaken;
    TextView amountToBeTaken;
    TextView amountToBeTakenText;
    TextView driverAllowance;

    public FareDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_fare_details, container, false);
        bookingId=view.findViewById(R.id.booking_id_value);
        date=view.findViewById(R.id.pickup_date_value);
        fromCity=view.findViewById(R.id.from_value);
        toCity=view.findViewById(R.id.to_value);
        distanceTravelled=view.findViewById(R.id.total_distance_value);
        timeTaken=view.findViewById(R.id.total_time_value);
        others=view.findViewById(R.id.others_value);
        packageCost=view.findViewById(R.id.package_cost_value);
        extraKmCharges=view.findViewById(R.id.extra_km_charge_value);
        extraTimeCharges=view.findViewById(R.id.extra_time_charge_value);
        parking=view.findViewById(R.id.parking_value);
        tollTax=view.findViewById(R.id.toll_tax_value);
        stateTax =view.findViewById(R.id.state_tax_value);
        gst=view.findViewById(R.id.gst_value);
        total=view.findViewById(R.id.total_value);
        advanceTaken =view.findViewById(R.id.advance_taken_value);
        amountToBeTaken=view.findViewById(R.id.amount_to_be_taken_from_guest_value);
        amountToBeTakenText=view.findViewById(R.id.amount_to_be_taken_from_guest_text);
        driverAllowance=view.findViewById(R.id.driver_allowance_value);

        booking=GsonStringConvertor.stringToGson(getArguments().getString("booking",null),Booking.class);

        if (booking==null){
            getActivity().getSupportFragmentManager().popBackStack();
        }

        amountToBeTakenText.setText(getString(R.string.amount_taken_from_guest_text));
        bookingId.setText(booking.getReferenceNo());
        fromCity.setText(booking.getPickupLocation());
        toCity.setText(booking.getDropLocation());

        InvoiceDetails  invoiceDetails=booking.getInvoiceDetails();
        if (invoiceDetails !=null) {
            String[] dateTime=invoiceDetails.getPickupDate().split(" ");
            Log.d("Date",GsonStringConvertor.gsonToString(dateTime));
            date.setText(dateTime[0] +" "+invoiceDetails.getPickupTime());
            distanceTravelled.setText(invoiceDetails.getKmsDone()+" km");
            if (booking.getTourType().equals("Local")) {
                timeTaken.setText(invoiceDetails.getHrsDone() + " hr");
            }
            else if (booking.getTourType().equals("Outstation")){
                timeTaken.setText(invoiceDetails.getHrsDone() + " day");
            }
            packageCost.setText("₹ "+invoiceDetails.getBaseRate());
            extraKmCharges.setText("₹ "+invoiceDetails.getExtraKmsCharge());
            extraTimeCharges.setText("₹ "+invoiceDetails.getExtraHrsCharge());
            parking.setText("₹ "+invoiceDetails.getParking());
            tollTax.setText("₹ "+invoiceDetails.getTollTax());
            stateTax.setText("₹ "+invoiceDetails.getStateTax());
            others.setText("₹ "+invoiceDetails.getExtras());
            driverAllowance.setText("₹ "+invoiceDetails.getDriverAllowance());
            gst.setText("₹ "+invoiceDetails.getIgst());
            total.setText("₹ "+invoiceDetails.getTotalAfterTax());
            advanceTaken.setText("₹ "+invoiceDetails.getTotalAfterTax());
            amountToBeTaken.setText("₹ 0");
        }
        return view;
    }

}
