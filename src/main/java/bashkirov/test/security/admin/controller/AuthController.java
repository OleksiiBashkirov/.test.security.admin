package bashkirov.test.security.admin.controller;

import bashkirov.test.security.admin.model.Person;
import bashkirov.test.security.admin.service.PersonRegistrationService;
import bashkirov.test.security.admin.validation.PersonValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final PersonValidator personValidator;
    private final PersonRegistrationService personRegistrationService;


    @GetMapping("/registration")
    public String registrationPage(
            @ModelAttribute("newRegistrationPerson") Person newRegistrationPerson
    ) {
        return "registration-page";
    }

    @PostMapping("/registration")
    public String registration(
            @ModelAttribute("newRegistrationPerson") Person newRegistrationPerson,
            BindingResult bindingResult
    ) {
        personValidator.validate(newRegistrationPerson, bindingResult);
        if (bindingResult.hasErrors()) {
            return "registration-page";
        }
        personRegistrationService.register(newRegistrationPerson);
        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String login() {
        return "enter-page";
    }

}
