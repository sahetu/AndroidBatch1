package android.batch1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    EditText username,name,email,contact,password,confirmPassword;
    Button edit,submit;

    TextView confirmPasswordTitle;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    RadioButton male,female;
    RadioGroup gender;
    String sGender = "";

    Spinner city;
    //String[] cityArray = {"Select City","Ahmedabad","Vadodara","Surat","Rajkot","Junagadh"};
    ArrayList<String> cityArray;

    SQLiteDatabase sqlDb;
    String sCity;

    SharedPreferences sp;
    ApiInterface apiInterface;
    ProgressDialog pd;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        sqlDb = openOrCreateDatabase("Batch1.db",MODE_PRIVATE,null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME VARCHAR(50),NAME VARCHAR(50),EMAIL VARCHAR(100),CONTACT BIGINT(10),PASSWORD VARCHAR(12),GENDER VARCHAR(6),CITY VARCHAR(50))";
        sqlDb.execSQL(tableQuery);

        username = findViewById(R.id.profile_username);
        name = findViewById(R.id.profile_name);
        email = findViewById(R.id.profile_email);
        contact = findViewById(R.id.profile_contact);
        password = findViewById(R.id.profile_password);
        confirmPassword = findViewById(R.id.profile_confirm_password);
        edit = findViewById(R.id.profile_edit_button);
        submit = findViewById(R.id.profile_button);

        confirmPasswordTitle = findViewById(R.id.profile_confirm_password_title);

        city = findViewById(R.id.profile_city);

        cityArray = new ArrayList<>();
        cityArray.add("Select City");
        cityArray.add("Junagadh");
        cityArray.add("Ahmedabad");
        cityArray.add("Rajkot");
        cityArray.add("Vadodara");
        cityArray.add("Baroda");

        ArrayAdapter adapter = new ArrayAdapter(ProfileActivity.this, android.R.layout.simple_list_item_1,cityArray);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        city.setAdapter(adapter);

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){

                }
                else {
                    sCity = cityArray.get(i);
                    new CommonMethod(ProfileActivity.this,sCity);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        male = findViewById(R.id.profile_male);
        female = findViewById(R.id.profile_female);
        gender = findViewById(R.id.profile_gender);

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = findViewById(i);
                sGender = rb.getText().toString();
                new CommonMethod(ProfileActivity.this,sGender);
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(confirmPassword.getText().toString().trim().equals("")){

                }
                else if(!password.getText().toString().trim().matches(confirmPassword.getText().toString().trim())){
                    confirmPassword.setError("Password Does Not Match");
                }
                else{

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().equals("")){
                    confirmPassword.setError("Confirm Password Required");
                }
                else if(!password.getText().toString().trim().matches(confirmPassword.getText().toString().trim())){
                    confirmPassword.setError("Password Does Not Match");
                }
                else{

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().trim().equals("")){
                    username.setError("Username Required");
                }
                else if(name.getText().toString().trim().equals("")){
                    name.setError("Name Required");
                }
                else if(email.getText().toString().trim().equals("")){
                    email.setError("Email Id Required");
                }
                else if(!email.getText().toString().trim().matches(emailPattern)){
                    email.setError("Valid Email Id Required");
                }
                else if(contact.getText().toString().trim().equals("")){
                    contact.setError("Contact No. Required");
                }
                else if(contact.getText().toString().trim().length()<10){
                    contact.setError("Valid Contact No. Required");
                }
                else if(password.getText().toString().trim().equals("")){
                    password.setError("Password Required");
                }
                else if(password.getText().toString().trim().length()<6){
                    password.setError("Min. 6 Char Password Required");
                }
                else if(confirmPassword.getText().toString().trim().equals("")){
                    confirmPassword.setError("Confirm Password Required");
                }
                else if(confirmPassword.getText().toString().trim().length()<6){
                    confirmPassword.setError("Min. 6 Char Confirm Password Required");
                }
                else if(!password.getText().toString().trim().matches(confirmPassword.getText().toString().trim())){
                    confirmPassword.setError("Password Does Not Match");
                }
                else if(gender.getCheckedRadioButtonId() == -1){
                    new CommonMethod(ProfileActivity.this,"Please Select Gender");
                }
                else if(city.getSelectedItemPosition()<=0){
                    new CommonMethod(ProfileActivity.this,"Please Select City");
                }
                else{
                    //updateSqlite();
                    if(new ConnectionDetector(ProfileActivity.this).networkConnected()){
                        //new doUpdate().execute();
                        pd = new ProgressDialog(ProfileActivity.this);
                        pd.setMessage("Please Wait...");
                        pd.setCancelable(false);
                        pd.show();
                        doUpdateRetrofit();
                    }
                    else{
                        new ConnectionDetector(ProfileActivity.this).networkDisconnected();
                    }
                }
            }
        });

        setData(false);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData(true);
            }
        });

    }

    private void doUpdateRetrofit() {
        Call<GetSignupData> call = apiInterface.updateProfileData(
                username.getText().toString(),
                name.getText().toString(),
                email.getText().toString(),
                contact.getText().toString(),
                password.getText().toString(),
                sGender,
                sCity,
                sp.getString(ConstantSp.ID,"")
        );

        call.enqueue(new Callback<GetSignupData>() {
            @Override
            public void onResponse(Call<GetSignupData> call, Response<GetSignupData> response) {
                pd.dismiss();
                if(response.code()==200){
                    if(response.body().status){
                        new CommonMethod(ProfileActivity.this,response.body().message);

                        sp.edit().putString(ConstantSp.USERNAME,username.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.NAME,name.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.EMAIL,email.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.CONTACT,contact.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.PASSWORD,password.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.GENDER,sGender).commit();
                        sp.edit().putString(ConstantSp.CITY,sCity).commit();

                        setData(false);

                    }
                    else{
                        new CommonMethod(ProfileActivity.this,response.body().message);
                    }
                }
                else{
                    new CommonMethod(ProfileActivity.this,"Server Error Code : "+response.code());
                }
            }

            @Override
            public void onFailure(Call<GetSignupData> call, Throwable t) {
                pd.dismiss();
                new CommonMethod(ProfileActivity.this,t.getMessage());
                Log.d("RESPONSE",t.getMessage());
            }
        });

    }

    private void updateSqlite() {
        String updateQuery = "UPDATE USERS SET USERNAME='"+username.getText().toString()+"',NAME='"+name.getText().toString()+"',EMAIL='"+email.getText().toString()+"',CONTACT='"+contact.getText().toString()+"',PASSWORD='"+password.getText().toString()+"',GENDER='"+sGender+"',CITY='"+sCity+"' WHERE USERID='"+sp.getString(ConstantSp.ID,"")+"'";
        sqlDb.execSQL(updateQuery);
        new CommonMethod(ProfileActivity.this,"Profile Update Successfully");

        sp.edit().putString(ConstantSp.USERNAME,username.getText().toString()).commit();
        sp.edit().putString(ConstantSp.NAME,name.getText().toString()).commit();
        sp.edit().putString(ConstantSp.EMAIL,email.getText().toString()).commit();
        sp.edit().putString(ConstantSp.CONTACT,contact.getText().toString()).commit();
        sp.edit().putString(ConstantSp.PASSWORD,password.getText().toString()).commit();
        sp.edit().putString(ConstantSp.GENDER,sGender).commit();
        sp.edit().putString(ConstantSp.CITY,sCity).commit();

        setData(false);
    }

    private void setData(boolean b) {
        username.setText(sp.getString(ConstantSp.USERNAME,""));
        name.setText(sp.getString(ConstantSp.NAME,""));
        email.setText(sp.getString(ConstantSp.EMAIL,""));
        contact.setText(sp.getString(ConstantSp.CONTACT,""));
        password.setText(sp.getString(ConstantSp.PASSWORD,""));
        confirmPassword.setText(sp.getString(ConstantSp.PASSWORD,""));

        sGender = sp.getString(ConstantSp.GENDER,"");

        if(sp.getString(ConstantSp.GENDER,"").equals("Male")){
            male.setChecked(true);
            female.setChecked(false);
        }
        else if(sp.getString(ConstantSp.GENDER,"").equals("Female")){
            male.setChecked(false);
            female.setChecked(true);
        }
        else{
            male.setChecked(false);
            female.setChecked(false);
        }

        sCity = sp.getString(ConstantSp.CITY,"");

        int iCityPosition = 0;
        for(int i=0;i<cityArray.size();i++){
            if(sp.getString(ConstantSp.CITY,"").equals(cityArray.get(i))){
                iCityPosition = i;
                break;
            }
        }

        city.setSelection(iCityPosition);

        username.setEnabled(b);
        name.setEnabled(b);
        email.setEnabled(b);
        contact.setEnabled(b);
        password.setEnabled(b);
        confirmPassword.setEnabled(b);

        male.setEnabled(b);
        female.setEnabled(b);

        city.setEnabled(b);

        if(b){
            confirmPasswordTitle.setVisibility(View.VISIBLE);
            confirmPassword.setVisibility(View.VISIBLE);
            edit.setVisibility(View.GONE);
            submit.setVisibility(View.VISIBLE);
        }
        else{
            confirmPasswordTitle.setVisibility(View.GONE);
            confirmPassword.setVisibility(View.GONE);
            edit.setVisibility(View.VISIBLE);
            submit.setVisibility(View.GONE);
        }

    }

    private class doUpdate extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ProfileActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("username",username.getText().toString());
            hashMap.put("name",name.getText().toString());
            hashMap.put("email",email.getText().toString());
            hashMap.put("contact",contact.getText().toString());
            hashMap.put("password",password.getText().toString());
            hashMap.put("gender",sGender);
            hashMap.put("city",sCity);
            hashMap.put("userId",sp.getString(ConstantSp.ID,""));
            return new MakeServiceCall().MakeServiceCall(ConstantSp.BASE_URL+"updateProfile.php",MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if(jsonObject.getBoolean("Status")){
                    new CommonMethod(ProfileActivity.this,jsonObject.getString("Message"));

                    sp.edit().putString(ConstantSp.USERNAME,username.getText().toString()).commit();
                    sp.edit().putString(ConstantSp.NAME,name.getText().toString()).commit();
                    sp.edit().putString(ConstantSp.EMAIL,email.getText().toString()).commit();
                    sp.edit().putString(ConstantSp.CONTACT,contact.getText().toString()).commit();
                    sp.edit().putString(ConstantSp.PASSWORD,password.getText().toString()).commit();
                    sp.edit().putString(ConstantSp.GENDER,sGender).commit();
                    sp.edit().putString(ConstantSp.CITY,sCity).commit();

                    setData(false);

                }
                else{
                    new CommonMethod(ProfileActivity.this,jsonObject.getString("Message"));
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}