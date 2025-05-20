package com.example.quizdeculturegnrale.repository;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import com.example.quizdeculturegnrale.dao.UserDao;
import com.example.quizdeculturegnrale.db.AppDatabase;
import com.example.quizdeculturegnrale.model.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private final UserDao userDao;
    private final ExecutorService executor;
    private final SharedPreferences sharedPreferences;
    private final Handler mainHandler;

    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        userDao = db.userDao();
        executor = Executors.newSingleThreadExecutor();
        sharedPreferences = application.getSharedPreferences("userPrefs", Application.MODE_PRIVATE);
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public void register(User user, OnUserOperationCallback callback) {
        executor.execute(() -> {
            try {
                User existingUser = userDao.getUserByEmail(user.getEmail());
                if (existingUser != null) {
                    callback.onError("Email déjà utilisé");
                    return;
                }

                long userId = userDao.insert(user);
                callback.onSuccess(userId);
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }

    public void login(String email, String password, OnUserOperationCallback callback) {
        executor.execute(() -> {
            try {
                User user = userDao.login(email, password);
                if (user != null) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong("userId", user.getId());
                    editor.putString("username", user.getUsername());
                    editor.putString("email", user.getEmail());
                    editor.apply();
                    callback.onSuccess(user.getId());
                } else {
                    callback.onError("Email ou mot de passe invalide");
                }
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }

    public void getUserById(long userId, OnUserDataCallback callback) {
        executor.execute(() -> {
            User user = userDao.getUserById(userId);
            mainHandler.post(() -> {
                if (user != null) {
                    callback.onSuccess(user);
                } else {
                    callback.onError("Utilisateur non trouvé");
                }
            });
        });
    }

    public void updateUserProfile(User user, OnUserOperationCallback callback) {
        executor.execute(() -> {
            try {
                userDao.updateUser(user);
                mainHandler.post(() -> callback.onSuccess(user.getId()));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    public void updateUserAddress(long userId, String address, OnUserOperationCallback callback) {
        executor.execute(() -> {
            try {
                User user = userDao.getUserById(userId);
                if (user != null) {
                    user.setAddress(address);
                    userDao.updateUser(user);
                    mainHandler.post(() -> callback.onSuccess(userId));
                } else {
                    mainHandler.post(() -> callback.onError("Utilisateur non trouvé"));
                }
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    public interface OnUserDataCallback {
        void onSuccess(User user);
        void onError(String error);
    }

    public interface OnUserOperationCallback {
        void onSuccess(long userId);
        void onError(String error);
    }
}
