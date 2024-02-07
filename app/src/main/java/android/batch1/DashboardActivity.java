package android.batch1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DashboardActivity extends AppCompatActivity {

    TextView email;
    SharedPreferences sp;

    Button logout, profile, deleteProfile, userDataList,userCustomList;
    SQLiteDatabase sqlDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);

        sqlDb = openOrCreateDatabase("Batch1.db", MODE_PRIVATE, null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME VARCHAR(50),NAME VARCHAR(50),EMAIL VARCHAR(100),CONTACT BIGINT(10),PASSWORD VARCHAR(12),GENDER VARCHAR(6),CITY VARCHAR(50))";
        sqlDb.execSQL(tableQuery);

        //new CommonMethod(DashboardActivity.this,"Message");

        /*Bundle bundle = getIntent().getExtras();
        String sEmail = bundle.getString("EMAIL");
        String sPassword = bundle.getString("PASSWORD");
        Log.d("RESPONSE",sEmail+"\n"+sPassword);*/

        String sEmail = sp.getString(ConstantSp.EMAIL, "");

        email = findViewById(R.id.dashboard_email);
        email.setText(sEmail);

        Log.d("RESPONSE_DASHBOARD",
                sp.getString(ConstantSp.ID, "") + "\n" +
                        sp.getString(ConstantSp.USERNAME, "") + "\n" +
                        sp.getString(ConstantSp.NAME, "") + "\n" +
                        sp.getString(ConstantSp.EMAIL, "") + "\n" +
                        sp.getString(ConstantSp.CONTACT, "") + "\n" +
                        sp.getString(ConstantSp.PASSWORD, "") + "\n" +
                        sp.getString(ConstantSp.GENDER, "") + "\n" +
                        sp.getString(ConstantSp.CITY, "")
        );

        logout = findViewById(R.id.dashboard_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sp.edit().remove(ConstantSp.ID);
                sp.edit().clear().commit();
                new CommonMethod(DashboardActivity.this, MainActivity.class);
                finish();
            }
        });

        profile = findViewById(R.id.dashboard_profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(DashboardActivity.this, ProfileActivity.class);
            }
        });

        deleteProfile = findViewById(R.id.dashboard_delete_profile);
        deleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deleteQuery = "DELETE FROM USERS WHERE USERID='" + sp.getString(ConstantSp.ID, "") + "'";
                sqlDb.execSQL(deleteQuery);
                new CommonMethod(DashboardActivity.this, "Profile Deleted Successfully");

                sp.edit().clear().commit();
                new CommonMethod(DashboardActivity.this, MainActivity.class);
                finish();
            }
        });

        userDataList = findViewById(R.id.dashboard_user_data_list);
        userDataList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(DashboardActivity.this, UserDataSimpleListActivity.class);
            }
        });

        userCustomList = findViewById(R.id.dashboard_custom_user_data_list);

        userCustomList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(DashboardActivity.this,CustomUserListActivity.class);
            }
        });

    }
}