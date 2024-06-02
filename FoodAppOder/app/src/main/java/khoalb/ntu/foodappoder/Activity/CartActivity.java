package khoalb.ntu.foodappoder.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import khoalb.ntu.foodappoder.Adapter.CartAdapter;
import khoalb.ntu.foodappoder.Helper.ChangeNumberItemsListener;
import khoalb.ntu.foodappoder.Helper.ManagmentCart;
import khoalb.ntu.foodappoder.databinding.ActivityCartBinding;

public class CartActivity extends BaseActivity {
    private ActivityCartBinding binding;
    private RecyclerView.Adapter adapter;
    private ManagmentCart managmentCart;
    private double tax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Bắt đầu quá trình gắn kết dữ liệu của Activity với giao diện
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        // Thiết lập các biến
        managmentCart = new ManagmentCart(this);

        // Gọi các phương thức để cài đặt giao diện và tính toán giỏ hàng
        setVariable1();
        setVariable2();
        calculateCart();
        initList();
    }

    // Khởi tạo danh sách đơn hàng
    private void initList() {
        // Kiểm tra nếu giỏ hàng trống
        if (managmentCart.getListCart().isEmpty()){
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        binding.cartView.setLayoutManager(linearLayoutManager);
        adapter = new CartAdapter(managmentCart.getListCart(), this, new ChangeNumberItemsListener() {
            @Override
            public void change() {
                calculateCart();
            }
        });
        binding.cartView.setAdapter(adapter);
    }

    // Tính toán tổng giỏ hàng
    private void calculateCart() {
        int delivery = 10000;
        long total = Math.round(managmentCart.getTotalFee()+ delivery);
        long itemTotal = Math.round(managmentCart.getTotalFee());

        binding.totalFeeTxt.setText(itemTotal+" VNĐ");
        binding.deliveryTxt.setText(delivery+" VNĐ");
        binding.totalTxt.setText(total+" VNĐ");
    }

    // Thiết lập sự kiện khi nhấn nút "Back"
    private void setVariable1() {
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // Thiết lập sự kiện khi nhấn nút "Order"
    private void setVariable2() {
        binding.orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xóa các mục trong giỏ hàng trước khi chuyển sang màn hình thành công
                managmentCart.clearCart();

                Intent intent =  new Intent(CartActivity.this, OrderSuccessfulActivity.class);
                startActivity(intent);
            }
        });
    }
}
