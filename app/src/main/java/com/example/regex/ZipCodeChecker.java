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
        final StringBuilder res = new StringBuilder();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run(){
                try
                {
                    URL url = new URL("https://kodpocztowy.intami.pl/api/" + zip_code);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.connect();
                    String city = "";
                    int status = con.getResponseCode();
                    if(status == 200){

                        Scanner scanner = new Scanner(url.openStream());

                        StringBuilder jsonData = new StringBuilder();
                        while (scanner.hasNext()) {
                            jsonData.append(scanner.nextLine());
                        }
                        scanner.close();

                        String data = String.valueOf(jsonData);
                        JSONArray jsonArr = new JSONArray(data);
                        for (int i = 0; i < jsonArr.length(); i++)
                        {
                            JSONObject jsonObj = jsonArr.getJSONObject(i);

                            String tempcity = jsonObj.getString("miejscowosc");

                            if(!tempcity.isEmpty()){
                                city = tempcity;
                                break;
                            }
                        }

                        res.append(city);

                    }
                    else{
                        Log.e("ERR","HttpResponseCode: " + status);
                    }

                }
                catch(Exception e){
                    Log.e("ERROR",e.toString());
                }

            }
        });
        t.start();
        t.join();

        String result = res.toString();
        return result;
    }

}
