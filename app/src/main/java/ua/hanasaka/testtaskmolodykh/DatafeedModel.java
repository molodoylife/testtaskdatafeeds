package ua.hanasaka.testtaskmolodykh;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Model class. Used for getting Call<> object
 *
 * @author Oleksandr Molodykh
 * @see Dataset
 */
public class DatafeedModel {

    @SerializedName("dataset")
    @Expose
    private Dataset dataset;

    /**
     * @return Dataset object
     */
    public Dataset getDataset() {
        return dataset;
    }
}