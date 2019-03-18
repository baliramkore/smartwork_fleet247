package com.fleet247.driver.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.fleet247.driver.R;
import com.fleet247.driver.fragment.CurrentBookingFragment;


//This Activity is To handle Show booking Details when a new Upcoming booking notification is received by Driver.
public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        String details=getIntent().getStringExtra("bookingInfo");
        CurrentBookingFragment currentBookingFragment=new CurrentBookingFragment();
        Bundle bundle=new Bundle();
        bundle.putString("bookingInfo",details);
        bundle.putBoolean("fromNotification",true);
        currentBookingFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.details_container,currentBookingFragment)
                .commit();
    }
}
