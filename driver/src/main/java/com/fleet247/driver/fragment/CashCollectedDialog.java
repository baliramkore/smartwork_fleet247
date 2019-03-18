package com.fleet247.driver.fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.fleet247.driver.R;
import com.fleet247.driver.viewmodels.BillingDetailsViewModels;

public class CashCollectedDialog  extends DialogFragment implements View.OnClickListener{

    View view;
    EditText cash;
    Button cashReceived;
    BillingDetailsViewModels billingDetailsViewModels;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.cash_received_dialog,container,false);
        billingDetailsViewModels= ViewModelProviders.of(getActivity()).get(BillingDetailsViewModels.class);
        cash=view.findViewById(R.id.cash_collected);
        cashReceived=view.findViewById(R.id.payment_received);

        cashReceived.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (cash.getText()!=null && cash.getText().toString().length()>1){
            billingDetailsViewModels.setCashCollected(cash.getText().toString());
            dismiss();
        }
        else {
            Snackbar.make(view,"Please Enter Collected Cash Amount",Snackbar.LENGTH_LONG).show();
        }
    }
}
