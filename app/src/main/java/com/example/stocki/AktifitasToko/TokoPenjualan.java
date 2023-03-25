package com.example.stocki.AktifitasToko;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stocki.Adapter.TokoPenjualanAdapter;
import com.example.stocki.ModelData.PenjualanModelData;
import com.example.stocki.ModelData.TipeBarangModel;
import com.example.stocki.ModelData.TokoModelData;
import com.example.stocki.ModelData.TransaksiModelData;
import com.example.stocki.ModelResponse.GetTipeBarangResponse;
import com.example.stocki.ModelResponse.PenjualanGetResponse;
import com.example.stocki.R;
import com.example.stocki.helper.SortbyIdPenjualanDESC;
import com.example.stocki.helper.TinyDB;
import com.example.stocki.service.APIService;
import com.example.stocki.service.RetrofitHelper;

import java.net.SocketTimeoutException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TokoPenjualan extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    RecyclerView recyclerView;
    TokoPenjualanAdapter tpAdapter;
    ArrayList<PenjualanModelData> penjualanList = new ArrayList<PenjualanModelData>();
    ArrayList<TransaksiModelData> transaksiList = new ArrayList<TransaksiModelData>();
    ArrayList<PenjualanModelData> temp= new ArrayList<>();
    TinyDB tinyDB;
    SwipeRefreshLayout swipeContainer;
    Spinner spinFilter;
    ArrayList<String> arrayTipe = new ArrayList<>();
    HashMap<String, String> hashTipe = new HashMap<String, String>();
    int jual = 0; int laba = 0; int modal = 0;
    TextView txPenjualan, txLaba, txModal;
    ArrayAdapter<String> adapter;
    String timeStamp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_penjualan);
        setTitle("Data Penjualan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tinyDB = new TinyDB(this);
        Date date = new Date();
        timeStamp = new SimpleDateFormat("dd-MM-yyyy_HH-mm").format(date);

        spinFilter = findViewById(R.id.spin_filter);
        spinFilter.setOnItemSelectedListener(this);

        txPenjualan = findViewById(R.id.tx_totalpenjualan);
        txLaba = findViewById(R.id.tx_totallaba);
        txModal = findViewById(R.id.tx_totalmodal);

        recyclerView = (RecyclerView) findViewById(R.id.recPenjualan);
        tpAdapter = new TokoPenjualanAdapter(penjualanList, TokoPenjualan.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                TokoPenjualan.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(tpAdapter);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipePenjualan);
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

        adapter = new ArrayAdapter<String>(TokoPenjualan.this, android.R.layout.simple_spinner_item, arrayTipe);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinFilter.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        swipeContainer.setRefreshing(true);
        getTipe();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        filter(arrayTipe.get(i));
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toko_penjualan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuRecTrans:
                Intent intent = new Intent(TokoPenjualan.this, TokoRecTransaksi.class);
                startActivity(intent);
                return true;
            case R.id.sort_harip:
                filterHari();
                return true;
            case R.id.sort_bulanp:
                filterBulan();
                return true;
            case R.id.sort_tahunp:
                filterTahun();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void filter(String text) {
        temp = new ArrayList<PenjualanModelData>();
        if (!text.equals("Semua")) {
            for (PenjualanModelData pmd : penjualanList) {
                if (pmd.getTipe().toLowerCase().contains(text.toLowerCase())) {
                    temp.add(pmd);
                }
            }
        } else {
            temp = penjualanList;
        }
        tpAdapter.updateList(temp);
        calculateBalance(temp);
    }

    public void filterHari() {
        temp = new ArrayList<PenjualanModelData>();
        for (PenjualanModelData p : penjualanList) {
            if (p.getTanggal().substring(8,10).equals(timeStamp.substring(0,2))) {
                temp.add(p);
            }
        }
        tpAdapter.updateList(temp);
        calculateBalance(temp);
    }

    public void filterBulan() {
        temp = new ArrayList<PenjualanModelData>();
        for (PenjualanModelData p : penjualanList) {
            if (p.getTanggal().substring(5,7).equals(timeStamp.substring(3,5))) {
                temp.add(p);
            }
        }
        tpAdapter.updateList(temp);
        calculateBalance(temp);
    }

    public void filterTahun() {
        temp = new ArrayList<PenjualanModelData>();
        for (PenjualanModelData p : penjualanList) {
            if (p.getTanggal().substring(0,4).equals(timeStamp.substring(6,10))) {
                temp.add(p);
            }
        }
        tpAdapter.updateList(temp);
        calculateBalance(temp);
    }

    public void getTipe() {
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<GetTipeBarangResponse> call = api.showTipebarang();
        System.out.println("masuk");
        call.enqueue(new Callback<GetTipeBarangResponse>() {
            @Override
            public void onResponse(Call<GetTipeBarangResponse> call, Response<GetTipeBarangResponse> response) {
                if (response.body().getStatus_code().equals("1")) {
                    arrayTipe.clear();
                    hashTipe.clear();
                    arrayTipe.add("Semua");
                    hashTipe.put("0", "Semua");
                    for (TipeBarangModel kb : response.body().getResult_tipebarang()) {
                        hashTipe.put(kb.getIdtipe(), kb.getTipe());
                        arrayTipe.add(kb.getTipe());
                    }
                    adapter.notifyDataSetChanged();
                    getListPenjualan();
                } else {
                    swipeContainer.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<GetTipeBarangResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(TokoPenjualan.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getListPenjualan() {
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<PenjualanGetResponse> call = api.showPenjualanToko(((TokoModelData)
                tinyDB.getObject("toko_login", TokoModelData.class)).getIdtoko());
        System.out.println("masuk");
        call.enqueue(new Callback<PenjualanGetResponse>() {
            @Override
            public void onResponse(Call<PenjualanGetResponse> call, Response<PenjualanGetResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code().equals("1")) {
                    penjualanList.clear();
                    penjualanList.addAll(response.body().getResult_penjualan());
                    temp = penjualanList;
                    Collections.sort(penjualanList, new SortbyIdPenjualanDESC());
                    tpAdapter.updateList(penjualanList);
                    calculateBalance(penjualanList);
                    swipeContainer.setRefreshing(false);
                } else {
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(TokoPenjualan.this, "Tidak ada penjualan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PenjualanGetResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(TokoPenjualan.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void calculateBalance(ArrayList<PenjualanModelData> calculateList) {
        modal = 0;
        jual = 0;
        laba = 0;
        for (PenjualanModelData pm : calculateList) {
            modal = modal + Integer.parseInt(pm.getJumhargajual());
            jual = jual + Integer.parseInt(pm.getJumhargajualtoko());
            laba = jual - modal;
        }
        txModal.setTextColor(Color.BLACK);
        txModal.setText("Rp " + doubleToStringNoDecimal(Double.parseDouble(String.valueOf(modal))));
        txPenjualan.setTextColor(Color.BLACK);
        txPenjualan.setText("Rp " + doubleToStringNoDecimal(Double.parseDouble(String.valueOf(jual))));
        txLaba.setTextColor(Color.BLACK);
        txLaba.setText("Rp " + doubleToStringNoDecimal(Double.parseDouble(String.valueOf(laba))));
    }

    public static String doubleToStringNoDecimal(double d) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter .applyPattern("#,###");
        return formatter.format(d).replace(",",".");
    }
}
