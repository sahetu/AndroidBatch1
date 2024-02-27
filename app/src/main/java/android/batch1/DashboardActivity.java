package android.batch1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class DashboardActivity extends AppCompatActivity {

    TextView email;
    SharedPreferences sp;

    Button logout, profile, deleteProfile, userDataList,userCustomList,userRecyclerview,myntraCat,subCatTask,activityFragment,tabLayout,bottomNav,navDemo;
    SQLiteDatabase sqlDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        /*getSupportActionBar().setTitle("Dashboard");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

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
                //deleteSqlite();
                if(new ConnectionDetector(DashboardActivity.this).networkConnected()){
                    new doDelete().execute();
                }
                else{
                    new ConnectionDetector(DashboardActivity.this).networkDisconnected();
                }
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

        userRecyclerview = findViewById(R.id.dashboard_recycler_data_list);
        userRecyclerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(DashboardActivity.this, UserRecyclerActivity.class);
            }
        });

        myntraCat = findViewById(R.id.dashboard_recycler_myntra_category);
        myntraCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(DashboardActivity.this, MyntraCategoryActivity.class);
            }
        });

        subCatTask = findViewById(R.id.dashboard_recycler_sub_category);
        subCatTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(DashboardActivity.this,SubCategoryCouponActivity.class);
            }
        });

        activityFragment = findViewById(R.id.dashboard_recycler_activity_fragment);
        activityFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(DashboardActivity.this,ActivityToFragmentActivity.class);
            }
        });

        tabLayout = findViewById(R.id.dashboard_recycler_tab_layout);
        tabLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(DashboardActivity.this, TabDemoActivity.class);
            }
        });

        bottomNav = findViewById(R.id.dashboard_recycler_bottom_nav);
        bottomNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(DashboardActivity.this, BottomNavActivity.class);
            }
        });

        navDemo = findViewById(R.id.dashboard_recycler_nav);
        navDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(DashboardActivity.this, NavDemoActivity.class);
            }
        });

    }

    private void deleteSqlite() {
        String deleteQuery = "DELETE FROM USERS WHERE USERID='" + sp.getString(ConstantSp.ID, "") + "'";
        sqlDb.execSQL(deleteQuery);
        new CommonMethod(DashboardActivity.this, "Profile Deleted Successfully");

        sp.edit().clear().commit();
        new CommonMethod(DashboardActivity.this, MainActivity.class);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private class doDelete extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(DashboardActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("userId",sp.getString(ConstantSp.ID,""));
            return new MakeServiceCall().MakeServiceCall(ConstantSp.BASE_URL+"deleteProfile.php",MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if(jsonObject.getBoolean("Status")){
                    new CommonMethod(DashboardActivity.this,jsonObject.getString("Message"));
                    sp.edit().clear().commit();
                    new CommonMethod(DashboardActivity.this, MainActivity.class);
                    finish();
                }
                else{
                    new CommonMethod(DashboardActivity.this,jsonObject.getString("Message"));
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}