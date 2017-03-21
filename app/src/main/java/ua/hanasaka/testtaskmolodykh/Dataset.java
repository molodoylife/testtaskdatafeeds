package ua.hanasaka.testtaskmolodykh;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Class containing response in List<List<String>> format
 *
 * @author Oleksandr Molodykh
 */
public class Dataset {
    @SerializedName("data")
    @Expose
    private List<List<String>> data = null;

    /**
     * for getting data from response
     *
     * @return List<List<String>> data
     */
    public List<List<String>> getData() {
        return data;
    }
}