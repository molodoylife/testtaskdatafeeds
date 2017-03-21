package ua.hanasaka.testtaskmolodykh;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * defining the REST API for Retrofit via the following interface
 *
 * @author Oleksandr Molodykh
 */
public interface QuandlApi {
    /**
     * GET method for creating request with specific params
     *
     * @param ticker  ticker for getting required information
     * @param api_key key of concrete user for access to special abilities
     * @return object used in DatafeedController for sending request
     */
    @GET("/api/v3/datasets/WIKI/{ticker}.json?column_index[]=1&column_index[]=2&column_index[]=3&" +
            "column_index[]=4")
    Call<DatafeedModel> loadChanges(@Path("ticker") String ticker, @Query("api_key") String api_key);
}

