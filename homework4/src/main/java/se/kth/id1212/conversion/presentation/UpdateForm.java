package se.kth.id1212.conversion.presentation;

import javax.validation.constraints.*;

public class UpdateForm {

    @NotNull(message = "Enter a from currency")
    @Size(min = 3, max = 3, message = "Currency must have 3 letters")
    private String from;

    @NotNull(message = "Enter a to currency")
    @Size(min = 3, max = 3, message = "Currency must have 3 letters")
    private String to;

    @NotNull(message = "Enter a to conversion rate")
    @Positive(message = "Conversion rate must be a positive number")
    private Float conversionRate;

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setConversionRate(Float conversionRate) {
        this.conversionRate = conversionRate;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Float getConversionRate() {
        return conversionRate;
    }
}
