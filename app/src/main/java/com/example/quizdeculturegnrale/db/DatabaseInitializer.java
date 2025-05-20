package com.example.quizdeculturegnrale.db;

import android.content.Context;
import android.util.Log;
import com.example.quizdeculturegnrale.model.Question;
import java.util.Arrays;
import java.util.List;

public class DatabaseInitializer {
    private static final String TAG = "DatabaseInitializer";

    public static void initializeDatabase(Context context) {
        Log.d(TAG, "Starting database initialization");
        AppDatabase db = AppDatabase.getInstance(context);
        
        // Check if database is empty
        if (db.questionDao().getAllQuestions().isEmpty()) {
            Log.d(TAG, "Database is empty, inserting sample questions");
            
            // Sample questions for Sports category
            Question sport1 = new Question(
                "Which country won the FIFA World Cup in 2018?",
                new String[]{"Brazil", "Germany", "France", "Argentina"},
                2, // France is correct
                "sport"
            );

            Question sport2 = new Question(
                "Who holds the record for most Olympic gold medals?",
                new String[]{"Usain Bolt", "Michael Phelps", "Carl Lewis", "Mark Spitz"},
                1, // Michael Phelps is correct
                "sport"
            );

            Question sport3 = new Question(
                "In which sport would you perform a slam dunk?",
                new String[]{"Football", "Basketball", "Tennis", "Golf"},
                1, // Basketball is correct
                "sport"
            );

            // Sample questions for Science category
            Question science1 = new Question(
                "What is the chemical symbol for gold?",
                new String[]{"Ag", "Fe", "Au", "Cu"},
                2, // Au is correct
                "science"
            );

            Question science2 = new Question(
                "Which planet is known as the Red Planet?",
                new String[]{"Venus", "Mars", "Jupiter", "Saturn"},
                1, // Mars is correct
                "science"
            );

            Question science3 = new Question(
                "What is the hardest natural substance on Earth?",
                new String[]{"Gold", "Iron", "Diamond", "Platinum"},
                2, // Diamond is correct
                "science"
            );

            // Sample questions for History category
            Question history1 = new Question(
                "In which year did World War II end?",
                new String[]{"1943", "1945", "1947", "1950"},
                1, // 1945 is correct
                "history"
            );

            Question history2 = new Question(
                "Who was the first President of the United States?",
                new String[]{"Thomas Jefferson", "John Adams", "George Washington", "Benjamin Franklin"},
                2, // George Washington is correct
                "history"
            );

            Question history3 = new Question(
                "The Great Wall of China was built during which dynasty?",
                new String[]{"Ming", "Qin", "Han", "Tang"},
                1, // Qin is correct
                "history"
            );

            // Sample questions for Computer category
            Question computer1 = new Question(
                "What does CPU stand for?",
                new String[]{"Central Processing Unit", "Computer Processing Unit", "Central Process Unit", "Computer Process Unit"},
                0, // Central Processing Unit is correct
                "computer"
            );

            Question computer2 = new Question(
                "Which programming language was created by James Gosling?",
                new String[]{"Python", "Java", "C++", "JavaScript"},
                1, // Java is correct
                "computer"
            );

            Question computer3 = new Question(
                "What is the main function of RAM?",
                new String[]{"Long-term storage", "Temporary storage", "Processing data", "Displaying images"},
                1, // Temporary storage is correct
                "computer"
            );

            // Sample questions for Inventions category
            Question invention1 = new Question(
                "Who invented the telephone?",
                new String[]{"Thomas Edison", "Alexander Graham Bell", "Nikola Tesla", "Albert Einstein"},
                1, // Alexander Graham Bell is correct
                "invention"
            );

            Question invention2 = new Question(
                "In which year was the first iPhone released?",
                new String[]{"2005", "2006", "2007", "2008"},
                2, // 2007 is correct
                "invention"
            );

            Question invention3 = new Question(
                "Who is credited with inventing the World Wide Web?",
                new String[]{"Bill Gates", "Tim Berners-Lee", "Steve Jobs", "Mark Zuckerberg"},
                1, // Tim Berners-Lee is correct
                "invention"
            );

            // Sample questions for General Knowledge category
            Question general1 = new Question(
                "What is the capital of France?",
                new String[]{"London", "Berlin", "Paris", "Madrid"},
                2, // Paris is correct
                "general"
            );

            Question general2 = new Question(
                "Which is the largest ocean on Earth?",
                new String[]{"Atlantic", "Indian", "Arctic", "Pacific"},
                3, // Pacific is correct
                "general"
            );

            Question general3 = new Question(
                "How many continents are there?",
                new String[]{"5", "6", "7", "8"},
                2, // 7 is correct
                "general"
            );

            // Insert all questions
            List<Question> questions = Arrays.asList(
                sport1, sport2, sport3,
                science1, science2, science3,
                history1, history2, history3,
                computer1, computer2, computer3,
                invention1, invention2, invention3,
                general1, general2, general3
            );
            
            try {
                db.questionDao().insertAll(questions);
                Log.d(TAG, "Successfully inserted " + questions.size() + " questions");
            } catch (Exception e) {
                Log.e(TAG, "Error inserting questions: " + e.getMessage());
            }
        } else {
            Log.d(TAG, "Database already contains questions, skipping initialization");
        }
    }
} 