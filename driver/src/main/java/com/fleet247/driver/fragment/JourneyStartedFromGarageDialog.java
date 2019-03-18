package com.fleet247.driver.fragment;


import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.fleet247.driver.R;

/**
 * Created by sandeep on 22/1/18.
 */

public class JourneyStartedFromGarageDialog extends DialogFragment implements OnClickListener {

    View view;
    EditText startKms;
    Button startFromGaragebutton;
    Button back;
    OnStartFromGarageClicked onStartFromGarageClicked;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.start_from_garage_dialog,container,false);
        startKms=view.findViewById(R.id.start_km_garage);
        startFromGaragebutton =view.findViewById(R.id.startJourneyFromGarage);
        back=view.findViewById(R.id.button_back);
        startFromGaragebutton.setOnClickListener(this);
        back.setOnClickListener(this);
        return view;
    }

    public void registerOnStartFromGarageClicked(OnStartFromGarageClicked onStartFromGarageClicked){
        this.onStartFromGarageClicked=onStartFromGarageClicked;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startJourneyFromGarage:
                if (startKms.getText() != null && startKms.getText().toString().length() > 0) {
                    onStartFromGarageClicked.onStartFromGarageClicked(startKms.getText().toString());
                    this.dismiss();
                } else {
                    Snackbar.make(view, "Start Km is Required Field", Snackbar.LENGTH_LONG).show();
                }
                break;
            case R.id.button_back:
                dismiss();
                getActivity().finish();
        }
    }

    public interface OnStartFromGarageClicked{
        void onStartFromGarageClicked(String startKm);
    }
}
