package android.batch1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class UserRecyclerActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<CustomList> arrayList;
    SQLiteDatabase sqlDb;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_recycler);

        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        sqlDb = openOrCreateDatabase("Batch1.db", MODE_PRIVATE, null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME VARCHAR(50),NAME VARCHAR(50),EMAIL VARCHAR(100),CONTACT BIGINT(10),PASSWORD VARCHAR(12),GENDER VARCHAR(6),CITY VARCHAR(50))";
        sqlDb.execSQL(tableQuery);

        recyclerView = findViewById(R.id.user_recyclerview);

        //Display Data as list
        //recyclerView.setLayoutManager(new LinearLayoutManager(UserRecyclerActivity.this));

        //Display Data as Grid
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        //Display Data as Horizontal Scroll
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        String selectQuery = "SELECT * FROM USERS";
        Cursor cursor = sqlDb.rawQuery(selectQuery,null);
        if(cursor.getCount()>0){
            arrayList = new ArrayList<>();
            while (cursor.moveToNext()){
                String sUserId = cursor.getString(0);
                String sUserName = cursor.getString(1);
                String sName = cursor.getString(2);
                String sEmail = cursor.getString(3);
                String sContact = cursor.getString(4);
                String sGender = cursor.getString(6);
                String sCity = cursor.getString(7);

                CustomList list = new CustomList();
                list.setId(sUserId);
                list.setUsername(sUserName);
                list.setName(sName);
                list.setEmail(sEmail);
                list.setContact(sContact);
                list.setGender(sGender);
                list.setCity(sCity);

                arrayList.add(list);
            }
            CustomRecyclerAdapter adapter = new CustomRecyclerAdapter(UserRecyclerActivity.this,arrayList);
            recyclerView.setAdapter(adapter);
        }

    }
}