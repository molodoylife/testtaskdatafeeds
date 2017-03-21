package ua.hanasaka.testtaskmolodykh;

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

public class UndocumentedDatafeedsReceiver extends AsyncTask<Void, Void, Boolean> {
    private static Context ctx;
    private static int datafeed;
    private static String ticker;
    private ProgressDialog pd;
    String error;
    private static RecyclerView recyclerView;
    List<List<String>> results;

    public UndocumentedDatafeedsReceiver(int datafeed, String ticker, Context ctx,
                                         RecyclerView recyclerView) {
        UndocumentedDatafeedsReceiver.recyclerView = recyclerView;
        UndocumentedDatafeedsReceiver.ctx = ctx;
        UndocumentedDatafeedsReceiver.datafeed = datafeed;
        UndocumentedDatafeedsReceiver.ticker = ticker;
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
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream;
            connection.connect();
            inputStream = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            if (retrieveListData(br))
                return true;
            else
                return false;
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

    private boolean retrieveListData(BufferedReader br) {
        results = new ArrayList<>();
        String line;
        try {
            br.readLine();
            switch (datafeed) {
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

    private List<String> getStringGoogleDatafeed(String row) {
        return new ArrayList<String>(Arrays.asList(row.substring(0, row.lastIndexOf(",")).split(",")));
    }

    private List<String> getStringYahooDatafeed(String row) {
        String[] initArr = row.split(",");
        return new ArrayList<String>(Arrays.asList(Arrays.copyOfRange(initArr, 0, 5)));
    }

    private List<String> getStringQuotomediaDatafeed(String row) {
        String[] initArr = row.split(",");
        return new ArrayList<String>(Arrays.asList(Arrays.copyOfRange(initArr, 0, 5)));
    }

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
