package com.example.numan.finans;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.etUn) EditText etUn;
    @BindView(R.id.etPw) EditText etPw;

    CharSequence un = "", pw = "";

    SP sp;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        sp = new SP(LoginActivity.this);

        if (!sp.getId().equals(""))
        {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

    }

    @OnClick(R.id.btnLogin)
    void doOp()
    {
        un = etUn.getText();
        pw = etPw.getText();
        Log.e("x","UN : "+un);

        new AsyncTask<String, String, String>()
        {
            Object response = null;

            protected String doInBackground(String... strings)
            {
                try
                {
                    response = Request.getObject("login", "un", ""+un, "pw", ""+pw);
                    return "ok";
                } catch (Exception e)
                {
                    Log.e("x","doInBG Ex : "+e);
                    return e.toString();
                }
            }

            protected void onPostExecute(String s)
            {
                try
                {
                    JSONObject jo = (JSONObject) response;
                    Log.e("x","Sonıc : "+jo.toString());
                    int res = new Integer(jo.getString("result"));
                    Log.e("x","RES : "+res);
                    if (res == 1)
                    {
                        sp.setLoginInfo(jo.getString("id"), jo.getString("un"),jo.getString("bakiye"));
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this,"Hatalı Giriş", Toast.LENGTH_SHORT)
                                .show();
                    }
                } catch (Exception e) { Log.e("x","onPost Ex : "+s); }
            }
        }.execute();

    }


}

