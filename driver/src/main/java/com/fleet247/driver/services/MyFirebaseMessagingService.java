package com.fleet247.driver.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.fleet247.driver.utility.NotificationUtill;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.fleet247.driver.R;
import com.fleet247.driver.activities.DetailsActivity;
import com.fleet247.driver.data.models.upcomingbooking.Booking;
import com.fleet247.driver.utility.GsonStringConvertor;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sandeep on 23/2/18.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private NotificationManager mNotificationManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("FCMmessage",remoteMessage.getData().toString());

        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(remoteMessage.getData().get("BookingDetails"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser=new JsonParser();

        Log.d("JsonObject",jsonObject.toString());
        Booking booking=GsonStringConvertor.stringToGson(jsonObject.toString(),Booking.class);
        Log.d("Booking",GsonStringConvertor.gsonToString(booking));

        mNotificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && mNotificationManager.getNotificationChannel(NotificationUtill.bookingChanneId)==null){
            NotificationUtill.createBookingNotificationChannel(getApplicationContext());
        }

        Intent intent=new Intent(this,DetailsActivity.class);
        intent.putExtra("bookingInfo",GsonStringConvertor.gsonToString(booking));

        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        String title=new String();
        String text=new String();

        switch (remoteMessage.getData().get("notification_type")){
            case "new_booking":
                title="New Booking Assigned";
                text=booking.getTourType()+" Booking on "+booking.getPickupDatetime()+" has been assigned";
                break;
            case "cancelled_booking":
                title="Booking Cancelled";
                text=booking.getTourType()+" Booking on "+booking.getPickupDatetime()+" has been cancelled";
        }


        String bigtext="Booking Id: "+booking.getReferenceNo()+"\n"
                +"Pickup: "+booking.getPickupLocation()+"\n"
                +"Date Time: "+booking.getPickupDatetime()+"\n"
                +"User: "+booking.getPassengers().get(0).getPeopleName()+" "+booking.getPassengers().get(0).getPeopleContact()+"\n"
                +"Usage: "+booking.getTourType();

        NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(this,NotificationUtill.bookingChanneId)
                .setSmallIcon(R.drawable.fleetnotificationlogo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.fleet247))
                .setContentTitle(title)
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(bigtext))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        mNotificationManager.notify(Integer.parseInt(booking.getBookingId()),mBuilder.build());


    }
}

//{"to":"fZIr8XWl7EQ:APA91bG_58PuL1B8joEIBVeKQwmt8dHuPCmuNQCukaFIuQfJEHLC24gofXdmH0aLZ_n4OyqAF5tjCHWqXO3uQbIB-arRh5lZ4cmXZxevOMzKSjqPoVEpvGJmU9Q9MNl-tUNqreRr61ke","data": {"booking_id":"96fb9b48825b741083d35b0137af1be0","reference_no":"TVTXIVX16038","pickup_datetime":"2018-03-06 15:10:00","pickup_location":"Chattarpur Metro Station Andheria Mor Village, Vasant Kunj, New Delhi, Delhi","drop_location":"Durga Vihar Roshan Vihar 1, Najafgarh, Delhi","trip_status":"Driver Assigned","tour_type":"Outstation","city_name":"New Delhi","package_name":"Outstation","Passengers":[{"id":"4814","admin_id":"21","group_id":"164","subgroup_id":"205","user_id":"217","people_cid":"SAN8765","people_name":"Sandeep","people_email":"joshisandeep2010@gmail.com","people_contact":"9899701993","age":null,"gender":null,"id_proof_type":null,"id_proof_no":null,"is_active":"1","has_dummy_email":"0","fcm_regid":"f2c5jVaw3dY:APA91bH-Fq9qXmu4WSsQVbNA38HB4DH9PJ55fSRCS37shLB3ps8XQlVTvX-TtnnX2un3rV0S-x-3O4ucWxN0QU0_rcD1hgmPbO4Ct6MKcbQdtFmz2SEI1JzaT4nkDWsJSVecxaOa-jwG","created":"2017-12-12 13:31:40","modified":"2017-12-12 13:39:58"}]},"priority":"high"}
/*
{
	"to": "ereiXpuh6_4:APA91bGSlcIa8GK7cyVURw54j0kdQGdF4Pgn7LEnJqM5HYsf706hBN7YqDDJk4BGbJrwcZVmKFXSMVOyPhQ16PMPwyJN5E6v4brA1awxo_TKTwL3G5amR5v4NcxMAjvfjdEFrkcfO6qI",
	"data": {
		"notification_type": "new_booking",
		"BookingDetails": {
			"booking_id": "97",
			"city_name": "Karnal",
			"cancel_reason": "null",
			"Passengers": [{
				"people_contact": "9582058187",
				"people_email": "chetan268@gmail.com",
				"people_name": "Chetan"
			}],
			"pickup_location": "Ashok Nagar, Bengaluru, Karnataka, India",
			"tour_type": "Local",
			"package_name": "8Hrs\/80Kms",
			"pickup_datetime": "2018-06-15 13:15",
			"drop_location": "",
			"reference_no": "TP97"
		}
	},
	"priority": "high"
}
 */