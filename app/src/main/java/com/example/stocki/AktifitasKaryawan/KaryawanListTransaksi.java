package com.example.stocki.AktifitasKaryawan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.stocki.Adapter.TokoListTransaksiAdapter;
import com.example.stocki.ModelData.BarangModelData;
import com.example.stocki.ModelData.KaryawanModelData;
import com.example.stocki.ModelData.Penjualan;
import com.example.stocki.ModelData.TokoModelData;
import com.example.stocki.ModelData.TransaksiProses;
import com.example.stocki.ModelResponse.InsertResponse;
import com.example.stocki.R;
import com.example.stocki.helper.NumberTextWatcherForThousand;
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

public class KaryawanListTransaksi extends AppCompatActivity {

    TinyDB tinyDB;
    RecyclerView recyclerView;
    TokoListTransaksiAdapter transaksiAdapter;
    TextView txTotal;
    ArrayList<TransaksiProses> arrayList = new ArrayList<TransaksiProses>();
    ArrayList<BarangModelData> barangList = new ArrayList<BarangModelData>();
    Button lanjutListtransaksi;
    public static Activity klTransaksiActivity;
    int total = 0; int jum = 0; int delete = 0; int modal = 0;
    int jual = 0; String bayar = "0"; String idtransaksi="0";
    ProgressDialog dialog;
    private String timeStamp, printContent = "";
//    TokoModelData toko;
    KaryawanModelData karyawan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_transaksi);

        tinyDB = new TinyDB(this);
        klTransaksiActivity = this;
//        toko = tinyDB.getObject("toko_login",TokoModelData.class);
        setTitle("Total Transaksi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Harap tunggu...");

        Bundle bundle = getIntent().getExtras();
        arrayList = bundle.getParcelableArrayList("list");
        Date date = new Date() ;
        timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date);
        txTotal = (TextView)findViewById(R.id.tx_total_transaksi);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerListTransaksi);
        transaksiAdapter = new TokoListTransaksiAdapter(arrayList,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(KaryawanListTransaksi.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(transaksiAdapter);

        System.out.println("list : "+arrayList);

        for (TransaksiProses t : arrayList){
            total = total + (Integer.parseInt(t.getJualtoko())*Integer.parseInt(t.getJumlah()));
        }

//         for (TransaksiProses t : arrayList){
//            System.out.println("jual : "+t.getJualtoko());
//            modal = modal + (Integer.parseInt(t.getJual())*Integer.parseInt(t.getJumlah()));
//            jual = jual + (Integer.parseInt(t.getJualtoko())*Integer.parseInt(t.getJumlah()));
//            jum = jum + Integer.parseInt(t.getJumlah());
//        }
//
//        for (TransaksiProses t : arrayList){
//            barangList.add(new BarangModelData(t.getId(),t.getIdadmin(),t.getModal(),t.getJumlah(),
//                    t.getJual(), t.getJualtoko(), t.getIdtipe()));
//        }
        txTotal.setText("Rp. "+doubleToStringNoDecimal(Double.parseDouble(String.valueOf(total))));

        lanjutListtransaksi = (Button) findViewById(R.id.btn_listtransaksi);
        lanjutListtransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(klTransaksiActivity, KaryawanInputBayarTransaksi.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("list", arrayList);
                intent.putExtras(bundle);
                startActivity(intent);
//                addTransaksi();
            }
        });
    }

    public void showEditTransaksi(final int posisi, Drawable image, String nama, String harga, String jumlah){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(KaryawanListTransaksi.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_transaksi, null);
        dialogBuilder.setView(dialogView);

        jum = Integer.parseInt(jumlah);
        total = 0;

        TextView txNama = (TextView)dialogView.findViewById(R.id.tx_namadb);
        TextView txKode = (TextView)dialogView.findViewById(R.id.tx_kode);
        TextView txJumItem = (TextView)dialogView.findViewById(R.id.tx_harga);
        TextView txHarga = (TextView)dialogView.findViewById(R.id.tx_jumlah_beli);
        TextView txMin = (TextView)dialogView.findViewById(R.id.btn_min);
        TextView txPlus = (TextView)dialogView.findViewById(R.id.btn_plus);
        final Button btnDelete = (Button)dialogView.findViewById(R.id.btn_delete);
        final TextView txJumlah = (TextView)dialogView.findViewById(R.id.tx_jumlah);
        final EditText edtHarga = (EditText) dialogView.findViewById(R.id.edt_harga);
        edtHarga.addTextChangedListener(new NumberTextWatcherForThousand(edtHarga));
        final EditText edtDiskon = (EditText) dialogView.findViewById(R.id.edt_diskon);
        final ImageView imgBarang = (ImageView) dialogView.findViewById(R.id.img_DialogEditTrans);

        RequestOptions options = new RequestOptions()
                .fitCenter()
                .error(R.drawable.ic_picture)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        Glide.with(getApplicationContext())
                .load(image)
                .apply(options)
                .into(imgBarang);

        txNama.setText(nama);
        txJumItem.setText(harga);
        txJumlah.setText(jumlah);
        txPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jum = jum + 1;
                txJumlah.setText(String.valueOf(jum));
            }
        });
        txMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (jum!=1){
                    jum = jum - 1;
                }
                txJumlah.setText(String.valueOf(jum));
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (delete==0){
                    delete = 1;
                    btnDelete.setText("Deleted");
                }else{
                    delete = 0;
                    btnDelete.setText("Delete");
                }
            }
        });
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                arrayList.get(posisi).setJumlah(txJumlah.getText().toString());
                if (!edtHarga.getText().toString().equals("")){
                    arrayList.get(posisi).setJualtoko(edtHarga.getText().toString());
                }
                if (delete==1){
                    arrayList.remove(posisi);
                }
                for (TransaksiProses t : arrayList){
                    total = total + (Integer.parseInt(t.getJualtoko())*Integer.parseInt(t.getJumlah()));
                }
                transaksiAdapter.notifyDataSetChanged();
                txTotal.setText("Rp. "+doubleToStringNoDecimal(Double.parseDouble(String.valueOf(total))));
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
                    Intent intent = new Intent(KaryawanListTransaksi.this, KaryawanBerhasilTransaksi.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("list", arrayList);
                    intent.putExtras(bundle);
                    intent.putExtra("id",idtransaksi);
                    intent.putExtra("jual",String.valueOf(jual).replaceAll("[\\u0020|A-Z|a-z|.]",""));
                    dialog.dismiss();
                    startActivity(intent);
                }else{
                    dialog.dismiss();
                    System.out.println("gagal : "+response.body().getStatus());
                    Toast.makeText(KaryawanListTransaksi.this,"Transaksi gagal",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<InsertResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    dialog.dismiss();
                    Toast.makeText(KaryawanListTransaksi.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        arrayList.clear();
        barangList.clear();
        transaksiAdapter.notifyDataSetChanged();
        onBackPressed();
        return true;
    }
    public static String doubleToStringNoDecimal(double d) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter .applyPattern("#,###");
        return formatter.format(d).replace(",",".");
    }
}
