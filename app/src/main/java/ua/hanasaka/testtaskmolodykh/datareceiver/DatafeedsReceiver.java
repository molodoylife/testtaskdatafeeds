package ua.hanasaka.testtaskmolodykh.datareceiver;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ua.hanasaka.testtaskmolodykh.DataURLs;
import ua.hanasaka.testtaskmolodykh.R;
import ua.hanasaka.testtaskmolodykh.adapter.DatafeedAdapter;

/**
 * This class used for work with  undocumented datafeeds.
 * Chosen datafeed and ticker passed in constructor as a Context and RecyclerView
 * When execute() method call from MainActivity in doInBackground() will
 * be set appropriate url, opened connection and gotten response. Then in onPostExecute()
 * will be created DatafeedAdapter and set one to recyclerview
 *
 * @author Oleksandr Molodykh
 */
public class DatafeedsReceiver extends AsyncTask<Void, Void, Boolean> {
    private final Context ctx;
    private final int datafeed;
    private final String ticker;
    private ProgressDialog pd;
    private String error;
    private final RecyclerView recyclerView;
    private List<List<String>> results;

    /**
     * constructor of DatafeedsReceiver object
     *
     * @param datafeed     number of datafeed
     * @param ticker       chosen ticker
     * @param ctx          Context ctx
     * @param recyclerView transfered RecyclerView
     */


    public DatafeedsReceiver(int datafeed, String ticker, Context ctx,
                             RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.ctx = ctx;
        this.datafeed = datafeed;
        this.ticker = ticker;
    }

    @Override
    protected void onPreExecute() {
        pd = new ProgressDialog(ctx);
        pd.setProgressStyle(R.style.myProgressDialog);
        pd.setMessage(ctx.getResources().getString(R.string.wait));
        pd.setIndeterminate(true);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        URL url = null;
        try {
            switch (datafeed) {
                case 0:
                    url = new URL(DataURLs.urlQuandlDataProvider + ticker + ".csv");
                    break;
                case 1:
                    url = new URL(DataURLs.urlGoogleDataProvider + ticker);
                    break;
                case 2:
                    url = new URL(DataURLs.urlYahooDataProvider + ticker);
                    break;
                case 3:
                    url = new URL(DataURLs.urlQuotemediaDataProvider + ticker);
                    break;
            }
            HttpURLConnection connection;
            if (url == null) {
                throw new NullPointerException();
            }
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream;
            connection.connect();
            inputStream = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            return retrieveListData(br);
        } catch (FileNotFoundException e) {
            error = ctx.getResources().getString(R.string.adUnsupportedTicker);
            return false;
        } catch (UnknownHostException e) {
            error = ctx.getResources().getString(R.string.adServerConnProblem);
            return false;
        } catch (Exception e) {
            error = "error " + e.getClass() + " mess=" + e.getMessage();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean flag) {
        super.onPostExecute(flag);
        if (flag) {
            LinearLayoutManager llm = new LinearLayoutManager(ctx);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(llm);
            DatafeedAdapter da = new DatafeedAdapter(results);
            recyclerView.setAdapter(da);
            da.notifyDataSetChanged();
            pd.dismiss();
        } else {
            pd.dismiss();
            if (error != null) {
                showAlert(error);
            }
        }
    }

    /**
     * setting List<List<String>> results
     *
     * @param br BufferedReader object with response
     * @return true if no exception is thrown
     */
    private boolean retrieveListData(BufferedReader br) {
        results = new ArrayList<>();
        String line;
        try {
            br.readLine();
            switch (datafeed) {
                case 0:
                    while ((line = br.readLine()) != null) {
                        results.add(getStringQuandlDatafeed(line));
                    }
                    break;
                case 1:
                    while ((line = br.readLine()) != null) {
                        results.add(getStringGoogleDatafeed(line));
                    }
                    break;
                case 2:
                    while ((line = br.readLine()) != null) {
                        results.add(getStringYahooDatafeed(line));
                    }
                    break;
                case 3:
                    int countRows = 0;
                    while ((line = br.readLine()) != null) {
                        countRows++;
                        results.add(getStringQuotomediaDatafeed(line));
                    }
                    if (countRows == 0) {
                        throw new FileNotFoundException();
                    }
                    break;
            }
        } catch (FileNotFoundException e) {
            error = ctx.getResources().getString(R.string.adUnsupportedTicker);
            return false;
        } catch (Exception e) {
            error = "error " + e.getClass() + " mess=" + e.getMessage();
            return false;
        }
        return true;
    }

    /**
     * getting ArrayList data for organization rows
     *
     * @param row raw row
     * @return ArrayList with concrete format adjusted data
     */
    private List<String> getStringGoogleDatafeed(String row) {
        return Arrays.asList(row.substring(0, row.lastIndexOf(",")).split(","));
    }

    private List<String> getStringQuandlDatafeed(String row) {
        String[] initArr = row.split(",");
        return Arrays.asList(Arrays.copyOfRange(initArr, 0, 5));
    }

    private List<String> getStringYahooDatafeed(String row) {
        String[] initArr = row.split(",");
        return Arrays.asList(Arrays.copyOfRange(initArr, 0, 5));
    }

    private List<String> getStringQuotomediaDatafeed(String row) {
        String[] initArr = row.split(",");
        return Arrays.asList(Arrays.copyOfRange(initArr, 0, 5));
    }

    /**
     * AlertDialog with error message
     *
     * @param mess error message
     */
    private void showAlert(String mess) {
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
