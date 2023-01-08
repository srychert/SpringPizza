package pl.srychert.SpringPizza.controller;

import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.srychert.SpringPizza.domain.User;
import pl.srychert.SpringPizza.repository.UserRepository;
import pl.srychert.SpringPizza.service.UserService;

@Controller
@RequestMapping("user")
public class WebUserController {
    private final UserService userService;
    private final UserRepository userRepository;

    public WebUserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/list")
    @Secured("ROLE_ADMIN")
    String getUserList(Model model) {
        model.addAttribute("userList", userService.getAll());
        return "user-list";
    }

    @GetMapping("/add")
    @Secured("ROLE_ADMIN")
    public String showAddForm(User user, Model model) {
        model.addAttribute("roles", userService.getAllRoles());
        return "user-add";
    }

    @GetMapping("/{userId}")
    @PreAuthorize("@authComponent.isAccountOwner(#userId) or hasRole('ROLE_ADMIN')")
    String getUser(@PathVariable Long userId, Model model) {
        model.addAttribute("user", userService.get(userId));
        return "user-details";
    }

    @GetMapping("/name/{userName}")
    @PreAuthorize("@authComponent.isAccountOwnerByName(#userName) or hasRole('ROLE_ADMIN')")
    String getUserByName(@PathVariable String userName, Model model) {
        model.addAttribute("user", userService.getByUserName(userName));
        return "user-details";
    }

    @PostMapping(path = "/add")
    @Secured("ROLE_ADMIN")
    public String add(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user-add";
        }

        var userInDb = userRepository.findByUserName(user.getUserName());
        if (userInDb.isPresent()) {
            result.rejectValue("userName", "error.UserName", "User with this name already exists");
            model.addAttribute("roles", userService.getAllRoles());
            return "user-add";
        }

        if (user.getRoles() == null) {
            userService.add(user);
        } else {
            userService.addWithRoles(user);
        }

        return "redirect:/user/list";
    }

    @GetMapping("/delete/{userId}")
    @PreAuthorize("@authComponent.isAccountOwner(#userId) or hasRole('ROLE_ADMIN')")
    public String delete(@PathVariable Long userId, Model model) {
        userService.delete(userId);
        return "redirect:/user/list";
    }

    @GetMapping("/edit/{userId}")
    @PreAuthorize("@authComponent.isAccountOwner(#userId) or hasRole('ROLE_ADMIN')")
    public String showEditForm(@PathVariable Long userId, Model model) {
        var user = userService.get(userId);

        model.addAttribute("user", user);
        model.addAttribute("roles", userService.getAllRoles());
        return "user-edit";
    }

    @PostMapping("/update/{userId}")
    @PreAuthorize("@authComponent.isAccountOwner(#userId) or hasRole('ROLE_ADMIN')")
    public String update(@PathVariable Long userId, @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setId(userId);
            model.addAttribute("user", user);
            model.addAttribute("roles", userService.getAllRoles());
            return "user-edit";
        }

        userService.update(userId, user);
        return "redirect:/user/list";
    }

    @GetMapping("/edit/{userId}/roles")
    @Secured("ROLE_ADMIN")
    public String showEditRolesForm(@PathVariable Long userId, Model model) {
        var user = userService.get(userId);

        model.addAttribute("user", user);
        model.addAttribute("roles", userService.getAllRoles());
        return "user-roles";
    }

    @PostMapping("/update/{userId}/roles")
    @Secured("ROLE_ADMIN")
    public String updateRoles(@PathVariable Long userId, User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setId(userId);
            model.addAttribute("user", user);
            model.addAttribute("roles", userService.getAllRoles());
            return "user-roles";
        }

        userService.updateRoles(userId, user.getRoles());
        return "redirect:/user/" + userId;
    }
}
