package clients.models.finnhub;

import com.fasterxml.jackson.annotation.JsonAlias;

public class FinnhubQuote {
    @JsonAlias(value = "c")
    public double cost;

    @JsonAlias(value = "d")
    public double change;

    @JsonAlias(value = "dp")
    public double percentChange;

    @JsonAlias(value = "h")
    public double high;

    @JsonAlias(value = "l")
    public double low;

    @JsonAlias(value = "o")
    public double dayOpen;

    @JsonAlias(value = "pc")
    public double previousClose;

    @JsonAlias(value = "t")
    public long timestamp;

    public String symbol;
}
