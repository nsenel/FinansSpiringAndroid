package com.example.numan.finans.frags;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.numan.finans.MainActivity;
import com.example.numan.finans.R;
import com.example.numan.finans.Request;
import com.example.numan.finans.SP;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONObject;


public class dovizDetay extends Fragment implements View.OnClickListener {
    SP sp;
    View root;
    Button btnGunluk,btnHaftalik,btnAylik,btnYillik,btnSatinAl;
    TextView tvDovizCinsi, tvBakiye, tvSatisFiyati, tvMax;
    EditText etAlinacakMiktar;
    GraphView graph;
    JSONObject dS = new JSONObject();
    int gosterilecekGunSayisi = 30;//grafik de gosterilecek gun sayisi
    LineGraphSeries<DataPoint> series ;//grafigi olusturacak degerler
    String dovizCinsi,dovizSatisFiyati;//ayrintisina bakilan doviz
    Double max;//satin alinabilecek max doviz miktari

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_doviz_detay, container, false);
        etAlinacakMiktar = (EditText) root.findViewById(R.id.etAlinacakMiktar);
        tvDovizCinsi = (TextView) root.findViewById(R.id.tvDovizCinsi);
        tvBakiye = (TextView) root.findViewById(R.id.tvBakiye);
        tvMax = (TextView) root.findViewById(R.id.tvMax);
        tvSatisFiyati = (TextView) root.findViewById(R.id.tvSatisFiyati);
        sp = new SP(getContext());
        dovizSatisFiyati = getArguments().getString("dovizSatisFiyati");
        dovizCinsi = getArguments().getString("dovizCinsi");
        max = Double.parseDouble(sp.getBakiye()) / Double.parseDouble(dovizSatisFiyati);// muvcut bakiye ile alinabilecek doviz miktarinin hesabi yapilir.
        tvDovizCinsi.setText(dovizCinsi); //ayrintisina bakilan doviz ismi
        tvBakiye.setText(String.format("%.2f", Double.parseDouble(sp.getBakiye())) + "$"); // kulanici bakiyesi
        tvSatisFiyati.setText(dovizSatisFiyati); //ayrintisina bakilan dovizin fiyati
        tvMax.setText(String.format("%.2f", max) + "$"); // muvcut bakiye ile alinabilecek doviz miktari

        btnGunluk= (Button) root.findViewById(R.id.btnGunluk);
        btnHaftalik= (Button) root.findViewById(R.id.btnHaftalik);
        btnAylik= (Button) root.findViewById(R.id.btnAylik);
        btnYillik= (Button) root.findViewById(R.id.btnYillik);
        btnSatinAl= (Button) root.findViewById(R.id.btnSatinAl);
        btnGunluk.setOnClickListener(this);
        btnHaftalik.setOnClickListener(this);
        btnAylik.setOnClickListener(this);
        btnYillik.setOnClickListener(this);
        btnSatinAl.setOnClickListener(this);


        graph = (GraphView) root.findViewById(R.id.graph);
        MyAsyncTask myAsyncTask = new MyAsyncTask();//datalari ceker ve grafigi olusturur
        myAsyncTask.execute();
        return root;
    }

    class MyAsyncTask extends AsyncTask<String, Void, String> { // butonlar araciligi ile calisir grafikte gosterilecek datayi webservisden alir grafige basar
        @Override
        protected String doInBackground(String... strings) {
            try {
                dS = Request.getObject("tarihAraligi", "dovizCinsi", dovizCinsi, "limit", (gosterilecekGunSayisi+1)+"");
                JSONArray kurDegerleri = dS.getJSONArray("degerler");
                series = new LineGraphSeries<>();
                for (int i = 0; i < kurDegerleri.length(); i++) {
                    DataPoint dp = new DataPoint(i, kurDegerleri.getDouble(i));
                    double x = kurDegerleri.getDouble(i);
                    //Log.e("x", "i : " + i +" x : "+ x);
                    series.appendData(new DataPoint(i, x), false, kurDegerleri.length());
                }
            } catch (Exception e) {
                Log.e("x", "KATEGORILER BG EX : " + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            graph.removeAllSeries();
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(0);
            graph.getViewport().setMaxX(gosterilecekGunSayisi);
            graph.addSeries(series);
            series.setDrawDataPoints(true);
            series.setDataPointsRadius(2);
        }
    }

    public void onClick(View v) {
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        switch (v.getId()) {
            case R.id.btnGunluk:
                gosterilecekGunSayisi=1;
                myAsyncTask.execute();
                Log.e("x", "KATEGORILER BG EX : " + "Gunluk butonu");
                break;
            case R.id.btnHaftalik:
                gosterilecekGunSayisi=7;
                myAsyncTask.execute();
                Log.e("x", "KATEGORILER BG EX : " + "Haftalik butonu");
                break;
            case R.id.btnAylik:
                gosterilecekGunSayisi=30;
                myAsyncTask.execute();
                Log.e("x", "KATEGORILER BG EX : " + "Aylik butonu");
                break;
            case R.id.btnYillik:
                gosterilecekGunSayisi=360;
                myAsyncTask.execute();
                Log.e("x", "KATEGORILER BG EX : " + "Yillik butonu");
                break;
            case R.id.btnSatinAl:
                final String miktar = etAlinacakMiktar.getText().toString();
                Log.e("x", "KATEGORILER BG EX : " + "Satin Alinacak Miktar : "+miktar);
                if(miktar.equals("")){Toast.makeText(getContext(),"Lutfen Deger Giriniz", Toast.LENGTH_SHORT).show();break;}//Deger girmediginde islem yapmaz uyari verir.
                if(Integer.parseInt(miktar)>max){Toast.makeText(getContext(),"Bakiye Yetersiz", Toast.LENGTH_SHORT).show();break;}

                else{
                    new AsyncTask<String, String, String>() {

                        @Override
                        protected String doInBackground(String... strings) {
                            try {
                                dS = Request.getObject("dovizAl","user_id",sp.getId(), "dovizCinsi", dovizCinsi, "kurFiyati",dovizSatisFiyati,"alinacakMiktar",miktar,"bakiye",sp.getBakiye());
                                double yeniBakiye = Double.parseDouble(sp.getBakiye())-(Integer.parseInt(miktar)*Double.parseDouble(dovizSatisFiyati));
                                sp.setBakiye(yeniBakiye+"");

                                Log.e("x", "Satin alma islemi : " + dS.toString()+" Yeni bakiye : "+ yeniBakiye);

                            } catch (Exception e) {
                                Log.e("x", "KATEGORILER BG EX : " + e);
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            max = Double.parseDouble(sp.getBakiye()) / Double.parseDouble(dovizSatisFiyati);
                            tvBakiye.setText(String.format("%.2f", Double.parseDouble(sp.getBakiye())) + "$");
                            tvMax.setText(String.format("%.2f", max) + "$");
                            etAlinacakMiktar.setText("");
                            Toast.makeText(getContext(),"Isleminiz Gerceklestirildi", Toast.LENGTH_SHORT).show();
                        }
                    }.execute();

                    break;
                }
            default:
                throw new RuntimeException("Unknow button ID");
        }
    }
}