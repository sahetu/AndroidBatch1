package android.batch1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class CustomUserListActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<CustomList> arrayList;
    SQLiteDatabase sqlDb;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_user_list);

        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        sqlDb = openOrCreateDatabase("Batch1.db", MODE_PRIVATE, null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME VARCHAR(50),NAME VARCHAR(50),EMAIL VARCHAR(100),CONTACT BIGINT(10),PASSWORD VARCHAR(12),GENDER VARCHAR(6),CITY VARCHAR(50))";
        sqlDb.execSQL(tableQuery);

        listView = findViewById(R.id.user_data_custom_listview);

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
            CustomListAdapter adapter = new CustomListAdapter(CustomUserListActivity.this,arrayList);
            listView.setAdapter(adapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sp.edit().putString(ConstantSp.USER_DETAIL_ID,arrayList.get(i).getId()).commit();
                /*sp.edit().putString(ConstantSp.USER_DETAIL_USERNAME,arrayList.get(i).getUsername()).commit();
                sp.edit().putString(ConstantSp.USER_DETAIL_NAME,arrayList.get(i).getName()).commit();
                sp.edit().putString(ConstantSp.USER_DETAIL_EMAIL,arrayList.get(i).getEmail()).commit();
                sp.edit().putString(ConstantSp.USER_DETAIL_CONTACT,arrayList.get(i).getContact()).commit();
                sp.edit().putString(ConstantSp.USER_DETAIL_GENDER,arrayList.get(i).getGender()).commit();
                sp.edit().putString(ConstantSp.USER_DETAIL_CITY,arrayList.get(i).getCity()).commit();*/

                new CommonMethod(CustomUserListActivity.this, UserDetailActivity.class);

            }
        });

    }
}