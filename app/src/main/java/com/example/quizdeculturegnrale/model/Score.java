package com.example.quizdeculturegnrale.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.example.quizdeculturegnrale.db.Converters;
import java.time.LocalDate;

@Entity(tableName = "scores",
        foreignKeys = {
            @ForeignKey(entity = User.class,
                    parentColumns = "id",
                    childColumns = "userId",
                    onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index("userId")})
@TypeConverters(Converters.class)
public class Score {
    @PrimaryKey(autoGenerate = true)
    private long id;
    
    private long userId;
    private String category;
    private int score;
    private int totalQuestions;
    private LocalDate date;
    
    // Constructor
    public Score(long userId, String category, int score, int totalQuestions, LocalDate date) {
        this.userId = userId;
        this.category = category;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.date = date;
    }
    
    // Getters and Setters
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public long getUserId() {
        return userId;
    }
    
    public void setUserId(long userId) {
        this.userId = userId;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public int getTotalQuestions() {
        return totalQuestions;
    }
    
    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
} 