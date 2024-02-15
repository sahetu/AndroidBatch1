package android.batch1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class SubCategoryCouponActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    String[] imageArray = {
            "https://media.istockphoto.com/id/938742222/photo/cheesy-pepperoni-pizza.jpg?s=612x612&w=0&k=20&c=D1z4xPCs-qQIZyUqRcHrnsJSJy_YbUD9udOrXpilNpI=",
            "https://media.istockphoto.com/id/1205482399/photo/traditional-south-indian-snacks-dosa-idli-medu-wada-on-banana-leaf.jpg?s=612x612&w=0&k=20&c=2SvIL78Y6YMO0AJeNTwQNgoqb6tDXMpmfSQcawrYf6c=",
            "https://images.unsplash.com/photo-1571091655789-405eb7a3a3a8?q=80&w=1000&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTh8fGJ1cmdlcnxlbnwwfHwwfHx8MA%3D%3D",
            "https://media.cnn.com/api/v1/images/stellar/prod/230320152905-07-mexican-foods-tamales.jpg?c=original&q=h_618,c_fill"
    };

    String[] nameArray = {"Pizza","South Indian","Burger","Maxican"};
    int[] outletsArray = {4,2,3,10};
    int[] totalDealArray = {10,5,1,13};
    int[] freeDealsArray = {2,4,1,6};

    ArrayList<SubCategoryList> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_coupon);

        recyclerView = findViewById(R.id.sub_category_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(SubCategoryCouponActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        arrayList = new ArrayList<>();
        for(int i=0;i<nameArray.length;i++){
            SubCategoryList list = new SubCategoryList();
            list.setName(nameArray[i]);
            list.setImage(imageArray[i]);
            list.setTotalDeal(totalDealArray[i]);
            list.setOutlets(outletsArray[i]);
            list.setFreeDeal(freeDealsArray[i]);
            arrayList.add(list);
        }
        SubCategoryAdapter adapter = new SubCategoryAdapter(SubCategoryCouponActivity.this,arrayList);
        recyclerView.setAdapter(adapter);
    }
}