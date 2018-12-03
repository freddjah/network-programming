package se.kth.id1212.conversion.domain;

import javax.persistence.*;

@Entity
@Table(name = "conversions")
public class CurrencyConversion implements CurrencyConversionDTO {


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private long id;

  @Column(name = "from_currency")
  private String from;

  @Column(name = "to_currency")
  private String to;

  @Column(name = "conversion_rate")
  private Float conversionRate;

  @Column(name = "number_of_conversions")
  private Integer numberOfConversions = 0;

  public CurrencyConversion(String from, String to, Float conversionRate) {
    this.from = from;
    this.to = to;
    this.conversionRate = conversionRate;
  }

  // Needed by JPA
  protected CurrencyConversion() {}

  @Override
  public long getId() {
    return this.id;
  }

  @Override
  public String getFrom() {
    return this.from;
  }

  @Override
  public String getTo() {
    return this.to;
  }

  @Override
  public Float getConversionRate() {
    return this.conversionRate;
  }

  @Override
  public Integer getNumberOfConversions() {
    return this.numberOfConversions;
  }

  public void incrementNumberOfConversions() {
    this.numberOfConversions++;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public void setConversionRate(Float conversionRate) {
    this.conversionRate = conversionRate;
  }
}
