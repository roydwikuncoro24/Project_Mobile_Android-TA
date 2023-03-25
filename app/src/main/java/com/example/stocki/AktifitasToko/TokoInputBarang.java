package com.example.stocki.AktifitasToko;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.example.stocki.AktifitasAdmin.AdminBarang;
import com.example.stocki.AktifitasAdmin.AdminInputBarang;
import com.example.stocki.ModelData.TokoModelData;
import com.example.stocki.ModelResponse.InsertResponse;
import com.example.stocki.ModelResponse.UploadImageResponse;
import com.example.stocki.R;
import com.example.stocki.helper.NumberTextWatcherForThousand;
import com.example.stocki.helper.TinyDB;

import com.example.stocki.service.APIService;
import com.example.stocki.service.RetrofitHelper;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.net.SocketTimeoutException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TokoInputBarang extends AppCompatActivity {
    TinyDB tinyDB;
    TextView tVnamaTk;
    EditText edtKode, edtNama, edtStok, edtHD, edtHJ, edtKet;
    Button btnSimpan, btnDelete;
    String tipe, idbarang, idadmin,idtipe;
    ImageView imgPhoto;
    ImageButton imgBarcode;
    String imageUrl = "";
    ProgressDialog dialogBr;
    Spinner spinToko;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputbarang);
        setTitle("Select Barang");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dialogBr = new ProgressDialog(this);
        dialogBr.setMessage("Harap tunggu...");

        tinyDB = new TinyDB(this);

        spinToko = (Spinner)findViewById(R.id.spin_toko);
        spinToko.setVisibility(View.GONE);
        tVnamaTk = (TextView)findViewById(R.id.tvNamaToko);
        tVnamaTk.setVisibility(View.GONE);
        edtKode = (EditText)findViewById(R.id.edt_kode_dbarangs);
        edtNama = (EditText)findViewById(R.id.edt_nama_dbarangs);
        edtStok = (EditText)findViewById(R.id.edt_stock_dbarangs);
        edtHD = (EditText)findViewById(R.id.edt_hd_dbarangs);
        edtHD.addTextChangedListener(new NumberTextWatcherForThousand(edtHD));
        edtHJ = (EditText)findViewById(R.id.edt_hj_dbarangs);
        edtHJ.addTextChangedListener(new NumberTextWatcherForThousand(edtHJ));
        edtKet = (EditText)findViewById(R.id.edt_ket_dbarangs);
        btnSimpan = (Button)findViewById(R.id.btn_simpan_dbarangs);
//        btnDelete = (Button)findViewById(R.id.btn_delete_dbarangs);
        imgPhoto = findViewById(R.id.img_photo_barang);
        imgBarcode = findViewById(R.id.btn_barcodeInput);
        imgBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(TokoInputBarang.this);
                scanIntegrator.initiateScan();
            }
        });
        if (getIntent().hasExtra("tipe")){
            if ((getIntent().getStringExtra("tipe")).equals("1")){
                tipe = "1";
                idadmin = getIntent().getStringExtra("idadmin");
                edtNama.setText(getIntent().getStringExtra("namabarang"));
                edtStok.setText(getIntent().getStringExtra("stok"));
                edtKode.setText(getIntent().getStringExtra("kode"));
                edtHD.setText(getIntent().getStringExtra("hargajual"));
                edtHJ.setText(getIntent().getStringExtra("hargajualtoko"));;
                edtKet.setText(getIntent().getStringExtra("keterangan"));
                idbarang = getIntent().getStringExtra("idbarang");
                idtipe = getIntent().getStringExtra("idtipe");
                imageUrl = getIntent().getStringExtra("image");
                if (idadmin.equals("0")){
                    edtNama.setEnabled(true);
                    edtStok.setEnabled(true);
                    edtKode.setEnabled(false);
                    edtHD.setEnabled(true);
                    edtHJ.setEnabled(true);
                    edtKet.setEnabled(true);
                } else {
                    edtNama.setEnabled(false);
                    edtStok.setEnabled(false);
                    edtKode.setEnabled(false);
                    edtHD.setEnabled(false);
                    imgPhoto.setEnabled(false);
                    imgBarcode.setEnabled(false);
//                    btnDelete.setVisibility(View.GONE);
                    edtHJ.setEnabled(true);
                    edtKet.setEnabled(true);
                }
            }
        }else {
            tipe = "0";
//            btnDelete.setVisibility(View.GONE);
        }

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEmpty(edtNama)||isEmpty(edtStok)||isEmpty(edtKode)||
                        isEmpty(edtHD)||isEmpty(edtHJ)){
                    Toast.makeText(TokoInputBarang.this,"Harap isi semua kolom",Toast.LENGTH_SHORT).show();
                }else if (!(Integer.parseInt(edtStok.getText().toString())<0)){
                    Toast.makeText(TokoInputBarang.this,"Harap tunggu...",Toast.LENGTH_SHORT).show();
                    if (!tipe.equals("1")) {
                        addBarang();
                    }else{
                        updateBarang();
                    }
                }else {
                    Toast.makeText(TokoInputBarang.this,"Stok harus lebih dari 0",Toast.LENGTH_SHORT).show();
                }
            }
        });
//        btnDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                    if (idadmin.equals("0")) {
//                        dialogBr.show();
//                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                switch (which) {
//                                    case DialogInterface.BUTTON_POSITIVE:
//                                        deleteBarang();
//                                        break;
//                                    case DialogInterface.BUTTON_NEGATIVE:
//                                        dialogBr.hide();
//                                        break;
//                                }
//                            }
//                        };
//                        AlertDialog.Builder builder = new AlertDialog.Builder(TokoInputBarang.this);
//                        builder
//                                .setMessage("Apakah anda yakin ingin hapus ?").setPositiveButton("Ya", dialogClickListener)
//                                .setNegativeButton("Tidak", dialogClickListener)
//                                .show();
//                    } else {
//                        dialogBr.hide();
//                        Toast.makeText(TokoInputBarang.this,
//                                "Anda tidak dapat menghapus barang distributor",Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//        });

        imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImgPicker();
            }
        });

        RequestOptions options = new RequestOptions()
                .fitCenter()
                .error(R.drawable.ic_picture)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        Glide.with(TokoInputBarang.this)
                .load(imageUrl)
                .apply(options)
                .into(imgPhoto);
    }

    private void showImgPicker(){
        ImagePicker.create(TokoInputBarang.this)
                .returnMode(ReturnMode.ALL) // set whether pick and / or camera action should return immediate result or not.
                .toolbarArrowColor(Color.BLACK) // Toolbar 'up' arrow color
                .includeVideo(false) // Show video on image picker
                .single() // single mode
                .start(); // start image picker activity with request code
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            dialogBr.show();
            //get a single image only
            Image image = ImagePicker.getFirstImageOrNull(data);
            final Uri fileUri;
            File mediaFile = new File(image.getPath());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                fileUri = FileProvider.getUriForFile(TokoInputBarang.this,
                        TokoInputBarang.this.getApplicationContext().
                                getPackageName() + ".my.package.name.provider", mediaFile);
            }else{
                fileUri = Uri.fromFile(mediaFile);
            }
            RequestBody requestFile = RequestBody.create(MediaType.parse
                    ("multipart/form-data"), mediaFile);

            MultipartBody.Part multipartBody =MultipartBody.Part.createFormData
                    ("fileToUpload",image.getName(),requestFile);
            APIService api = RetrofitHelper.getClient().create(APIService.class);
            Call<UploadImageResponse> call = api.uploadImage(multipartBody);
            call.enqueue(new Callback<UploadImageResponse>() {
                @Override
                public void onResponse(Call<UploadImageResponse> call, Response
                        <UploadImageResponse> response) {
                    if (response.body().getStatus_code().equals("1")) {
                        imgPhoto.setImageURI(fileUri);
                        Toast.makeText(TokoInputBarang.this,
                                "Perubahan gambar sukses",Toast.LENGTH_SHORT).show();
                        System.out.println("Upload berhasil");
                        imageUrl = response.body().getUrl();
                        dialogBr.dismiss();
                    }else{
                        dialogBr.dismiss();
                        Toast.makeText(TokoInputBarang.this,"Error : "
                                +response.body().getStatus(),Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<UploadImageResponse> call, Throwable t) {
                    dialogBr.dismiss();
                }
            });
        } else {
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (scanningResult != null) {
                String scanContent = scanningResult.getContents();
                edtKode.setText(scanContent);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Tidak ada data!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    public void addBarang() {
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<InsertResponse> call = api.TokoAddBarang(((TokoModelData) tinyDB.getObject("toko_login", TokoModelData.class)).getIdtoko(),
                edtKode.getText().toString(), edtNama.getText().toString(), edtStok.getText().toString(),
                edtHD.getText().toString().replace(",",""),
                edtHJ.getText().toString().replace(",",""),
                edtKet.getText().toString(), imageUrl);
        System.out.println("masuk");
        call.enqueue(new Callback<InsertResponse>() {
            @Override
            public void onResponse(Call<InsertResponse> call, Response<InsertResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code().equals("1")) {
                    Toast.makeText(TokoInputBarang.this, "Tambah barang berhasil", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(TokoInputBarang.this, "Tambah barang gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InsertResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
//                    swipeContainer.setRefreshing(false);
                    Toast.makeText(TokoInputBarang.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateBarang() {
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<InsertResponse> call = api.TokoUpdateBarang(idbarang,
                edtNama.getText().toString(),
                edtKode.getText().toString(),
                edtHD.getText().toString().replace(",",""),
                edtStok.getText().toString(),
                edtKet.getText().toString(),
                edtHJ.getText().toString().replace(",",""),
                imageUrl,idtipe);
        System.out.println("masuk");
        call.enqueue(new Callback<InsertResponse>() {
            @Override
            public void onResponse(Call<InsertResponse> call, Response<InsertResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code().equals("1")) {
                    Intent intent = new Intent(TokoInputBarang.this, TokoBarang.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Toast.makeText(TokoInputBarang.this, "Update barang berhasil", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(TokoInputBarang.this, "Update barang gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InsertResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
//                    swipeContainer.setRefreshing(false);
                    Toast.makeText(TokoInputBarang.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void deleteBarang() {
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<InsertResponse> call = api.deleteBarang(idbarang);
        System.out.println("masuk");
        call.enqueue(new Callback<InsertResponse>() {
            @Override
            public void onResponse(Call<InsertResponse> call, Response<InsertResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code().equals("1")) {
                    Toast.makeText(TokoInputBarang.this, "Delete barang berhasil", Toast.LENGTH_SHORT).show();
                    dialogBr.dismiss();
//                    getListBarang();
                    finish();
                }else{
                    dialogBr.dismiss();
                    Toast.makeText(TokoInputBarang.this, "Delete barang gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InsertResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    dialogBr.dismiss();
                    Toast.makeText(TokoInputBarang.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
