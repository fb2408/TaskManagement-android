package com.example.testapp.model;

public class TaskType {

    private Integer id;
    private String taskTypeName;

    public TaskType() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTaskTypeName() {
        return taskTypeName;
    }

    public void setTaskTypeName(String taskTypeName) {
        this.taskTypeName = taskTypeName;
    }

    public TaskType (Integer id, String taskTypeName){
        this.id = id;
        this.taskTypeName = taskTypeName;
    }

}
