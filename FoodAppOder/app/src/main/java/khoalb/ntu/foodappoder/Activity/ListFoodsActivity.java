package khoalb.ntu.foodappoder.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import khoalb.ntu.foodappoder.Adapter.FoodListAdapter;
import khoalb.ntu.foodappoder.Domain.Foods;
import khoalb.ntu.foodappoder.R;
import khoalb.ntu.foodappoder.databinding.ActivityListFoodsBinding;

public class ListFoodsActivity extends BaseActivity {
    ActivityListFoodsBinding binding;
    private RecyclerView.Adapter adapterListFood;
    private ArrayList<Foods> list;  // Danh sách dữ liệu
    private int CategoryId;
    private String CategoryName;
    private String searchText;
    private boolean isSrearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityListFoodsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getIntentExtra();
        initList();
    }

    private void initList() {
        list = new ArrayList<>();
        adapterListFood = new FoodListAdapter(list);
        binding.foodListView.setLayoutManager(new GridLayoutManager(ListFoodsActivity.this, 2));
        binding.foodListView.setAdapter(adapterListFood);

        DatabaseReference myRef = database.getReference("Foods");
        binding.progressBar.setVisibility(View.VISIBLE);

        Query query;
        if (isSrearch) {
            query = myRef.orderByChild("Title").startAt(searchText).endAt(searchText + '\uf8ff');
        } else {
            query = myRef.orderByChild("CategoryId").equalTo(CategoryId);
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Foods food = issue.getValue(Foods.class);
                        if (food != null) {
                            list.add(food);
                        }
                    }
                    adapterListFood.notifyDataSetChanged();
                    binding.progressBar.setVisibility(View.GONE);
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    // Handle case when no data found, maybe show a message to user
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
                // Handle the error, maybe show a message to user
            }
        });
    }

    private void getIntentExtra() {
        CategoryId = getIntent().getIntExtra("CategoryId", 0);
        CategoryName = getIntent().getStringExtra("CategoryName");
        searchText = getIntent().getStringExtra("text");
        isSrearch = getIntent().getBooleanExtra("isSearch", false);

        binding.titleTxt.setText(CategoryName);
        binding.backBtn.setOnClickListener(v -> finish());
    }
}
