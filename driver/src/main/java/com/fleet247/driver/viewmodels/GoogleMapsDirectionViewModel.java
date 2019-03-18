package com.fleet247.driver.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.fleet247.driver.data.models.distancetime.EndDistanceTime;
import com.fleet247.driver.data.models.distancetime.EstimateDistanceTime;
import com.fleet247.driver.data.models.googledistancematrix.Elements;
import com.fleet247.driver.data.repository.GoogleMapsDirectionRepository;

/**
 * Created by sandeep on 7/11/17.
 */

public class GoogleMapsDirectionViewModel extends AndroidViewModel{
    GoogleMapsDirectionRepository googleMapsDirectionRepository;
    LiveData<String> polyLine;
    LiveData<Elements> elements;
    LiveData<String> distance;
    LiveData<String> estimateTime;
    LiveData<String> error;
    LiveData<String> endDistance;
    LiveData<EstimateDistanceTime> estimateDistanceTime;
    LiveData<EndDistanceTime> endDistanceTime;

    public GoogleMapsDirectionViewModel(@NonNull Application application) {
        super(application);
        googleMapsDirectionRepository=new GoogleMapsDirectionRepository();
        polyLine=googleMapsDirectionRepository.getPolyLine();
        elements=googleMapsDirectionRepository.getElements();
        distance=googleMapsDirectionRepository.getDistance();
        estimateTime=googleMapsDirectionRepository.getEstimateTime();
        error=googleMapsDirectionRepository.getError();
        endDistance=googleMapsDirectionRepository.getEndDistance();
        estimateDistanceTime=googleMapsDirectionRepository.getEndEstimateDistanceTime();
        endDistanceTime=googleMapsDirectionRepository.getEndDistanceTime();
    }

    public LiveData<String> getPolyLine() {
        return polyLine;
    }

    public void getDirection(LatLng pickup,LatLng drop,String key){
        googleMapsDirectionRepository.getDirctions(pickup,drop,key);
    }

    public LiveData<EstimateDistanceTime> getEstimateDistanceTime() {
        return estimateDistanceTime;
    }

    public LiveData<Elements> getElements() {
        return elements;
    }

    public void getEstimateDistanceTime(LatLng pickUp,LatLng drop,String key){
        googleMapsDirectionRepository.getEstimateDistanceTime(pickUp,drop,key);
    }

    public LiveData<EndDistanceTime> getEndDistanceTime() {
        return endDistanceTime;
    }

    public void getEndEstimateDistanceTime(LatLng dropLocation, LatLng startLocation, LatLng garageLocation, String key){
        googleMapsDirectionRepository.getEndEstimateDistanceTime(dropLocation,startLocation,garageLocation,key);
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
}
