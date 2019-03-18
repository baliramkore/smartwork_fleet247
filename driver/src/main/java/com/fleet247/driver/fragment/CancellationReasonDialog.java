package com.fleet247.driver.fragment;


import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.fleet247.driver.R;

public class CancellationReasonDialog extends DialogFragment implements View.OnClickListener {

    View view;
    EditText cancellationReason;
    Button confirmCancel;
    CancelBookingListener cancelBookingListener;


    public void addCancelBookingListener(CancelBookingListener cancelBookingListener){
        this.cancelBookingListener=cancelBookingListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.cancellation_reason_dialog,container,false);
        cancellationReason=view.findViewById(R.id.cancel_reason);
        confirmCancel=view.findViewById(R.id.confirm_cancel);
        confirmCancel.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (cancellationReason.getText()!=null && cancellationReason.getText().toString().length()>0) {
            cancelBookingListener.onConfirmCancelClicked(cancellationReason.getText().toString());
            dismiss();
        }
        else {
            Snackbar.make(view,"Please Enter Cancellation Reason",Snackbar.LENGTH_LONG).show();
        }
    }

    public interface CancelBookingListener{
        void onConfirmCancelClicked(String cancellationReason);
    }
}
