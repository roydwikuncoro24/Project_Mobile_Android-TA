package com.example.stocki.AktifitasAdmin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stocki.ModelData.AdminModelData;
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

public class AdminTambahMitra extends AppCompatActivity {

    EditText namatoko, nampem, username, email, alamat, notelp, pass, ulangi;
    Button daftar;
    TextView login;
    SpotsDialog dialogregister;
    String getIdowner;
    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminregtoko);

//        setTitle("Tambah Mitra");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tinyDB = new TinyDB(this);
        namatoko = findViewById(R.id.txtRegisterNama);
        email = findViewById(R.id.txtRegisterMail);
        nampem = findViewById(R.id.txtRegisterNamaPem);
        username = findViewById(R.id.txtRegisterUsername);
        alamat = findViewById(R.id.txtRegisterAlamat);
        notelp = findViewById(R.id.txtRegisterTelp);
        pass = findViewById(R.id.txtRegisterPass);
        ulangi = findViewById(R.id.txtRegisterUlangi);
        daftar = findViewById(R.id.btnDaftar);
        dialogregister = new SpotsDialog(AdminTambahMitra.this, "Loading...");

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(namatoko)||isEmpty(email)||isEmpty(nampem)||isEmpty(username)
                        ||isEmpty(alamat)||isEmpty(notelp)||isEmpty(pass)||isEmpty(ulangi)) {
                    Toast.makeText(AdminTambahMitra.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
                }else if(!pass.getText().toString().equals(ulangi.getText().toString())) {
                    Toast.makeText(AdminTambahMitra.this,"Kata sandi tidak sama!",Toast.LENGTH_SHORT).show();
                } else {
                    Register();
                }
            }
        });
    }

    public void Register(){
        dialogregister.show();
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<InsertResponse> call = api.AdminAddMitra((
                (AdminModelData) tinyDB.getObject("admin_login", AdminModelData.class)).getIdadmin(),
                namatoko.getText().toString(),nampem.getText().toString(),
                email.getText().toString(), username.getText().toString(),
                alamat.getText().toString(), notelp.getText().toString(),
                pass.getText().toString());
        System.out.println("masuk");
        call.enqueue(new Callback<InsertResponse>() {
            @Override
            public void onResponse(Call<InsertResponse> call, Response<InsertResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code().equals("1")) {
                    Toast.makeText(AdminTambahMitra.this, "Register Mitra berhasil", Toast.LENGTH_SHORT).show();
                    //dialogberhasil();
                    finish();
                    dialogregister.dismiss();
                }else{
                    dialogregister.dismiss();
                    Toast.makeText(AdminTambahMitra.this, ""+ response.body().getStatus(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InsertResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    dialogregister.dismiss();
//                    swipeContainer.setRefreshing(false);
                    Toast.makeText(AdminTambahMitra.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
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
