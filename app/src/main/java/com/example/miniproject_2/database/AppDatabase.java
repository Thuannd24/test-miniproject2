package com.example.miniproject_2.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.miniproject_2.dao.CategoryDao;
import com.example.miniproject_2.dao.UserDao;
import com.example.miniproject_2.entity.Category;
import com.example.miniproject_2.entity.User;

@Database(entities = {User.class, Category.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "miniproject2.db";
    private static volatile AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            DB_NAME
                    )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return instance;
    }

    public abstract UserDao userDao();
    public abstract CategoryDao categoryDao();

    public void seedInitialData() {
        if (userDao().findByUsername("admin") == null) {
            userDao().insert(new User("admin", "admin", "Administrator", "admin"));
            userDao().insert(new User("user", "123", "Regular User", "user"));

            categoryDao().insert(new Category("Điện tử"));
            categoryDao().insert(new Category("Thời trang"));
            categoryDao().insert(new Category("Gia dụng"));
        }
    }
}
