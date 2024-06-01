package khoalb.ntu.foodappoder.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;

import khoalb.ntu.foodappoder.Domain.Foods;
import khoalb.ntu.foodappoder.Helper.ChangeNumberItemsListener;
import khoalb.ntu.foodappoder.Helper.ManagmentCart;
import khoalb.ntu.foodappoder.R;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewholdel> {
    ArrayList<Foods> list;
    private ManagmentCart managmentCart;
    ChangeNumberItemsListener changeNumberItemsListener;

    // Constructor để nhận danh sách mặt hàng trong giỏ hàng, Context và ChangeNumberItemsListener
    public CartAdapter(ArrayList<Foods> list, Context context, ChangeNumberItemsListener changeNumberItemsListener) {
        this.list = list;
        managmentCart = new ManagmentCart(context);
        this.changeNumberItemsListener = changeNumberItemsListener;
    }

    @NonNull
    @Override
    public CartAdapter.viewholdel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tạo giao diện cho mỗi mục danh sách trong giỏ hàng
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart,parent,false);
        return new viewholdel(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.viewholdel holder, int position) {
        // Hiển thị thông tin của mỗi mặt hàng trong giỏ hàng
        holder.title.setText(list.get(position).getTitle());
        holder.feeEachItem.setText((list.get(position).getNumberInCart() * list.get(position).getPrice()) + " VNĐ");
        holder.totalEachItem.setText(list.get(position).getNumberInCart() + " * VNĐ" + (list.get(position).getPrice()));
        holder.num.setText(list.get(position).getNumberInCart() + "");

        // Sử dụng Glide để tải hình ảnh từ URL và hiển thị
        Glide.with(holder.itemView.getContext())
                .load(list.get(position).getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);

        // Xử lý sự kiện khi người dùng nhấn vào nút tăng số lượng mặt hàng
        holder.plusItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managmentCart.plusNumberItem(list, position, new ChangeNumberItemsListener() {
                    @Override
                    public void change() {
                        notifyDataSetChanged();
                        changeNumberItemsListener.change();
                    }
                });
            }
        });

        // Xử lý sự kiện khi người dùng nhấn vào nút giảm số lượng mặt hàng
        holder.minusItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managmentCart.minusNumberItem(list, position, new ChangeNumberItemsListener() {
                    @Override
                    public void change() {
                        notifyDataSetChanged();
                        changeNumberItemsListener.change();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size(); // Trả về số lượng mặt hàng trong giỏ hàng
    }

    // Lớp viewholder để giữ các thành phần giao diện của mỗi mục danh sách trong giỏ hàng
    public class viewholdel extends RecyclerView.ViewHolder {
        TextView title, feeEachItem, plusItem, minusItem, totalEachItem, num;
        ImageView pic;

        public viewholdel(@NonNull View itemView) {
            super(itemView);
            // Liên kết các thành phần giao diện với các biến tương ứng
            title = itemView.findViewById(R.id.titleTxt);
            pic = itemView.findViewById(R.id.pic);
            feeEachItem = itemView.findViewById(R.id.feeEachItem);
            plusItem = itemView.findViewById(R.id.plusCartBtn);
            minusItem = itemView.findViewById(R.id.minusCartBtn);
            totalEachItem = itemView.findViewById(R.id.totalEachItem);
            pic = itemView.findViewById(R.id.pic);
            num = itemView.findViewById(R.id.numberItem);
        }
    }
}
