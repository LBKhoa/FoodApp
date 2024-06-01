package khoalb.ntu.foodappoder.Adapter;

import android.content.Context;
import android.content.Intent;
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

import khoalb.ntu.foodappoder.Activity.DetailActivity;
import khoalb.ntu.foodappoder.Domain.Foods;
import khoalb.ntu.foodappoder.R;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.viewholder> {
    ArrayList<Foods> items;
    Context context;

    // Constructor để nhận danh sách các món ăn và Context
    public FoodListAdapter(ArrayList<Foods> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public FoodListAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tạo giao diện cho mỗi mục danh sách món ăn
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.viewholder_list_food,parent,false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodListAdapter.viewholder holder, int position) {
        // Hiển thị thông tin của mỗi món ăn
        holder.titleTxt.setText(items.get(position).getTitle());
        holder.timeTxt.setText(items.get(position).getTimeValue()+ " phút");
        holder.priceTxt.setText(items.get(position).getPrice()+" VNĐ");
        holder.starTxt.setText(" "+items.get(position).getStar());

        // Tải hình ảnh của món ăn từ URL sử dụng Glide và áp dụng hiệu ứng CenterCrop và RoundedCorners
        Glide.with(context)
                .load(items.get(position).getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);

        // Xử lý sự kiện khi người dùng nhấn vào một món ăn
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("object", items.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size(); // Trả về số lượng món ăn
    }

    // Lớp viewholder để giữ các thành phần giao diện của mỗi mục danh sách món ăn
    public class viewholder extends RecyclerView.ViewHolder {
        TextView titleTxt, priceTxt, starTxt, timeTxt;
        ImageView pic;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            // Liên kết các thành phần giao diện với các biến tương ứng
            titleTxt = itemView.findViewById(R.id.titleTxt);
            priceTxt = itemView.findViewById(R.id.priceTxt);
            starTxt = itemView.findViewById(R.id.rateTxt);
            timeTxt = itemView.findViewById(R.id.timeTxt);
            pic =  itemView.findViewById(R.id.img);
        }
    }
}
