package khoalb.ntu.foodappoder.Adapter;

import android.annotation.SuppressLint;
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

import java.util.ArrayList;

import khoalb.ntu.foodappoder.Activity.ListFoodsActivity;
import khoalb.ntu.foodappoder.Domain.Category;
import khoalb.ntu.foodappoder.R;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.viewholder> {
    ArrayList<Category> items;
    Context context;

    // Constructor để nhận danh sách các mục danh mục và Context
    public CategoryAdapter(ArrayList<Category> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CategoryAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tạo giao diện cho mỗi mục danh mục
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category,parent, false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.viewholder holder, @SuppressLint("RecyclerView") int position) {
        // Hiển thị thông tin của mỗi mục danh mục
        holder.titleTxt.setText(items.get(position).getName());

        // Đặt hình nền cho hình ảnh danh mục dựa trên vị trí
        switch (position){
            case 0:{
                holder.pic.setBackgroundResource(R.drawable.cat_0_background);
                break;
            }
            case 1:{
                holder.pic.setBackgroundResource(R.drawable.cat_1_background);
                break;
            }
            case 2:{
                holder.pic.setBackgroundResource(R.drawable.cat_2_background);
                break;
            }
            case 3:{
                holder.pic.setBackgroundResource(R.drawable.cat_3_background);
                break;
            }
            case 4:{
                holder.pic.setBackgroundResource(R.drawable.cat_4_background);
                break;
            }
            case 5:{
                holder.pic.setBackgroundResource(R.drawable.cat_5_background);
                break;
            }
            case 6:{
                holder.pic.setBackgroundResource(R.drawable.cat_6_background);
                break;
            }
            case 7:{
                holder.pic.setBackgroundResource(R.drawable.cat_7_background);
                break;
            }
        }

        // Tải hình ảnh danh mục từ URL sử dụng Glide
        int drawableResourceId = context.getResources().getIdentifier(items.get(position).getImagePath()
                ,"drawable",holder.itemView.getContext().getPackageName());
        Glide.with(context)
                .load(drawableResourceId)
                .into(holder.pic);

        // Xử lý sự kiện khi người dùng nhấn vào một danh mục
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListFoodsActivity.class);
                intent.putExtra("CategoryId",items.get(position).getId());
                intent.putExtra("CategoryName",items.get(position).getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size(); // Trả về số lượng mục danh mục
    }

    // Lớp viewholder để giữ các thành phần giao diện của mỗi mục danh mục
    public class viewholder extends RecyclerView.ViewHolder {
        TextView titleTxt;
        ImageView pic;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            // Liên kết các thành phần giao diện với các biến tương ứng
            titleTxt = itemView.findViewById(R.id.catNameTxt);
            pic = itemView.findViewById(R.id.imgCat);
        }
    }
}
