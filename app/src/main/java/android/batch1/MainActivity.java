package android.batch1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Button login;
    EditText email,password;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    TextView createAccount;
    SQLiteDatabase sqlDb;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        sqlDb = openOrCreateDatabase("Batch1.db",MODE_PRIVATE,null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME VARCHAR(50),NAME VARCHAR(50),EMAIL VARCHAR(100),CONTACT BIGINT(10),PASSWORD VARCHAR(12),GENDER VARCHAR(6),CITY VARCHAR(50))";
        sqlDb.execSQL(tableQuery);

        login = findViewById(R.id.main_login);

        createAccount = findViewById(R.id.main_create_account);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(MainActivity.this, SignupActivity.class);
            }
        });

        email = findViewById(R.id.main_email);
        password = findViewById(R.id.main_password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().trim().equals("")){
                    email.setError("Username Required");
                }
                /*else if(!email.getText().toString().trim().matches(emailPattern)){
                    email.setError("Valid Email Id Required");
                }*/
                else if(password.getText().toString().trim().equals("")){
                    password.setError("Password Required");
                }
                else if(password.getText().toString().trim().length()<6){
                    password.setError("Min. 6 Char Password Required");
                }
                else{
                    //sqliteLogin(view);
                    if(new ConnectionDetector(MainActivity.this).networkConnected()){
                        new doLogin().execute();
                    }
                    else{
                        new ConnectionDetector(MainActivity.this).networkDisconnected();
                    }
                }
            }
        });
    }

    private void sqliteLogin(View view) {
        String selectQuery = "SELECT * FROM USERS WHERE (EMAIL='"+email.getText().toString()+"' OR USERNAME='"+email.getText().toString()+"') AND PASSWORD='"+password.getText().toString()+"'";
        Cursor cursor = sqlDb.rawQuery(selectQuery,null);
        if(cursor.getCount()>0) {
            System.out.println("Login Successfully");
            Log.d("RESPONSE", "Login Successfully");
            Log.e("RESPONSE", "Login Successfully");
            Log.w("RESPONSE", "Login Successfully");

            //Toast.makeText(MainActivity.this,R.string.login_message,Toast.LENGTH_LONG).show();
            new CommonMethod(MainActivity.this, getResources().getString(R.string.login_message));
            //Snackbar.make(view,R.string.login_message,Snackbar.LENGTH_SHORT).show();
            new CommonMethod(view, getResources().getString(R.string.login_message));

                        /*Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("EMAIL", email.getText().toString().trim());
                        bundle.putString("PASSWORD", password.getText().toString().trim());
                        intent.putExtras(bundle);
                        startActivity(intent);*/

            while (cursor.moveToNext()){
                String sUserId = cursor.getString(0);
                String sUserName = cursor.getString(1);
                String sName = cursor.getString(2);
                String sEmail = cursor.getString(3);
                String sContact = cursor.getString(4);
                String sPassword = cursor.getString(5);
                String sGender = cursor.getString(6);
                String sCity = cursor.getString(7);

                sp.edit().putString(ConstantSp.ID,sUserId).commit();
                sp.edit().putString(ConstantSp.USERNAME,sUserName).commit();
                sp.edit().putString(ConstantSp.NAME,sName).commit();
                sp.edit().putString(ConstantSp.EMAIL,sEmail).commit();
                sp.edit().putString(ConstantSp.CONTACT,sContact).commit();
                sp.edit().putString(ConstantSp.PASSWORD,sPassword).commit();
                sp.edit().putString(ConstantSp.GENDER,sGender).commit();
                sp.edit().putString(ConstantSp.CITY,sCity).commit();

                Log.d("USER_DATA",sUserId+"_____"+sCity);
            }

            new CommonMethod(MainActivity.this,DashboardActivity.class);
            finish();
        }
        else{
            new CommonMethod(MainActivity.this,"Login Unsuccessfully");
        }
    }

    private class doLogin extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("email",email.getText().toString());
            hashMap.put("password",password.getText().toString());
            return new MakeServiceCall().MakeServiceCall(ConstantSp.BASE_URL+"login.php",MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if(jsonObject.getBoolean("Status")){
                    new CommonMethod(MainActivity.this,jsonObject.getString("Message"));

                    JSONArray array = jsonObject.getJSONArray("UserDetails");
                    for(int i=0;i<array.length();i++){
                        JSONObject object = array.getJSONObject(i);

                        sp.edit().putString(ConstantSp.ID,object.getString("userId")).commit();
                        sp.edit().putString(ConstantSp.USERNAME,object.getString("userName")).commit();
                        sp.edit().putString(ConstantSp.NAME,object.getString("name")).commit();
                        sp.edit().putString(ConstantSp.EMAIL,object.getString("email")).commit();
                        sp.edit().putString(ConstantSp.CONTACT,object.getString("contact")).commit();
                        sp.edit().putString(ConstantSp.PASSWORD,"").commit();
                        sp.edit().putString(ConstantSp.GENDER,object.getString("gender")).commit();
                        sp.edit().putString(ConstantSp.CITY,object.getString("city")).commit();
                    }
                    new CommonMethod(MainActivity.this,DashboardActivity.class);
                    finish();
                }
                else{
                    new CommonMethod(MainActivity.this,jsonObject.getString("Message"));
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}