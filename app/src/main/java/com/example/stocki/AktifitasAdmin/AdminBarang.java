package com.example.stocki.AktifitasAdmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stocki.Adapter.AdminBarangAdapter;
import com.example.stocki.ModelData.AdminModelData;
import com.example.stocki.ModelData.BarangModelData;
import com.example.stocki.ModelData.TokoModelData;
import com.example.stocki.ModelResponse.BarangGetResponse;
import com.example.stocki.ModelResponse.GetMitraNKaryawan;
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
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminBarang extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    RecyclerView recyclerView;
    AdminBarangAdapter bAdapter;
    ArrayList<BarangModelData> resultBarang = new ArrayList<BarangModelData>();
    ArrayList<BarangModelData> tempBar= new ArrayList<>();
    TinyDB tinyDB;
    SwipeRefreshLayout swipeContainer;
    Spinner spinFilter;
    FloatingActionButton fab;
    int pos,tipe;
    ArrayList<String> arrayToko = new ArrayList<>();
    HashMap<String, String> hashToko= new HashMap<String, String>();
    int aset = 0;
    TextView txAset;
    ArrayAdapter<String> adapter;
    ProgressDialog progressDialog;
    String toko, namatoko;
    ImageButton imgButton,gridButton, listButton;
    EditText edtSearch;
    int modeView = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_barang);
        getSupportActionBar().setTitle("Barang Admin");
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

        spinFilter = findViewById(R.id.spin_filterbarang);
        spinFilter.setOnItemSelectedListener(this);

        txAset = findViewById(R.id.tx_nilaiaset);

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
                filterNama(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });
//        imgButton = (ImageButton)findViewById(R.id.btn_search_barang);
//        imgButton.setOnClickListener(new View.OnClickListener() {
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
                bAdapter = new AdminBarangAdapter(resultBarang, AdminBarang.this,0);
                recyclerView.setLayoutManager(new GridLayoutManager(AdminBarang.this, 3));
                recyclerView.setAdapter(bAdapter);
                gridButton.setVisibility(View.GONE);
                listButton.setVisibility(View.VISIBLE);
            }
        });
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modeView = 0;
                bAdapter = new AdminBarangAdapter(resultBarang, AdminBarang.this,1);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AdminBarang.this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(bAdapter);
                listButton.setVisibility(View.GONE);
                gridButton.setVisibility(View.VISIBLE);
            }
        });
        recyclerView = (RecyclerView)findViewById(R.id.recyclerDBarang);
        bAdapter = new AdminBarangAdapter(resultBarang,this, tipe);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                AdminBarang.this, LinearLayoutManager.VERTICAL, false);
        if (tipe==0){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }else {
            recyclerView.setLayoutManager(layoutManager);
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(bAdapter);

        fab = (FloatingActionButton) findViewById(R.id.fab_dbarang);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminBarang.this, AdminInputBarang.class);
                startActivity(intent);
            }
        });

        System.out.println("list : "+ resultBarang);
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
        swipeContainer.setRefreshing(true);

        adapter = new ArrayAdapter<String>(AdminBarang.this, android.R.layout.simple_spinner_item, arrayToko);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinFilter.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        swipeContainer.setRefreshing(true);
        getListToko();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_barang, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle item selection
//        switch (item.getItemId()) {
//            case R.id.menuPengajuan:
//                return true;
//            case R.id.menuRektur:
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
//                    TokoList.addAll(response.body().getResult_mitra());
                    for (TokoModelData kb : response.body().getResult_mitra()) {
                        hashToko.put(kb.getIdtoko(), kb.getNamatoko());
                        arrayToko.add(kb.getNamatoko());
                    }
                    adapter.notifyDataSetChanged();
                    getListBarang();
                } else {
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(AdminBarang.this, "Belum ada barang", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetMitraNKaryawan> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(AdminBarang.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
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
    public void setPos(int pos) {
        this.pos = pos;
    }

    public void setFinish(){
        finish();
    }

    public void getListBarang(){
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<BarangGetResponse> call = api.showBarangAdmin(((AdminModelData)
                tinyDB.getObject("admin_login", AdminModelData.class)).getIdadmin());
        System.out.println("masuk");
        call.enqueue(new Callback<BarangGetResponse>() {
            @Override
            public void onResponse(Call<BarangGetResponse> call, Response<BarangGetResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code().equals("1")) {
                    resultBarang.clear();
                    resultBarang.addAll(response.body().getResult_barang());
                    tempBar = resultBarang;
                    calculateAset(resultBarang);
                    Collections.sort(resultBarang, new SortbyIdBarangDESC());
                    bAdapter.updateList(resultBarang);
                    swipeContainer.setRefreshing(false);
                }else{
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(AdminBarang.this,"Tidak ada barang",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<BarangGetResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(AdminBarang.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void calculateAset(ArrayList<BarangModelData> calculateBarang) {
        aset = 0;
        for (BarangModelData barang : calculateBarang) {
            aset = aset + Integer.parseInt(barang.getAset());
        }
        txAset.setTextColor(Color.BLACK);
        txAset.setText("Rp " + doubleToStringNoDecimal(String.valueOf(aset)));
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
                if (br.getNamatoko().toLowerCase().contains(text.toLowerCase())) {
                    tempBar.add(br);
                }
            }
        } else {
            tempBar = resultBarang;
        }
        bAdapter.updateList(tempBar);
        calculateAset(tempBar);
    }

    public void filterNama(String text) {
        tempBar = new ArrayList<BarangModelData>();
        for(BarangModelData d: resultBarang){
            if(d.getNamabarang().toLowerCase().contains(text.toLowerCase())){
                tempBar.add(d);
            } else if(d.getKode().toLowerCase().contains(text.toLowerCase())){
                tempBar.add(d);
            }
        }
        bAdapter.updateList(tempBar);
    }
}
