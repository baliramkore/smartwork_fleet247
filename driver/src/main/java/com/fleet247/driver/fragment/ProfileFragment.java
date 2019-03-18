package com.fleet247.driver.fragment;


import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fleet247.driver.R;
import com.fleet247.driver.activities.LoginActivity;
import com.fleet247.driver.glide.GlideApp;
import com.fleet247.driver.viewmodels.DriverInfoViewModel;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener{



    ImageView profilePicture;
    ImageView editProfilePicture;
    View view;
    TextView driverName;
    TextView driverContactNo;
    TextView driverEmailId;
    TextView driverlicenceNo;
    Button logoutBtn;
    private static final int PICK_IMAGE=100;
    DriverInfoViewModel driverInfoViewModel;
    View successToastView;
    TextView toastSuccess;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_profile, container, false);
        profilePicture=view.findViewById(R.id.profile_pic);
        editProfilePicture=view.findViewById(R.id.edit_profile_pic);
        driverName=view.findViewById(R.id.driver_name);
        driverContactNo=view.findViewById(R.id.phone_no);
        driverEmailId=view.findViewById(R.id.email_id);
        driverlicenceNo=view.findViewById(R.id.licence_no);
        logoutBtn=view.findViewById(R.id.logout);
        successToastView=inflater.inflate(R.layout.toast_success_view,(ViewGroup)view.findViewById(R.id.toast_layout_success));
        toastSuccess=successToastView.findViewById(R.id.toast_success_text);

        editProfilePicture.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);

        driverInfoViewModel= ViewModelProviders.of(getActivity()).get(DriverInfoViewModel.class);

        driverName.setText(driverInfoViewModel.getDriver().getValue().driverName);
        if (driverInfoViewModel.getDriver().getValue().driverContact!=null &&
                driverInfoViewModel.getDriver().getValue().driverContact.length()>0) {
            driverContactNo.setText(driverInfoViewModel.getDriver().getValue().driverContact);
        }
        else {
            driverContactNo.setText("-Unavailable");
        }
        if (driverInfoViewModel.getDriver().getValue().driverEmail!=null &&
                driverInfoViewModel.getDriver().getValue().driverEmail.length()>0) {
            driverEmailId.setText(driverInfoViewModel.getDriver().getValue().driverEmail);
        }
        else {
            driverEmailId.setText("-Unavailable");
        }

        if (driverInfoViewModel.getDriver().getValue().getLicenceNo()!=null &&
                driverInfoViewModel.getDriver().getValue().getLicenceNo().length()>3){
            driverlicenceNo.setText(driverInfoViewModel.getDriver().getValue().getLicenceNo());
        }
        else {
            driverlicenceNo.setText("-Unavailable");
        }

        if(driverInfoViewModel.hasCurrentBooking()){
            logoutBtn.setEnabled(false);
        }

        GlideApp
                .with(getActivity())
                .load("https://lh3.googleusercontent.com/TOkhqGlCVCc0kFY2afJQC0NKLvkgpX_qPZsALeivItQJozsNrkhRLEYTOdfGDVU5OxTNpw=s85")
                .placeholder(R.drawable.ic_user)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.action_button)
                .into(profilePicture);

        driverInfoViewModel.getLogoutStatus().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
                if (s.equals("successful")){
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    Toast.makeText(getActivity(),"Logout Successful",Toast.LENGTH_LONG).show();
                    Toast toast=new Toast(getActivity().getApplicationContext());
                    toast.setGravity(Gravity.BOTTOM,0,300);
                    toastSuccess.setText("Logout Successful");
                    toast.setView(successToastView);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.show();
                    getActivity().finish();
                }

            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edit_profile_pic:
                Log.d("Profile Pic","Updated");
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                break;
            case R.id.logout:
                driverInfoViewModel.performLogout(driverInfoViewModel.getAccessToken().getValue());
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_IMAGE:
                try {
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    profilePicture.setImageBitmap(BitmapFactory.decodeStream(inputStream, new Rect(profilePicture.getMaxHeight(), profilePicture.getMaxWidth(), profilePicture.getMaxHeight(), profilePicture.getMaxWidth()), options));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
        }
    }
}
