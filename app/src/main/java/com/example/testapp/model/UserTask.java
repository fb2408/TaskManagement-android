package com.example.testapp.model;


public class UserTask {

    private Integer id;

    private Boolean finished = false;

    private Boolean reqRedirect = false;


    private User user;

    private Task task;

    private OrganizationUnit organizationUnit;


    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public Boolean getReqRedirect() {
        return reqRedirect;
    }

    public void setReqRedirect(Boolean reqRedirect) {
        this.reqRedirect = reqRedirect;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User worker) {
        this.user = user;
    }

    public OrganizationUnit getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(OrganizationUnit organizationUnit) {
        this.organizationUnit = organizationUnit;
    }


}
