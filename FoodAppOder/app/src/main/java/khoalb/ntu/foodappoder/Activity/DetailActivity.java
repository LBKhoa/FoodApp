package khoalb.ntu.foodappoder.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import khoalb.ntu.foodappoder.Domain.Foods;
import khoalb.ntu.foodappoder.Helper.ManagmentCart;
import khoalb.ntu.foodappoder.R;
import khoalb.ntu.foodappoder.databinding.ActivityDetailBinding;

public class DetailActivity extends BaseActivity {
    ActivityDetailBinding binding;
    private Foods object;
    private int num = 1;
    private ManagmentCart managementCart;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Nhận dữ liệu được truyền qua Intent
        getIntentExtra();

        // Thiết lập các biến và sự kiện
        setVariable();
    }

    // Thiết lập các biến và sự kiện
    private void setVariable() {
        managementCart = new ManagmentCart(this);

        // Sự kiện khi nhấn nút "Back"
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Hiển thị thông tin sản phẩm
        Glide.with(DetailActivity.this)
                .load(object.getImagePath())
                .into(binding.pic);
        binding.priceTxt.setText(object.getPrice()+" VNĐ");
        binding.titleTxt.setText(object.getTitle());
        binding.decriptionTxt.setText(object.getDescription());
        binding.rateTxt.setText(object.getStar()+"Đánh giá");
        binding.ratingBar.setRating((float) object.getStar());
        binding.totalTxt.setText((num*object.getPrice()+" VNĐ"));

        // Sự kiện khi nhấn nút "Tăng số lượng"
        binding.plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = num + 1;
                binding.numTxt.setText(num+" ");
                binding.totalTxt.setText((num * object.getPrice()+" VNĐ"));
            }
        });

        // Sự kiện khi nhấn nút "Giảm số lượng"
        binding.minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num > 1){
                    num = num - 1;
                    binding.numTxt.setText(num+" ");
                    binding.totalTxt.setText((num * object.getPrice()+" VNĐ"));
                }
            }
        });

        // Sự kiện khi nhấn nút "Thêm vào giỏ hàng"
        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                object.setNumberInCart(num);
                managementCart.insertFood(object);
            }
        });
    }

    // Nhận dữ liệu từ Intent
    private void getIntentExtra() {
        object = (Foods) getIntent().getSerializableExtra("object");
    }
}
