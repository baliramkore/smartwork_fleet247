package com.fleet247.driver.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.fleet247.driver.data.models.distancetime.EndDistanceTime;
import com.fleet247.driver.data.models.distancetime.EstimateDistanceTime;
import com.fleet247.driver.data.models.googledirection.GoogleDirectionApiResonse;
import com.fleet247.driver.data.models.googledistancematrix.DistanceMatrixApiResponse;
import com.fleet247.driver.data.models.googledistancematrix.Elements;
import com.fleet247.driver.retrofit.ConfigRetrofit;
import com.fleet247.driver.retrofit.GoogleAPI;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sandeep on 7/11/17.
 */

public class GoogleMapsDirectionRepository {

    MutableLiveData<String> polyLine;
    MutableLiveData<Elements> elements;
    MutableLiveData<String> distance;
    MutableLiveData<String> estimateTime;
    MutableLiveData<String> endDistance;
    MutableLiveData<String> endEstimateTime;
    MutableLiveData<EstimateDistanceTime> endEstimateDistanceTime;
    MutableLiveData<EstimateDistanceTime> journeyEstimateDistanceTime;
    MutableLiveData<EndDistanceTime> endDistanceTime;
    GoogleAPI googleAPI; //Retrofit API to get response from google servers.
    MutableLiveData<String> error; //error from googleAPI request.

    public GoogleMapsDirectionRepository(){
        polyLine=new MutableLiveData<>();
        elements=new MutableLiveData<>();
        distance=new MutableLiveData<>();
        estimateTime=new MutableLiveData<>();
        endDistance=new MutableLiveData<>();
        endEstimateTime=new MutableLiveData<>();
        endEstimateDistanceTime =new MutableLiveData<>();
        journeyEstimateDistanceTime=new MutableLiveData<>();
        endDistanceTime=new MutableLiveData<>();
        error=new MutableLiveData<>();
        googleAPI = ConfigRetrofit.configGoogleRetrofit(GoogleAPI.class);
    }

    public LiveData<String> getPolyLine() {
        return polyLine;
    }

    public LiveData<Elements> getElements() {
        return elements;
    }

    public LiveData<String> getDistance() {
        return distance;
    }

    public LiveData<String> getEstimateTime() {
        return estimateTime;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<String> getEndDistance() {
        return endDistance;
    }

    public MutableLiveData<EstimateDistanceTime> getEndEstimateDistanceTime() {
        return endEstimateDistanceTime;
    }

    public MutableLiveData<EstimateDistanceTime> getJourneyEstimateDistanceTime() {
        return journeyEstimateDistanceTime;
    }

    public MutableLiveData<EndDistanceTime> getEndDistanceTime() {
        return endDistanceTime;
    }

    public void getDirctions(LatLng pickup, LatLng drop, String key){
        String pickUp=+pickup.latitude+","+pickup.longitude;
        String dropLocation=+drop.latitude+","+drop.longitude;
        googleAPI.getDirections(pickUp,dropLocation,key).enqueue(new Callback<GoogleDirectionApiResonse>() {
            @Override
            public void onResponse(Call<GoogleDirectionApiResonse> call, Response<GoogleDirectionApiResonse> response) {
                if (response.isSuccessful() && response.body().getStatus().equals("OK") && response.body().getRoutes().size()>0){
                    polyLine.setValue(response.body().getRoutes().get(0).getOverviewPolyline().getPoints());
                }
            }

            @Override
            public void onFailure(Call<GoogleDirectionApiResonse> call, Throwable t) {

            }
        });
    }

    public void getDistanceTime(LatLng pickup,LatLng drop,String key){
        String pickUp=+pickup.latitude+","+pickup.longitude;
        String dropLocation=+drop.latitude+","+drop.longitude;
        googleAPI.getEstimateDistanceAndTime(pickUp,dropLocation,"now","pessimistic",key).enqueue(new Callback<DistanceMatrixApiResponse>() {
            @Override
            public void onResponse(Call<DistanceMatrixApiResponse> call, Response<DistanceMatrixApiResponse> response) {
                if (response.isSuccessful() && response.body().getStatus().equals("OK") && response.body().getRows().get(0).getElements().get(0).getStatus().equals("OK")){
                    elements.setValue(response.body().getRows().get(0).getElements().get(0));
                }
            }

            @Override
            public void onFailure(Call<DistanceMatrixApiResponse> call, Throwable t) {

            }
        });
    }

    public void getEstimateDistanceTime(LatLng origin,LatLng destination,String key){
        String pickUp=+origin.latitude+","+origin.longitude;
        String dropLocation=+destination.latitude+","+destination.longitude;
        googleAPI.getEstimateDistanceAndTime(pickUp, dropLocation,"now","pessimistic", key).enqueue(new Callback<DistanceMatrixApiResponse>() {
            @Override
            public void onResponse(Call<DistanceMatrixApiResponse> call, Response<DistanceMatrixApiResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus().equals("OK")){
                        distance.setValue(response.body().getRows().get(0).getElements().get(0).getDistance().getText());
                        estimateTime.setValue(response.body().getRows().get(0).getElements().get(0).getDurationInTraffic().getText());
                    }
                    else {
                        error.setValue("Connection Error");
                    }
                }
            }

            @Override
            public void onFailure(Call<DistanceMatrixApiResponse> call, Throwable t) {
                error.setValue("Connection Error: "+t.getMessage());
            }
        });
    }



    public void getEndEstimateDistanceTime(LatLng drop,LatLng startLocation,LatLng garageLocation,String key){
        String pickUp=startLocation.latitude+","+startLocation.longitude+"|"+garageLocation.latitude+","+garageLocation.longitude;
        String dropLocation=+drop.latitude+","+drop.longitude;
        googleAPI.getEstimateDistanceAndTime(pickUp, dropLocation,"now","pessimistic", key).enqueue(new Callback<DistanceMatrixApiResponse>() {
            @Override
            public void onResponse(Call<DistanceMatrixApiResponse> call, Response<DistanceMatrixApiResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus().equals("OK")){
                        String distance=String.format(Locale.US,"%.2f",response.body().getRows().get(0).getElements().get(0).getDistance().getValue()/1000);
                        String time=response.body().getRows().get(0).getElements().get(0).getDurationInTraffic().getText();
                        String garagedistance=response.body().getRows().get(1).getElements().get(0).getDistance().getText();
                        String garagetime=response.body().getRows().get(1).getElements().get(0).getDurationInTraffic().getText();
                        endDistanceTime.setValue(new EndDistanceTime(new EstimateDistanceTime(distance,time),new EstimateDistanceTime(garagedistance,garagetime)));
                    }
                    else {
                        error.setValue("Connection Error");
                    }
                }
            }

            @Override
            public void onFailure(Call<DistanceMatrixApiResponse> call, Throwable t) {
                error.setValue("Connection Error: "+t.getMessage());
            }
        });
    }


}
