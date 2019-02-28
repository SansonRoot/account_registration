package com.softmastersgroup.umo.umoagent.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class BioData implements Serializable {
    @Expose private String firstname;
    @Expose private String lastname;
    @Expose private String othernames;
    @Expose private String dateofbirth;
    @Expose private int age;
    @Expose private String birthplace;
    @Expose private String telephone;
    @Expose private String email;
    @Expose private String gender;
    @Expose private String nationality;
    @Expose private String type;
    @Expose private String popularname;


    public BioData() {
    }

    public BioData(String firstname, String lastname, String othernames, String dateofbirth, int age, String birthplace, String telephone, String email, String gender, String nationality) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.othernames = othernames;
        this.dateofbirth = dateofbirth;
        this.age = age;
        this.birthplace = birthplace;
        this.telephone = telephone;
        this.email = email;
        this.gender = gender;
        this.nationality = nationality;
    }

    public String getPopularname() {
        return popularname;
    }

    public void setPopularname(String popularname) {
        this.popularname = popularname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getOthernames() {
        return othernames;
    }

    public void setOthernames(String othernames) {
        this.othernames = othernames;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
