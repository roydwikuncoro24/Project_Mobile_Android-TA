package com.example.stocki.AktifitasKaryawan;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.stocki.Adapter.TokoBarangAdapter;
import com.example.stocki.AktifitasToko.TokoInputBarang;
import com.example.stocki.ModelData.BarangModelData;
import com.example.stocki.ModelData.KaryawanModelData;
import com.example.stocki.ModelData.TipeBarangModel;
import com.example.stocki.ModelData.TokoModelData;
import com.example.stocki.ModelResponse.BarangGetResponse;
import com.example.stocki.ModelResponse.GetTipeBarangResponse;
import com.example.stocki.R;
import com.example.stocki.helper.SortbyIdBarangDESC;
import com.example.stocki.helper.TinyDB;
import com.example.stocki.service.APIService;
import com.example.stocki.service.RetrofitHelper;

import java.net.SocketTimeoutException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KaryawanBarang extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    TokoBarangAdapter bAdapter;
    RecyclerView recyclerView;
    ArrayList<BarangModelData> resultBarang = new ArrayList<BarangModelData>();
    ArrayList<BarangModelData> tempBar= new ArrayList<>();
    int pos, tipe;
    TinyDB tinyDB;
    Spinner spinFilter;
    ArrayList<String> arraytipe = new ArrayList<>();
    HashMap<String, String> hashtipe= new HashMap<String, String>();
    ArrayAdapter<String> adapter;
    private SwipeRefreshLayout swipeContainer;
    private ImageButton imgButton, gridButton, listButton;
    private EditText edtSearch;
    int modeView = 0;
    FloatingActionButton fab;
    LinearLayout linAset, linNilai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_barang);
        getSupportActionBar().setTitle("Barang Toko");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tinyDB = new TinyDB(this);

        if (getIntent().hasExtra("tipe")){
            if (getIntent().getStringExtra("tipe").equals("1")){
                tipe = 1;
            }else {
                tipe = 0;
            }
        }

        linAset = (LinearLayout)findViewById(R.id.linierTotalAset);
        linNilai = (LinearLayout)findViewById(R.id.linierNilaiAset);
        linAset.setVisibility(View.GONE);
        linNilai.setVisibility(View.GONE);

        spinFilter = (Spinner) findViewById(R.id.spin_filterbarang);
        spinFilter.setOnItemSelectedListener(this);

        edtSearch = (EditText)findViewById(R.id.edt_searchdb);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // filter your list from your input
                filterCari(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });
//        imgButton = (ImageButton)findViewById(R.id.btnTk_search_barang);
//       imgButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                filterCari(edtSearch.getText().toString());
//            }
//        });

        gridButton = (ImageButton)findViewById(R.id.btn_grid_barang);
        listButton = (ImageButton)findViewById(R.id.btn_list_barang);

        gridButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modeView = 1;
                bAdapter = new TokoBarangAdapter(resultBarang, KaryawanBarang.this,0);
                recyclerView.setLayoutManager(new GridLayoutManager(KaryawanBarang.this, 3));
                recyclerView.setAdapter(bAdapter);
                gridButton.setVisibility(View.GONE);
                listButton.setVisibility(View.VISIBLE);
            }
        });
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modeView = 0;
                bAdapter = new TokoBarangAdapter(resultBarang, KaryawanBarang.this,1);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(KaryawanBarang.this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(bAdapter);
                listButton.setVisibility(View.GONE);
                gridButton.setVisibility(View.VISIBLE);
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.recyclerDBarang);
        bAdapter = new TokoBarangAdapter(resultBarang,this, tipe);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(KaryawanBarang.this,
                LinearLayoutManager.VERTICAL, false);
        if (tipe==0){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }else {
            recyclerView.setLayoutManager(layoutManager);
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(bAdapter);

        swipeContainer = (SwipeRefreshLayout)findViewById(R.id.swipeDBarang);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListBarang();
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
//        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setRefreshing(true);

        fab = (FloatingActionButton) findViewById(R.id.fab_dbarang);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KaryawanBarang.this, TokoInputBarang.class);
                startActivity(intent);
            }
        });

        adapter = new ArrayAdapter<String>(KaryawanBarang.this, android.R.layout.simple_spinner_item, arraytipe);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinFilter.setAdapter(adapter);

    }

//    @Override
//    public void onRefresh() {
//        getListBarang();
//    }

    @Override
    protected void onResume() {
        super.onResume();
        swipeContainer.setRefreshing(true);
        getTipebarang();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void getTipebarang() {
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<GetTipeBarangResponse> call = api.showTipebarang();
        System.out.println("masuk");
        call.enqueue(new Callback<GetTipeBarangResponse>() {
            @Override
            public void onResponse(Call<GetTipeBarangResponse> call, Response<GetTipeBarangResponse> response) {
                if (response.body().getStatus_code().equals("1")) {
                    arraytipe.clear();
                    hashtipe.clear();
                    arraytipe.add("Semua");
                    hashtipe.put("0", "Semua");
//                    TokoList.addAll(response.body().getResult_mitra());
                    for (TipeBarangModel kb : response.body().getResult_tipebarang()) {
                        hashtipe.put(kb.getIdtipe(), kb.getTipe());
                        arraytipe.add(kb.getTipe());
                    }
                    adapter.notifyDataSetChanged();
                    getListBarang();
                } else {
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(KaryawanBarang.this, "Belum ada barang", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetTipeBarangResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(KaryawanBarang.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public void setFinish(){
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        filter(arraytipe.get(i));
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void getListBarang(){
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<BarangGetResponse> call = api.showBarangToko(((KaryawanModelData)tinyDB.getObject("karyawan_login", KaryawanModelData.class)).getIdtoko());
        System.out.println("masuk");
        call.enqueue(new Callback<BarangGetResponse>() {
            @Override
            public void onResponse(Call<BarangGetResponse> call, Response<BarangGetResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code().equals("1")) {
                    resultBarang.clear();
                    resultBarang.addAll(response.body().getResult_barang());
                    tempBar = resultBarang;
                    Collections.sort(resultBarang, new SortbyIdBarangDESC());
                    bAdapter.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);
//                    tes();
                }else{
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(KaryawanBarang.this,"Tidak ada barang",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<BarangGetResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(KaryawanBarang.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void filterCari(String text){
        List<BarangModelData> temp = new ArrayList();
        for(BarangModelData d: resultBarang){
            if(d.getNamabarang().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }else if(d.getKode().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        //update recyclerview
        bAdapter.updateList(temp);
    }

    public static String doubleToStringNoDecimal(String value) {
        double d = Double.parseDouble(value);
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###");
        return formatter.format(d).replace(",", ".");
    }

    public void filter(String text) {
        tempBar = new ArrayList<BarangModelData>();
        if (!text.equals("Semua")) {
            for (BarangModelData br : resultBarang) {
                if (br.getTipe().toLowerCase().contains(text.toLowerCase())) {
                    tempBar.add(br);
                }
            }
        } else {
            tempBar = resultBarang;
        }
        bAdapter.updateList(tempBar);
    }
}
