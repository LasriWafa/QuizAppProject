package com.example.quizdeculturegnrale.db;

import android.content.Context;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.quizdeculturegnrale.dao.QuestionDao;
import com.example.quizdeculturegnrale.dao.ScoreDao;
import com.example.quizdeculturegnrale.dao.UserDao;
import com.example.quizdeculturegnrale.model.User;
import com.example.quizdeculturegnrale.model.Question;
import com.example.quizdeculturegnrale.model.Score;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class AppDatabaseTest {
    private AppDatabase db;
    private UserDao userDao;
    private QuestionDao questionDao;
    private ScoreDao scoreDao;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        userDao = db.userDao();
        questionDao = db.questionDao();
        scoreDao = db.scoreDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void writeAndReadUser() {
        User user = new User("testUser", "test@example.com", "password");
        long userId = userDao.insert(user);
        User loadedUser = userDao.getUserByEmail("test@example.com");
        assertNotNull(loadedUser);
        assertEquals("testUser", loadedUser.getUsername());
    }

    @Test
    public void writeAndReadQuestion() {
        Question question = new Question(
            "Test question?",
            new String[]{"A", "B", "C", "D"},
            0,
            "test"
        );
        questionDao.insert(question);
        List<Question> questions = questionDao.getQuestionsByCategory("test");
        assertFalse(questions.isEmpty());
        assertEquals("Test question?", questions.get(0).getQuestionText());
    }

    @Test
    public void writeAndReadScore() {
        User user = new User("testUser", "test@example.com", "password");
        long userId = userDao.insert(user);

        Score score = new Score((int)userId, "test", 10, 3, LocalDate.parse("2025-12-03"));
        scoreDao.insert(score);
        
        List<Score> scores = scoreDao.getUserScores((int)userId);
        assertFalse(scores.isEmpty());
        assertEquals(10, scores.get(0).getScore());
    }
} 