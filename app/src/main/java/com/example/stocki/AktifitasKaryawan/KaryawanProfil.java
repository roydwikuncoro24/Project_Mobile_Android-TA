package com.example.stocki.AktifitasKaryawan;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stocki.ModelData.KaryawanModelData;
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

public class KaryawanProfil extends AppCompatActivity {

    EditText mail, telp, alamat;
    String smail, salamat, stelp;
    TextView nama;
    SpotsDialog dialogtoko;
    Menu action;
    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        getSupportActionBar().setTitle("Profil Karyawan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tinyDB = new TinyDB(this);

        nama = findViewById(R.id.tvnamaUser);
        mail = findViewById(R.id.txtMail);
        telp = findViewById(R.id.txtTelp);
        alamat = findViewById(R.id.txtAlamat);
        mail.setEnabled(false);
        telp.setEnabled(false);
        alamat.setEnabled(false);
        dialogtoko = new SpotsDialog(KaryawanProfil.this, "Loading...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getProfile();
    }

    public void getProfile(){
        dialogtoko.show();
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<GetProfilResponse> call = api.getKaryawan(((KaryawanModelData) tinyDB.getObject("karyawan_login", KaryawanModelData.class)).getIdkaryawan());
        System.out.println("masuk");
        call.enqueue(new Callback<GetProfilResponse>() {
            @Override
            public void onResponse(Call<GetProfilResponse> call, retrofit2.Response<GetProfilResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code()==1) {
                    nama.setText(response.body().getResult_karyawan().get(0).getNamakaryawan());
                    telp.setText(response.body().getResult_karyawan().get(0).getNotelp());
                    alamat.setText(response.body().getResult_karyawan().get(0).getAlamat());
                    mail.setText(response.body().getResult_karyawan().get(0).getEmail());
                    dialogtoko.dismiss();
                }
            }
            @Override
            public void onFailure(Call<GetProfilResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    dialogtoko.dismiss();
                    Toast.makeText(KaryawanProfil.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_profiledit, menu);
        action = menu;
        action.findItem(R.id.menuSave).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuEdit:
                mail.setEnabled(true);
                telp.setEnabled(true);
                alamat.setEnabled(true);
                mail.setFocusableInTouchMode(true);
                alamat.setFocusableInTouchMode(true);
                telp.setFocusableInTouchMode(true);
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                assert inputMethodManager != null;
                inputMethodManager.showSoftInput(mail, InputMethodManager.SHOW_IMPLICIT);
                action.findItem(R.id.menuEdit).setVisible(false);
                action.findItem(R.id.menuSave).setVisible(true);
                return true;

            case R.id.menuSave:
                smail = mail.getText().toString();
                salamat = alamat.getText().toString();
                stelp = telp.getText().toString();

                if(smail.isEmpty()){
                    snackBar("Harap isi email anda!", R.color.Error);
                    mail.setFocusable(true);
                }else if (stelp.isEmpty()){
                    snackBar("Harap isi nomor telepon anda!", R.color.Error);
                    telp.setFocusable(true);
                }else if (salamat.isEmpty()){
                    snackBar("Harap isi alamat anda!", R.color.Error);
                    alamat.setFocusable(true);
                }else {
                    action.findItem(R.id.menuEdit).setVisible(true);
                    action.findItem(R.id.menuSave).setVisible(false);
                    updateProfile();
                    mail.setFocusableInTouchMode(false);
                    telp.setFocusableInTouchMode(false);
                    alamat.setFocusableInTouchMode(false);
                    mail.setFocusable(false);
                    telp.setFocusable(false);
                    alamat.setFocusable(false);
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateProfile(){
        dialogtoko.show();
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<InsertResponse> call = api.updateProfilKaryawan(((KaryawanModelData) tinyDB.getObject("karyawan_login", KaryawanModelData.class)).getIdkaryawan(),
                mail.getText().toString(),telp.getText().toString(), alamat.getText().toString());
        System.out.println("masuk");
        call.enqueue(new Callback<InsertResponse>() {
            @Override
            public void onResponse(Call<InsertResponse> call, Response<InsertResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code().equals("1")) {
                    dialogtoko.dismiss();
                    Toast.makeText(KaryawanProfil.this, "Update berhasil", Toast.LENGTH_SHORT).show();
                    KaryawanModelData karyawan = ((KaryawanModelData) tinyDB.getObject("karyawan_login", KaryawanModelData.class));
                    karyawan.setEmail(mail.getText().toString());
                    karyawan.setNotelp(telp.getText().toString());
                    karyawan.setAlamat(alamat.getText().toString());
                    tinyDB.putObject("karyawan_login", karyawan);
                    getProfile();
                }else{
                    dialogtoko.dismiss();
                    Toast.makeText(KaryawanProfil.this, "Update gagal", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<InsertResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    dialogtoko.dismiss();
                    Toast.makeText(KaryawanProfil.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void snackBar(String pesan, int warna) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), pesan, Snackbar.LENGTH_LONG)
                .setAction("Action", null);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), warna));
        snackbar.show();
    }
}
