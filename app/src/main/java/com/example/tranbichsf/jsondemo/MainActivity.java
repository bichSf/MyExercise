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

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listUser;
    ArrayList<UserModel> arrayUser;
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

        new DownloadTask().execute();

        try {
            String jsonString = "";
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
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

//        listUser.setOnLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                Toast.makeText(MainActivity.this, arrayUser.get(i), Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });
    }

    private class DownloadTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("https://jsonplaceholder.typicode.com/users");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                int responseCode = con.getResponseCode();
                Log.v("TAG", "Response code: " + responseCode);

                InputStream inputStream = con.getInputStream();
                OutputStream outputStream = openFileOutput("users.json", MODE_PRIVATE);

                byte[] buffer = new byte[1024];
                int len;

                while ((len = inputStream.read(buffer)) > 0)
                    outputStream.write(buffer, 0, len);

                Log.v("TAG", String.valueOf(buffer));

                outputStream.close();
                inputStream.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        }
    }

}
