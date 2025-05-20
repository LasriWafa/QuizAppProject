package com.example.quizdeculturegnrale.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.quizdeculturegnrale.model.Question;
import java.util.List;

@Dao
public interface QuestionDao {
    @Insert
    void insert(Question question);
    
    @Insert
    void insertAll(List<Question> questions);
    
    @Query("SELECT * FROM questions WHERE category = :category")
    List<Question> getQuestionsByCategory(String category);
    
    @Query("SELECT * FROM questions")
    List<Question> getAllQuestions();
    
    @Query("SELECT DISTINCT category FROM questions")
    List<String> getAllCategories();
}
