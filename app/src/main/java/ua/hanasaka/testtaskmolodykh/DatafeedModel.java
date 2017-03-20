package ua.hanasaka.testtaskmolodykh;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DatafeedModel {

    @SerializedName("dataset")
    @Expose
    private Dataset dataset;

    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

}