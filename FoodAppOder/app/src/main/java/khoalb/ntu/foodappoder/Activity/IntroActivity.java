package khoalb.ntu.foodappoder.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import khoalb.ntu.foodappoder.R;
import khoalb.ntu.foodappoder.databinding.ActivityIntroBinding;


public class IntroActivity extends BaseActivity {
    ActivityIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Gắn kết dữ liệu của Activity với layout
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Thiết lập các biến và sự kiện
        setVariable();
        getWindow().setStatusBarColor(Color.parseColor("#FFE485"));
    }

    // Thiết lập các biến và sự kiện
    private void setVariable() {
        // Sự kiện khi nhấn nút "Đăng nhập"
        binding.loginBtn.setOnClickListener(v -> {
            // Kiểm tra nếu người dùng đã đăng nhập
            if (mAuth.getCurrentUser() != null) {
                startActivity(new Intent(IntroActivity.this, MainActivity.class));
            } else {
                startActivity(new Intent(IntroActivity.this, LoginActivity.class));
            }
        });

        // Sự kiện khi nhấn nút "Đăng ký"
        binding.signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("IntroActivity", "Signup button clicked");
                startActivity(new Intent(IntroActivity.this, SignupActivity.class));
            }
        });
    }
}
