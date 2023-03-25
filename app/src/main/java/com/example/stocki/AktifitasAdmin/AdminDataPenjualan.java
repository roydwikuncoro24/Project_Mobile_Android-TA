package com.example.stocki.AktifitasAdmin;

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

import com.example.stocki.Adapter.AdminPenjualanAdapter;
import com.example.stocki.ModelData.AdminModelData;
import com.example.stocki.ModelData.Penjualan;
import com.example.stocki.ModelData.PenjualanModelData;
import com.example.stocki.ModelData.TokoModelData;
import com.example.stocki.ModelData.TransaksiModelData;
import com.example.stocki.ModelResponse.GetMitraNKaryawan;
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

public class AdminDataPenjualan extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    RecyclerView recyclerView;
    AdminPenjualanAdapter tapAdapter;
    ArrayList<PenjualanModelData> penjualantList = new ArrayList<PenjualanModelData>();
    ArrayList<PenjualanModelData> temp= new ArrayList<>();
    TinyDB tinyDB;
    SwipeRefreshLayout swipeContainer;
    Spinner spinFilter;
    ArrayList<String> arrayToko = new ArrayList<>();
    HashMap<String, String> hashToko = new HashMap<String, String>();
    int jual = 0;; int laba = 0; int modal = 0;
    TextView txPenjualan, txLaba, txModal;
    ArrayAdapter<String> adapter;
    String timeStamp = "";
    AdminModelData admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_penjualan);
        setTitle("Data Penjualan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tinyDB = new TinyDB(this);
        admin = ((AdminModelData) tinyDB.getObject("admin_login", AdminModelData.class));

        Date date = new Date();
        timeStamp = new SimpleDateFormat("dd-MM-yyyy_HH-mm").format(date);

        spinFilter = findViewById(R.id.spin_filter);
        spinFilter.setOnItemSelectedListener(this);

        txPenjualan = findViewById(R.id.tx_totalpenjualan);
        txLaba = findViewById(R.id.tx_totallaba);
        txModal = findViewById(R.id.tx_totalmodal);

        recyclerView = (RecyclerView) findViewById(R.id.recPenjualan);
        tapAdapter = new AdminPenjualanAdapter(penjualantList, AdminDataPenjualan.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                AdminDataPenjualan.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(tapAdapter);

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

        adapter = new ArrayAdapter<String>(AdminDataPenjualan.this, android.R.layout.simple_spinner_item, arrayToko);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinFilter.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        swipeContainer.setRefreshing(true);
        getListToko();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filterwaktu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.sort_hari:
                filterHari();
                return true;
            case R.id.sort_bulan:
                filterBulan();
                return true;
            case R.id.sort_tahun:
                filterTahun();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void filterHari() {
        temp = new ArrayList<PenjualanModelData>();
        for (PenjualanModelData p : penjualantList) {
            if (p.getTanggal().substring(0, 2).equals(timeStamp.substring(0, 2))) {
                temp.add(p);
            }
        }
        tapAdapter.updateList(temp);
        calculateBalance(temp);
    }

    public void filterBulan() {
        temp = new ArrayList<PenjualanModelData>();
        for (PenjualanModelData p : penjualantList) {
            if (p.getTanggal().substring(3, 5).equals(timeStamp.substring(3, 5))) {
                temp.add(p);
            }
        }
        tapAdapter.updateList(temp);
        calculateBalance(temp);
    }

    public void filterTahun() {
        temp = new ArrayList<PenjualanModelData>();
        for (PenjualanModelData p : penjualantList) {
            if (p.getTanggal().substring(6, 10).equals(timeStamp.substring(6, 10))) {
                temp.add(p);
            }
        }
        tapAdapter.updateList(temp);
        calculateBalance(temp);
    }

    public void getListToko() {
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<GetMitraNKaryawan> call = api.showMitra(((AdminModelData)
                tinyDB.getObject("admin_login", AdminModelData.class)).getIdadmin());
        System.out.println("masuk");
        call.enqueue(new Callback<GetMitraNKaryawan>() {
            @Override
            public void onResponse(Call<GetMitraNKaryawan> call, Response<GetMitraNKaryawan> response) {
                if (response.body().getStatus_code().equals("1")) {
                    arrayToko.clear();
                    hashToko.clear();
                    arrayToko.add("Semua");
                    hashToko.put("0", "Semua");
                    for (TokoModelData kb : response.body().getResult_mitra()) {
                        hashToko.put(kb.getIdtoko(), kb.getNamatoko());
                        arrayToko.add(kb.getNamatoko());
                    }
                    adapter.notifyDataSetChanged();
                    getListPenjualan();
                } else {
                    swipeContainer.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<GetMitraNKaryawan> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(AdminDataPenjualan.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        filter(arrayToko.get(i));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void getListPenjualan() {
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<PenjualanGetResponse> call = api.showDataPenjualanAdmin(((AdminModelData)
                tinyDB.getObject("admin_login", AdminModelData.class)).getIdadmin());
        System.out.println("masuk");
        call.enqueue(new Callback<PenjualanGetResponse>() {
            @Override
            public void onResponse(Call<PenjualanGetResponse> call, Response<PenjualanGetResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code().equals("1")) {
                    penjualantList.clear();
                    penjualantList.addAll(response.body().getResult_penjualan());
                    temp = penjualantList;
                    Collections.sort(penjualantList, new SortbyIdPenjualanDESC());
                    tapAdapter.updateList(penjualantList);
                    calculateBalance(penjualantList);
                    swipeContainer.setRefreshing(false);
                } else {
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(AdminDataPenjualan.this, "Tidak ada transaksi", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<PenjualanGetResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(AdminDataPenjualan.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void calculateBalance(ArrayList<PenjualanModelData> calculateList) {
        modal = 0;
        jual = 0;
        laba = 0;
        for (PenjualanModelData rk : calculateList) {
            modal = modal + Integer.parseInt(rk.getJumhargadasar());
            jual = jual + Integer.parseInt(rk.getJumhargajual());
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

    public void filter(String text) {
        temp = new ArrayList<PenjualanModelData>();
        if (!text.equals("Semua")) {
            for (PenjualanModelData rk : penjualantList) {
                if (rk.getNamatoko().toLowerCase().contains(text.toLowerCase())) {
                    temp.add(rk);
                }
            }
        } else {
            temp = penjualantList;
        }
        tapAdapter.updateList(temp);
        calculateBalance(temp);
    }
}
