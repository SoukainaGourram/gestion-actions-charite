package com.example.demo.controllers;

import com.example.demo.entities.Action;
import com.example.demo.entities.Organisation;
import com.example.demo.entities.User;
import com.example.demo.services.ActionService;
import com.example.demo.services.DonationService;
import com.example.demo.services.OrganisationService;
import com.example.demo.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {

    private final ActionService actionService;
    private final OrganisationService organisationService;
    private final UserService userService;
    private final DonationService donationService;

    public WebController(ActionService actionService,
                         OrganisationService organisationService,
                         UserService userService,
                         DonationService donationService) {
        this.actionService = actionService;
        this.organisationService = organisationService;
        this.userService = userService;
        this.donationService = donationService;
    }

    // ─── Page d'accueil ──────────────────────────────────────────
    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    // ─── Login ───────────────────────────────────────────────────
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    // ─── Register ────────────────────────────────────────────────
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    // ─── Dashboard ───────────────────────────────────────────────
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        model.addAttribute("actions", actionService.getAll());
        model.addAttribute("organisations", organisationService.getAll());
        model.addAttribute("totalActions", actionService.getAll().size());
        model.addAttribute("totalOrgs", organisationService.getAll().size());
        model.addAttribute("totalDons", donationService.getAll().size());
        return "dashboard";
    }

    // ─── Liste des actions ────────────────────────────────────────
    @GetMapping("/actions")
    public String actions(@RequestParam(required = false) String category, Model model) {
        model.addAttribute("actions", actionService.getAllOrByCategory(category));
        model.addAttribute("selectedCategory", category);
        return "actions/list";
    }

    // ─── Détail d'une action ──────────────────────────────────────
    @GetMapping("/actions/{id}")
    public String actionDetail(@PathVariable Long id, Model model) {
        model.addAttribute("action", actionService.findById(id));
        model.addAttribute("progress", actionService.getProgress(id));
        return "actions/detail";
    }

    // ─── Formulaire nouvelle action ───────────────────────────────
    @GetMapping("/actions/new")
    public String newActionForm(Model model) {
        model.addAttribute("action", new Action());
        model.addAttribute("organisations", organisationService.getAll());
        return "actions/form";
    }

    // ─── Formulaire modifier action ───────────────────────────────
    @GetMapping("/actions/edit/{id}")
    public String editActionForm(@PathVariable Long id, Model model) {
        model.addAttribute("action", actionService.findById(id));
        model.addAttribute("organisations", organisationService.getAll());
        return "actions/form";
    }

    // ─── Liste des organisations ──────────────────────────────────
    @GetMapping("/organisations")
    public String organisations(Model model) {
        model.addAttribute("organisations", organisationService.getAll());
        return "organisations/list";
    }

    // ─── Détail d'une organisation ────────────────────────────────
    @GetMapping("/organisations/{id}")
    public String organisationDetail(@PathVariable Long id, Model model) {
        Organisation org = organisationService.findById(id);
        model.addAttribute("organisation", org);
        model.addAttribute("actions", actionService.getAll()
                .stream()
                .filter(a -> a.getOrganisation() != null && a.getOrganisation().getId().equals(id))
                .toList());
        return "organisations/detail";
    }

    // ─── Formulaire nouvelle organisation ────────────────────────
    @GetMapping("/organisations/new")
    public String newOrgForm(Model model) {
        model.addAttribute("organisation", new Organisation());
        return "organisations/form";
    }


    @GetMapping("/profile")
    public String profile(Model model, Authentication auth) {
        User user = userService.findByEmail(auth.getName());
        model.addAttribute("user", user);
        model.addAttribute("donations", donationService.getByUserId(user.getId()));
        return "user/profile";
    }
}