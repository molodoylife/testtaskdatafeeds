package ua.hanasaka.testtaskmolodykh;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ua.hanasaka.testtaskmolodykh.datareceiver.DatafeedsReceiver;

/**
 * MainActivity class
 *
 * @author Oleksandr Molodykh
 */
public class MainActivity extends AppCompatActivity {
    private Spinner spinnerAllDataFeeds;
    private AutoCompleteTextView tvTicker;
    private RecyclerView recyclerView;
    private List<String> tickers;

    /**
     * checking Internet connection, initializing tickers and view elements
     *
     * @param savedInstanceState savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (isInternetAvailable())
            loadTickers();
        else
            Toast.makeText(this, getResources().getString(R.string.internetNotAvailable), Toast.LENGTH_SHORT).show();
        initializeViewElements();
    }

    /**
     * @return true if Internet is available
     */
    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data
                return true;
            }
        } else {
            // not connected to the internet
            return false;
        }
        return false;
    }

    /**
     * initializing view elements
     */
    private void initializeViewElements() {
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        tvTicker = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        tvTicker.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.search, 0);
        tvTicker.setOnTouchListener(OnSearchTouchListener);
        spinnerAllDataFeeds = (Spinner) findViewById(R.id.spinnerAllDataFeeds);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item,
                getResources().getStringArray(R.array.items));
        spinnerAllDataFeeds.setAdapter(adapter);
        spinnerAllDataFeeds.setSelection(0, false);
    }

    /**
     * loading tickers from server
     */
    private void loadTickers() {
        TickerLoader tl = new TickerLoader();
        tl.execute();
    }

    /**
     * used for handling click on search icon
     */
    private final View.OnTouchListener OnSearchTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getX() >= (tvTicker.getRight() - tvTicker.getTotalPaddingRight())
                        && event.getX() <= (tvTicker.getRight())) {
                    onSearchClick();
                    return true;
                }
            }
            return false;
        }
    };

    /**
     * used for overriding default behavior of selecting suggestion from autocompletetextview
     */
    private final AdapterView.OnItemClickListener OnTickerSelectedListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            if (getCurrentFocus().getWindowToken() != null)
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            String selection = (String) parent.getItemAtPosition(position);
            tvTicker.setText("");
            String workTicker = selection.substring(0, selection.indexOf(','));
            tvTicker.append(workTicker);
        }
    };

    /**
     * Depending on the chosen datafeed and the specified ticker
     * from this method starts the process of obtaining information
     * from certain servers
     */
    private void onSearchClick() {
        int spinnerPosition = spinnerAllDataFeeds.getSelectedItemPosition();
        String ticker = tvTicker.getText().toString();
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus().getWindowToken() != null)
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        DatafeedsReceiver datafeedsReceiver =
                new DatafeedsReceiver(spinnerPosition, ticker,
                        MainActivity.this, recyclerView);
        datafeedsReceiver.execute();
    }

    /**
     * member inner class for asynchronous loading tickers
     * from server
     *
     * @author Oleksandr Molodykh
     */
    class TickerLoader extends AsyncTask<Void, Void, Void> {
        private ProgressDialog pd;
        private String err = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setProgressStyle(R.style.myProgressDialog);
            pd.setMessage(MainActivity.this.getResources().getString(R.string.pdLoadingTickers));
            pd.setIndeterminate(true);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }

        /**
         * Creating connection and getting response
         */
        @Override
        protected Void doInBackground(Void... params) {
            URL url;
            tickers = new ArrayList<>();
            try {
                url = new URL(DataURLs.urlTickers);
                HttpURLConnection connection;
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream inputStream;
                connection.connect();
                inputStream = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                br.readLine();
                while ((line = br.readLine()) != null) {
                    line = line.substring(5);
                    tickers.add(line);
                }
            } catch (Exception e) {
                err = "Error " + e.getClass() + " mess=" + e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void o) {
            super.onPostExecute(o);
            pd.dismiss();
            if (err != null) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.toastTickersNotLoaded), Toast.LENGTH_SHORT).show();
            } else {
                tvTicker.setAdapter(new ArrayAdapter<>(MainActivity.this,
                        android.R.layout.simple_dropdown_item_1line, tickers));
                tvTicker.setOnItemClickListener(OnTickerSelectedListener);
            }
        }
    }
}
