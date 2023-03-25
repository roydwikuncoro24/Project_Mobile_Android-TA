package com.example.stocki.AktifitasToko;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stocki.Adapter.TokoTanggunganAdapter;
import com.example.stocki.ModelData.BayarTanggunganModelData;
import com.example.stocki.ModelData.TanggunganModelData;
import com.example.stocki.ModelData.TokoModelData;
import com.example.stocki.ModelData.TransaksiModelData;
import com.example.stocki.ModelResponse.BayarTanggunganGetResponse;
import com.example.stocki.ModelResponse.TanggunganGetResponse;
import com.example.stocki.R;
import com.example.stocki.helper.SortbyIdBarangDESC;
import com.example.stocki.helper.SortbyIdBayarTanggunganDESC;
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

public class TokoTanggungan extends AppCompatActivity {

    RecyclerView recyclerView;
    TokoTanggunganAdapter ttAdapter;
    ArrayList<TanggunganModelData> resultTanggungan = new ArrayList<TanggunganModelData>();
    ArrayList<TanggunganModelData> temp= new ArrayList<>();
    ArrayList<BayarTanggunganModelData> resultBayarTanggungan = new ArrayList<BayarTanggunganModelData>();
    ArrayList<BayarTanggunganModelData> tempBayar= new ArrayList<>();
    TinyDB tinyDB;
    SwipeRefreshLayout swipeContainer;
    int tanggungan = 0; int bayar = 0; int sisa = 0;
    String timeStamp = "";
    TextView txTanggungan, txBayar, txSisa;
    TokoModelData toko;
    FloatingActionButton fabBay, fabReset;
    LinearLayout lin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tanggungan);
        setTitle("Tanggungan & Pembayaran");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tinyDB = new TinyDB(this);
        Date date = new Date();
        timeStamp = new SimpleDateFormat("dd-MM-yyyy_HH-mm").format(date);
        toko = ((TokoModelData) tinyDB.getObject("toko_login", TokoModelData.class));

        lin = findViewById(R.id.linierFilterTanggungan);
        lin.setVisibility(View.GONE);

        txTanggungan = findViewById(R.id.tx_totaltanggungan);
        txBayar = findViewById(R.id.tx_totalbayar);
        txSisa = findViewById(R.id.tx_totalsisa);

        recyclerView = (RecyclerView) findViewById(R.id.recTanggungan);
        ttAdapter = new TokoTanggunganAdapter(resultBayarTanggungan, TokoTanggungan.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                TokoTanggungan.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(ttAdapter);

        System.out.println("list : "+ resultTanggungan);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeTanggungan);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListBayarTanggungan();
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeContainer.setRefreshing(true);

        fabBay = (FloatingActionButton)findViewById(R.id.fab_bayar);
        fabBay.setVisibility(View.GONE);
        fabReset = (FloatingActionButton)findViewById(R.id.fab_reset);
        fabReset.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        swipeContainer.setRefreshing(true);
        getListTanggungan();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void getListTanggungan() {
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<TanggunganGetResponse> call = api.showTanggunganToko(((TokoModelData)
                tinyDB.getObject("toko_login", TokoModelData.class)).getIdtoko());
        System.out.println("masuk");
        call.enqueue(new Callback<TanggunganGetResponse>() {
            @Override
            public void onResponse(Call<TanggunganGetResponse> call, Response<TanggunganGetResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code().equals("1")) {
                    resultTanggungan.clear();
                    resultTanggungan.addAll(response.body().getResult_tanggungan());
                    temp = resultTanggungan;
                    getListBayarTanggungan();
                    calculateTanggungan(resultTanggungan,resultBayarTanggungan);
                    swipeContainer.setRefreshing(false);
                } else {
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(TokoTanggungan.this, "Tidak ada tanggungan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TanggunganGetResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(TokoTanggungan.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getListBayarTanggungan() {
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<BayarTanggunganGetResponse> call = api.showBayarTanggunganToko(((TokoModelData)
                tinyDB.getObject("toko_login", TokoModelData.class)).getIdtoko());
        System.out.println("masuk");
        call.enqueue(new Callback<BayarTanggunganGetResponse>() {
            @Override
            public void onResponse(Call<BayarTanggunganGetResponse> call, Response<BayarTanggunganGetResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code().equals("1")) {
                    resultBayarTanggungan.clear();
                    resultBayarTanggungan.addAll(response.body().getResult_bayartanggungan());
                    Collections.sort(resultBayarTanggungan, new SortbyIdBayarTanggunganDESC());
                    tempBayar = resultBayarTanggungan;
                    ttAdapter.updateList(resultBayarTanggungan);
                    calculateTanggungan(resultTanggungan, resultBayarTanggungan);
                    swipeContainer.setRefreshing(false);
                } else {
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(TokoTanggungan.this, "Tidak ada pembayaran tanggungan", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<BayarTanggunganGetResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(TokoTanggungan.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void calculateTanggungan(ArrayList<TanggunganModelData> calculateList, ArrayList<BayarTanggunganModelData> calculateBayar) {
        tanggungan = 0;
        bayar = 0;
        sisa = 0;
        for (TanggunganModelData rk : calculateList) {
            tanggungan = tanggungan + Integer.parseInt(rk.getTanggungan());
        }
        for (BayarTanggunganModelData bt : calculateBayar) {
            bayar = bayar + Integer.parseInt(bt.getPembayaran());
        }
        sisa = tanggungan - bayar;
        txTanggungan.setTextColor(Color.BLACK);
        txTanggungan.setText("Rp " + doubleToStringNoDecimal(String.valueOf(tanggungan)));
        txBayar.setTextColor(Color.BLACK);
        txBayar.setText("Rp " + doubleToStringNoDecimal(String.valueOf(bayar)));
        txSisa.setTextColor(Color.RED);
        txSisa.setText("Rp " + doubleToStringNoDecimal(String.valueOf(sisa)));
    }

    public static String doubleToStringNoDecimal(String value) {
        double d = Double.parseDouble(value);
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###");
        return formatter.format(d).replace(",", ".");
    }
}
