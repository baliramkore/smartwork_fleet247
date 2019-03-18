package com.fleet247.driver.retrofit;


import com.fleet247.driver.data.models.googledirection.GoogleDirectionApiResonse;
import com.fleet247.driver.data.models.googledistancematrix.DistanceMatrixApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by sandeep on 7/11/17.
 */

public interface GoogleAPI {

    @GET(ApiURLs.directionApiURL)
    Call<GoogleDirectionApiResonse> getDirections(@Query("origin") String pickup,
                                                  @Query("destination") String drop,
                                                  @Query("key") String directionKey);

    @GET(ApiURLs.distanceApiURL)
    Call<DistanceMatrixApiResponse> getEstimateDistanceAndTime(@Query("origins") String pickup,
                                                               @Query("destinations") String drop,
                                                               @Query("departure_time") String departureTime,
                                                               @Query("traffic_model") String trafficModel,
                                                               @Query("key") String key);


}
