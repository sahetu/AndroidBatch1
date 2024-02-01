package android.batch1;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class CommonMethod {

    CommonMethod(Context activity, String message){
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    CommonMethod(View view,String message){
        Snackbar.make(view,message,Snackbar.LENGTH_SHORT).show();
    }

    CommonMethod(Context context,Class<?> nextClass){
        Intent intent = new Intent(context,nextClass);
        context.startActivity(intent);
    }

}
