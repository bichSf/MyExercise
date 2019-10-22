package com.example.tranbichsf.jsondemo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity {

    ListView listUser;
    ArrayList<UserModel> arrayUser;
    ArrayList<String> arrayNameUser;
    UserModel user;
    AddressModel address;
    GeoModel geo;
    CompanyModel company;

    public ArrayList<UserModel> getArrayUser() {
        return arrayUser;
    }

    public void setArrayUser(ArrayList<UserModel> arrayUser) {
        this.arrayUser = arrayUser;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listUser = (ListView) findViewById(R.id.listUser);

        arrayUser = new ArrayList<>();
        arrayNameUser = new ArrayList<>();

        DownloadTask str = new DownloadTask();
        str.execute("https://jsonplaceholder.typicode.com/users");

        try {
            String jsonString ="";
            JSONArray jArr = new JSONArray(jsonString);

            for (int i = 0; i < jArr.length(); i++) {
                user = new UserModel();
                address = new AddressModel();
                geo = new GeoModel();
                company = new CompanyModel();

                geo.setLat(jArr.getJSONObject(i).getJSONObject("address").getJSONObject("geo").getDouble("lat"));
                geo.setLng(jArr.getJSONObject(i).getJSONObject("address").getJSONObject("geo").getDouble("lng"));

                address.setStreet(jArr.getJSONObject(i).getJSONObject("address").getString("street"));
                address.setSuite(jArr.getJSONObject(i).getJSONObject("address").getString("suite"));
                address.setCity(jArr.getJSONObject(i).getJSONObject("address").getString("city"));
                address.setZipcode(jArr.getJSONObject(i).getJSONObject("address").getString("zipcode"));
                address.setGeo(geo);

                company.setName(jArr.getJSONObject(i).getJSONObject("company").getString("name"));
                company.setCatchPhrase(jArr.getJSONObject(i).getJSONObject("company").getString("catchPhrase"));
                company.setBs(jArr.getJSONObject(i).getJSONObject("company").getString("bs"));

                user.setId(jArr.getJSONObject(i).getInt("id"));
                user.setName(jArr.getJSONObject(i).getString("name"));
                user.setUsername(jArr.getJSONObject(i).getString("username"));
                user.setEmail(jArr.getJSONObject(i).getString("email"));
                user.setAddress(address);
                user.setPhone(jArr.getJSONObject(i).getString("phone"));
                user.setWebsite(jArr.getJSONObject(i).getString("website"));
                user.setCompany(company);

                this.arrayUser.add(user);
                this.arrayNameUser.add(user.getName());
            }

            ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, arrayUser);

            listUser.setAdapter(adapter);

//            listUser.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//                @Override
//                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long id) {
//                    makeText(MainActivity.this, arrayUser.get(i), Toast.LENGTH_SHORT).show();
//                    return false;
//                }
//            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            StringBuffer response = new StringBuffer();
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                Log.v("TAG", "Response code: " + responseCode);

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response.toString();
        }
    }
}