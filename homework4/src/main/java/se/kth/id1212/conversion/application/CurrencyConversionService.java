package se.kth.id1212.conversion.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import se.kth.id1212.conversion.domain.CurrencyConversion;
import se.kth.id1212.conversion.repository.CurrencyConversionRepository;

import java.util.Comparator;
import java.util.List;


/**
 * <p>This is the bank application class, which defines tasks that can be
 * performed by the domain layer.</p>
 *
 * <p>Transaction demarcation is defined by methods in this class, a
 * transaction starts when a method is called from the presentation layer, and ends (commit or rollback) when that
 * method returns.</p>
 */
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
@Service
public class CurrencyConversionService {

  @Autowired
  private CurrencyConversionRepository currencyConversionRepository;

  public CurrencyConversion createCurrency(String fromCurrency, String toCurrency, Float conversionRate) {
    return this.currencyConversionRepository.save(new CurrencyConversion(fromCurrency, toCurrency, conversionRate));
  }

  public void deleteCurrency(long id) {
    this.currencyConversionRepository.deleteById(id);
  }

  public CurrencyConversion updateCurrency(long id, String fromCurrency, String toCurrency, Float conversionRate) {

    CurrencyConversion conversion = this.findCurrency(id);
    conversion.setFrom(fromCurrency);
    conversion.setTo(toCurrency);
    conversion.setConversionRate(conversionRate);
    this.currencyConversionRepository.save(conversion);

    return conversion;
  }

  public CurrencyConversion findCurrency(Long id) {
    return this.currencyConversionRepository.findById(id).get();
  }

  public List<CurrencyConversion> findAll() {
    List<CurrencyConversion> conversions = this.currencyConversionRepository.findAll();
    conversions.sort(Comparator.comparing(CurrencyConversion::getFrom));

    return conversions;
  }

  public float convert(CurrencyConversion currencyConversion, float amount) {

    currencyConversion.incrementNumberOfConversions();
    this.currencyConversionRepository.save(currencyConversion);

    return amount * currencyConversion.getConversionRate();
  }
}