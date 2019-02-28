package com.softmastersgroup.umo.umoagent.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Auth implements Serializable {

    @Expose private String userid;
    @Expose private String firstname;
    @Expose private String lastname;
    @Expose private String othernames;
    @Expose private String phone;
    @Expose private int age;
    @Expose private String gender;
    @Expose private String nationality;
    @Expose private String phone_verified_at;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

    public String getPhone_verified_at() {
        return phone_verified_at;
    }

    public void setPhone_verified_at(String phone_verified_at) {
        this.phone_verified_at = phone_verified_at;
    }
}
