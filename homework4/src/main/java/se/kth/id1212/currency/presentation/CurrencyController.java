package se.kth.id1212.currency.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

/**
 * Handles all HTTP requests to context root.
 */
@Controller
@Scope("session")
public class CurrencyController {
  static final String DEFAULT_PAGE_URL = "/";

  /**
   * A get request for the account page.
   *
   * @param model Model objects used by the account page.
   * @return The account page url.
   */
  @GetMapping("/")
  public String showAccountView(Model model) {
    model.addAttribute("test", "haj");
    return "index";
  }
}