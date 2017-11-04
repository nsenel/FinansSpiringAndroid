

import json
import urllib2
import mysql.connector

dovizCinsleri=["AUD", "BGN", "BRL","CAD","CHF","CNY","CZK","DKK","GBP","HKD","HRK","HUF","IDR","ILS","INR","JPY","KRW","MXN","MYR","NOK","NZD","PHP","PLN","RON","RUB","SEK","SGD","THB","TRY","ZAR","EUR"]
names=""
for  i in dovizCinsleri:
    names=names+i +" double DEFAULT NULL,"
String = "DROP TABLE IF EXISTS kurlar;CREATE TABLE kurlar (id int(11) NOT NULL AUTO_INCREMENT,tarih date DEFAULT NULL,"+names+"PRIMARY KEY (id)) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;"
#print String
con = mysql.connector.connect(user='root',
                              password ='',
                              host='localhost',
                              database='finansdata')


cur = con.cursor()
temp_degerler=""
for ay in range(1,10):
    for gun in range(1,32):
        degerler="'2017-0"+str(ay)+"-0"+str(gun)+"'"
        tarih="2017-0"+str(ay)+"-0"+str(gun)
        if gun>9:
            degerler="'2017-0"+str(ay)+"-"+str(gun)+"'"
            tarih="2017-0"+str(ay)+"-"+str(gun)
        try:
            data=json.load(urllib2.urlopen("https://api.fixer.io/"+tarih+"?base=USD"))
            dovizler = data.get("rates")
            for i in dovizCinsleri:
                degerler=degerler+","+str(dovizler.get(i))
            if degerler[30:]!=temp_degerler:
                sql = ("insert into kurlar (tarih,AUD, BGN, BRL, CAD, CHF, CNY, CZK, DKK, GBP, HKD, HRK, HUF, IDR, ILS, INR, JPY, KRW, MXN, MYR, NOK, NZD, PHP, PLN, RON, RUB, SEK, SGD, THB, TRY, ZAR, EUR) values ("+degerler+")")
                cur.execute(sql)
                con.commit()
                temp_degerler=degerler[30:]
                print sql
        except:
            pass

cur.close()
con.close()