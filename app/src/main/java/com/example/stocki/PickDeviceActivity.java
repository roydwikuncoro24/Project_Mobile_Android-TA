package com.example.stocki;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stocki.helper.Perangkat;
import com.example.stocki.helper.TinyDB;

public class PickDeviceActivity extends AppCompatActivity {

    private TinyDB tinyDB;
    private Perangkat bluetoothDevice = null;
    private TextView txNama;
    private EditText edtUcapan;
    private Button btnPilih, btnSimpanUcapan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_bluetooth);

        tinyDB = new TinyDB(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Pilih Bluetooth");

        txNama = (TextView)findViewById(R.id.tx_device_name);
        btnPilih = (Button)findViewById(R.id.btn_pilih_device);
        btnPilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(
                        PickDeviceActivity.this,PickDeviceList.class),1);
            }
        });

        try {
            bluetoothDevice = tinyDB.getObject("device",Perangkat.class);
            txNama.setText(bluetoothDevice.getNama());
        }catch (Exception e){
            Toast.makeText(PickDeviceActivity.this,"Silahkan pilih perangkat bluetooth",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            bluetoothDevice = tinyDB.getObject("device",Perangkat.class);
            txNama.setText(bluetoothDevice.getNama());
            Toast.makeText(PickDeviceActivity.this,"Berhasil pilih perangkat",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(PickDeviceActivity.this,"pilih perangkat gagal",Toast.LENGTH_SHORT).show();
        }
    }
}
