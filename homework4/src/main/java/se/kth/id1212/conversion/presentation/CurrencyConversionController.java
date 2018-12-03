package se.kth.id1212.conversion.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import se.kth.id1212.conversion.application.CurrencyConversionService;
import se.kth.id1212.conversion.domain.CurrencyConversion;
import se.kth.id1212.conversion.domain.CurrencyConversionDTO;

import javax.validation.Valid;
import java.util.List;

/**
 * Handles all HTTP requests to context root.
 */
@Controller
@Scope("session")
public class CurrencyConversionController {
  static final String DEFAULT_PAGE_URL = "/";
  static final String GET_CURRENCY_URL = "/conversion/{id}";

  @Autowired
  private CurrencyConversionService conversionService;

  /**
   * A get request for the account page.
   *
   * @param model Model objects used by the account page.
   * @return The account page url.
   */
  /*@GetMapping(DEFAULT_PAGE_URL)
  public String index(Model model) {
    currentCurrency = currencyService.createCurrency("USD", "SEK", (float) 9.046);
    model.addAttribute("test", currentCurrency.toString());
    return "index";
  }*/

  @GetMapping("/")
  public String index(Model model, CurrencyConversionForm currencyConversionForm) {
    List<CurrencyConversion> currencyConversions = conversionService.findAll();
    model.addAttribute("currencyConversions", currencyConversions);
    model.addAttribute("currencyConversionForm", currencyConversionForm);
    return "index";
  }

  @PostMapping("/")
  public String convert(@Valid CurrencyConversionForm form, BindingResult result, Model model) {
    if (result.hasErrors()) {
      System.out.println("Shit went wrong with form \n" + result.toString());
      return index(model, form);
    }

    CurrencyConversion currencyConversion = this.conversionService.findCurrency(form.getCurrencyId());
    float convertedAmount = CurrencyConversionService.convert(currencyConversion.getConversionRate(), form.getAmount());

    model.addAttribute("amount", form.getAmount());
    model.addAttribute("convertedAmount", convertedAmount);
    model.addAttribute("currencyConversion", currencyConversion);

    return "convert";
  }

  // Get CurrencyConversion with Id
  /*@RequestMapping(GET_CURRENCY_URL)
  public String showCurrency(@PathVariable("id") long id, Model model) {
    currentCurrency = currencyService.findCurrency(id);
    model.addAttribute("test", currentCurrency.getName());
    return "index";
  }*/
}