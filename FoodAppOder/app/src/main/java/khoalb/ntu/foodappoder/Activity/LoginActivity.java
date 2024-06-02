package khoalb.ntu.foodappoder.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.Objects;

import khoalb.ntu.foodappoder.Helper.CartHelper;
import khoalb.ntu.foodappoder.R;

public class LoginActivity extends AppCompatActivity {
    EditText loginUsername, loginPassword;
    Button loginBtn;
    TextView signupRedirecTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ các thành phần giao diện
        loginUsername = findViewById(R.id.edtLoginUser);
        loginPassword = findViewById(R.id.edtLoginPass);
        loginBtn = findViewById(R.id.loginBtn);
        signupRedirecTxt = findViewById(R.id.sigupRedirecTxt);

        // Sự kiện khi nhấn nút "Đăng nhập"
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra tính hợp lệ của username và password
                if (!validateUsername() | !validatePassword()){

                }else {
                    // Kiểm tra thông tin người dùng
                    checkUser();
                }
            }
        });

        // Sự kiện khi nhấn chuyển hướng đến trang đăng ký
        signupRedirecTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    // Phương thức kiểm tra tính hợp lệ của username
    public Boolean validateUsername(){
        String val = loginUsername.getText().toString();
        if (val.isEmpty()){
            loginUsername.setError("Username không được để trống");
            return false;
        }else {
            loginUsername.setError(null);
            return true;
        }
    }

    // Phương thức kiểm tra tính hợp lệ của password
    public Boolean validatePassword(){
        String val = loginPassword.getText().toString();
        if (val.isEmpty()){
            loginPassword.setError("Password không được để trống");
            return false;
        }else {
            loginPassword.setError(null);
            return true;
        }
    }

    // Phương thức kiểm tra thông tin của người dùng
    public void checkUser() {
        String userUserName = loginUsername.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUserName);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    loginUsername.setError(null);
                    String passwordFromDB = snapshot.child(userUserName).child("password").getValue(String.class);

                    if (Objects.equals(passwordFromDB, userPassword)) {
                        String fullNameFromDB = snapshot.child(userUserName).child("name").getValue(String.class);

                        // Lấy giỏ hàng của người dùng từ Firebase
                        DatabaseReference cartReference = FirebaseDatabase.getInstance().getReference("carts").child(userUserName);
                        cartReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot cartSnapshot) {
                                CartHelper cart = cartSnapshot.getValue(CartHelper.class);
                                    // Lưu tên người dùng và giỏ hàng vào SharedPreferences
                                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("username", userUserName); // Lưu tên người dùng
                                    editor.putString("cart", new Gson().toJson(cart)); // Lưu giỏ hàng dưới dạng JSON
                                    editor.apply();

                                    // Chuyển đến MainActivity sau khi đăng nhập thành công
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("name", fullNameFromDB); // truyền tên đầy đủ
                                    startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("Firebase", "Lỗi khi lấy dữ liệu giỏ hàng", error.toException());
                                Toast.makeText(LoginActivity.this, "Lỗi khi lấy dữ liệu giỏ hàng", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        loginPassword.setError("Thông tin đăng nhập không hợp lệ");
                        loginPassword.requestFocus();
                    }
                } else {
                    loginUsername.setError("Người dùng không tồn tại");
                    loginUsername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi khi hủy kết nối với Firebase
                Toast.makeText(LoginActivity.this, "Lỗi khi kết nối với Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
