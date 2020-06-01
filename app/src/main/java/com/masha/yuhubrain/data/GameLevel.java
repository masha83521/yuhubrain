package com.masha.yuhubrain.data;

import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public class GameLevel {
    private String name;
    private List<DocumentReference> questions;
    private String description;
    private String imageLink;
    private String id;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getDescription() {
        return description;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DocumentReference> getQuestions() {
        return questions;
    }

    public void setQuestions(List<DocumentReference> questions) {
        this.questions = questions;
    }
    public GameLevel(){}
    public GameLevel(String id,String name, List<DocumentReference> questions, String description, String imageLink){
        this.id = id;
        this.name = name;
        this.questions = questions;
        this.description = description;
        this.imageLink = imageLink;
    }
}
