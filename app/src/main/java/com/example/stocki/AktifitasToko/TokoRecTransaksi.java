package com.example.stocki.AktifitasToko;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stocki.Adapter.TokoRecTransaksiAdapter;
import com.example.stocki.ModelData.TokoModelData;
import com.example.stocki.ModelData.TransaksiModelData;
import com.example.stocki.ModelResponse.GetTransaksiResponse;
import com.example.stocki.R;
import com.example.stocki.helper.SortbyIdTransaksiDESC;
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
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TokoRecTransaksi extends AppCompatActivity {

    RecyclerView recyclerView;
    TokoRecTransaksiAdapter tapAdapter;
    ArrayList<TransaksiModelData> resultList = new ArrayList<TransaksiModelData>();
    TinyDB tinyDB;
    SwipeRefreshLayout swipeContainer;
    int masuk = 0;
    TextView txPenjualan;
    ArrayList<TransaksiModelData> temp;
    String timeCurrent;
    String timeStamp;
    TokoModelData toko;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordtransaksi);
        setTitle("Data Transaksi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tinyDB = new TinyDB(this);
        toko = ((TokoModelData) tinyDB.getObject("toko_login", TokoModelData.class));

        Date date = new Date();
        timeCurrent = new SimpleDateFormat("dd").format(date);
        timeStamp = new SimpleDateFormat("dd-MM-yyyy_HH-mm").format(date);

        txPenjualan = findViewById(R.id.tx_totalpenjualan);

        recyclerView = (RecyclerView) findViewById(R.id.recTransaksiToko);
        tapAdapter = new TokoRecTransaksiAdapter(resultList, TokoRecTransaksi.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                TokoRecTransaksi.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(tapAdapter);

        System.out.println("list : "+ resultList);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeRecTrans);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListTransaksi();
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
        temp = new ArrayList<TransaksiModelData>();
        for (TransaksiModelData p : resultList){
            if (p.getTanggal().substring(8,10).equals(timeStamp.substring(0,2))){
                temp.add(p);
            }
        }
        tapAdapter.updateList(temp);
    }

    public void filterBulan() {
        temp = new ArrayList<TransaksiModelData>();
        for (TransaksiModelData p : resultList){
            if (p.getTanggal().substring(5,7).equals(timeStamp.substring(3,5))){
                temp.add(p);
            }
        }
        tapAdapter.updateList(temp);
    }
    public void filterTahun() {
        temp = new ArrayList<TransaksiModelData>();
        for (TransaksiModelData p : resultList){
            if (p.getTanggal().substring(0,4).equals(timeStamp.substring(6,10))){
                temp.add(p);
            }
        }
        tapAdapter.updateList(temp);
     }

    public void getListTransaksi() {
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<GetTransaksiResponse> call = api.showDataTransaksiToko(((TokoModelData)
                tinyDB.getObject("toko_login", TokoModelData.class)).getIdtoko());
        System.out.println("masuk");
        call.enqueue(new Callback<GetTransaksiResponse>() {
            @Override
            public void onResponse(Call<GetTransaksiResponse> call, Response<GetTransaksiResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code().equals("1")) {
                    resultList.clear();
                    resultList.addAll(response.body().getResult_transaksi());
                    temp = resultList;
                    Collections.sort(resultList, new SortbyIdTransaksiDESC());
                    tapAdapter.updateList(resultList);
                    swipeContainer.setRefreshing(false);
                } else {
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(TokoRecTransaksi.this, "Tidak ada record transaksi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetTransaksiResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(TokoRecTransaksi.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
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
