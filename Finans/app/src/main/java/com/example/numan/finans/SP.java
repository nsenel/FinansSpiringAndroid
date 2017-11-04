package com.example.numan.finans;

/**
 * Created by numan on 14.09.2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by EmirCivas on 22.8.2017.
 */

public class SP
{
    SharedPreferences sp;
    SharedPreferences.Editor edit;

    public SP(Context c)
    {
        sp = PreferenceManager.getDefaultSharedPreferences(c);
        edit = sp.edit();
    }

    public void setBakiye(String bakiye)
    {
        edit.putString("bakiye", bakiye).commit();
    }

    public String getToken()
    {
        return sp.getString("tok","");
    }

    public void clear()
    {
        edit.clear().commit();
    }

    public void setLoginInfo(String id, String un,String bakiye)
    {
        edit.putString("un",un);
        edit.putString("id",id);
        edit.putString("bakiye",bakiye);
        edit.commit();
    }

    public String getUn() { return sp.getString("un","NAN"); }
    public String getId() { return sp.getString("id",""); }
    public String getBakiye() {Log.e("x","Mevcut Bakiye : "+sp.getString("bakiye","0.00")); return sp.getString("bakiye","0.00");  }
}
