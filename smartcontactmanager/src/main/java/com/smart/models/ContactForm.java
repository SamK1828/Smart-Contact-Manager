package com.smart.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ContactForm {
    @NotBlank(message = "Contact name is required.")
    @Size(max = 80, message = "Contact name must be at most 80 characters.")
    private String contactName;

    @Size(max = 80, message = "Second name must be at most 80 characters.")
    private String secondName;

    @Size(max = 80, message = "Workplace must be at most 80 characters.")
    private String work;

    @NotBlank(message = "Phone number is required.")
    @Size(max = 30, message = "Phone number must be at most 30 characters.")
    private String phone;

    @Size(max = 1000, message = "Description must be at most 1000 characters.")
    private String description;

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
