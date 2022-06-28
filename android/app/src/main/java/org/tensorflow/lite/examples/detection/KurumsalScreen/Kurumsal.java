package org.tensorflow.lite.examples.detection.KurumsalScreen;

public class Kurumsal {

    String id = "";
    String firstName = "";
    String lastName = "";
    String mail = "";
    String password = "";
    String phoneNumber = "";
    String companyName = "";
    boolean rememberMe = false;

    public Kurumsal(){}
    public Kurumsal(String id, String firstName, String lastName, String mail, String password, String phoneNumber, String companyName, boolean rememberMe) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.companyName = companyName;
        this.rememberMe = rememberMe;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }



}
