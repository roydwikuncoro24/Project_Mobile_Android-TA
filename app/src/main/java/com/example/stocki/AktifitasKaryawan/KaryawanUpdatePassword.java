package com.example.stocki.AktifitasKaryawan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stocki.AktifitasToko.TokoUpdatePassword;
import com.example.stocki.Login;
import com.example.stocki.ModelData.KaryawanModelData;
import com.example.stocki.ModelData.TokoModelData;
import com.example.stocki.ModelResponse.GetProfilResponse;
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

public class KaryawanUpdatePassword extends AppCompatActivity {

    EditText sandi, csandi;
    Button update;
    SpotsDialog dialog;
    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        setTitle("Update Password");
        tinyDB = new TinyDB(this);

        sandi = findViewById(R.id.txtGantiPassword);
        csandi = findViewById(R.id.txtConfirmGantiPassword);
        update = findViewById(R.id.btnUpdatePassword);
        dialog =  new SpotsDialog(KaryawanUpdatePassword.this, "Loading...");

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(sandi)||isEmpty(csandi)) {
                    Toast.makeText(KaryawanUpdatePassword.this,
                            "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
                }else if(!sandi.getText().toString().equals(csandi.getText().toString())) {
                    Toast.makeText(KaryawanUpdatePassword.this,
                            "Kata sandi tidak sama!",Toast.LENGTH_SHORT).show();
                } else {
                    updatePass();
                }
            }
        });
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void updatePass(){
        dialog.show();
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<InsertResponse> call = api.
                updatePassKaryawan(((KaryawanModelData) tinyDB.getObject("karyawan_login",
                        KaryawanModelData.class)).getIdkaryawan(),
                        sandi.getText().toString());
        System.out.println("masuk");
        call.enqueue(new Callback<InsertResponse>() {
            @Override
            public void onResponse(Call<InsertResponse> call, Response<InsertResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code().equals("1")) {
                    dialog.dismiss();
                    KaryawanModelData karyawan = ((KaryawanModelData) tinyDB.getObject(
                            "karyawan_login", KaryawanModelData.class));
                    tinyDB.putObject("karyawan_login", karyawan);
                    Intent intent = new Intent(KaryawanUpdatePassword.this, Login.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(KaryawanUpdatePassword.this,
                            "Update berhasil", Toast.LENGTH_SHORT).show();
                }else{
                    dialog.dismiss();
                    Toast.makeText(KaryawanUpdatePassword.this,
                            "Update gagal", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<InsertResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    dialog.dismiss();
                    Toast.makeText(KaryawanUpdatePassword.this,
                            "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
