package clients.models.finnhub;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public class FinnhubCandle {

    @JsonAlias(value = "c")
    public List<Double> close;

    @JsonAlias(value = "h")
    public List<Double> high;

    @JsonAlias(value = "l")
    public List<Double> low;

    @JsonAlias(value = "o")
    public List<Double> open;

    @JsonAlias(value = "t")
    public List<Long> timestamp;

    @JsonAlias(value = "v")
    public List<Double> volume;
}
