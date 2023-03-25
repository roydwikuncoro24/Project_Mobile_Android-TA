package com.example.stocki.AktifitasToko;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.stocki.Adapter.InfoBarangAdapter;
import com.example.stocki.ModelData.PenjualanModelData;
import com.example.stocki.ModelResponse.InsertResponse;
import com.example.stocki.ModelResponse.PenjualanGetResponse;
import com.example.stocki.R;
import com.example.stocki.helper.SortbyIdPenjualanDESC;
import com.example.stocki.helper.TinyDB;
import com.example.stocki.service.APIService;
import com.example.stocki.service.RetrofitHelper;

import java.net.SocketTimeoutException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TokoInfoBarang extends AppCompatActivity {

    InfoBarangAdapter infoBarAdapter;
    TinyDB tinyDB;
    RecyclerView recyclerView;
    ArrayList<PenjualanModelData> penjualantList = new ArrayList<PenjualanModelData>();
    ArrayList<PenjualanModelData> temp= new ArrayList<>();
    String tipe, idbarang;
    SpotsDialog dialog;
    ImageView imgPhoto;
    String imageUrl = "", idtoko, namatoko, hdasar,hjual, idadmin, idtipe;
    TextView tVnama, tVstok, tVkode, tVhdasar, tVhjual, tVket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infobarang);

        getSupportActionBar().setTitle("Info Barang");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dialog = new SpotsDialog(TokoInfoBarang.this, "Loading...");

        tinyDB = new TinyDB(this);
        tVnama = findViewById(R.id.tx_InfoBar_Nama);
        tVstok = findViewById(R.id.tx_InfoBar_Stok);
        tVkode = findViewById(R.id.tx_InfoBar_Kode);
        tVhdasar = findViewById(R.id.tx_InfoBar_Hdasar);
        tVhjual = findViewById(R.id.tx_InfoBar_Hjual);
        tVket = findViewById(R.id.tx_InfoBar_Ket);
        imgPhoto = findViewById(R.id.img_Info_barang);

        idbarang = getIntent().getStringExtra("idbarang");
        idtipe = getIntent().getStringExtra("idtipe");
        imageUrl = getIntent().getStringExtra("image");
        idtoko = getIntent().getStringExtra("idtoko");
        namatoko = getIntent().getStringExtra("namatoko");
        idadmin = getIntent().getStringExtra("idadmin");
        hdasar = getIntent().getStringExtra("hargajual");
        hjual = getIntent().getStringExtra("hargajualtoko");
        tVnama.setText(getIntent().getStringExtra("namabarang"));
        tVstok.setText(getIntent().getStringExtra("stok"));
        tVkode.setText(getIntent().getStringExtra("kode"));
        tVhdasar.setText("Rp. "+ doubleToStringNoDecimal(Double.parseDouble(hdasar)));
        tVhjual.setText("Rp. "+ doubleToStringNoDecimal(Double.parseDouble(hjual)));
        tVket.setText(getIntent().getStringExtra("keterangan"));
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .error(R.drawable.ic_picture)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        Glide.with(TokoInfoBarang.this)
                .load(imageUrl)
                .apply(options)
                .into(imgPhoto);

        recyclerView = findViewById(R.id.recInfoBarang);
        infoBarAdapter = new InfoBarangAdapter(penjualantList, TokoInfoBarang.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                TokoInfoBarang.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(infoBarAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getListPenjualan();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(idadmin.equals("0")){
            inflater.inflate(R.menu.menu_infobarang, menu);
        }else {
            inflater.inflate(R.menu.menu_infobarang2, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuEdit:
                Intent intent = new Intent(TokoInfoBarang.this, TokoInputBarang.class);
                intent.putExtra("tipe","1");
                intent.putExtra("namabarang",tVnama.getText().toString());
                intent.putExtra("stok",tVstok.getText().toString());
                intent.putExtra("kode", tVkode.getText().toString());
                intent.putExtra("hargajual",hdasar.replace("Rp. ",""));
                intent.putExtra("hargajualtoko",hjual.replace("Rp. ",""));
                intent.putExtra("idtoko",idtoko);
                intent.putExtra("namatoko",namatoko);
                intent.putExtra("idadmin",idadmin);
                intent.putExtra("idbarang",idbarang);
                intent.putExtra("idtipe",idtipe);
                intent.putExtra("image",imageUrl);
                intent.putExtra("keterangan",tVket.getText().toString());
                startActivity(intent);
                return true;
            case R.id.menuHapus:
                dialog.show();
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                deleteBarang();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(TokoInfoBarang.this);
                builder.setMessage("Apakah anda yakin ingin hapus ?").setPositiveButton("Ya", dialogClickListener)
                        .setNegativeButton("Tidak", dialogClickListener).show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void getListPenjualan() {
        dialog.show();
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<PenjualanGetResponse> call = api.showDataPenjualanBarang(idbarang);
        System.out.println("masuk");
        call.enqueue(new Callback<PenjualanGetResponse>() {
            @Override
            public void onResponse(Call<PenjualanGetResponse> call, Response<PenjualanGetResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code().equals("1")) {
                    dialog.dismiss();
                    penjualantList.clear();
                    penjualantList.addAll(response.body().getResult_penjualan());
                    temp = penjualantList;
                    Collections.sort(penjualantList, new SortbyIdPenjualanDESC());
                    infoBarAdapter.updateList(penjualantList);
                } else {
                    dialog.dismiss();
                    Toast.makeText(TokoInfoBarang.this, "Tidak ada transaksi", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<PenjualanGetResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    dialog.dismiss();
                    Toast.makeText(TokoInfoBarang.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void deleteBarang() {
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<InsertResponse> call = api.deleteBarang(idbarang);
        System.out.println("masuk");
        call.enqueue(new Callback<InsertResponse>() {
            @Override
            public void onResponse(Call<InsertResponse> call, Response<InsertResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code().equals("1")) {
                    Toast.makeText(TokoInfoBarang.this, "Delete barang berhasil", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
//                    getListBarang();
                    finish();
                }else{
                    dialog.dismiss();
                    Toast.makeText(TokoInfoBarang.this, "Delete barang gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InsertResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    dialog.dismiss();
                    Toast.makeText(TokoInfoBarang.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static String doubleToStringNoDecimal(double d) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter .applyPattern("#,###");
        return formatter.format(d).replace(",",".");
    }
}
