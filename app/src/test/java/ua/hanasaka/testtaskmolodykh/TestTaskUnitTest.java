package ua.hanasaka.testtaskmolodykh;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Testing are methods returned true List<String> results
 *
 * @author Oleksandr Molodykh
 */
public class TestTaskUnitTest {

    /**
     * Test method for checking is DatafeedsReceiver.getStringQuandlDatafeed() returns correct
     * List<String> result
     *
     * @throws Exception
     */
    @Test
    public void returnedListFromQuandlAsExpected() throws Exception {
        assertThat("List contains only needed data", getStringQuandlDatafeed
                ("2017-03-23,141.26,141.5844,140.61,140.92,19994964.0,0.0,1.0,141.26,141.5844," +
                        "140.61,140.92,19994964.0").size(), is(5));

        assertThat("List contains only needed data", getStringGoogleDatafeed
                ("23-Mar-17,141.26,141.58,140.61,140.92,20346301").size(), is(5));

        assertThat("List contains only needed data", getStringQuotomediaDatafeed
                ("2017-03-23,141.26,141.5844,140.61,140.92,20346301,-0.50,-0.35%,140.92,2868824778.67,127779")
                .size(), is(5));

        assertThat("List contains only needed data", getStringYahooDatafeed
                ("2017-03-23,141.259995,141.580002,140.610001,140.919998,20285700,140.919998").size(), is(5));
    }

    /**
     * tested method (DatafeedsReceiver.getStringGoogleDatafeed())
     *
     * @param row
     * @return
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

}