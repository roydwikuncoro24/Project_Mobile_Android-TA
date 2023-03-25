package com.example.stocki;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stocki.AktifitasAdmin.AdminUpdatePassword;
import com.example.stocki.AktifitasKaryawan.KaryawanUpdatePassword;
import com.example.stocki.ModelResponse.LoginResponse;
import com.example.stocki.AktifitasToko.TokoUpdatePassword;
import com.example.stocki.helper.TinyDB;
import com.example.stocki.service.APIService;
import com.example.stocki.service.RetrofitHelper;

import java.net.SocketTimeoutException;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LupaPassword extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    EditText txtusername;
    Button btnGantiPassword;
    SpotsDialog dialog;
    private SharedPreferences pref;
    TinyDB tinyDB;
    String[] menu = {"Pilih User", "Distributor", "Toko", "Karyawan"};
    Spinner s11;
    ConnectivityManager conMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);

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

        } catch (Exception e) {

        }

        btnGantiPassword = findViewById(R.id.btnGantiPassword);
        txtusername = findViewById(R.id.txtLupaPassword);
        dialog = new SpotsDialog(LupaPassword.this, "Login...");

        TextView linkforget = findViewById(R.id.linkLogin);
        linkforget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LupaPassword.this, Login.class);
                startActivity(i);
            }
        });

        s11 = (Spinner) findViewById(R.id.spinnerLupapas);
        s11.setBackgroundColor(getResources().getColor(R.color.white));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menu);
        s11.setAdapter(adapter);

        btnGantiPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = s11.getSelectedItemPosition();
            }
        });

        btnGantiPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = s11.getSelectedItemPosition();
                String mUsername = txtusername.getText().toString().trim();

                if (mUsername.isEmpty()) {
                    txtusername.setError("Masukan username terlebih dahulu");
                    Toast.makeText(LupaPassword.this, "Masukan username terlebih dahulu", Toast.LENGTH_SHORT).show();
                    txtusername.setFocusable(true);
                } else if (menu[index] == "Distributor"){
                    loginAdmin();
                } else if (menu[index] == "Toko") {
                    loginToko();
                } else if (menu[index] == "Karyawan") {
                    loginKaryawan();
                } else {
                    Toast.makeText(LupaPassword.this,"Maaf, anda belum memilih User..!!", Toast.LENGTH_SHORT).show();
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
        Call<LoginResponse> call = api.lupaPassToko(txtusername.getText().toString());
        System.out.println("masuk");
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code()==1) {
                    System.out.println("login berhasil");
                    tinyDB.putObject("toko_login",response.body().getResult_toko().get(0));
                    Intent intent = new Intent(LupaPassword.this, TokoUpdatePassword.class);
                    dialog.dismiss();
                    startActivity(intent);
                    finish();
                }else {
                    dialog.dismiss();
                    Toast.makeText(LupaPassword.this, "User tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    dialog.dismiss();
                    Toast.makeText(LupaPassword.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void loginKaryawan(){
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<LoginResponse> call = api.lupaPassKaryawan(txtusername.getText().toString());
        System.out.println("masuk");
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code()==1) {
                    System.out.println("login berhasil");
                    tinyDB.putObject("karyawan_login",response.body().getResult_karyawan().get(0));
                    Intent intent = new Intent(LupaPassword.this, KaryawanUpdatePassword.class);
                    dialog.dismiss();
                    startActivity(intent);
                    finish();
                }else {
                    dialog.dismiss();
                    Toast.makeText(LupaPassword.this, "User tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    dialog.dismiss();
                    Toast.makeText(LupaPassword.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void loginAdmin(){
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<LoginResponse> call = api.lupaPassAdmin(txtusername.getText().toString());
        System.out.println("masuk");
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code()==1) {
                    System.out.println("login berhasil");
                    tinyDB.putObject("admin_login",response.body().getResult_admin().get(0));
                    Intent intent = new Intent(LupaPassword.this, AdminUpdatePassword.class);
                    dialog.dismiss();
                    startActivity(intent);
                    finish();
                }else {
                    dialog.dismiss();
                    Toast.makeText(LupaPassword.this, "User tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    dialog.dismiss();
                    Toast.makeText(LupaPassword.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
