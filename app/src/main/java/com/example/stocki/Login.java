package com.example.stocki;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stocki.AktifitasAdmin.Admin;
import com.example.stocki.AktifitasKaryawan.Karyawan;
import com.example.stocki.ModelResponse.LoginResponse;
import com.example.stocki.AktifitasToko.Toko;
import com.example.stocki.helper.TinyDB;
import com.example.stocki.service.APIService;
import com.example.stocki.service.RetrofitHelper;

import java.net.SocketTimeoutException;

import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText txtusername, txtpass;
    Button btnLogin;
    SpotsDialog dialoglogin;
    private SharedPreferences pref;
    TinyDB tinyDB;
    String[] menu = {"Pilih User", "Distributor", "Toko", "Karyawan"};
    Spinner s11;
    ConnectivityManager conMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }
        tinyDB = new TinyDB(this);
        try {
//            if (tinyDB.getObject("toko_login", TokoModelData.class) != null) {
//                startActivity(new Intent(Login.this, Toko.class));
//                finish();
//            }else if (tinyDB.getObject("admin_login", AdminModelData.class) != null){
//                startActivity(new Intent(Login.this, Admin.class));
//                finish();
//            }

        } catch (Exception e) {

        }

        btnLogin = findViewById(R.id.btnLogin);
        txtusername = findViewById(R.id.txtLoginUsername);
        txtpass = findViewById(R.id.txtLoginPass);
        dialoglogin = new SpotsDialog(Login.this, "Login...");

        TextView linkforget = findViewById(R.id.linkforget);
        linkforget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, LupaPassword.class);
                startActivity(i);
            }
        });

        s11 = (Spinner) findViewById(R.id.spinnerLogin);
        s11.setBackgroundColor(getResources().getColor(R.color.white));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menu);
        s11.setAdapter(adapter);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = s11.getSelectedItemPosition();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = s11.getSelectedItemPosition();
                String mUsername = txtusername.getText().toString().trim();
                String mPass = txtpass.getText().toString().trim();

                if (mUsername.isEmpty()){
                    txtusername.setError("Masukan username terlebih dahulu");
                    Toast.makeText(Login.this, "Masukan username terlebih dahulu", Toast.LENGTH_SHORT).show();
                    txtusername.setFocusable(true);
                } else if(mPass.isEmpty()){
                    Toast.makeText(Login.this, "Masukan password terlebih dahulu", Toast.LENGTH_SHORT).show();
                    txtpass.setFocusable(true);
                } else if (menu[index] == "Distributor"){
                    loginAdmin();
                } else if (menu[index] == "Toko") {
                    loginToko();
                } else if (menu[index] == "Karyawan") {
                    loginKaryawan();
                } else {
                    Toast.makeText(Login.this,"Maaf, anda belum memilih User..!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner spinner = (Spinner) adapterView;
        if((spinner.getId() == R.id.spinnerLogin)&&spinner.getSelectedItemPosition()>=0) {
            ((TextView) s11.getSelectedView()).setTextColor(getResources().getColor(R.color.white));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void loginToko(){
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<LoginResponse> call = api.loginToko(txtusername.getText().toString(),txtpass.getText().toString());
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code()==1) {
                    System.out.println("login berhasil");
                    tinyDB.putObject("toko_login",response.body().getResult_toko().get(0));
                    Intent intent = new Intent(Login.this, Toko.class);
                    dialoglogin.dismiss();
                    startActivity(intent);
                    finish();
                }else {
                    dialoglogin.dismiss();
                    Toast.makeText(Login.this, "Login gagal", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                System.out.println(t.getMessage());
                if (t instanceof SocketTimeoutException) {
                    dialoglogin.dismiss();
                    Toast.makeText(Login.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void loginKaryawan(){
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<LoginResponse> call = api.loginKaryawan(txtusername.getText().toString(),txtpass.getText().toString());
        System.out.println("masuk");
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code()==1) {
                    System.out.println("login berhasil");
                    tinyDB.putObject("karyawan_login",response.body().getResult_karyawan().get(0));
                    tinyDB.putObject("toko_login",response.body().getResult_toko().get(0));
                    Intent intent = new Intent(Login.this, Karyawan.class);
                    dialoglogin.dismiss();
                    startActivity(intent);
                    finish();
                }else {
                    dialoglogin.dismiss();
                    Toast.makeText(Login.this, "Login gagal", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    dialoglogin.dismiss();
                    Toast.makeText(Login.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void loginAdmin(){
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<LoginResponse> call = api.loginAdmin(txtusername.getText().toString(),txtpass.getText().toString());
        System.out.println("masuk");
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code()==1) {
                    System.out.println("login berhasil");
                    tinyDB.putObject("admin_login",response.body().getResult_admin().get(0));
                    Intent intent = new Intent(Login.this, Admin.class);
                    dialoglogin.dismiss();
                    startActivity(intent);
                    finish();
                }else {
                    dialoglogin.dismiss();
                    Toast.makeText(Login.this, "Login gagal", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    dialoglogin.dismiss();
                    Toast.makeText(Login.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    boolean doubleBackPressed=false;
    @Override
    public void onBackPressed(){
        if (doubleBackPressed){
            finish();
            System.exit(0);
        } else {
            doubleBackPressed=true;
            final ScrollView scrollView = findViewById(R.id.logintoko);
            Snackbar.make(scrollView, getString(R.string.pressbackagain),Snackbar.LENGTH_SHORT).show();
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackPressed=false;
                }
            },2000);
        }
    }
}

