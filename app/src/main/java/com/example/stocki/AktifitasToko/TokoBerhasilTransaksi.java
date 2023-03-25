package com.example.stocki.AktifitasToko;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stocki.ModelData.BarangModelData;
import com.example.stocki.ModelData.KaryawanModelData;
import com.example.stocki.ModelData.TokoModelData;
import com.example.stocki.ModelData.TransaksiProses;
import com.example.stocki.R;
import com.example.stocki.helper.BluetoothPrinter;
import com.example.stocki.helper.Perangkat;
import com.example.stocki.helper.TinyDB;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class TokoBerhasilTransaksi extends AppCompatActivity {

    TinyDB tinyDB;
    ArrayList<TransaksiProses> arrayList = new ArrayList<TransaksiProses>();
    ArrayList<BarangModelData> barangList = new ArrayList<BarangModelData>();
    String idtransaksi="0";
    String jual = "0";
    String bayar = "0";
    int kembali = 0;
    TextView txKembalian;
    Button btnTransLagi, btnPrint, btnShare;
    TokoModelData toko;
    KaryawanModelData karyawan;
    private ProgressDialog dialog;
    byte FONT_TYPE;
    private static BluetoothSocket btsocket;
    private static OutputStream outputStream;
    private String timeStamp, printContent = "";
    private final UUID SPP_UUID = UUID
            .fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berhasiltransaksi);

        tinyDB = new TinyDB(this);
        toko = tinyDB.getObject("toko_login",TokoModelData.class);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Harap tunggu...");

        Bundle bundle = getIntent().getExtras();
        arrayList = bundle.getParcelableArrayList("list");
        idtransaksi = getIntent().getStringExtra("idtransaksi");
        jual = getIntent().getStringExtra("jual");
        bayar = getIntent().getStringExtra("bayar");
        kembali = Integer.parseInt(bayar.replace(".",""))-Integer.parseInt(jual.replace(".",""));
        Date date = new Date() ;
        //timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date);
        timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date);

        btnTransLagi = (Button)findViewById(R.id.btn_transaksi_ulang);
        btnPrint = (Button)findViewById(R.id.btn_cetak);
        btnShare = (Button)findViewById(R.id.btn_share);

        btnTransLagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent returnIntent = new Intent();
                TokoListTransaksi.tlTransaksiActivity.setResult(111);
                TokoListTransaksi.tlTransaksiActivity.finish();
                TokoInputBayarTransaksi.tinputBayarActivity.finish();
                finish();
            }
        });
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printStruk2();
            }
        });
        setPrintContent();
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, printContent);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
    }

    public void setPrintContent(){
        printContent = printContent+timeStamp;
        printContent = printContent+"\n"+toko.getNamatoko();
        printContent = printContent+"\n"+toko.getAlamat();
        printContent = printContent+"\n"+toko.getNotelp();
        printContent = printContent+"\n"+"--------------------------------";
        printContent = printContent+"\n"+"No. Transaksi : "+idtransaksi;
        printContent = printContent+"\n"+toko.getNamapemilik();
        printContent = printContent+"\n"+"--------------------------------";
        for (TransaksiProses transaksi : arrayList){
            printContent = printContent+"\n"+transaksi.getNamabarang();
            String jumhar = transaksi.getJumlah() + " x " +
                    doubleToStringNoDecimal(String.valueOf(Double.parseDouble(transaksi.getJualtoko()))) + " = ";
            int jumlah = Integer.parseInt(transaksi.getJumlah())*Integer.parseInt(transaksi.getJualtoko());
            String jumlahString = doubleToStringNoDecimal(String.valueOf(Double.parseDouble(String.valueOf(jumlah))));
            printContent = printContent+"\n"+jumhar+jumlahString;
        }
        printContent = printContent+"\n"+"--------------------------------";
        printContent = printContent+"\n"+"Total   : "+doubleToStringNoDecimal(String.valueOf(Double.parseDouble(jual)));
        printContent = printContent+"\n"+"Bayar   : "+doubleToStringNoDecimal(bayar);
        printContent = printContent+"\n"+"Kembali : "+doubleToStringNoDecimal(String.valueOf(kembali));
        printContent = printContent+"\n"+"--------------------------------";
        try {
            printContent = printContent+"\n"+tinyDB.getString("ucapan");
        }catch (Exception e){
            printContent = printContent+"\n"+"Terima kasih telah berbelanja di Toko "+toko.getNamatoko();
        }
    }

    public void printStruk2(){
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        //BluetoothDevice mBtDevice = btAdapter.getBondedDevices().iterator().next();   // Get first paired device
        //BluetoothDevice mBtDevice = tinyDB.getObject("device",BluetoothDevice.class);
        try {
            BluetoothDevice mBtDevice =
                    btAdapter.getRemoteDevice(tinyDB.getObject("device", Perangkat.class).getAddress());
            final BluetoothPrinter mPrinter = new BluetoothPrinter(mBtDevice);
            mPrinter.connectPrinter(new BluetoothPrinter.PrinterConnectListener() {
                @Override
                public void onConnected() {
                    mPrinter.setAlign(BluetoothPrinter.ALIGN_CENTER);
                    Date date = new Date() ;
                    String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date);
                    mPrinter.printCustom(toko.getNamatoko(),3,1);
                    mPrinter.printCustom("Petugas:"+toko.getNamapemilik(),1,1);
                    printContent = printContent+"\n"+toko.getNamatoko();
                    mPrinter.printCustom(timeStamp.substring(0,10) + " "+timeStamp.substring(11,19),1,1);
                    mPrinter.printCustom("No. Transaksi : "+idtransaksi,1,1);
                    mPrinter.printCustom("--------------------------------",1,0);
                    for (TransaksiProses transaksi : arrayList){
                        mPrinter.printCustom(transaksi.getNamabarang(),1,1);
                        String jumhar = transaksi.getJumlah() + " x " +
                                doubleToStringNoDecimal(String.valueOf(Double.parseDouble(transaksi.getJualtoko()))) + " = ";
                        int jumlah = Integer.parseInt(transaksi.getJumlah())*Integer.parseInt(transaksi.getJualtoko());
                        String jumlahString = doubleToStringNoDecimal(String.valueOf(Double.parseDouble(String.valueOf(jumlah))));
                        mPrinter.printCustom(jumhar+jumlahString,1,1);
                    }
                    mPrinter.printCustom("--------------------------------",1,0);
                    mPrinter.printCustom("Total   : "+doubleToStringNoDecimal(String.valueOf(Double.parseDouble(jual))),1,1);
                    mPrinter.printCustom("Bayar   : "+doubleToStringNoDecimal(bayar),1,1);
                    mPrinter.printCustom("Kembali : "+doubleToStringNoDecimal(String.valueOf(kembali)),1,1);
                    mPrinter.printCustom("--------------------------------",1,0);
                    mPrinter.printCustom("TERIMAKASIH TELAH BERBELANJA",1,1);

                    mPrinter.printCustom(" ",1,1);
                    mPrinter.printNewLine();
                    mPrinter.finish();
                    dialog.dismiss();
                }
                @Override
                public void onFailed() {
                    Log.d("BluetoothPrinter", "Conection failed");
                    dialog.dismiss();
                }
            });
        }catch (Exception e){
            Toast.makeText(TokoBerhasilTransaksi.this,"Belum setting perangkat bluetooth",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static String doubleToStringNoDecimal(String number) {
        Double d = Double.parseDouble(number);
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter .applyPattern("#,###");
        return formatter.format(d).replace(",",".");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if(btsocket!= null){
                outputStream.close();
                btsocket.close();
                btsocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
