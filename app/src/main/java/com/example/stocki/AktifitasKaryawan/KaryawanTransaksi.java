package com.example.stocki.AktifitasKaryawan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stocki.Adapter.TokoTransaksiAdapter;
import com.example.stocki.AktifitasToko.TokoListTransaksi;
import com.example.stocki.ModelData.BarangModelData;
import com.example.stocki.ModelData.KaryawanModelData;
import com.example.stocki.ModelData.TokoModelData;
import com.example.stocki.ModelData.TransaksiProses;
import com.example.stocki.ModelResponse.BarangGetResponse;
import com.example.stocki.R;
import com.example.stocki.helper.SortbyIdBarangTransaksiProsesDESC;
import com.example.stocki.helper.TinyDB;
import com.example.stocki.service.APIService;
import com.example.stocki.service.RetrofitHelper;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KaryawanTransaksi extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    List<TransaksiProses> transaksiList = new ArrayList<TransaksiProses>();
    List<TransaksiProses> allTransaksiList = new ArrayList<TransaksiProses>();
    ArrayList<TransaksiProses> transaksiarrayList = new ArrayList<TransaksiProses>();
    ArrayList<BarangModelData> barangList = new ArrayList<BarangModelData>();
    RecyclerView recyclerView;
    public static Activity karyawanTransaksi;
    TokoTransaksiAdapter tokoTransaksiAdapter;
    ImageButton barcodeButton, gridButton, listButton;
    TinyDB tinyDB;
    ProgressDialog dialog;
    EditText edtSearch;
    Button btnLanjut;
    AlertDialog alertDialog;
    int jum, pos, tipe;
    Menu menu;
    int posPesanan = 0;
    int modeView = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);
        getSupportActionBar().setTitle("Transaksi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        karyawanTransaksi = this;

        tinyDB = new TinyDB(this);
        dialog = new ProgressDialog(this);

        dialog.setMessage("Harap tunggu..");
        btnLanjut = (Button) findViewById(R.id.btn_lanjuttrans);
        edtSearch = (EditText) findViewById(R.id.edt_searchtrans);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tokoTransaksiAdapter.getFilter().filter(s.toString());
                // filter your list from your input
                //filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });
        gridButton = (ImageButton)findViewById(R.id.btn_grid_barangTk);
        listButton = (ImageButton)findViewById(R.id.btn_list_barangTk);
        barcodeButton = (ImageButton) findViewById(R.id.btn_barcodeTrans);
        barcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(karyawanTransaksi);
                scanIntegrator.initiateScan();
            }
        });
        gridButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modeView = 1;
                tokoTransaksiAdapter = new TokoTransaksiAdapter(transaksiList, KaryawanTransaksi.this,0);
                recyclerView.setLayoutManager(new GridLayoutManager(KaryawanTransaksi.this, 3));
                recyclerView.setAdapter(tokoTransaksiAdapter);
                gridButton.setVisibility(View.GONE);
                listButton.setVisibility(View.VISIBLE);
            }
        });
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modeView = 0;
                tokoTransaksiAdapter = new TokoTransaksiAdapter(transaksiList, KaryawanTransaksi.this,1);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(KaryawanTransaksi.this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(tokoTransaksiAdapter);
                listButton.setVisibility(View.GONE);
                gridButton.setVisibility(View.VISIBLE);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerTransaksiToko);
        tokoTransaksiAdapter = new TokoTransaksiAdapter(transaksiList, this, tipe);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(KaryawanTransaksi.this,
                LinearLayoutManager.VERTICAL, false);
        if (tipe==0){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }else {
            recyclerView.setLayoutManager(layoutManager);
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(tokoTransaksiAdapter);

        jum = 0;

        btnLanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getJum() > 0) {
                    Intent intent = new Intent(KaryawanTransaksi.this, KaryawanListTransaksi.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("list", transaksiarrayList);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 123);
                } else {
                    Toast.makeText(KaryawanTransaksi.this,
                            "Tidak ada barang yang dipilih", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        getListBarang();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void addTransaksi(TransaksiProses transaksi) {
        jum = jum + 1;
        if (!transaksiarrayList.contains(transaksi)) {
            transaksiarrayList.add(transaksi);
        } else {
            int index = transaksiarrayList.indexOf(transaksi);
            int jumlah = Integer.parseInt(transaksiarrayList.get(index).getJumlah());
            int stok = Integer.parseInt(transaksiarrayList.get(index).getStok());
            transaksiarrayList.get(index).setJumlah(String.valueOf(jumlah));
            transaksiarrayList.get(index).setStok(String.valueOf(stok));
        }
        btnLanjut.setText("Lanjut > " + String.valueOf(jum));
    }

    public int getJum() {
        return jum;
    }

    public void setJum(int jum) {
        this.jum = jum;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            if (resultCode == 111) {
                dialog.show();
                transaksiarrayList.clear();
                transaksiList.clear();
                allTransaksiList.clear();
                for (BarangModelData b : barangList) {
                    if (Integer.parseInt(b.getStok()) > 0) {
                        Collections.sort(transaksiList, new SortbyIdBarangTransaksiProsesDESC());
                        transaksiList.add(new TransaksiProses(b.getId(), b.getIdadmin(),b.getNamabarang(),
                                b.getKode(),"0", b.getHargadasar(), b.getStok(), b.getHargajual(),
                                b.getHargajualtoko(), "1", b.getImage(),b.getidTipe()));
                        allTransaksiList.add(new TransaksiProses(b.getId(), b.getIdadmin(),b.getNamabarang(),
                                b.getKode(),"0", b.getHargadasar(), b.getStok(), b.getHargajual(),
                                b.getHargajualtoko(), "1", b.getImage(),b.getidTipe()));
                    }
                }
                jum = 0;
                btnLanjut.setText("Lanjut > " + String.valueOf(jum));
                tokoTransaksiAdapter.notifyDataSetChanged();
                //((MenuActivity)getActivity()).resetTransaksi();
                dialog.dismiss();
            }
        } else if (result != null) {
            if (result.getContents() != null) {
                showDialogBarcode(result.getContents());
                Toast.makeText(KaryawanTransaksi.this, result.getContents(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getListBarang() {
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<BarangGetResponse> call = api.
                showBarangToko(((KaryawanModelData) tinyDB.getObject("karyawan_login", KaryawanModelData.class)).getIdtoko());
        System.out.println("masuk");
        call.enqueue(new Callback<BarangGetResponse>() {
            @Override
            public void onResponse(Call<BarangGetResponse> call, Response<BarangGetResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code().equals("1")) {
                    dialog.dismiss();
                    transaksiList.clear();
                    barangList.clear();
                    barangList.addAll(response.body().getResult_barang());
                    for (BarangModelData b : response.body().getResult_barang()) {
                        if (Integer.parseInt(b.getStok()) > 0&&Integer.parseInt(b.getHargajualtoko()) >0) {
                            transaksiList.add(
                                    new TransaksiProses(b.getId(),b.getIdadmin(), b.getNamabarang(),
                                            b.getKode(),"0", b.getHargadasar(), b.getStok(), b.getHargajual(),
                                            b.getHargajualtoko(), "1", b.getImage(),b.getidTipe()));
                        }
                        allTransaksiList.add(
                                new TransaksiProses(b.getId(),b.getIdadmin(), b.getNamabarang(),
                                        b.getKode(),"0", b.getHargadasar(), b.getStok(), b.getHargajual(),
                                        b.getHargajualtoko(), "1", b.getImage(),b.getidTipe()));
                    }
                    //barangList.addAll(response.body().getResult_barang());
                    Collections.sort(transaksiList, new SortbyIdBarangTransaksiProsesDESC());
                    tokoTransaksiAdapter.notifyDataSetChanged();
                    //swipeContainer.setRefreshing(false);
//                    tes();
                } else {
                    dialog.dismiss();
                    Toast.makeText(KaryawanTransaksi.this, "Tidak ada barang", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<BarangGetResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    dialog.dismiss();
                    Toast.makeText(KaryawanTransaksi.this, "Harap periksa koneksi internet",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        invalidateOptionsMenu();
        dialog.show();
        btnLanjut.setText("Lanjut > 0");
        getListBarang();
        resetTransaksi();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.transaksi_refresh, menu);
        return true;
    }

    public void resetTransaksi() {
        transaksiarrayList.clear();
        transaksiList.clear();
        for (BarangModelData b : barangList) {
            if (Integer.parseInt(b.getStok()) > 0) {
                transaksiList.add(new
                        TransaksiProses(b.getId(),b.getIdadmin(), b.getNamabarang(),
                        b.getKode(),"0", b.getHargadasar(), b.getStok(), b.getHargajual(),
                        b.getHargajualtoko(), "1", b.getImage(),b.getidTipe()));
            }
        }
        jum = 0;
        btnLanjut.setText("Lanjut > " + String.valueOf(jum));
        Collections.sort(transaksiList, new SortbyIdBarangTransaksiProsesDESC());
        tokoTransaksiAdapter.notifyDataSetChanged();
        //((MenuActivity)getActivity()).resetTransaksi();
        dialog.dismiss();
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.menuRefresh:
                resetTransaksi();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showDialogBarcode(final String kode){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(KaryawanTransaksi.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_barcode, null);
        dialogBuilder.setView(dialogView);
        final TextView txProduk = (TextView) dialogView.findViewById(R.id.tx_produk);
        final EditText edtJumlah = (EditText) dialogView.findViewById(R.id.edt_jumlah);
        edtJumlah.setText("1");
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int i = 0;
                if(!edtJumlah.getText().toString().equals("")){
                    for (BarangModelData b : barangList){
                        i++;
                        if (b.getKode().equals(kode)){
                            transaksiarrayList.add(new TransaksiProses(b.getId(),b.getIdadmin(), b.getNamabarang(),
                                    b.getKode(),"0", b.getHargadasar(), b.getStok(), b.getHargajual(),
                                    b.getHargajualtoko(), "1", b.getImage(),b.getidTipe()));
                            txProduk.setText(b.getNamabarang());
                            setJum(getJum()+Integer.parseInt(edtJumlah.getText().toString()));
                            btnLanjut.setText("Lanjut > "+String.valueOf(getJum()));
                            break;
                        }
                    }
                    if (i==0){
                        Toast.makeText(KaryawanTransaksi.this, "Barang tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(KaryawanTransaksi.this, "Nama dan Harga harap diisi", Toast.LENGTH_SHORT).show();
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
}
