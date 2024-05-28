package khoalb.ntu.foodappoder.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

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

        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupUsername = findViewById(R.id.signup_user);
        signupPassword = findViewById(R.id.signup_pass);
        signupBtn = findViewById(R.id.signupBtn);
        loginRedirecTxt = findViewById(R.id.loginRedirecTxt);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                String name = signupName.getText().toString();
                String email = signupEmail.getText().toString();
                String username = signupUsername.getText().toString();
                String password = signupPassword.getText().toString();

                UserHelper userHelper = new UserHelper(name, email, username, password);
                reference.child(username).setValue(userHelper);

                // Create a new cart node for the user
                DatabaseReference cartRef = reference.child(username).child("cart");
                Map<String, Object> initialCartData = new HashMap<>();
                initialCartData.put("items", new HashMap<>()); // Initialize cart items as empty
                initialCartData.put("total", 0); // Initialize total as 0
                cartRef.setValue(initialCartData);

                Toast.makeText(SignupActivity.this, "Bạn đã đăng ký thành công", Toast.LENGTH_SHORT).show();
                Intent intent =  new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });




        loginRedirecTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}