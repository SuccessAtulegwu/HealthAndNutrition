package com.example.healthandnutrition.model;

public class MenuModel{

    private String image;
    private String menuId;
    private String name;
    private  String why;
    private  String benefits;
    private  String nutrients;
    private String about;

    public MenuModel() {
    }

    public MenuModel(String image, String menuId, String name, String why, String benefits, String nutrients, String about) {
        this.image = image;
        this.menuId = menuId;
        this.name = name;
        this.why = why;
        this.benefits = benefits;
        this.nutrients = nutrients;
        this.about = about;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWhy() {
        return why;
    }

    public void setWhy(String why) {
        this.why = why;
    }

    public String getBenefits() {
        return benefits;
    }

    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }

    public String getNutrients() {
        return nutrients;
    }

    public void setNutrients(String nutrients) {
        this.nutrients = nutrients;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}





