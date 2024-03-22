package android.batch1;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add;

    ApiInterface apiInterface;
    ProgressDialog pd;

    ArrayList<CategoryList> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        add = findViewById(R.id.category_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(CategoryActivity.this, AddCategoryActivity.class);
            }
        });

        recyclerView = findViewById(R.id.category_recyclerview);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(new ConnectionDetector(CategoryActivity.this).networkConnected()){
            pd = new ProgressDialog(CategoryActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
            getData();
        }
        else{
            new ConnectionDetector(CategoryActivity.this).networkDisconnected();
        }
    }

    private void getData() {
        Call<GetCategoryData> call = apiInterface.getCategoryData();
        call.enqueue(new Callback<GetCategoryData>() {
            @Override
            public void onResponse(Call<GetCategoryData> call, Response<GetCategoryData> response) {
                pd.dismiss();
                if(response.code()==200){
                    if(response.body().status){
                        arrayList = new ArrayList<>();
                        for (int i = 0; i < response.body().categoryData.size(); i++) {
                            CategoryList list = new CategoryList();
                            list.setId(response.body().categoryData.get(i).categoryId);
                            list.setName(response.body().categoryData.get(i).name);
                            list.setImage(response.body().categoryData.get(i).image);
                            arrayList.add(list);
                        }
                        CategoryAdapter adapter = new CategoryAdapter(CategoryActivity.this, arrayList);
                        recyclerView.setAdapter(adapter);

                        final SkeletonScreen skeletonScreen = Skeleton.bind(recyclerView)
                                .adapter(adapter)
                                .load(R.layout.item_skeleton_category)
                                .shimmer(true)
                                .angle(45)
                                .color(R.color.black)
                                .duration(400)
                                .count(arrayList.size())
                                .show();
                        recyclerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                skeletonScreen.hide();
                            }
                        }, 500);

                    }
                    else{
                        new CommonMethod(CategoryActivity.this,response.body().message);
                    }
                }
                else{
                    new CommonMethod(CategoryActivity.this,"Server Error Code : "+response.code());
                }
            }

            @Override
            public void onFailure(Call<GetCategoryData> call, Throwable t) {
                pd.dismiss();
                new CommonMethod(CategoryActivity.this,t.getMessage());
            }
        });
    }
}