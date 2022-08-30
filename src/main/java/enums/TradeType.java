package enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TradeType {
    @JsonValue(value = true)
    BUY("buy"),

    @JsonValue(value = true)
    SELL("sell");

    private final String tradeType;

    TradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getValue() {
        return this.tradeType;
    }
}
