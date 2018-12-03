package se.kth.id1212.conversion.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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

  @Autowired
  private CurrencyConversionService conversionService;

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
      return index(model, form);
    }

    CurrencyConversion currencyConversion = this.conversionService.findCurrency(form.getCurrencyId());
    float convertedAmount = this.conversionService.convert(currencyConversion, form.getAmount());

    model.addAttribute("amount", form.getAmount());
    model.addAttribute("convertedAmount", convertedAmount);
    model.addAttribute("currencyConversion", currencyConversion);

    return "convert";
  }

  // ADMIN PAGES FOR CURRENCY CONVERSION

  @GetMapping("/admin")
  public String adminIndex(@Param(value="message") String message, Model model) {
    List<CurrencyConversion> currencyConversions = conversionService.findAll();

    int totalNumberOfConversions = 0;
    for (CurrencyConversion conversion : currencyConversions) {
      totalNumberOfConversions += conversion.getNumberOfConversions();
    }

    model.addAttribute("currencyConversions", currencyConversions);
    model.addAttribute("totalNumberOfConversions", totalNumberOfConversions);

    if (message != null) {
      switch (message) {

        case "create":
          model.addAttribute("message", "The conversion was successfully created.");
          break;

        case "delete":
          model.addAttribute("message", "The conversion was successfully deleted.");
          break;

        case "edit":
          model.addAttribute("message", "The conversion was successfully updated.");
          break;
      }
    }

    return "admin/index";
  }

  @GetMapping("/admin/create")
  public String adminCreateConversionForm(Model model, UpdateForm updateForm) {

    model.addAttribute("updateForm", updateForm);
    return "admin/create";
  }

  @PostMapping("/admin/create")
  public String adminCreateConversion(@Valid UpdateForm form, BindingResult result, Model model) {

    if (result.hasErrors()) {
      return adminCreateConversionForm(model, form);
    }

    this.conversionService.createCurrency(form.getFrom(), form.getTo(), form.getConversionRate());

    model.addAttribute("message", "create");

    return "redirect:/admin";
  }

  @GetMapping("/admin/edit/{id}")
  public String adminEditConversionForm(@PathVariable long id, UpdateForm updateForm, Model model) {

    CurrencyConversion currencyConversion = this.conversionService.findCurrency(id);

    updateForm.setFrom(currencyConversion.getFrom());
    updateForm.setTo(currencyConversion.getTo());
    updateForm.setConversionRate(currencyConversion.getConversionRate());
    model.addAttribute("updateForm", updateForm);
    model.addAttribute("conversion", currencyConversion);

    return "admin/edit";
  }

  @PostMapping("/admin/edit/{id}")
  public String adminEditConversion(@PathVariable long id, @Valid UpdateForm form, BindingResult result, Model model) {

    if (result.hasErrors()) {
      return adminEditConversionForm(id, form, model);
    }

    this.conversionService.updateCurrency(id, form.getFrom(), form.getTo(), form.getConversionRate());

    model.addAttribute("message", "edit");

    return "redirect:/admin";
  }

  @GetMapping("/admin/delete/{id}")
  public String adminDeleteConversion(@PathVariable long id, Model model) {

    this.conversionService.deleteCurrency(id);

    model.addAttribute("message", "delete");

    return "redirect:/admin";
  }
}