package ua.hanasaka.testtaskmolodykh;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface QuandlApi {
    @GET("/api/v3/datasets/WIKI/{ticker}.json?column_index[]=1&column_index[]=2&column_index[]=3&" +
            "column_index[]=4")
    Call<DatafeedModel> loadChanges(@Path("ticker") String ticker, @Query("api_key") String api_key);
}

