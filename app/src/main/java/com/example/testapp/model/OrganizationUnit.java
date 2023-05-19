package com.example.testapp.model;

import android.os.LocaleList;

import java.time.LocalTime;

public class OrganizationUnit {

    private Integer id;

    private String name;

    private String openTime;

    private String closeTime;

    private OrganizationUnit superorganizationunit;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public OrganizationUnit getSuperorganizationunit() {
        return superorganizationunit;
    }

    public void setSuperorganizationunit(OrganizationUnit superorganizationunit) {
        this.superorganizationunit = superorganizationunit;
    }
}
