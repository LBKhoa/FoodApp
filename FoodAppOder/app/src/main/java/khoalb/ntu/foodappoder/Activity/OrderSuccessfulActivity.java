package khoalb.ntu.foodappoder.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import khoalb.ntu.foodappoder.R;

public class OrderSuccessfulActivity extends AppCompatActivity {
    Button btnHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_successful);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Tìm và liên kết nút "btnHome" từ tệp tin layout XML
        btnHome = findViewById(R.id.btnHome);

        // Định nghĩa hành động khi nút "btnHome" được nhấp vào
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để chuyển đến MainActivity khi nút "btnHome" được nhấp vào
                Intent intent = new Intent(OrderSuccessfulActivity.this, MainActivity.class);
                startActivity(intent); // Bắt đầu activity mới
            }
        });
    }
}
