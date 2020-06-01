package com.masha.yuhubrain.data;

import com.google.firebase.firestore.DocumentReference;

public class GameQuestion {
    private DocumentReference anwser1;
    private DocumentReference anwser2;
    private DocumentReference correctAnwser;
    private String id;
    private String name;

    public DocumentReference getAnwser1() {
        return anwser1;
    }

    public DocumentReference getAnwser2() {
        return anwser2;
    }

    public void setAnwser1(DocumentReference anwser1) {
        this.anwser1 = anwser1;
    }

    public void setAnwser2(DocumentReference anwser2) {
        this.anwser2 = anwser2;
    }

    public void setCorrectAnwser(DocumentReference correctAnwser) {
        this.correctAnwser = correctAnwser;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DocumentReference getCorrectAnwser() {
        return correctAnwser;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public GameQuestion(String id, String name, DocumentReference awnser1, DocumentReference awnser2, DocumentReference correctAnwser){
        this.id = id;
        this.name = name;
        this.anwser1 = awnser1;
        this.anwser2 = awnser2;
    }
    public GameQuestion(){}
}
