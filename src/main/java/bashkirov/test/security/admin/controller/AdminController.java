package bashkirov.test.security.admin.controller;

import bashkirov.test.security.admin.model.Person;
import bashkirov.test.security.admin.service.PersonDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final PersonDetailsService personDetailsService;

    @GetMapping
    public String adminPage(
            @ModelAttribute("person") Person person,
            Model model
    ) {
        List<Person> personAdminList = personDetailsService.getAllPersonAdmin();
        List<Person> personUserList = personDetailsService.getAllPersonUser();
        model.addAttribute("personAdminList", personAdminList);
        model.addAttribute("personUserList", personUserList);

        return "admin-page";
    }

    // список юзерів, яких можа назначити адміном(асайн)
    // список адмінів, і функцію зняти роль адміна(реліз)

    @PatchMapping("/add")
    public String addAdmin(
            @ModelAttribute("person") Person person
    ) {
        personDetailsService.addAdmin(person.getId());
        return "redirect:/admin";
    }

    @PatchMapping("/remove/{id}")
    public String removeAdmin(
            @PathVariable("id") int id
    ) {
        personDetailsService.removeAdmin(id);
        return "redirect:/admin";
    }
}
