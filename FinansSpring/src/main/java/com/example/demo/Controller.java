/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 *
 * @author numan
 */
@RestController
@EnableWebMvc
public class Controller {
    
    DB db = new DB();
    
    @RequestMapping("/kurlar/latest")
    public DovizlerObject latestKurlar(HttpSession session)//son kayitli row daki kurlari alir androideki doviz listesi icin
    {
        //String[] cinsler={"AUD", "BGN", "BRL","CAD","CHF","CNY","CZK","DKK","GBP","HKD","HRK","HUF","IDR","ILS","INR",
          //                "JPY","KRW","MXN","MYR","NOK","NZD","PHP","PLN","RON","RUB","SEK","SGD","THB","TRY","ZAR","EUR"};
        DovizlerObject kurlar = new DovizlerObject();//tek tarih cok sayida doviz
        try {
            ResultSet degerler = db.qW1R("select * from kurlar order by id DESC LIMIT 1;");
            kurlar.setTarih(degerler.getString("tarih"));
            kurlar.setAUD(degerler.getDouble("AUD"));
            kurlar.setBGN(degerler.getDouble("BGN"));
            kurlar.setBRL(degerler.getDouble("BRL"));
            kurlar.setCAD(degerler.getDouble("CAD"));
            kurlar.setCHF(degerler.getDouble("CHF"));
            kurlar.setCNY(degerler.getDouble("CNY"));
            kurlar.setCZK(degerler.getDouble("CZK"));
            kurlar.setDKK(degerler.getDouble("DKK"));
            kurlar.setGBP(degerler.getDouble("GBP"));
            kurlar.setHKD(degerler.getDouble("HKD"));
            kurlar.setHRK(degerler.getDouble("HRK"));
            kurlar.setHUF(degerler.getDouble("HUF"));
            kurlar.setIDR(degerler.getDouble("IDR"));
            kurlar.setILS(degerler.getDouble("ILS"));
            kurlar.setINR(degerler.getDouble("INR"));
            kurlar.setJPY(degerler.getDouble("JPY"));
            kurlar.setKRW(degerler.getDouble("KRW"));
            kurlar.setMXN(degerler.getDouble("MXN"));
            kurlar.setMYR(degerler.getDouble("MYR"));
            kurlar.setNOK(degerler.getDouble("NOK"));
            kurlar.setNZD(degerler.getDouble("NZD"));
            kurlar.setPHP(degerler.getDouble("PHP"));
            kurlar.setPLN(degerler.getDouble("PLN"));
            kurlar.setRON(degerler.getDouble("RON"));
            kurlar.setRUB(degerler.getDouble("RUB"));
            kurlar.setSEK(degerler.getDouble("SEK"));
            kurlar.setSGD(degerler.getDouble("SGD"));
            kurlar.setTHB(degerler.getDouble("THB"));
            kurlar.setTRY(degerler.getDouble("TRY"));
            kurlar.setZAR(degerler.getDouble("ZAR"));
            kurlar.setEUR(degerler.getDouble("EUR"));
            
        } catch (Exception e) { e.printStackTrace();}
        
        return kurlar;
    }
    @RequestMapping("/kurlar/tarihAraligi")
    public DovizObject addMedia(@RequestParam("dovizCinsi") String dovizCins,
            @RequestParam("limit") String limit,HttpSession session)//son kayitli row daki kurlari alir androideki doviz listesi icin
    {
        
        DovizObject kurlar = new DovizObject();//tek doviz cok sayida tarih
        ArrayList<String> tarihler = new ArrayList<>();//tarihler
        ArrayList<Double> kurDegerleri = new ArrayList<>();//gunluk kur bilgisi
        try {
            ResultSet degerler = db.qWMR("select tarih,"+dovizCins+" from kurlar order by id DESC LIMIT "+limit);
            while(degerler.next()) {
                tarihler.add(0,degerler.getString("tarih"));
                kurDegerleri.add(0,degerler.getDouble(dovizCins));
            }
            kurlar.setDegerler(kurDegerleri);
            kurlar.setTarihler(tarihler);
            
            
        } catch (Exception e) { e.printStackTrace();}
        
        return kurlar;
    }
    @RequestMapping("/kurlar/login")
    public Map<String,String> login(@RequestParam("un") String un, @RequestParam("pw") String pw)
    {
        String sql = "select count(*) as 'cnt', id, un, ad,bakiye from users where un = '%s' and pw = '%s'";
        sql = String.format(sql, un, pw);
        
        ResultSet rs = db.qW1R(sql);
        HashMap<String,String> hm = new HashMap<>();
        try
        {
            String id = rs.getString("id");
            hm.put("result", rs.getString("cnt"));
            hm.put("id", rs.getString("id"));
            hm.put("un", rs.getString("un"));
            hm.put("bakiye", rs.getString("bakiye"));
            
        } catch (Exception e ) { e.printStackTrace(); }
        
        return hm;
    }
    @RequestMapping("/kurlar/dovizAl")
    public String setZiyaret(@RequestParam("user_id") String id,
            @RequestParam("dovizCinsi") String dovizCinsi,
            @RequestParam("kurFiyati") String kurFiyati,
            @RequestParam("alinacakMiktar") String alinacakMiktar,
            @RequestParam("bakiye") String bakiye)
    {
        Double kalanBakiye = Double.parseDouble(bakiye)-Integer.parseInt(alinacakMiktar);
        
        String sql = "INSERT INTO islemler VALUES (0, %s, '%s', %s, %s, NULL, now(), NULL);";
        db.qWNR(String.format(sql, id,dovizCinsi,alinacakMiktar,kurFiyati));//doviz alimi gerceklesir
        String sql2="update users set bakiye = %s where id =%s;";
        db.qWNR(String.format(sql2,kalanBakiye.toString(),id));//bakiyeden dusulur
        return  "{'msg':'ok'}";
    }
    /*@RequestMapping("/kurlar/satisListesi") // Duzeltilecek
    public HashMap<String,String> satisListesii(@RequestParam("user_id") String id)
    {
        ArrayList<satisListesi> liste = new ArrayList<>();
        HashMap<String,String> hm = new HashMap<>();
        try {
            ResultSet degerler = db.qWMR("select kurlar.*,islemler.id as id1,islemler.alis_fiyati,islemler.doviz_cinsi from islemler,kurlar where user_id= "+id+" and satis_fiyati IS NULL and kurlar.id=(SELECT MAX(id) FROM kurlar)");//sadece satilmamis dovisleri goster
            
            while(degerler.next()) {
                
                //satisListesi eleman= new satisListesi();
                //eleman.setIslem_id(degerler.getInt("id1"));
                ArrayList<String> islemElemanlari = new ArrayList<>();
                islemElemanlari.add(0,degerler.getString("alis_fiyati"));
                islemElemanlari.add(0,degerler.getString("doviz_cinsi"));
                System.out.println("sutun ismi  :"+islemElemanlari.get(0));
                islemElemanlari.add(0,degerler.getString(islemElemanlari.get(0)));
                hm.put(degerler.getString("id1"),islemElemanlari.toString());
                
                //eleman.setIslemeAitVeriler(islemElemanlari);
                //liste.add(eleman);
            }
        } catch (Exception e) { e.printStackTrace();}
        return hm;
    }*/
    @RequestMapping("/kurlar/satisListesi")
    public satisListesi satisListesii(@RequestParam("user_id") String id)
    {
        satisListesi liste = new satisListesi();//
        ArrayList<String> dovizCinsleri = new ArrayList<>();//sahip olunan doviz cinsleri
        ArrayList<Double> alindigiKur = new ArrayList<>();//satin alindigi tarihteki kur
        ArrayList<Double> satisKuru = new ArrayList<>();//satacagi kur fiyati 
        ArrayList<Integer> islemId = new ArrayList<>();//kayitli islem id leri
        try {
            ResultSet degerler = db.qWMR("select * from islemler where user_id= "+id+" and satis_fiyati IS NULL");//sadece satilmamis dovisleri goster
            while(degerler.next()) {
                dovizCinsleri.add(0,degerler.getString("doviz_cinsi"));
                alindigiKur.add(0,degerler.getDouble("alis_fiyati"));
                islemId.add(0,degerler.getInt("id"));
            }
        } catch (Exception e) { e.printStackTrace();}
        if(dovizCinsleri.size()>0){//eger kulanicinin sahip oldugu doviz varsa mevcut kuru cekmek icin bu islem yapilir
            String dovizisimleri="";
            for (int i = 0; i < dovizCinsleri.size(); i++) {dovizisimleri=dovizisimleri+dovizCinsleri.get(i)+",";}
            dovizisimleri=dovizisimleri.substring(0, dovizisimleri.length()-1);//en sondaki virgulu silmek icin
            System.out.println("select " +dovizisimleri+" from kurlar order by id DESC LIMIT 1;");
            ResultSet degerler=db.qW1R("select " +dovizisimleri+" from kurlar order by id DESC LIMIT 1;");
            try {
                for (int i = 0; i < dovizCinsleri.size(); i++) {
                    satisKuru.add(degerler.getDouble(dovizCinsleri.get(i)));
                }
            } catch (Exception e) {
            }
        }
        
        liste.setSuandakiKur(satisKuru);
        liste.setAlindigiKur(alindigiKur);
        liste.setIslemId(islemId);
        liste.setDovizCinsi(dovizCinsleri);
        
        return liste;
    }
    @RequestMapping("/kurlar/dovizBoz")
    public String dovizBoz(@RequestParam("user_id") String id,
            @RequestParam("islem_id") String islem_id,
            @RequestParam("satisKuru") String satisKuru,
            @RequestParam("bakiye") String bakiye)
    {
        Double yBakiye =Double.parseDouble(bakiye);//doviz bozma isleminden ele gecen para
        try {
            ResultSet degerler=db.qW1R("select * from islemler where id="+islem_id);
            Double kazanilan = degerler.getInt("miktar")*Double.parseDouble(satisKuru);
            yBakiye+=kazanilan;//bakiyeye eklendi
            
        } catch (Exception e) {
        }
        String sql="update users set bakiye = %s where id =%s;";
        db.qWNR(String.format(sql,yBakiye.toString(),id));//bakiyeden dusulur
        String sql2="update islemler set satis_fiyati = %s,satis_tarihi=now() where id =%s;";
        db.qWNR(String.format(sql2,satisKuru,islem_id));//islemleri gunceller
        
        
        return  "{'bakiye':'"+String.format("%.2f", yBakiye)+"'}";
    }
    @RequestMapping("/kurlar/islemGecmisi")
    public satisListesi ıslemGecmisi(@RequestParam("user_id") String id)
    {
        satisListesi liste = new satisListesi();//
        ArrayList<String> dovizCinsleri = new ArrayList<>();//satilan doviz cinsleri
        ArrayList<Double> alindigiKur = new ArrayList<>();//satilan tarihteki kur
        ArrayList<Double> satisKuru = new ArrayList<>();//satildigi kur fiyati 
        ArrayList<Integer> islemId = new ArrayList<>();//kayitli islem id leri
        try {
            ResultSet degerler = db.qWMR("select * from islemler where user_id= "+id+" and satis_fiyati!='NULL'");//sadece satilmamis dovisleri goster
            while(degerler.next()) {
                dovizCinsleri.add(0,degerler.getString("doviz_cinsi"));
                alindigiKur.add(0,degerler.getDouble("satis_fiyati"));
                islemId.add(0,degerler.getInt("id"));
            }
        } catch (Exception e) { e.printStackTrace();}
        if(dovizCinsleri.size()>0){//eger kulanicinin sahip oldugu doviz varsa mevcut kuru cekmek icin bu islem yapilir
            String dovizisimleri="";
            for (int i = 0; i < dovizCinsleri.size(); i++) {dovizisimleri=dovizisimleri+dovizCinsleri.get(i)+",";}
            dovizisimleri=dovizisimleri.substring(0, dovizisimleri.length()-1);//en sondaki virgulu silmek icin
            System.out.println("select " +dovizisimleri+" from kurlar order by id DESC LIMIT 1;");
            ResultSet degerler=db.qW1R("select " +dovizisimleri+" from kurlar order by id DESC LIMIT 1;");
            try {
                for (int i = 0; i < dovizCinsleri.size(); i++) {
                    satisKuru.add(degerler.getDouble(dovizCinsleri.get(i)));
                }
            } catch (Exception e) {
            }
        }
        
        liste.setSuandakiKur(satisKuru);
        liste.setAlindigiKur(alindigiKur);
        liste.setIslemId(islemId);
        liste.setDovizCinsi(dovizCinsleri);
        
        return liste;
    }
    
}
