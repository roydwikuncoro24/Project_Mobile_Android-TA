package com.example.stocki.AktifitasAdmin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stocki.Adapter.AdminTanggunganAdapter;
import com.example.stocki.ModelData.AdminModelData;
import com.example.stocki.ModelData.BayarTanggunganModelData;
import com.example.stocki.ModelData.TanggunganModelData;
import com.example.stocki.ModelData.TokoModelData;
import com.example.stocki.ModelResponse.BayarTanggunganGetResponse;
import com.example.stocki.ModelResponse.GetMitraNKaryawan;
import com.example.stocki.ModelResponse.GetTransaksiResponse;
import com.example.stocki.ModelResponse.InsertResponse;
import com.example.stocki.ModelResponse.TanggunganGetResponse;
import com.example.stocki.R;
import com.example.stocki.helper.NumberTextWatcherForThousand;
import com.example.stocki.helper.SortbyIdBayarTanggunganDESC;
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

public class AdminTanggungan extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    RecyclerView recyclerView;
    AdminTanggunganAdapter ttAdapter;
    ArrayList<BayarTanggunganModelData> resultBayar = new ArrayList<BayarTanggunganModelData>();
    ArrayList<BayarTanggunganModelData> tempBay = new ArrayList<>();
    ArrayList<TanggunganModelData> resultTangungan = new ArrayList<TanggunganModelData>();
    ArrayList<TanggunganModelData> tempRec= new ArrayList<>();
    List<TokoModelData> TokoList = new ArrayList<>();
    TinyDB tinyDB;
    SwipeRefreshLayout swipeContainer;
    Spinner spinFilter;
    ArrayList<String> arrayToko = new ArrayList<>();
    HashMap<String, String> hashToko= new HashMap<String, String>();
    ArrayAdapter<String> adapter;
    int tanggungan = 0; int bayar = 0; int sisa = 0;
    TextView txTanggungan, txBayar, txSisa;
    AdminModelData admin;
    ProgressDialog progressDialog;
    String pembayaran, toko, namatoko;
    FloatingActionButton fabBayar, fabReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tanggungan);
        setTitle("Tanggungan & Pembayaran");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Harap tunggu...");

        tinyDB = new TinyDB(this);
        admin = ((AdminModelData) tinyDB.getObject("admin_login", AdminModelData.class));

        spinFilter = findViewById(R.id.spin_filtertanggungan);
        spinFilter.setOnItemSelectedListener(this);


        txTanggungan = findViewById(R.id.tx_totaltanggungan);
        txBayar = findViewById(R.id.tx_totalbayar);
        txSisa = findViewById(R.id.tx_totalsisa);
        adapter = new ArrayAdapter<String>(AdminTanggungan.this, android.R.layout.simple_spinner_item, arrayToko);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinFilter.setAdapter(adapter);

        recyclerView = (RecyclerView) findViewById(R.id.recTanggungan);
        ttAdapter = new AdminTanggunganAdapter(resultBayar, resultTangungan, AdminTanggungan.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                AdminTanggungan.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(ttAdapter);

        System.out.println("list : "+ resultBayar);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeTanggungan);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListBayarTangungan();
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeContainer.setRefreshing(true);

        fabBayar = (FloatingActionButton)findViewById(R.id.fab_bayar);
        fabBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinFilter.getSelectedItemPosition()<=0){
                    Toast.makeText(AdminTanggungan.this, "Toko harus dipilih!",Toast.LENGTH_SHORT).show();
                } else {
                    showDialogBayar();
                }
            }
        });

        fabReset = (FloatingActionButton)findViewById(R.id.fab_reset);
        fabReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinFilter.getSelectedItemPosition()<=0){
                    Toast.makeText(AdminTanggungan.this, "Toko harus dipilih!",Toast.LENGTH_SHORT).show();
                } else {
                    showDialogReset();
                }
            }
        });
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
                    TokoList.addAll(response.body().getResult_mitra());
                    for (TokoModelData kb : response.body().getResult_mitra()) {
                        kb.getIdtoko().equals(toko);
                        hashToko.put(kb.getIdtoko(), kb.getNamatoko());
                        arrayToko.add(kb.getNamatoko());
                    }
                    spinFilter.setSelection(0);
                    adapter.notifyDataSetChanged();
                    getListTanggungan();
                    getListBayarTangungan();
                } else {
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(AdminTanggungan.this, "Belum ada pembayaran", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetMitraNKaryawan> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(AdminTanggungan.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
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

    public void getListTanggungan() {
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<TanggunganGetResponse> call = api.showTanggunganAdmin((admin).getIdadmin());
        System.out.println("masuk");
        call.enqueue(new Callback<TanggunganGetResponse>() {
            @Override
            public void onResponse(Call<TanggunganGetResponse> call, Response<TanggunganGetResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code().equals("1")) {
                    resultTangungan.clear();
                    resultTangungan.addAll(response.body().getResult_tanggungan());
                    tempRec = resultTangungan;
                    calculateTanggungan(resultTangungan,resultBayar);
                    swipeContainer.setRefreshing(false);
                } else {
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(AdminTanggungan.this, "Belum ada Pembayaran", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TanggunganGetResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(AdminTanggungan.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getListBayarTangungan() {
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<BayarTanggunganGetResponse> call = api.showBayarTanggunganAdmin(((AdminModelData) tinyDB.getObject("admin_login", AdminModelData.class)).getIdadmin());
        System.out.println("masuk");
        call.enqueue(new Callback<BayarTanggunganGetResponse>() {
            @Override
            public void onResponse(Call<BayarTanggunganGetResponse> call, Response<BayarTanggunganGetResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code().equals("1")) {
                    resultBayar.clear();
                    resultBayar.addAll(response.body().getResult_bayartanggungan());
                    Collections.sort(resultBayar, new SortbyIdBayarTanggunganDESC());
                    tempBay = resultBayar;
                    ttAdapter.updateList(resultBayar,resultTangungan);
                    calculateTanggungan(resultTangungan,resultBayar);
                    swipeContainer.setRefreshing(false);
                } else {
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(AdminTanggungan.this, "Tidak ada record", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BayarTanggunganGetResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(AdminTanggungan.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void calculateTanggungan(ArrayList<TanggunganModelData> calculateList, ArrayList<BayarTanggunganModelData> calculateBayar) {
        tanggungan = 0;
        for (TanggunganModelData rk : calculateList) {
            tanggungan = tanggungan + Integer.parseInt(rk.getTanggungan());
        }
        bayar = 0;
        for (BayarTanggunganModelData bt : calculateBayar) {
            bayar = bayar + Integer.parseInt(bt.getPembayaran());
        }
        txBayar.setTextColor(Color.BLACK);
        txBayar.setText("Rp " + doubleToStringNoDecimal(String.valueOf(bayar)));
        txTanggungan.setTextColor(Color.BLACK);
        txTanggungan.setText("Rp " + doubleToStringNoDecimal(String.valueOf(tanggungan)));
        sisa = 0;
        sisa = tanggungan - bayar;
        txSisa.setTextColor(Color.RED);
        txSisa.setText("Rp " + doubleToStringNoDecimal(String.valueOf(sisa)));
    }

    public static String doubleToStringNoDecimal(String value) {
        double d = Double.parseDouble(value);
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###");
        return formatter.format(d).replace(",", ".");
    }

    public void filter(String text) {
        tempBay = new ArrayList<BayarTanggunganModelData>();
        tempRec = new ArrayList<TanggunganModelData>();
        if (!text.equals("Semua")) {
            for (TanggunganModelData rk : resultTangungan) {
                if (rk.getNamatoko().toLowerCase().contains(text.toLowerCase())) {
                    tempRec.add(rk);
                }
            }
            for (BayarTanggunganModelData bt : resultBayar) {
                if (bt.getNamatoko().toLowerCase().contains(text.toLowerCase())) {
                    tempBay.add(bt);
                }
            }
        } else {
            tempBay = resultBayar;
            tempRec = resultTangungan;
        }
        ttAdapter.updateList(tempBay,tempRec);
        calculateTanggungan(tempRec,tempBay);
    }

    public void showDialogBayar(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AdminTanggungan.this);
        if((spinFilter.getId() == R.id.spin_filtertanggungan)&&spinFilter.getSelectedItemPosition()>0) {
            int s = spinFilter.getSelectedItemPosition();
            toko = TokoList.get(s-1).getIdtoko();
        }
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_bayartanggungan, null);
        dialogBuilder.setView(dialogView);
        final TextView tvToko = (TextView) dialogView.findViewById(R.id.tx_namatoko);
        tvToko.setText(this.spinFilter.getSelectedItem().toString());
        final EditText edtNilai = (EditText) dialogView.findViewById(R.id.edt_pembayaran);
        edtNilai.addTextChangedListener(new NumberTextWatcherForThousand(edtNilai));
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.show();
                pembayaran = edtNilai.getText().toString().replace(",","");
                namatoko = tvToko.getText().toString();
                System.out.println("pembayaran : "+pembayaran+" sisa : "+sisa);
                if (Integer.parseInt(pembayaran)>Integer.parseInt(String.valueOf(sisa))){
                    Toast.makeText(AdminTanggungan.this, "Maaf pembayaran melebihi tanggungan",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }else{
                    nilaiBayar();
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    public void showDialogReset(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AdminTanggungan.this);
        if((spinFilter.getId() == R.id.spin_filtertanggungan)&&spinFilter.getSelectedItemPosition()>0) {
            int s = spinFilter.getSelectedItemPosition();
            toko = TokoList.get(s-1).getIdtoko();
        }
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.show();
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        resetData();
                        spinFilter.setAdapter(adapter);
                        progressDialog.dismiss();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        progressDialog.dismiss();
                        break;
                }
            }
        };
        dialogBuilder
                .setTitle("Perhatian")
                .setMessage("Apakah anda yakin akan mereset data dari toko tersebut?\n"+
                                "Data Penjualan\nData TokoTransaksi\nData Pembayaran\ndari toko tersebut akan terhapus."
                        )
                .setPositiveButton("Ya", dialogClickListener)
                .setNegativeButton("Tidak", dialogClickListener).show();
    }

    public void resetData(){
        progressDialog.show();
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<InsertResponse> call = api.resetData(toko);
        System.out.println("masuk");
        call.enqueue(new Callback<InsertResponse>() {
            @Override
            public void onResponse(Call<InsertResponse> call, Response<InsertResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code().equals("1")) {
                    progressDialog.dismiss();
                    Intent intent = new Intent(AdminTanggungan.this, Admin.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(AdminTanggungan.this, "Reset data toko berhasil", Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(AdminTanggungan.this, "Reset data toko gagal", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<InsertResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    progressDialog.dismiss();
                    Toast.makeText(AdminTanggungan.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void nilaiBayar(){
        progressDialog.show();
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<InsertResponse> call = api.addBayarTanggungan(
                toko, admin.getIdadmin(), pembayaran, namatoko);
        System.out.println("masuk");
        call.enqueue(new Callback<InsertResponse>() {
            @Override
            public void onResponse(Call<InsertResponse> call, Response<InsertResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code().equals("1")) {
                    progressDialog.dismiss();
                    getListToko();
                    spinFilter.setAdapter(adapter);
                    Toast.makeText(AdminTanggungan.this, "Bayar berhasil", Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(AdminTanggungan.this, "Bayar gagal", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<InsertResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    progressDialog.dismiss();
                    Toast.makeText(AdminTanggungan.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
