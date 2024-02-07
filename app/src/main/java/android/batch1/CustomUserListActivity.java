package android.batch1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_user_list);

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
                AlertDialog.Builder builder = new AlertDialog.Builder(CustomUserListActivity.this);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("Selected User Data");
                builder.setMessage(arrayList.get(i).getName());
                builder.setPositiveButton("Dismis", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
        });

    }
}