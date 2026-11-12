package com.smart.controller;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smart.models.Contact;
import com.smart.models.ContactForm;
import com.smart.service.ContactService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {
    private final ContactService contactService;

    public UserController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/dashboard")
    public String dashboard(
            @AuthenticationPrincipal UserDetails principal,
            @RequestParam(name = "q", required = false) String query,
            @RequestParam(name = "tag", required = false) String tag,
            @RequestParam(defaultValue = "false") boolean favorites,
            @RequestParam(defaultValue = "name") String sort,
            Model model) {
        String sortOrder = "recent".equals(sort) ? "recent" : "name";
        model.addAttribute("title", "Your contacts | Smart Contact Manager");
        model.addAttribute("contacts", contactService.findContacts(principal, query, favorites, tag, sortOrder));
        model.addAttribute("query", query == null ? "" : query);
        model.addAttribute("tag", tag == null ? "" : tag);
        model.addAttribute("favorites", favorites);
        model.addAttribute("sort", sortOrder);
        model.addAttribute("isFiltered",
                (query != null && !query.isBlank()) || (tag != null && !tag.isBlank()) || favorites);
        return "normal/user_dashboard";
    }

    @GetMapping("/contacts/new")
    public String newContact(Model model) {
        model.addAttribute("title", "Add contact | Smart Contact Manager");
        model.addAttribute("contactForm", new ContactForm());
        model.addAttribute("formAction", "/user/contacts");
        model.addAttribute("heading", "Add a contact");
        return "normal/contact_form";
    }

    @PostMapping("/contacts")
    public String createContact(
            @AuthenticationPrincipal UserDetails principal,
            @Valid @ModelAttribute("contactForm") ContactForm contactForm,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "Add contact | Smart Contact Manager");
            model.addAttribute("formAction", "/user/contacts");
            model.addAttribute("heading", "Add a contact");
            return "normal/contact_form";
        }
        contactService.createContact(principal, contactForm);
        return "redirect:/user/dashboard?created";
    }

    @GetMapping("/contacts/{contactId}/edit")
    public String editContact(
            @PathVariable int contactId,
            @AuthenticationPrincipal UserDetails principal,
            Model model) {
        Contact contact = contactService.findContact(principal, contactId);
        model.addAttribute("title", "Edit contact | Smart Contact Manager");
        model.addAttribute("contactForm", toForm(contact));
        model.addAttribute("formAction", "/user/contacts/" + contactId);
        model.addAttribute("heading", "Edit contact");
        return "normal/contact_form";
    }

    @PostMapping("/contacts/{contactId}")
    public String updateContact(
            @PathVariable int contactId,
            @AuthenticationPrincipal UserDetails principal,
            @Valid @ModelAttribute("contactForm") ContactForm contactForm,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "Edit contact | Smart Contact Manager");
            model.addAttribute("formAction", "/user/contacts/" + contactId);
            model.addAttribute("heading", "Edit contact");
            return "normal/contact_form";
        }
        contactService.updateContact(principal, contactId, contactForm);
        return "redirect:/user/dashboard?updated";
    }

    @PostMapping("/contacts/{contactId}/delete")
    public String deleteContact(
            @PathVariable int contactId,
            @AuthenticationPrincipal UserDetails principal) {
        contactService.deleteContact(principal, contactId);
        return "redirect:/user/dashboard?deleted";
    }

    @PostMapping("/contacts/{contactId}/favorite")
    public String toggleFavorite(
            @PathVariable int contactId,
            @AuthenticationPrincipal UserDetails principal,
            @RequestParam(name = "q", required = false) String query,
            @RequestParam(name = "tag", required = false) String tag,
            @RequestParam(defaultValue = "false") boolean favorites,
            @RequestParam(defaultValue = "name") String sort,
            RedirectAttributes redirectAttributes) {
        contactService.toggleFavorite(principal, contactId);
        redirectAttributes.addAttribute("favoriteUpdated", "");
        if (query != null && !query.isBlank()) {
            redirectAttributes.addAttribute("q", query);
        }
        if (tag != null && !tag.isBlank()) {
            redirectAttributes.addAttribute("tag", tag);
        }
        redirectAttributes.addAttribute("favorites", favorites);
        redirectAttributes.addAttribute("sort", "recent".equals(sort) ? "recent" : "name");
        return "redirect:/user/dashboard";
    }

    private ContactForm toForm(Contact contact) {
        ContactForm form = new ContactForm();
        form.setContactName(contact.getContactName());
        form.setSecondName(contact.getSecondName());
        form.setWork(contact.getWork());
        form.setPhone(contact.getPhone());
        form.setDescription(contact.getDescription());
        form.setTags(contact.getTags());
        return form;
    }
}
