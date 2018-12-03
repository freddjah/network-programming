package se.kth.id1212.conversion.domain;

public interface CurrencyConversionDTO {
  long getId();
  String getFrom();
  String getTo();
  Float getConversionRate();
  Integer getNumberOfConversions();
}
