package com.fleet247.driver.retrofit;



import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sandeep on 6/7/17.
 */

public class ConfigRetrofit {
    /**
     * Creates Retrofit object for accessing fleet247 APIs
     */
    private static Retrofit retrofit=new Retrofit.Builder()
            .baseUrl(ApiURLs.baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(configOkhttp())
            .build();

    /**
     * Creates Retrofit object for accessing google APIS
     */
    private static Retrofit googleApiRetrofit=new Retrofit.Builder()
            .baseUrl(ApiURLs.googleApiBaseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(configOkhttp())
            .build();

    /**
     *
     * @param service API Interface
     * @return RetrofitClass created by Retrofit to access Fleet247 API.
     */
    public static <S> S configRetrofit(Class<S> service){
        return retrofit.create(service);
    }

    /**
     *
     * @param service service API Interface
     * @return  RetrofitClass created by Retrofit to access Google API.
     */
    public static <S> S configGoogleRetrofit(Class<S> service){
        return googleApiRetrofit.create(service);
    }

    private static OkHttpClient configOkhttp(){
        OkHttpClient.Builder okHttpClientbuilder=new OkHttpClient.Builder();
        okHttpClientbuilder.addInterceptor(configOkHttpLoggingIncepter());
        return okHttpClientbuilder.build();

    }

    private static HttpLoggingInterceptor configOkHttpLoggingIncepter(){
        HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

}
