package com.example.quizdeculturegnrale.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.quizdeculturegnrale.model.Score;
import java.util.List;

@Dao
public interface ScoreDao {
    @Insert
    void insert(Score score);
    
    @Query("SELECT * FROM scores WHERE userId = :userId ORDER BY date DESC")
    List<Score> getUserScores(long userId);
    
    @Query("SELECT * FROM scores WHERE userId = :userId AND category = :category ORDER BY date DESC")
    List<Score> getUserScoresByCategory(long userId, String category);
    
    @Query("SELECT * FROM scores ORDER BY score DESC LIMIT 10")
    List<Score> getTopScores();
} 