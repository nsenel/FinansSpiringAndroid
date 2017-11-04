package com.example.numan.finans.frags;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.numan.finans.MainActivity;
import com.example.numan.finans.R;
import com.example.numan.finans.Request;
import com.example.numan.finans.SP;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class gecmisIslemler extends Fragment {
    View root;
    ListView lv;
    LayoutInflater li;
    BaseAdapter ba;
    JSONObject dS = new JSONObject();
    SP sp;
    JSONArray dovizCinsleri = new JSONArray();
    JSONArray dovizAlinanFiyat = new JSONArray();
    JSONArray dovizAnlikFiyat = new JSONArray();
    JSONArray islemID = new JSONArray();
    Double satisFiyati;//dovizi suanki fiyati
    //ArrayList<String> dovizCinsleri=new ArrayList<>();
    //ArrayList<String> dovizAlinanFiyat=new ArrayList<>();
    //ArrayList<String> dovizAnlikFiyat=new ArrayList<>();
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_gecmis_islemler, container, false);
        sp = new SP(getContext());
        li = inflater;
        lv=(ListView) root.findViewById(R.id.lv);

        refresh();//Verileri Almak icin AsyncTask calistirir

        ba=new BaseAdapter() {
            @Override
            public int getCount() {
                try {
                    return dovizCinsleri.length();
                }catch (Exception e){e.toString();}
                return 0;
            }

            @Override
            public Object getItem(int i) { return null; }

            @Override
            public long getItemId(int i) {return 0;}

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view==null){ view = inflater.inflate(R.layout.list_item,null);}

                TextView tvDoviz = (TextView) view.findViewById(R.id.tvDoviz);
                TextView tvAlis = (TextView) view.findViewById(R.id.tvAlis);
                TextView tvSatis = (TextView) view.findViewById(R.id.tvSatis);


                try {
                    tvDoviz.setText(dovizCinsleri.get(i).toString());
                    tvAlis.setText(dovizAlinanFiyat.get(i).toString());
                    satisFiyati=Double.parseDouble(dovizAnlikFiyat.get(i).toString());
                    tvSatis.setText(String.format("%.4f", satisFiyati));
                }catch (Exception e){e.toString();}


                return view;
            }
        };
        Log.e("x","Item Sayisi : "+dovizCinsleri.length());
        //if(dovizCinsleri.length()>0) {
        lv.setAdapter(ba);
        //}

        return root;
    }

    void refresh(){
        new AsyncTask<String, String, String>()
        {

            @Override
            protected String doInBackground(String... strings) {
                try
                {
                    //dS = Request.getObject("latest","base","USD");//fixer.io icin
                    dS = Request.getObject("islemGecmisi","user_id",sp.getId());
                    dovizAnlikFiyat=dS.getJSONArray("suandakiKur");
                    dovizCinsleri= dS.getJSONArray("dovizCinsi");
                    dovizAlinanFiyat= dS.getJSONArray("alindigiKur");//satildigi kur
                    islemID= dS.getJSONArray("islemId");


                    Log.e("x","My_USD : "+dS.toString());
                } catch (Exception e)
                {
                    Log.e("x","KATEGORILER BG EX : "+e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                ba.notifyDataSetChanged();
            }
        }.execute();
    }

}