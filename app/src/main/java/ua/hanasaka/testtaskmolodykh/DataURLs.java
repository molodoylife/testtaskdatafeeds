package ua.hanasaka.testtaskmolodykh;

/**
 * Class for containing urls for work
 *
 * @author Oleksandr Molodykh
 */
public class DataURLs {
    public static final String urlTickers = "https://s3.amazonaws.com/quandl-static-content/" +
            "Ticker+CSV%27s/WIKI_tickers.csv";
    public static final String urlQuandlDataProvider = "https://www.quandl.com/api/v3/datasets/WIKI/";
    public static final String urlGoogleDataProvider = "http://www.google.com/finance/historical?" +
            "output=csv&q=";
    public static final String urlQuotemediaDataProvider = "http://app.quotemedia.com/quotetools/" +
            "getHistoryDownload.csv?&webmasterId=501&isRanged=false&symbol=";
    public static final String urlYahooDataProvider = "http://ichart.finance.yahoo.com/table.csv?" +
            "d=6&e=1&g=d&a=7&b=19&ignore=.csv&s=";
}
