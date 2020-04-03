package edu.neu.foodiefriendfinder.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class User {

    private String userId;
    private String firstName;
    private String lastName;
    private String phone;
    private List<String> cuisine;
    private String dob;
    private List<String> languages;
    private String gender;
    private String email;
    private boolean isOnline;

    public User() {
    }

    public User(String userId, String firstName, String lastName, String email, String phone,
                List<String> cuisine, String dob, List<String> languages, String gender) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.cuisine = cuisine;
        this.dob = dob;
        this.languages = languages;
        this.gender = gender;
        this.isOnline = false;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getCuisine() {
        return cuisine;
    }

    public void setCuisine(List<String> cuisine) {
        this.cuisine = cuisine;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public boolean getOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void addLanguage(String language) {
        this.languages.add(language);
    }

    public void addCuisine(String cuisine) {
        this.cuisine.add(cuisine);
    }

    public void removeLanguage(String language) {
        this.languages.remove(language);
    }

    public void removeCuisine(String cuisine) {
        this.cuisine.remove(cuisine);
    }

    public String getEmail() {
        return email;
    }

    public void setEmaill(String email) {
        this.email = email;
    }
}
