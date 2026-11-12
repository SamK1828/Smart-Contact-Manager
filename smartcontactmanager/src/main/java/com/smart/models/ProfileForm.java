package com.smart.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProfileForm {
    @NotBlank(message = "Please enter your name.")
    @Size(min = 2, max = 60, message = "Name must be between 2 and 60 characters.")
    private String name;

    @NotBlank(message = "Please add a short description.")
    @Size(max = 500, message = "About must be at most 500 characters.")
    private String about;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
