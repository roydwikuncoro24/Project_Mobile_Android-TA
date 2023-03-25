package com.example.stocki.AktifitasToko;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stocki.Adapter.TokoBarangAdapter;
import com.example.stocki.Adapter.TokoDetailRecTransaksiAdapter;
import com.example.stocki.ModelData.AdminModelData;
import com.example.stocki.ModelData.BarangModelData;
import com.example.stocki.ModelData.KaryawanModelData;
import com.example.stocki.ModelData.PenjualanModelData;
import com.example.stocki.ModelData.TokoModelData;
import com.example.stocki.ModelData.TransaksiModelData;
import com.example.stocki.ModelResponse.BarangGetResponse;
import com.example.stocki.ModelResponse.GetTransaksiResponse;
import com.example.stocki.ModelResponse.PenjualanGetResponse;
import com.example.stocki.R;
import com.example.stocki.helper.TinyDB;
import com.example.stocki.service.APIService;
import com.example.stocki.service.RetrofitHelper;

import java.net.SocketTimeoutException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TokoDetailRecTransaksi extends AppCompatActivity {

    RecyclerView recyclerView;
    TokoDetailRecTransaksiAdapter tokoDetailRecTransaksiAdapter;
    ArrayList<TransaksiModelData> transaksiList;
    ArrayList<PenjualanModelData> penjualanList = new ArrayList<PenjualanModelData>();
    ArrayList<BarangModelData> barangArrayList = new ArrayList<BarangModelData>();
    TinyDB tinyDB;
    String idtransaksi, ket, jumlah, jmlbar, tanggal = "", timeStamp;;
    SwipeRefreshLayout swipeContainer;
    String nilai;
    TextView txPetugas, txTanggal, txJmlBar, txJumlahHar;
    ProgressDialog progressDialog;
    TokoModelData toko;
    AdminModelData admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordtransaksidetail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tinyDB = new TinyDB(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Harap tunggu...");
        nilai = "0";

        txJumlahHar = (TextView)findViewById(R.id.tx_jumlah_laporan);
        txJmlBar = (TextView)findViewById(R.id.tx_jmlbar_laporan);
        txTanggal = (TextView)findViewById(R.id.tx_tgl_laporan);
        txPetugas = (TextView)findViewById(R.id.tx_petugas_laporan);

        timeStamp = new SimpleDateFormat("dd-MM-yyyy_HH-mm").format(new Date());

        if (getIntent().hasExtra("id")){
            idtransaksi = getIntent().getStringExtra("id");
            setTitle("No. TokoTransaksi : "+idtransaksi);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerHTransaksi);
        tokoDetailRecTransaksiAdapter = new TokoDetailRecTransaksiAdapter(penjualanList, TokoDetailRecTransaksi.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(TokoDetailRecTransaksi.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(tokoDetailRecTransaksiAdapter);

        System.out.println("list : "+ barangArrayList);

        swipeContainer = (SwipeRefreshLayout)findViewById(R.id.swipeHTransaksi);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListPenjualan();
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeContainer.setRefreshing(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        swipeContainer.setRefreshing(true);
        getListTransaksi();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void getListPenjualan() {
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<PenjualanGetResponse> call = api.showListTransaksiToko(((TokoModelData)
                tinyDB.getObject("toko_login", TokoModelData.class)).getIdtoko(), idtransaksi);
        System.out.println("masuk");
        call.enqueue(new Callback<PenjualanGetResponse>() {
            @Override
            public void onResponse(Call<PenjualanGetResponse> call, Response<PenjualanGetResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code().equals("1")) {
                    penjualanList.clear();
                    penjualanList.addAll(response.body().getResult_penjualan());
                    tokoDetailRecTransaksiAdapter.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);
                } else {
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(TokoDetailRecTransaksi.this, "Tidak ada data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PenjualanGetResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(TokoDetailRecTransaksi.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getListTransaksi() {
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<GetTransaksiResponse> call = api.showDataTransaksiTokoDetail(idtransaksi);
        System.out.println("masuk");
        call.enqueue(new Callback<GetTransaksiResponse>() {
            @Override
            public void onResponse(Call<GetTransaksiResponse> call, Response<GetTransaksiResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code().equals("1")) {
                    txJumlahHar.setText("Rp. " + doubleToStringNoDecimal(String.valueOf(Double.parseDouble(response.body().getResult_transaksi().get(0).getJual()))));
                    txJmlBar.setText(response.body().getResult_transaksi().get(0).getJumlah());
                    txPetugas.setText(response.body().getResult_transaksi().get(0).getPetugas());
                    txTanggal.setText(response.body().getResult_transaksi().get(0).getTanggal());
                    swipeContainer.setRefreshing(false);
                    getListPenjualan();
                } else {
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(TokoDetailRecTransaksi.this, "Tidak ada record transaksi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetTransaksiResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(TokoDetailRecTransaksi.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static String doubleToStringNoDecimal(String value) {
        double d = Double.parseDouble(value);
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###");
        return formatter.format(d).replace(",", ".");
    }

}
