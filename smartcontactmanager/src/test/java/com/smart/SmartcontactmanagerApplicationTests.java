package com.smart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.server.ResponseStatusException;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.models.Contact;
import com.smart.models.ContactForm;
import com.smart.service.ContactService;

@SpringBootTest
@AutoConfigureMockMvc
class SmartcontactmanagerApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ContactService contactService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void clearDatabase() {
        contactRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void contextLoads() {
    }

    @Test
    void registrationRequiresAgreement() throws Exception {
        mockMvc.perform(post("/do_register")
                        .with(csrf())
                        .param("name", "Taylor Example")
                        .param("email", "taylor@example.com")
                        .param("password", "correct-horse-10")
                        .param("about", "A test account"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"));

        assertFalse(userRepository.existsByEmailIgnoreCase("taylor@example.com"));
    }

    @Test
    void registrationHashesPasswordAndLoginOpensDashboard() throws Exception {
        mockMvc.perform(post("/do_register")
                        .with(csrf())
                        .param("name", "Taylor Example")
                        .param("email", "Taylor@example.com")
                        .param("password", "correct-horse-10")
                        .param("about", "A test account")
                        .param("agreement", "on"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?registered"));

        com.smart.models.User savedUser = userRepository.findByEmailIgnoreCase("taylor@example.com").orElseThrow();
        assertNotEquals("correct-horse-10", savedUser.getPassword());
        assertTrue(passwordEncoder.matches("correct-horse-10", savedUser.getPassword()));

        MvcResult loginResult = mockMvc.perform(post("/do_login")
                        .with(csrf())
                        .param("email", "taylor@example.com")
                        .param("password", "correct-horse-10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/dashboard"))
                .andReturn();

        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession(false);
        mockMvc.perform(get("/user/dashboard").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("normal/user_dashboard"));
    }

    @Test
    void successfulLoginUpgradesLegacyPlaintextPassword() throws Exception {
        com.smart.models.User legacyUser = createUser("legacy@example.com");
        legacyUser.setPassword("legacy-password");
        userRepository.saveAndFlush(legacyUser);

        signIn("legacy@example.com", "legacy-password");

        String upgradedPassword = userRepository.findByEmailIgnoreCase("legacy@example.com")
                .orElseThrow()
                .getPassword();
        assertNotEquals("legacy-password", upgradedPassword);
        assertTrue(passwordEncoder.matches("legacy-password", upgradedPassword));
    }

    @Test
    void authenticatedUserCanManageContactsAndAccountPagesRender() throws Exception {
        com.smart.models.User savedUser = userRepository.saveAndFlush(createUser("taylor@example.com"));
        MockHttpSession session = signIn("taylor@example.com", "correct-horse-10");

        mockMvc.perform(get("/user/contacts/new").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("normal/contact_form"));
        mockMvc.perform(get("/user/profile").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("normal/profile"));
        mockMvc.perform(get("/user/settings").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("normal/settings"));

        mockMvc.perform(post("/user/contacts")
                        .session(session)
                        .with(csrf())
                        .param("contactName", "Jordan")
                        .param("phone", "+1 555 0100")
                        .param("work", "Studio"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/dashboard?created"));

        Contact contact = contactRepository.findAllByUser_IdOrderByContactNameAsc(savedUser.getId()).get(0);
        mockMvc.perform(get("/user/contacts/{contactId}/edit", contact.getcId()).session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("normal/contact_form"));

        mockMvc.perform(post("/user/contacts/{contactId}", contact.getcId())
                        .session(session)
                        .with(csrf())
                        .param("contactName", "Jordan Lee")
                        .param("phone", "+1 555 0101"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/dashboard?updated"));

        mockMvc.perform(post("/user/profile")
                        .session(session)
                        .with(csrf())
                        .param("name", "Taylor Updated")
                        .param("about", "Updated profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/profile?updated"));
        assertEquals("Taylor Updated", userRepository.findById(savedUser.getId()).orElseThrow().getName());

        mockMvc.perform(post("/user/settings/password")
                        .session(session)
                        .with(csrf())
                        .param("currentPassword", "correct-horse-10")
                        .param("newPassword", "another-correct-pass-11")
                        .param("confirmPassword", "another-correct-pass-11"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/settings?passwordChanged"));
        assertTrue(passwordEncoder.matches(
                "another-correct-pass-11",
                userRepository.findById(savedUser.getId()).orElseThrow().getPassword()));

        mockMvc.perform(post("/user/contacts/{contactId}/delete", contact.getcId())
                        .session(session)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/dashboard?deleted"));
        assertTrue(contactRepository.findAllByUser_IdOrderByContactNameAsc(savedUser.getId()).isEmpty());
    }

    @Test
    void contactCannotBeReadByAnotherAccount() {
        com.smart.models.User owner = userRepository.saveAndFlush(createUser("owner@example.com"));
        userRepository.saveAndFlush(createUser("other@example.com"));

        ContactForm form = new ContactForm();
        form.setContactName("Jordan");
        form.setPhone("+1 555 0100");
        Contact contact = contactService.createContact(principal("owner@example.com"), form);

        assertEquals(contact.getcId(),
                contactService.findContact(principal("owner@example.com"), contact.getcId()).getcId());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> contactService.findContact(principal("other@example.com"), contact.getcId()));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals(1, contactRepository.findAllByUser_IdOrderByContactNameAsc(owner.getId()).size());
    }

    private com.smart.models.User createUser(String email) {
        com.smart.models.User user = new com.smart.models.User();
        user.setName("Test User");
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("correct-horse-10"));
        user.setAbout("Test account");
        user.setRole("ROLE_USER");
        user.setEnabled(true);
        return user;
    }

    private UserDetails principal(String email) {
        return User.withUsername(email).password("not-used").roles("USER").build();
    }

    private MockHttpSession signIn(String email, String password) throws Exception {
        MvcResult result = mockMvc.perform(post("/do_login")
                        .with(csrf())
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        return (MockHttpSession) result.getRequest().getSession(false);
    }
}
