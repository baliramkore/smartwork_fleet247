package com.fleet247.driver.data.repository;

import android.app.Application;
import android.util.Log;

import com.fleet247.driver.data.models.Signature;
import com.fleet247.driver.data.room.DriverAppDatabase;
import com.fleet247.driver.data.room.dao.SignatureDAO;
import com.fleet247.driver.utility.GsonStringConvertor;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SignatureRepository {

    public static SignatureRepository INSTANCE; //SignatureRepository object to make singleton pattern
    private SignatureDAO signatureDAO;  // SignatureDAO object to save and retrieve Signature.
    private Executor saveSignatureExecutor; // Executor to save Signature in database in different thread.
    private Executor deleteSignatureExecutor; // Executor to delete saved Signature from database in different thread.
    private Executor getSignature; // Executor to get saved signature from database in different thread.

    private SignatureRepository(Application application){
        signatureDAO= DriverAppDatabase.getDriverDatabase(application).signatureDAO();
        saveSignatureExecutor= Executors.newSingleThreadExecutor();
        deleteSignatureExecutor=Executors.newSingleThreadExecutor();
        getSignature=Executors.newSingleThreadExecutor();
    }

    public static SignatureRepository getInstance(Application application){
        if (INSTANCE==null){
            INSTANCE=new SignatureRepository(application);
        }
        return INSTANCE;
    }

    /**
     * Save signature locally
     * @param bookingId bookingId for which signature is taken.
     * @param type when signature is taken (during start or end of booking)
     * @param bookingType booking Type
     * @param encodedSignatureImage encoded signature Image
     */
    public void saveSignature(String bookingId,String type,String bookingType,String encodedSignatureImage){
        signatureDAO.addSignature(new Signature(bookingId,type,bookingType, Calendar.getInstance().getTimeInMillis(),encodedSignatureImage));
    }

    /**
     *
     * @return Signature object
     */
    public Signature getSignature(){
        Log.d("Signature", GsonStringConvertor.gsonToString(signatureDAO.getSignature()));
        return signatureDAO.getSignature();
    }

    /**
     * Delete signature from database using bookingId and type.
     * @param bookingId bookingId of signature to be deleted.
     * @param type when signature is taken (during start or end of booking)
     */
    public void deleteSignature(String bookingId, String type){
        signatureDAO.deleteSignature(bookingId, type);
    }

    /**
     *
     * @return list of all Signatures stored in database.
     */
    public List<Signature> getAllSignatures(){
        return signatureDAO.getAllSignature();
    }
}
