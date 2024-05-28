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
    ActivityDetailBinding biding;
    private Foods object;
    private int num = 1;
    private ManagmentCart managmentCart;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        biding = ActivityDetailBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(biding.getRoot());
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getIntentExtra();
        setVariable();

    }

    private void setVariable() {
        managmentCart = new ManagmentCart(this);
        biding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Glide.with(DetailActivity.this)
                .load(object.getImagePath())
                .into(biding.pic);
        biding.priceTxt.setText("$"+object.getPrice());
        biding.titleTxt.setText(object.getTitle());
        biding.decriptionTxt.setText(object.getDescription());
        biding.rateTxt.setText(object.getStar()+"Đánh giá");
        biding.ratingBar.setRating((float) object.getStar());
        biding.totalTxt.setText((num*object.getPrice()+"$"));

        biding.plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = num + 1;
                biding.numTxt.setText(num+" ");
                biding.totalTxt.setText("$"+(num * object.getPrice()));
            }
        });
        biding.minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num > 1){
                    num = num - 1;
                    biding.numTxt.setText(num+" ");
                    biding.totalTxt.setText("$"+(num * object.getPrice()));
                }
            }
        });
        biding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                object.setNumberInCart(num);
                managmentCart.insertFood(object);
            }
        });
    }

    private void getIntentExtra() {
        object = (Foods) getIntent().getSerializableExtra("object");
    }
}