package com.fleet247.driver.fragment;


import androidx.lifecycle.ViewModelProviders;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.fleet247.driver.R;
import com.fleet247.driver.retrofit.ConfigRetrofit;
import com.fleet247.driver.retrofit.UploadSignature;
import com.fleet247.driver.viewmodels.DriverInfoViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignatureCapture extends Fragment implements View.OnClickListener{

    View view;
    GestureOverlayView signatureScreen;
    Button saveSign;
    ImageView signImage;
    Button clearButton;
    DriverInfoViewModel driverInfoViewModel;

    public SignatureCapture() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_signature_capture, container, false);
        signatureScreen=view.findViewById(R.id.signature_screen);
        saveSign=view.findViewById(R.id.save_signature);
        signImage=view.findViewById(R.id.signature_image);
        clearButton=view.findViewById(R.id.clear_signature);

        saveSign.setVisibility(View.GONE);
        clearButton.setVisibility(View.GONE);

        signatureScreen.setDrawingCacheEnabled(true);

        driverInfoViewModel= ViewModelProviders.of(this).get(DriverInfoViewModel.class);

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
        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.save_signature:  File singJpeg = new File("/storage/emulated/0/Android/data/com.fleet247.driver/cache/sign.jpeg");
            if (singJpeg.exists()) {
                if (singJpeg.delete()) {
                    Log.d("File", "Deleted");
                } else {
                    Log.d("File", "Not Deleted");
                }
            } else {
                Log.d("File", "Do not Exist");
            }

            Bitmap signature = Bitmap.createBitmap(signatureScreen.getDrawingCache());
            File sign = new File(getActivity().getExternalCacheDir(), "sign.jpeg");
            FileOutputStream fileOutputStream = null;
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();

            try {
                fileOutputStream = new FileOutputStream(sign);
               // signature.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                signature.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                String image=Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT);
                int i=0;
                Log.d("ImageSize",image.length()+" ");
               /* fileOutputStream.flush();
                fileOutputStream.close();
                sign.setReadable(true);
                sign.setWritable(true);
                Log.d("FilePath", sign.getAbsolutePath());
                GlideApp.with(getActivity())
                        .load(sign.getAbsolutePath())
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(signImage);
                UploadSignature uploadSignature= ConfigRetrofit.configRetrofit(UploadSignature.class);
                uploadSignature.storeSignature(driverInfoViewModel.getAccessToken().getValue(),
                        "34",
                        "start",
                        image).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.code()==204){
                            Log.d("Signature","Saved");
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
                */
                signImage.setVisibility(View.VISIBLE);
                saveSign.setVisibility(View.GONE);
                clearButton.setVisibility(View.GONE);
            } catch (Exception e) {

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
