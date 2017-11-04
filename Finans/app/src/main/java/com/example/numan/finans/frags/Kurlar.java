package com.example.numan.finans.frags;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.numan.finans.MainActivity;
import com.example.numan.finans.R;
import com.example.numan.finans.Request;

import org.json.JSONArray;
import org.json.JSONObject;

public class Kurlar extends Fragment implements AdapterView.OnItemClickListener {
    View root;
    ListView lv;
    LayoutInflater li;
    BaseAdapter ba;
    JSONObject dS = new JSONObject();
    final String[] cinsler = {"AUD", "BGN", "BRL","CAD","CHF","CNY","CZK","DKK","GBP","HKD","HRK","HUF","IDR","ILS","INR","JPY","KRW","MXN","MYR","NOK","NZD","PHP","PLN","RON","RUB","SEK","SGD","THB","TRY","ZAR","EUR"};
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_kurlar, container, false);
        li = inflater;
        lv=(ListView) root.findViewById(R.id.lv);
        ba=new BaseAdapter() {
            @Override
            public int getCount() {
                return cinsler.length;
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

                tvDoviz.setText(cinsler[i]);
                try {
                    //String satisFiyati = dS.getJSONObject("rates").getString(cinsler[i]); //fixer.io icin
                    String satisFiyati = dS.getString(cinsler[i].toLowerCase());
                    double alisFiyati = Double.parseDouble(satisFiyati)-0.05;
                    tvAlis.setText(satisFiyati);
                    tvSatis.setText(String.format("%.4f", alisFiyati));
                }catch (Exception e) {Log.e("x","ListView Dovizler : "+e);}

                return view;
            }
        };
        lv.setAdapter(ba);
        lv.setOnItemClickListener(this);
        new AsyncTask<String, String, String>()
        {

            @Override
            protected String doInBackground(String... strings) {
                try
                {
                    //dS = Request.getObject("latest","base","USD");//fixer.io icin
                    dS = Request.getObject("latest");
                    for(int i=0;i<cinsler.length;i++){
                        //Log.e("x","My_USD : "+dS.getJSONObject("rates").get(cinsler[i]));}
                        Log.e("x","My_USD : "+dS.get(cinsler[i]));}
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
        return root;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        try
        {
            dovizDetay fu = new dovizDetay();
            Bundle b = new Bundle();
            b.putString("dovizCinsi",cinsler[i]);
            b.putString("dovizSatisFiyati", dS.getString(cinsler[i].toLowerCase()));
            fu.setArguments(b);

            MainActivity s=  (MainActivity) getActivity();
            s.moveTo(fu, true);
            Log.e("x","Burdayim OnClikin sonunda");
        } catch (Exception e) {Log.e("x","ONcilick Hatasi : "+e);}
    }
}
