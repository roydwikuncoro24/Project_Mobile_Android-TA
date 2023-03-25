package com.example.stocki.AktifitasToko;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stocki.AktifitasAdmin.AdminTambahMitra;
import com.example.stocki.ModelData.AdminModelData;
import com.example.stocki.ModelData.TokoModelData;
import com.example.stocki.ModelResponse.InsertResponse;
import com.example.stocki.R;
import com.example.stocki.helper.TinyDB;
import com.example.stocki.service.APIService;
import com.example.stocki.service.RetrofitHelper;

import java.net.SocketTimeoutException;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TokoTambahKaryawan extends AppCompatActivity {

    EditText username, nama, email, alamat, telp, pass, cpass;
    Button daftar;
    TextView login;
    SpotsDialog dialogregister;
    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tokoregkaryawan);

//        setTitle("Tambah Karyawan");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tinyDB = new TinyDB(this);

        username = findViewById(R.id.txtRegisterUsername);
        nama = findViewById(R.id.txtRegisterNama);
        email = findViewById(R.id.txtRegisterMail);
        alamat = findViewById(R.id.txtRegisterAlamat);
        telp = findViewById(R.id.txtRegisterTelp);
        pass = findViewById(R.id.txtRegisterPass);
        cpass = findViewById(R.id.txtRegisterUlangi);
        daftar = findViewById(R.id.btnDaftar);
        dialogregister =  new SpotsDialog(TokoTambahKaryawan.this, "Loading...");

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(username)||isEmpty(nama)||isEmpty(email)||isEmpty(alamat)||
                        isEmpty(telp)||isEmpty(pass)||isEmpty(cpass)) {
                    Toast.makeText(TokoTambahKaryawan.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
                }else if(!pass.getText().toString().equals(cpass.getText().toString())) {
                    Toast.makeText(TokoTambahKaryawan.this,"Kata sandi tidak sama!",Toast.LENGTH_SHORT).show();
                } else {
                    Register();
                }
            }
        });
    }

    public void Register(){
        dialogregister.show();
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<InsertResponse> call = api.TokoAddKaryawan((
                (TokoModelData) tinyDB.getObject("toko_login", TokoModelData.class)).getIdtoko(),
                nama.getText().toString(),username.getText().toString(),
                email.getText().toString(), alamat.getText().toString(),
                telp.getText().toString(),pass.getText().toString());
        System.out.println("masuk");
        call.enqueue(new Callback<InsertResponse>() {
            @Override
            public void onResponse(Call<InsertResponse> call, Response<InsertResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code().equals("1")) {
                    Toast.makeText(TokoTambahKaryawan.this, "Register Mitra berhasil", Toast.LENGTH_SHORT).show();
                    //dialogberhasil();
                    finish();
                    dialogregister.dismiss();
                }else{
                    dialogregister.dismiss();
                    Toast.makeText(TokoTambahKaryawan.this, ""+ response.body().getStatus(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InsertResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    dialogregister.dismiss();
//                    swipeContainer.setRefreshing(false);
                    Toast.makeText(TokoTambahKaryawan.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
