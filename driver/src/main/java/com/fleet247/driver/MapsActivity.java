package com.fleet247.driver;

import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

public class    MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(28.596501, 76.990833),
                        new LatLng(28.603836, 76.987478),
                        new LatLng(28.609126, 76.985405),
                        new LatLng(28.612686, 76.985874),
                        new LatLng(28.612417, 76.991019),
                        new LatLng(28.612417, 76.991019)));
        googleMap.addPolyline( new PolylineOptions().addAll(PolyUtil.decode("snulDogcuMJLPDFFKHiIbIoCrC_DbDsCdBuAn@iClAmBv@qE~AiAb@uCfAgA`@wGzB_J`DuEdBkFjBsAf@o@JM@?@EFGDK?CAg@ZaDbA{RlH_KnD{Ah@mAj@_Bj@qE`B_DdAeExAcCt@iA^GzBy@vMQhCG^K^O\\}BjCg@p@c@z@{@jBoAzC{@dBgAhByAjBmAhB}@vAU`@{AjBe@f@aBvAmClBcDfCyAhAi@t@w@hBc@pAsG{HoGiHgKuLoGkHw@w@sAqAkAmAeFsEmMlMyDfDqEzD_@b@gHdIaRfQ}H|GgN|MWRQ[{IgKeCmCmFsEOIOG_@Dk@Mc@SuAs@aBs@cDiBoB_AqBu@iC_BiH}DaPqHo@U[C_@?wA@{EC{@E]M]Qe@]iDqB]WY[sGwH}@_AoAmBcA{Ag@[qCcBcGwDiB_AS[w@u@oDuDkD}D{@iAOOYOSC]BQHs@j@YNKBU@wDUsNe@MMgAMIXWzAk@|BU\\M\\OTONo@ZsI`EMNc@TqBz@wGvB}JjD_LxD}HnC}DnAq@Z{DtAq@V}@f@_Bz@QBs@`@}C|AiBhAiFzC}IbFgCrAwAd@qB`@cDf@oF`AcBR[RSDqHlB_DhAkMrEiLnEgATyAN}LtAyBPuKn@sCXUJq@H_CVqELeLZsANeGvAcBZoJdDsBl@sGfAsFnA}Ad@_Br@iBz@mClAaBn@sAn@uAn@mGzBqHbD{JtC{HxBeE|AwCx@aAVwAd@uD`Au@TsErAgBp@_@FoBv@qCnAsBx@kARkAIWDi@FWBOH")));

        Log.d("PolyLine",polyline1.toString());
    }
}
