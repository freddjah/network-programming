package se.kth.id1212.conversion.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import se.kth.id1212.conversion.domain.CurrencyConversion;
import se.kth.id1212.conversion.repository.CurrencyConversionRepository;

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

  public CurrencyConversion findCurrency(Long id) {
    return this.currencyConversionRepository.findById(id).get();
  }

  public List<CurrencyConversion> findAll() {
    return this.currencyConversionRepository.findAll();
  }

  public static float convert(float conversionRate, float amount) {
    return amount * conversionRate;
  }
}