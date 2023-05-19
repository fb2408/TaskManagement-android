package com.example.testapp.model;





public class User {

    private Integer id;

    private String name;


    private String gender;


    private Boolean active = false;


    private Integer ability;


    private Integer workingYears;

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public Boolean getRegistred() {
        return registered;
    }

    public void setRegistred(Boolean registred) {
        this.registered = registred;
    }

    private String deviceToken;

    private Boolean registered;

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Boolean getWorker() {
        return isWorker;
    }

    public void setWorker(Boolean worker) {
        isWorker = worker;
    }

    private Boolean isAdmin;

    private Boolean isWorker;


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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getAbility() {
        return ability;
    }

    public void setAbility(Integer ability) {
        this.ability = ability;
    }

    public Integer getWorkingYears() {
        return workingYears;
    }

    public void setWorkingYears(Integer workingYears) {
        this.workingYears = workingYears;
    }

}
