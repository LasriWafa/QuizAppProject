package com.example.quizdeculturegnrale.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.example.quizdeculturegnrale.db.Converters;

@Entity(tableName = "questions")
@TypeConverters(Converters.class)
public class Question {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String questionText;
    private String[] options;
    private int correctOptionIndex;
    private String category; // e.g., "sport", "science", "education"
    
    // Constructor
    public Question(String questionText, String[] options, int correctOptionIndex, String category) {
        this.questionText = questionText;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
        this.category = category;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getQuestionText() {
        return questionText;
    }
    
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
    
    public String[] getOptions() {
        return options;
    }
    
    public void setOptions(String[] options) {
        this.options = options;
    }
    
    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }
    
    public void setCorrectOptionIndex(int correctOptionIndex) {
        this.correctOptionIndex = correctOptionIndex;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
}
