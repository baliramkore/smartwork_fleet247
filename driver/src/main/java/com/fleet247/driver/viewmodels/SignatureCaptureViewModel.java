package com.fleet247.driver.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;

import com.fleet247.driver.data.models.Signature;
import com.fleet247.driver.data.repository.SignatureRepository;

import java.util.List;

public class SignatureCaptureViewModel extends AndroidViewModel{
    private SignatureRepository signatureRepository;
    public SignatureCaptureViewModel(@NonNull Application application) {
        super(application);
        signatureRepository=SignatureRepository.getInstance(application);
    }

    public void setSignature(String bookingId,String type,String bookingType,String encodedImage){
        signatureRepository.saveSignature(bookingId,bookingType, type, encodedImage);
    }

    public Signature getSignature(){
        return signatureRepository.getSignature();
    }

    public List<Signature> getAllSignature(){
        return signatureRepository.getAllSignatures();
    }

    public void deleteSignature(String bookingId,String type){
        signatureRepository.deleteSignature(bookingId,type);
    }

}
