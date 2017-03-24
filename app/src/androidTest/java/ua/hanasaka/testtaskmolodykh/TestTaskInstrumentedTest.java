package ua.hanasaka.testtaskmolodykh;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @author Oleksandr Molodykh
 */
@RunWith(AndroidJUnit4.class)
public class TestTaskInstrumentedTest {
    private Context context;

    /**
     * setting up context
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        context = InstrumentationRegistry.getContext();
    }

    /**
     * testing is Internet available
     *
     * @throws Exception
     */
    @Test
    public void isInternetAvailable() throws Exception {
        assertTrue(isInternetAvailableTest());
    }

    /**
     * tested method
     *
     * @return true if Internet available
     */
    private boolean isInternetAvailableTest() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
}
