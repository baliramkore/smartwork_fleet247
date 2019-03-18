package com.fleet247.driver.fragment;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fleet247.driver.R;
import com.fleet247.driver.activities.HomeActivity;
import com.fleet247.driver.data.models.endride.InvoiceDetails;
import com.fleet247.driver.data.models.upcomingbooking.Booking;
import com.fleet247.driver.utility.GsonStringConvertor;
import com.fleet247.driver.viewmodels.BillingDetailsViewModels;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookingCompletedFragment extends Fragment implements View.OnClickListener{


    public BookingCompletedFragment() {
        // Required empty public constructor
    }

    View view;
    View toastView;
    View toastErrorView;
    Booking booking;
    InvoiceDetails invoiceDetails;
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
    TextView driverAllowance;
    TextView gst;
    TextView total;
    TextView advanceTaken;
    TextView amountToBeTaken;

    TextView toBeCollectedAmount;
    TextView callOperator;


    TextView otherText;
    TextView packageCostText;
    TextView extraKmChargeText;
    TextView extraTimeChargeText;
    TextView parkingText;
    TextView tollTaxText;
    TextView stateTaxText;
    TextView driverAllowanceText;
    TextView gstText;
    TextView totalText;
    TextView advanceTakenText;

    TextView bookingSuccessfulMessage;
    TextView errorMessage;


    Button ok;

    BillingDetailsViewModels billingDetailsViewModels;

    String phoneNo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.green));

        billingDetailsViewModels= ViewModelProviders.of(getActivity()).get(BillingDetailsViewModels.class);

        view=inflater.inflate(R.layout.fragment_booking_completed,container,false);
        if (billingDetailsViewModels.isBookingBillingPending() ) {
            booking=billingDetailsViewModels.getBookingDetails();
            invoiceDetails=billingDetailsViewModels.getInvoiceDetails();
        }
        else {
            booking = GsonStringConvertor.stringToGson(getArguments().getString("booking"), Booking.class);
            invoiceDetails = GsonStringConvertor.stringToGson(getArguments().getString("invoiceDetails"), InvoiceDetails.class);
        }

        toastView=inflater.inflate(R.layout.toast_success_view,(ViewGroup)view.findViewById(R.id.toast_layout_success));
        toastErrorView=inflater.inflate(R.layout.toast_layout_error,(ViewGroup)view.findViewById(R.id.toast_layout_error));

        bookingId=view.findViewById(R.id.booking_id_value);
        date=view.findViewById(R.id.pickup_date_value);
        fromCity=view.findViewById(R.id.from_value);
        toCity=view.findViewById(R.id.to_value);
        ok=view.findViewById(R.id.btn_ok);
        distanceTravelled=view.findViewById(R.id.total_distance_value);
        timeTaken=view.findViewById(R.id.total_time_value);
        toBeCollectedAmount=view.findViewById(R.id.to_be_collected_amount);
        others=view.findViewById(R.id.others_value);
        packageCost=view.findViewById(R.id.package_cost_value);
        extraKmCharges=view.findViewById(R.id.extra_km_charge_value);
        extraTimeCharges=view.findViewById(R.id.extra_time_charge_value);
        parking=view.findViewById(R.id.parking_value);
        tollTax=view.findViewById(R.id.toll_tax_value);
        stateTax =view.findViewById(R.id.state_tax_value);
        driverAllowance=view.findViewById(R.id.driver_allowance_value);
        gst=view.findViewById(R.id.gst_value);
        total=view.findViewById(R.id.total_value);
        advanceTaken =view.findViewById(R.id.advance_taken_value);
        amountToBeTaken=view.findViewById(R.id.amount_to_be_taken_from_guest_value);
        callOperator=view.findViewById(R.id.call_operator_billing);

        otherText=view.findViewById(R.id.others_text);
        packageCostText=view.findViewById(R.id.package_cost_text);
        extraKmChargeText=view.findViewById(R.id.extra_km_charge_text);
        extraTimeChargeText=view.findViewById(R.id.extra_time_charge_text);
        parkingText=view.findViewById(R.id.parking_text);
        packageCostText=view.findViewById(R.id.package_cost_text);
        tollTaxText=view.findViewById(R.id.toll_tax_text);
        stateTaxText=view.findViewById(R.id.state_tax_text);
        driverAllowanceText=view.findViewById(R.id.driver_allowance_text);
        gstText=view.findViewById(R.id.gst_text);
        totalText=view.findViewById(R.id.total_text);
        advanceTakenText=view.findViewById(R.id.advance_taken_text);
        bookingSuccessfulMessage=toastView.findViewById(R.id.toast_success_text);
        errorMessage=toastErrorView.findViewById(R.id.toast_error_message);

        advanceTaken.setVisibility(View.GONE);
        advanceTakenText.setVisibility(View.GONE);

        //View view=getLayoutInflater().inflate(R.layout.toast_success_view)

        bookingId.setText(booking.getReferenceNo());
        fromCity.setText(booking.getPickupLocation());
        toCity.setText(booking.getDropLocation());
        toBeCollectedAmount.setText("₹ "+String.format("%.2f",Double.parseDouble(invoiceDetails.getAmountToBeCollected())));
        amountToBeTaken.setText("₹ "+String.format("%.2f",Double.parseDouble(invoiceDetails.getAmountToBeCollected())));
        Log.d("TripDetails",GsonStringConvertor.gsonToString(invoiceDetails));
        if (booking.getBookingType().equalsIgnoreCase("Prepaid")){
            others.setVisibility(View.GONE);
            packageCost.setVisibility(View.GONE);
            extraKmCharges.setVisibility(View.GONE);
            parking.setVisibility(View.GONE);
            tollTax.setVisibility(View.GONE);
            stateTax.setVisibility(View.GONE);
            others.setVisibility(View.GONE);
            driverAllowance.setVisibility(View.GONE);
            gst.setVisibility(View.GONE);
            total.setVisibility(View.GONE);
            advanceTaken.setVisibility(View.GONE);

            otherText.setVisibility(View.GONE);
            packageCostText.setVisibility(View.GONE);
            extraKmChargeText.setVisibility(View.GONE);
            parkingText.setVisibility(View.GONE);
            tollTaxText.setVisibility(View.GONE);
            stateTaxText.setVisibility(View.GONE);
            driverAllowanceText.setVisibility(View.GONE);
            gstText.setVisibility(View.GONE);
            totalText.setVisibility(View.GONE);
            advanceTakenText.setVisibility(View.GONE);
            callOperator.setVisibility(View.GONE);
            //advanceTaken.setText("₹ "+invoiceDetails.getTotalAfterTax());
        }
        if (invoiceDetails !=null) {
            String[] dateTime=invoiceDetails.getPickupDate().split(" ");
            Log.d("Date",GsonStringConvertor.gsonToString(dateTime));

            if (Double.parseDouble(invoiceDetails.getAmountToBeCollected())>0){
                ok.setText(R.string.enter_cash_collected);
            }
            date.setText(dateTime[0] +" "+invoiceDetails.getPickupTime());
            distanceTravelled.setText(invoiceDetails.getKmsDone()+"km");
            timeTaken.setText(invoiceDetails.getHrsDone()+"hr");
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
        }
        ok.setOnClickListener(this);
        callOperator.setOnClickListener(this);

        billingDetailsViewModels.getStatus().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Toast toast=new Toast(getActivity().getApplicationContext());
                toast.setGravity(Gravity.CENTER,0,300);
                toast.setDuration(Toast.LENGTH_LONG);
                if (s!=null && s.equals("Booking Successful")){
                    Log.d("Booking","Successful");
                    billingDetailsViewModels.setStatus(null);
                    bookingSuccessfulMessage.setText("Booking Completed Successfully");
                    toast.setView(toastView);
                    toast.show();
                    startActivity(new Intent(getActivity(), HomeActivity.class));
                    getActivity().finish();
                }
                else if (s!=null && s.equals("Error")){
                    billingDetailsViewModels.setStatus(null);
                    errorMessage.setText("Connection Error");
                    toast.setView(toastErrorView);
                    toast.show();
                }
            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                if (invoiceDetails.getAmountToBeCollected()!=null && Double.parseDouble(invoiceDetails.getAmountToBeCollected())>0) {
                    CashCollectedDialog cashCollectedDialog = new CashCollectedDialog();
                    cashCollectedDialog.show(getActivity().getSupportFragmentManager(), "Cash");
                } else {
                    billingDetailsViewModels.setCashCollected("0");
                }
                break;

            case R.id.call_operator_billing:
                if (billingDetailsViewModels.getOperatorNumber()!=null && billingDetailsViewModels.getOperatorNumber().length()>5){
                    phoneNo=billingDetailsViewModels.getOperatorNumber();
                }
                else {
                    phoneNo="+918030656503";
                }
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNo));
                startActivity(intent);

        }
    }
}
