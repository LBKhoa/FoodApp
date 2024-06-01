package khoalb.ntu.foodappoder.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import khoalb.ntu.foodappoder.R;

// BaseActivity là lớp cơ sở cho tất cả các Activity trong ứng dụng
public class BaseActivity extends AppCompatActivity {
    // Khai báo các biến
    FirebaseAuth mAuth; // Đối tượng Firebase Authentication
    FirebaseDatabase database; // Đối tượng Firebase Database

    // TAG được sử dụng cho việc logging, giúp xác định nơi thông báo
    public String TAG = "uilover";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Khởi tạo đối tượng Firebase Database
        database = FirebaseDatabase.getInstance();
        // Khởi tạo đối tượng Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Thiết lập màu của thanh trạng thái
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
    }
}
