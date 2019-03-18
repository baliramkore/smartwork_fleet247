package com.fleet247.driver.utility;

import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.util.Log;

import com.fleet247.driver.retrofit.UploadSignature;
import com.fleet247.driver.services.NetworkChangeJobService;
import com.fleet247.driver.services.UpdateTravelledPathJobService;
import com.fleet247.driver.services.UploadEndSignatureJobService;
import com.fleet247.driver.services.UploadSignatureJobService;
import com.fleet247.driver.services.UploadSignaturesJobService;

public class JobSchedularUtility {

    private static final int JOB_ID=111;
    private static JobScheduler jobScheduler;
    private static final int UPDATE_POLYLINE_JOB_ID=112;
    private static final int UPLOAD_SIGNATURE_START_JOB_ID=113;
    private static final int UPLOAD_SIGNATURE_END_JOB_ID=114;
    private static final int UPLOAD_SIGNATURES_JOB_ID=115;

    private static void createSchedular(Application application){
        if (jobScheduler==null) {
            jobScheduler = (JobScheduler) application.getSystemService(application.JOB_SCHEDULER_SERVICE);
        }
    }

    public static void scheduleNetworkChangeJob(Application application){
        createSchedular(application);
        ComponentName componentName = new ComponentName(application, NetworkChangeJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, componentName);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPeriodic(15 * 60 * 1000);
        builder.setPersisted(true);

        int status=jobScheduler.schedule(builder.build());
        if (status==JobScheduler.RESULT_SUCCESS){
            Log.d("Service","Scheduled");
        }
    }

    public static void deleteNetworkChangeJob(Application application){
        createSchedular(application);
        jobScheduler.cancel(JOB_ID);
    }

    public static void scheduleUpdatePolylineChangeJob(Application application){
        createSchedular(application);
        ComponentName componentName=new ComponentName(application, UpdateTravelledPathJobService.class);
        JobInfo.Builder builder=new JobInfo.Builder(UPDATE_POLYLINE_JOB_ID,componentName);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPeriodic(15*60*1000);
        builder.setPersisted(true);
        int status=jobScheduler.schedule(builder.build());
        if (status==JobScheduler.RESULT_SUCCESS){
            Log.d("Service","Scheduled");
        }

    }

    public static void scheduleUploadStartSignatureJob(Application application){
        createSchedular(application);
        ComponentName componentName=new ComponentName(application, UploadSignatureJobService.class);
        JobInfo.Builder builder=new JobInfo.Builder(UPLOAD_SIGNATURE_START_JOB_ID,componentName);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setMinimumLatency(5000);
        builder.setPersisted(true);
        int status=jobScheduler.schedule(builder.build());
        if (status==JobScheduler.RESULT_SUCCESS){
            Log.d("UploadSignatureJobStart","Scheduled");
        }

    }

    public static void scheduleUploadEndSignatureJob(Application application){
        createSchedular(application);
        ComponentName componentName=new ComponentName(application, UploadEndSignatureJobService.class);
        JobInfo.Builder builder=new JobInfo.Builder(UPLOAD_SIGNATURE_END_JOB_ID,componentName);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPersisted(true);
        int status=jobScheduler.schedule(builder.build());
        if (status==JobScheduler.RESULT_SUCCESS){
            Log.d("UploadSignatureJobEnd","Scheduled");
        }

    }

    public static void scheduleUploadSignaturesJob(Application application){
        createSchedular(application);
        ComponentName componentName=new ComponentName(application, UploadSignaturesJobService.class);
        JobInfo.Builder builder=new JobInfo.Builder(UPLOAD_SIGNATURES_JOB_ID,componentName);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPersisted(true);
        int status=jobScheduler.schedule(builder.build());
        if (status==JobScheduler.RESULT_SUCCESS){
            Log.d("UploadSignatureJobAll","Scheduled");
        }

    }



    public static void deleteUpdatePolylineChangeJob(Application application){
        createSchedular(application);
        jobScheduler.cancel(UPDATE_POLYLINE_JOB_ID);
    }

    public static void deleteStartSignatureUploadJob(Application application){
        createSchedular(application);
        jobScheduler.cancel(UPLOAD_SIGNATURE_START_JOB_ID );
    }

    public static void deleteEndSignatureUploadJob(Application application){
        createSchedular(application);
        jobScheduler.cancel(UPLOAD_SIGNATURE_END_JOB_ID);
    }

    public static void deleteUploadSignaturesJob(Application application){
        createSchedular(application);
        jobScheduler.cancel(UPLOAD_SIGNATURES_JOB_ID);
    }

    public static boolean isNetworkChangeJobScheduled(Application application,int jobId){
        createSchedular(application);
        for (JobInfo jobInfo:jobScheduler.getAllPendingJobs()){
            if (jobInfo.getId()==JOB_ID){
                return true;
            }
        }
        return false;
    }

    public static boolean isPolylineJobScheduled(Application application){
        createSchedular(application);
        for (JobInfo jobInfo:jobScheduler.getAllPendingJobs()){
            if (jobInfo.getId()==UPDATE_POLYLINE_JOB_ID){
                return true;
            }
        }
        return false;
    }

    public static boolean isUploadSignaturesJobScheduled(Application application){
        createSchedular(application);
        for (JobInfo jobInfo:jobScheduler.getAllPendingJobs()){
            if (jobInfo.getId()==UPLOAD_SIGNATURES_JOB_ID){
                return true;
            }
        }
        return false;
    }



    public static boolean allJobsFinished(Application application){
        createSchedular(application);
        for (JobInfo jobInfo:jobScheduler.getAllPendingJobs()){
            if (jobInfo.getId()==UPDATE_POLYLINE_JOB_ID ||
                    jobInfo.getId()==UPLOAD_SIGNATURE_START_JOB_ID||
                    jobInfo.getId()==UPLOAD_SIGNATURE_END_JOB_ID){
                return false;
            }
        }
        return true;
    }
}
