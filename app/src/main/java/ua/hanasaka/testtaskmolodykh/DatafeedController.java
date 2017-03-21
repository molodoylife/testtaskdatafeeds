package ua.hanasaka.testtaskmolodykh;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DatafeedController implements Callback<DatafeedModel> {
    private static String api_key;
    private static String ticker;
    private static Context ctx;
    private static RecyclerView recyclerView;
    private List<List<String>> listData;
    private ProgressDialog pd;


    private DatafeedController() {
    }

    private static class DatafeedControllerHelper {
        private static final DatafeedController INSTANCE = new DatafeedController();
    }

    public static DatafeedController getInstance(String API_KEY, String ticker, RecyclerView
            recyclerView, Context ctx) {
        DatafeedController.ctx = ctx;
        DatafeedController.recyclerView = recyclerView;
        DatafeedController.api_key = API_KEY;
        DatafeedController.ticker = ticker;
        return DatafeedControllerHelper.INSTANCE;
    }

    public void start() {
        pd = new ProgressDialog(ctx);
        pd.setProgressStyle(R.style.myProgressDialog);
        pd.setMessage(ctx.getResources().getString(R.string.wait));
        pd.setIndeterminate(true);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DataURLs.urlQuandlDataProviderBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        QuandlApi quandlAPI = retrofit.create(QuandlApi.class);

        Call<DatafeedModel> call = quandlAPI.loadChanges(DatafeedController.ticker, DatafeedController.api_key);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<DatafeedModel> call, Response<DatafeedModel> response) {
        if (response.isSuccessful()) {
            DatafeedModel df = response.body();
            Dataset dataset = df.getDataset();
            listData = dataset.getData();
            LinearLayoutManager llm = new LinearLayoutManager(ctx);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(llm);
            DatafeedAdapter da = new DatafeedAdapter(listData);
            recyclerView.setAdapter(da);
            da.notifyDataSetChanged();
            pd.dismiss();
        } else {
            pd.dismiss();
            try {
                String err = response.errorBody().string();
                showAlert(err);
            } catch (IOException e) {
                String err = "error " + e.getClass() + " mess=" + e.getMessage();
                showAlert(err);
            }
        }
    }

    @Override
    public void onFailure(Call<DatafeedModel> call, Throwable t) {
        pd.dismiss();
        String err = ctx.getResources().getString(R.string.adServerConnProblem);
        showAlert(err);
    }

    private void showAlert(String mess){
        AlertDialog dialog = new AlertDialog.Builder(ctx)
                .setMessage(mess)
                .setPositiveButton(R.string.yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).create();
        dialog.show();
    }
}