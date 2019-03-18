package com.fleet247.driver.activities;

import androidx.lifecycle.ViewModelProviders;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fleet247.driver.R;
import com.fleet247.driver.viewmodels.CurrentBookingViewModel;
import com.fleet247.driver.viewmodels.DriverInfoViewModel;
import com.fleet247.driver.viewmodels.SignatureCaptureViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class SignatureCaptureActivity extends AppCompatActivity implements View.OnClickListener{

    GestureOverlayView signatureScreen;
    Button saveSign;
    ImageView signImage;
    Button clearButton;
    DriverInfoViewModel driverInfoViewModel;
    CurrentBookingViewModel currentBookingViewModel;
    SignatureCaptureViewModel signatureCaptureViewModel;
    EditText signatorySign;
    LinearLayout saveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_signature_capture);
        signatureScreen=findViewById(R.id.signature_screen);
        saveSign=findViewById(R.id.save_signature);
        signImage=findViewById(R.id.signature_image);
        clearButton=findViewById(R.id.clear_signature);
        signatorySign=findViewById(R.id.signatory);
        saveView =findViewById(R.id.save_layout);

        saveSign.setVisibility(View.GONE);
        clearButton.setVisibility(View.GONE);

        signatureScreen.setDrawingCacheEnabled(true);

        currentBookingViewModel=ViewModelProviders.of(this).get(CurrentBookingViewModel.class);

        driverInfoViewModel= ViewModelProviders.of(this).get(DriverInfoViewModel.class);

        signatureCaptureViewModel=ViewModelProviders.of(this).get(SignatureCaptureViewModel.class);

        signatureScreen.addOnGestureListener(new GestureOverlayView.OnGestureListener() {
            @Override
            public void onGestureStarted(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {

            }

            @Override
            public void onGesture(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {

            }

            @Override
            public void onGestureEnded(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {
                saveSign.setVisibility(View.VISIBLE);
                clearButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onGestureCancelled(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {

            }
        });

        saveSign.setOnClickListener(this);
        clearButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.save_signature:

                if(signatorySign.getText()!=null && signatorySign.getText().length()>1) {
                    Bitmap signature = Bitmap.createBitmap(signatureScreen.getDrawingCache());
                    File sign = new File(getExternalCacheDir(), "sign.jpeg");
                    FileOutputStream fileOutputStream = null;
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                    try {
                        fileOutputStream = new FileOutputStream(sign);
                        // signature.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                        signature.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        String image = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                        int i = 0;
                        Log.d("ImageSize", image.length() + " ");
               /* fileOutputStream.flush();
                fileOutputStream.close();
                sign.setReadable(true);
                sign.setWritable(true);
                Log.d("FilePath", sign.getAbsolutePath());
                GlideApp.with(getActivity())
                        .load(sign.getAbsolutePath())
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(signImage);*/
                        if (getIntent().getStringExtra("type").equals("start")){
                            currentBookingViewModel.saveStartSignatoryName(signatorySign.getText().toString());
                        }
                        else if (getIntent().getStringExtra("type").equals("end")){
                            currentBookingViewModel.saveEndSignatoryName(signatorySign.getText().toString());
                        }
                        currentBookingViewModel.saveSignature(image, getIntent().getStringExtra("type"));
                        signatureCaptureViewModel.setSignature(currentBookingViewModel.getCurrentBooking().getValue().getBookingId(),
                                getIntent().getStringExtra("type"),
                                currentBookingViewModel.getCurrentBooking().getValue().getBookingType(),
                                image);
                        signatureCaptureViewModel.getSignature();
                        setResult(RESULT_OK, getIntent());
                        Log.d("SignatureCapture","Activity");
                        finish();

                    } catch (Exception e) {

                    }
                }
                else {
                    Toast.makeText(this,getString(R.string.signatory_unfilled_error),Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.clear_signature:
                signatureScreen.cancelClearAnimation();
                signatureScreen.clear(true);
                clearButton.setVisibility(View.GONE);
                saveSign.setVisibility(View.GONE);
        }
    }
}
