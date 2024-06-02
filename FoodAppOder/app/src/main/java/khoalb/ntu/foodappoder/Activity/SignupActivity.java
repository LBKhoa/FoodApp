package khoalb.ntu.foodappoder.Activity;

import android.content.Intent;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import khoalb.ntu.foodappoder.Helper.CartHelper;
import khoalb.ntu.foodappoder.Helper.UserHelper;
import khoalb.ntu.foodappoder.R;

public class SignupActivity extends AppCompatActivity {
    EditText signupName, signupEmail, signupUsername, signupPassword;
    TextView loginRedirecTxt;
    Button signupBtn;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Liên kết các thành phần giao diện với các biến tương ứng
        signupName = findViewById(R.id.edtSignupName);
        signupEmail = findViewById(R.id.edtSignupEmail);
        signupUsername = findViewById(R.id.edtSignupUser);
        signupPassword = findViewById(R.id.edtSignupPass);
        signupBtn = findViewById(R.id.signupBtn);
        loginRedirecTxt = findViewById(R.id.loginRedirecTxt);

        // Định nghĩa hành động khi nút "signupBtn" được nhấp vào
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khởi tạo Firebase Database và DatabaseReferences
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                // Lấy thông tin từ các trường nhập liệu
                String name = signupName.getText().toString();
                String email = signupEmail.getText().toString();
                String username = signupUsername.getText().toString();
                String password = signupPassword.getText().toString();

                // Tạo một đối tượng CartHelper mới và thêm các mục vào giỏ hàng
                CartHelper cartHelper = new CartHelper();
                List<CartHelper.Item> items = new ArrayList<>();
                items.add(new CartHelper.Item("item1", 1));
                items.add(new CartHelper.Item("item2", 2));
                cartHelper.setItems(items);

                // Tạo một đối tượng UserHelper mới và thêm vào database
                UserHelper userHelper = new UserHelper(name, email, username, password, cartHelper);
                reference.child(username).setValue(userHelper)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Hiển thị thông báo đăng ký thành công và chuyển đến màn hình đăng nhập
                                    Toast.makeText(SignupActivity.this, "Bạn đã đăng ký thành công", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                } else {
                                    // Xử lý lỗi nếu việc tạo người dùng thất bại
                                    Log.e("Firebase", "Tạo người dùng thất bại", task.getException());
                                    Toast.makeText(SignupActivity.this, "Lỗi tạo người dùng", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });



        // Định nghĩa hành động khi văn bản "loginRedirecTxt" được nhấp vào
        loginRedirecTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến màn hình đăng nhập
                Intent intent =  new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
