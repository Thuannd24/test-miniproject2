package com.example.miniproject_2.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.miniproject_2.dao.CategoryDao;
import com.example.miniproject_2.dao.OrderDao;
import com.example.miniproject_2.dao.OrderDetailDao;
import com.example.miniproject_2.dao.ProductDao;
import com.example.miniproject_2.dao.UserDao;
import com.example.miniproject_2.entity.Category;
import com.example.miniproject_2.entity.Order;
import com.example.miniproject_2.entity.OrderDetail;
import com.example.miniproject_2.entity.Product;
import com.example.miniproject_2.entity.User;

@Database(entities = {User.class, Category.class, Product.class, Order.class, OrderDetail.class}, version = 2, exportSchema = false)
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
    public abstract ProductDao productDao();
    public abstract OrderDao orderDao();
    public abstract OrderDetailDao orderDetailDao();

    public void seedInitialData() {
        if (userDao().findByUsername("admin") == null) {
            userDao().insert(new User("admin", "admin", "Administrator", "admin"));
            userDao().insert(new User("user", "123", "Regular User", "user"));

            long cat1 = categoryDao().insert(new Category("Điện tử"));
            long cat2 = categoryDao().insert(new Category("Thời trang"));
            long cat3 = categoryDao().insert(new Category("Gia dụng"));

            productDao().insert(new Product((int)cat1, "iPhone 15 Pro Max", 1299.00, "Màn hình 6.7 inch, chip A17 Pro", "https://vcdn1-sohoa.vnecdn.net/2023/09/13/iphone-15-pro-max-finish-select-202309-6-7-inch-blue-titanium-1694572233.jpg"));
            productDao().insert(new Product((int)cat1, "Samsung Galaxy S24 Ultra", 1199.00, "Camera 200MP, S Pen tích hợp", "https://images.samsung.com/is/image/samsung/p6pim/vn/2401/gallery/vn-galaxy-s24-s928-sm-s928bztqvxx-539327823"));
            productDao().insert(new Product((int)cat2, "Áo sơ mi nam", 25.00, "Chất liệu cotton thoáng mát", "https://product.hstatic.net/1000366086/product/1_5613437e6b72449a888c7f3e69f848f0_master.jpg"));
            productDao().insert(new Product((int)cat2, "Quần Jean Nữ", 35.00, "Dáng ôm thời trang", "https://bizweb.dktcdn.net/100/415/697/products/quan-jean-nu-lung-cao-dang-om-jean-giay-qj2110-3.jpg"));
            productDao().insert(new Product((int)cat3, "Nồi chiên không dầu", 150.00, "Dung tích 5L, công suất 1500W", "https://m.media-amazon.com/images/I/71YvC6fWnAL._AC_SL1500_.jpg"));
            productDao().insert(new Product((int)cat3, "Máy hút bụi", 200.00, "Công nghệ hút lốc xoáy cực mạnh", "https://m.media-amazon.com/images/I/61kGv6T+6vL._AC_SL1500_.jpg"));
        }
    }
}
