package khoalb.ntu.foodappoder.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import khoalb.ntu.foodappoder.R;
import khoalb.ntu.foodappoder.databinding.ActivityMainBinding;
import khoalb.ntu.foodappoder.databinding.ActivitySignupBinding;

public class SignupActivity extends BaseActivity {
    ActivitySignupBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setVariable();
    }

    private void setVariable() {
        binding.signupBtn.setOnClickListener(v -> {
            String email = binding.userEdt.getText().toString();
            String password = binding.passEdt.getText().toString();

            if (password.length() < 6){
                Toast.makeText(SignupActivity.this,"Mật khẩu của bạn không đủ 6 ký tự",Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SignupActivity.this, task -> {
                if (task.isSuccessful()){
                    Log.i(TAG,"onComplete: ");
                    startActivity(new Intent(SignupActivity.this,MainActivity.class));
                }else {
                    Log.i(TAG,"failure: "+task.getException());
                    Toast.makeText(SignupActivity.this,"Authentication failed", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}