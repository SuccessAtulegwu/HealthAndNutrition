package com.example.healthandnutrition.model;

public class Model {
   private String Name;
   private String Image;

    public Model() {
    }

    public Model(String name, String image) {
        Name = name;
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
