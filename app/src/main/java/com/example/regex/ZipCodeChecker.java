package com.example.regex;

import android.util.Log;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class ZipCodeChecker {

    static String checkCity(String zip_code) throws Exception{
        final String[] result = {""};
        Thread t = new Thread(new Runnable() {
            @Override
            public void run(){
                try
                {
                    URL url = new URL("https://kodpocztowy.intami.pl/api/" + zip_code);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.connect();
                    int status = con.getResponseCode();
                    if(status == 200){
                        Log.d("TAG", "checkCity: ");
                    StringBuilder infoString = new StringBuilder();
                    Scanner scanner = new Scanner(url.openStream());

                    while(scanner.hasNext()){
                        infoString.append(scanner.nextLine());
                    }
                    JSONArray jsonArr = new JSONArray();
                    jsonArr.put(infoString);
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("result",jsonArr);

                    Log.d("zipcode", String.valueOf(infoString));

                    result[0] = String.valueOf(infoString);

                    }
                    else{
                        Log.e("ERR","HttpResponseCode: " + status);
                    }

                }
                catch(Exception e){
                    Log.e("ERR",e.toString());
                }

            }
        });
        t.start();
        t.join();
        return result[0];
    }

}
