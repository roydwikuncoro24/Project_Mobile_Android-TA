package com.example.stocki.AktifitasKaryawan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stocki.AktifitasToko.TokoBerhasilTransaksi;
import com.example.stocki.ModelData.BarangModelData;
import com.example.stocki.ModelData.KaryawanModelData;
import com.example.stocki.ModelData.Penjualan;
import com.example.stocki.ModelData.TokoModelData;
import com.example.stocki.ModelData.TransaksiProses;
import com.example.stocki.ModelResponse.InsertResponse;
import com.example.stocki.R;
import com.example.stocki.helper.TinyDB;
import com.example.stocki.service.APIService;
import com.example.stocki.service.RetrofitHelper;

import java.net.SocketTimeoutException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KaryawanInputBayarTransaksi extends AppCompatActivity {

    TextView txBayar, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9
            , btnC, btn0, btnDot, txNama, txTotal;
    String temp;
    ArrayList<TransaksiProses> arrayList = new ArrayList<TransaksiProses>();
    ArrayList<BarangModelData> barangList = new ArrayList<BarangModelData>();
    int modal = 0;
    int jual = 0;
    int jum = 0;
    TinyDB tinyDB;
    String idtransaksi="0";
    String bayar = "";
    public static Activity kinputBayarActivity;
    ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_bayar);

        kinputBayarActivity = this;

        tinyDB = new TinyDB(this);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Harap tunggu...");
        Bundle bundle = getIntent().getExtras();
        arrayList = bundle.getParcelableArrayList("list");
        for (TransaksiProses t : arrayList){
            modal = modal + (Integer.parseInt(t.getJual())*Integer.parseInt(t.getJumlah()));
            jual = jual + (Integer.parseInt(t.getJualtoko())*Integer.parseInt(t.getJumlah()));
            jum = jum + Integer.parseInt(t.getJumlah());
        }
        for (TransaksiProses t : arrayList){
            barangList.add(new BarangModelData(t.getId(),t.getIdadmin(),
                    t.getModal(),t.getJumlah(),t.getJual(), t.getJualtoko(), t.getIdtipe()));
        }

        setTitle("Pembayaran Transaksi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final DecimalFormat decim = new DecimalFormat("#,###.##");
        txBayar = (TextView) findViewById(R.id.tx_jumlah_bayar);
        txTotal = (TextView) findViewById(R.id.tx_TotalBelanja);
        txTotal.setText("Total Rp "+decim.format((jual)));

        btn0 = (TextView) findViewById(R.id.btn_0);
        btn1 = (TextView) findViewById(R.id.btn_1);
        btn2 = (TextView) findViewById(R.id.btn_2);
        btn3 = (TextView) findViewById(R.id.btn_3);
        btn4 = (TextView) findViewById(R.id.btn_4);
        btn5 = (TextView) findViewById(R.id.btn_5);
        btn6 = (TextView) findViewById(R.id.btn_6);
        btn7 = (TextView) findViewById(R.id.btn_7);
        btn8 = (TextView) findViewById(R.id.btn_8);
        btn9 = (TextView) findViewById(R.id.btn_9);
        btnC = (TextView) findViewById(R.id.btn_c);
//        btnDot = (TextView) findViewById(R.id.btn_dot);
        temp = "0";
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp = txBayar.getText().toString();
                if (temp.length()<11) {
                    temp = temp.replaceAll("[\\u0020|A-Z|a-z|.]", "");
                    //txBayar.setText(temp+"0");
                    txBayar.setText("Rp "+decim.format(Integer.valueOf(temp + "0")).replace(",", "."));
                }else{
                    txBayar.setText("Rp ");
                    temp = "0";
                }
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp = txBayar.getText().toString();
                if (temp.length()<11) {
                    temp = temp.replaceAll("[\\u0020|A-Z|a-z|.]", "");
                    //txBayar.setText(temp+"0");
                    txBayar.setText("Rp "+decim.format(Integer.valueOf(temp + "1")).replace(",", "."));
                }else{
                    txBayar.setText("Rp ");
                    temp = "0";
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp = txBayar.getText().toString();
                if (temp.length()<11) {
                    temp = temp.replaceAll("[\\u0020|A-Z|a-z|.]", "");
                    //txBayar.setText(temp+"0");
                    txBayar.setText("Rp "+decim.format(Integer.valueOf(temp + "2")).replace(",", "."));
                }else{
                    txBayar.setText("Rp ");
                    temp = "0";
                }
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp = txBayar.getText().toString();
                if (temp.length()<11) {
                    temp = temp.replaceAll("[\\u0020|A-Z|a-z|.]", "");
                    //txBayar.setText(temp+"0");
                    txBayar.setText("Rp "+decim.format(Integer.valueOf(temp + "3")).replace(",", "."));
                }else{
                    txBayar.setText("Rp ");
                    temp = "0";
                }
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp = txBayar.getText().toString();
                if (temp.length()<11) {
                    temp = temp.replaceAll("[\\u0020|A-Z|a-z|.]", "");
                    //txBayar.setText(temp+"0");
                    txBayar.setText("Rp "+decim.format(Integer.valueOf(temp + "4")).replace(",", "."));
                }else{
                    txBayar.setText("Rp ");
                    temp = "0";
                }
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp = txBayar.getText().toString();
                if (temp.length()<11) {
                    temp = temp.replaceAll("[\\u0020|A-Z|a-z|.]", "");
                    //txBayar.setText(temp+"0");
                    txBayar.setText("Rp "+decim.format(Integer.valueOf(temp + "5")).replace(",", "."));
                }else{
                    txBayar.setText("Rp ");
                    temp = "0";
                }
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp = txBayar.getText().toString();
                if (temp.length()<11) {
                    temp = temp.replaceAll("[\\u0020|A-Z|a-z|.]", "");
                    //txBayar.setText(temp+"0");
                    txBayar.setText("Rp "+decim.format(Integer.valueOf(temp + "6")).replace(",", "."));
                }else{
                    txBayar.setText("Rp ");
                    temp = "0";
                }
            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp = txBayar.getText().toString();
                if (temp.length()<11) {
                    temp = temp.replaceAll("[\\u0020|A-Z|a-z|.]", "");
                    //txBayar.setText(temp+"0");
                    txBayar.setText("Rp "+decim.format(Integer.valueOf(temp + "7")).replace(",", "."));
                }else{
                    txBayar.setText("Rp ");
                    temp = "0";
                }
            }
        });
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp = txBayar.getText().toString();
                if (temp.length()<11) {
                    temp = temp.replaceAll("[\\u0020|A-Z|a-z|.]", "");
                    //txBayar.setText(temp+"0");
                    txBayar.setText("Rp "+decim.format(Integer.valueOf(temp + "8")).replace(",", "."));
                }else{
                    txBayar.setText("Rp ");
                    temp = "0";
                }
            }
        });
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp = txBayar.getText().toString();
                if (temp.length()<11) {
                    temp = temp.replaceAll("[\\u0020|A-Z|a-z|.]", "");
                    //txBayar.setText(temp+"0");
                    txBayar.setText("Rp "+decim.format(Integer.valueOf(temp + "9")).replace(",", "."));
                }else{
                    txBayar.setText("Rp ");
                    temp = "0";
                }
            }
        });
//        btnDot.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                temp = txBayar.getText().toString();
//                txBayar.setText(""+temp+".");
//            }
//        });
        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txBayar.setText("Rp ");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_inputpembayaran, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.menuLanjut:
                bayar = txBayar.getText().toString().replaceAll("[\\u0020|A-Z|a-z|.]","");
                if (bayar.equals("")){
                    bayar = "0";
                }
                if ((jual<=Integer.parseInt(bayar))) {
                    addTransaksi();
                }else {
                    Toast.makeText(KaryawanInputBayarTransaksi.this,"Jumlah bayar kurang",Toast.LENGTH_SHORT).show();
                }
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

    public void addTransaksi(){
        dialog.show();
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Penjualan penjualan = new Penjualan(
                ((KaryawanModelData)tinyDB.getObject("karyawan_login", KaryawanModelData.class)).getIdtoko(),
                String.valueOf(modal),String.valueOf(jual),String.valueOf(jum),"1",
                ((KaryawanModelData)tinyDB.getObject("karyawan_login", KaryawanModelData.class)).getNamakaryawan(),
                ((TokoModelData)tinyDB.getObject("toko_login", TokoModelData.class)).getNamatoko(),
                barangList);
        Call<InsertResponse> call = api.addTransaksi(penjualan);
        System.out.println("masuk");
        call.enqueue(new Callback<InsertResponse>() {
            @Override
            public void onResponse(Call<InsertResponse> call, Response<InsertResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code().equals("1")) {
                    idtransaksi = String.valueOf(response.body().getIdtransaksi());
                    Intent intent = new Intent(kinputBayarActivity, KaryawanBerhasilTransaksi.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("list", arrayList);
                    intent.putExtras(bundle);
                    intent.putExtra("idtransaksi",idtransaksi);
                    intent.putExtra("jual",String.valueOf(jual).replaceAll("[\\u0020|A-Z|a-z|.]",""));
                    intent.putExtra("bayar",bayar);
                    dialog.dismiss();
                    startActivity(intent);
                }else{
                    dialog.dismiss();
                    System.out.println("gagal : "+response.body().getStatus());
                    Toast.makeText(KaryawanInputBayarTransaksi.this,"TokoTransaksi gagal",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<InsertResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    dialog.dismiss();
                    Toast.makeText(KaryawanInputBayarTransaksi.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    public void addTransaksi(){
//        APIService api = RetrofitHelper.getClient().create(APIService.class);
//
//        Pembelian pembelian = new Pembelian(((User)tinyDB.getObject("user_login", User.class)).getId_toko(),
//                String.valueOf(modal),String.valueOf(jual),String.valueOf(jum),"1",isCash, "" ,id_pelsup,edtJatuhTempo.getText().toString(),
//                ((User)tinyDB.getObject("user_login", User.class)).getNama(),barangList);
//        Call<InsertResponse> call = api.addTransaksi(pembelian);
//        System.out.println("masuk");
//        call.enqueue(new Callback<InsertResponse>() {
//            @Override
//            public void onResponse(Call<InsertResponse> call, Response<InsertResponse> response) {
//                System.out.println("re : " + response.body().getStatus_code());
//                if (response.body().getStatus_code().equals("1")) {
//                    Toast.makeText(InputBayarTransaksi.this,"TokoTransaksi berhasil",Toast.LENGTH_SHORT).show();
//                    idtransaksi = String.valueOf(response.body().getId_transaksi());
//                    //showStrukDialog();
//                    Intent intent = new Intent(InputBayarTransaksi.this, BerhasilBayarActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putParcelableArrayList("list", arrayList);
//                    intent.putExtras(bundle);
//                    intent.putExtra("id",idtransaksi);
//                    intent.putExtra("jual",String.valueOf(jual).replaceAll("[\\u0020|A-Z|a-z|.]",""));
//                    //intent.putExtra("bayar",txBayar.getText().toString().replaceAll("[\\u0020|A-Z|a-z|.]",""));
//                    intent.putExtra("bayar",bayar);
//                    dialog.dismiss();
//                    startActivity(intent);
//                }else{
//                    dialog.dismiss();
//                    System.out.println("gagal : "+response.body().getStatus());
//                    Toast.makeText(InputBayarTransaksi.this,"TokoTransaksi gagal",Toast.LENGTH_SHORT).show();
//                }
//            }
//            @Override
//            public void onFailure(Call<InsertResponse> call, Throwable t) {
//                if (t instanceof SocketTimeoutException) {
//                    dialog.dismiss();
//                    Toast.makeText(InputBayarTransaksi.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }


    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

}
