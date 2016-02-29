package com.silentdevelopers.facultyrater.main_body;

/**
 * Created by varun on 28/2/16.
 */
public class DataProviderFaculty {

    private int img_res;
    private String name, designation, experience, time;

    public DataProviderFaculty(int img_res, String name, String designation, String experience, String time) {
        this.img_res = img_res;
        this.name = name;
        this.designation = designation;
        this.experience = experience;
        this.time = time;
    }

    public void setImg_res(int img_res) {
        this.img_res = img_res;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDesignation() {
        return designation;

    }

    public String getExperience() {
        return experience;
    }

    public String getTime() {
        return time;
    }

    public int getImg_res() {
        return img_res;
    }

    public String getName() {
        return name;
    }
}