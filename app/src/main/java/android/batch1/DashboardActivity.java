package android.batch1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class DashboardActivity extends AppCompatActivity {

    TextView email;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        //new CommonMethod(DashboardActivity.this,"Message");

        /*Bundle bundle = getIntent().getExtras();
        String sEmail = bundle.getString("EMAIL");
        String sPassword = bundle.getString("PASSWORD");
        Log.d("RESPONSE",sEmail+"\n"+sPassword);*/

        String sEmail = sp.getString(ConstantSp.EMAIL,"");

        email = findViewById(R.id.dashboard_email);
        email.setText(sEmail);

        Log.d("RESPONSE_DASHBOARD",
                sp.getString(ConstantSp.ID,"")+"\n"+
                        sp.getString(ConstantSp.USERNAME,"")+"\n"+
                        sp.getString(ConstantSp.NAME,"")+"\n"+
                        sp.getString(ConstantSp.EMAIL,"")+"\n"+
                        sp.getString(ConstantSp.CONTACT,"")+"\n"+
                        sp.getString(ConstantSp.PASSWORD,"")+"\n"+
                        sp.getString(ConstantSp.GENDER,"")+"\n"+
                        sp.getString(ConstantSp.CITY,"")
        );

    }
}