package com.example.stocki.AktifitasAdmin;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.example.stocki.ModelData.AdminModelData;
import com.example.stocki.ModelData.BarangModelData;
import com.example.stocki.ModelData.TokoModelData;
import com.example.stocki.ModelResponse.GetMitraNKaryawan;
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
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminInputBarang extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    TinyDB tinyDB;
    Spinner spinToko;
    List<TokoModelData> TokoList = new ArrayList<TokoModelData>();
    EditText edtKode, edtNama, edtStok, edtHD, edtHJ, edtKet;
    Button btnSimpan, btnDelete;
    List<BarangModelData> barangList = new ArrayList<BarangModelData>();
    String tipe, idbarang, idtoko, namatoko;
    List<String> listNama = new ArrayList<String>();
    ArrayAdapter arrayAdapter;
    ImageView imgPhoto;
    ImageButton imgBarcode;
    String imageUrl = "";
    int aset = 0;
    TextView tVnamaTk;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputbarang);
        setTitle("Select Barang");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Harap tunggu...");

        tinyDB = new TinyDB(this);
        tVnamaTk = (TextView)findViewById(R.id.tvNamaToko);
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
        spinToko = (Spinner)findViewById(R.id.spin_toko);
        imgBarcode = findViewById(R.id.btn_barcodeInput);
        imgBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(AdminInputBarang.this);
                scanIntegrator.initiateScan();
            }
        });

        if (getIntent().hasExtra("tipe")){
            if ((getIntent().getStringExtra("tipe")).equals("1")){
                tipe = "1";
                edtNama.setText(getIntent().getStringExtra("namabarang"));
                edtStok.setText(getIntent().getStringExtra("stok"));
                edtKode.setText(getIntent().getStringExtra("kode"));
                edtHD.setText(getIntent().getStringExtra("hargadasar"));
                edtHJ.setText(getIntent().getStringExtra("hargajual"));;
                idtoko = getIntent().getStringExtra("idtoko");
                namatoko = getIntent().getStringExtra("namatoko");
                edtKet.setText(getIntent().getStringExtra("keterangan"));
                idbarang = getIntent().getStringExtra("idbarang");
                imageUrl = getIntent().getStringExtra("image");
                spinToko.setVisibility(View.GONE);
                tVnamaTk.setText(getIntent().getStringExtra("namatoko"));
                edtKode.setEnabled(false);
                imgBarcode.setEnabled(false);
            }
        }else {
            tipe = "0";
            tVnamaTk.setVisibility(View.GONE);
//            btnDelete.setVisibility(View.GONE);
        }

        spinToko.setOnItemSelectedListener(this);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listNama);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinToko.setAdapter(arrayAdapter);
        getToko();

        System.out.println("list : "+barangList);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEmpty(edtNama)||isEmpty(edtStok)||isEmpty(edtKode)||isEmpty(edtHD)||
                        isEmpty(edtHJ)||(spinToko.getSelectedItemPosition()==0)){
                    Toast.makeText(AdminInputBarang.this,"Harap isi semua kolom",Toast.LENGTH_SHORT).show();
                    }else if (!(Integer.parseInt(edtStok.getText().toString())<0)){
                    Toast.makeText(AdminInputBarang.this,"Harap tunggu...",Toast.LENGTH_SHORT).show();
                    if (!tipe.equals("1")) {
                        addBarang();
                    }else{
                        updateBarang();
                    }
                }else {
                    Toast.makeText(AdminInputBarang.this,"Stok harus lebih dari 0",Toast.LENGTH_SHORT).show();
                }
            }
        });

//        btnDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
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

        Glide.with(AdminInputBarang.this)
                .load(imageUrl)
                .apply(options)
                .into(imgPhoto);
    }

    private void showImgPicker(){
        ImagePicker.create(AdminInputBarang.this)
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
            // Get a list of picked images
            /*List<Image> images = ImagePicker.getImages(data);*/
            dialog.show();
            //get a single image only
            Image image = ImagePicker.getFirstImageOrNull(data);

            final Uri fileUri;
            File mediaFile = new File(image.getPath());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                fileUri = FileProvider.getUriForFile(AdminInputBarang.this,
                        AdminInputBarang.this.getApplicationContext().
                                getPackageName() + ".my.package.name.provider", mediaFile);
            }else{
                fileUri = Uri.fromFile(mediaFile);
            }

            RequestBody requestFile = RequestBody.create(MediaType.parse
                    ("multipart/form-data"), mediaFile);

            MultipartBody.Part multipartBody =MultipartBody.Part.createFormData
                    ("fileToUpload",image.getName(),requestFile);

            //Call<ResponseBody> responseBodyCall = service.addRecord(token, userId, "fileName", multipartBody);
            APIService api = RetrofitHelper.getClient().create(APIService.class);
            Call<UploadImageResponse> call = api.uploadImage(multipartBody);
            call.enqueue(new Callback<UploadImageResponse>() {
                @Override
                public void onResponse(Call<UploadImageResponse> call, Response
                        <UploadImageResponse> response) {
                    if (response.body().getStatus_code().equals("1")) {
                        imgPhoto.setImageURI(fileUri);
                        Toast.makeText(AdminInputBarang.this,
                                "Perubahan gambar sukses",Toast.LENGTH_SHORT).show();
                        System.out.println("Upload berhasil");
                        imageUrl = response.body().getUrl();
                        dialog.dismiss();
                    }else{
                        dialog.dismiss();
                        Toast.makeText(AdminInputBarang.this,"Error : "
                                +response.body().getStatus(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UploadImageResponse> call, Throwable t) {
                    dialog.dismiss();
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner spinner = (Spinner) adapterView;
        if((spinner.getId() == R.id.spin_toko)&&spinner.getSelectedItemPosition()>0) {
            int s = spinner.getSelectedItemPosition();
            idtoko = TokoList.get(s - 1).getIdtoko();
            Toast.makeText(AdminInputBarang.this,TokoList.get(s-1).getNamatoko(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void getToko() {
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<GetMitraNKaryawan> call = api.showMitra(((AdminModelData)tinyDB.getObject
                ("admin_login", AdminModelData.class)).getIdadmin());
        System.out.println("masuk");
        call.enqueue(new Callback<GetMitraNKaryawan>() {
            @Override
            public void onResponse(Call<GetMitraNKaryawan> call, Response<GetMitraNKaryawan> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code().equals("1")) {
                    System.out.println("masuk kategori");
                    listNama.clear();
                    TokoList.addAll(response.body().getResult_mitra());
                    listNama.add("Pilih Toko");
                    int i = 0;
                    int temp = 0;
                    for (TokoModelData kb : response.body().getResult_mitra()) {
                        if (kb.getIdtoko().equals(idtoko)) {
                            temp = i;
                        }
                        listNama.add(kb.getNamatoko());
                        i++;
                    }
                    arrayAdapter.notifyDataSetChanged();
                    if (tipe.equals("1")) {
                        spinToko.setSelection(temp + 1);
                    } else {
                        spinToko.setSelection(0);
                    }
                }
            }
            @Override
            public void onFailure(Call<GetMitraNKaryawan> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
//                    swipeContainer.setRefreshing(false);
                    Toast.makeText(AdminInputBarang.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    public void addBarang() {
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<InsertResponse> call = api.addBarang(
                ((AdminModelData) tinyDB.getObject("admin_login", AdminModelData.class)).getIdadmin(),
                idtoko,
                edtKode.getText().toString(),
                edtNama.getText().toString(),
                edtStok.getText().toString(),
                edtHD.getText().toString().replace(",",""),
                edtHJ.getText().toString().replace(",",""),
                edtKet.getText().toString(),
                imageUrl);
        System.out.println("masuk");
        call.enqueue(new Callback<InsertResponse>() {
            @Override
            public void onResponse(Call<InsertResponse> call, Response<InsertResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code().equals("1")) {
                    Toast.makeText(AdminInputBarang.this, response.body().getStatus(), Toast.LENGTH_SHORT).show();
                    finish();

                }else{
                    Toast.makeText(AdminInputBarang.this, response.body().getStatus(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<InsertResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
//                    swipeContainer.setRefreshing(false);
                    Toast.makeText(AdminInputBarang.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateBarang() {
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<InsertResponse> call = api.updateBarang(idbarang, idtoko,
                edtNama.getText().toString(),
                edtKode.getText().toString(),
                edtHD.getText().toString().replace(",",""),
                edtStok.getText().toString(),
                edtKet.getText().toString(),
                edtHJ.getText().toString().replace(",",""),
                imageUrl);
        System.out.println("masuk");
        call.enqueue(new Callback<InsertResponse>() {
            @Override
            public void onResponse(Call<InsertResponse> call, Response<InsertResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code().equals("1")) {
                    Intent intent = new Intent(AdminInputBarang.this, AdminBarang.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Toast.makeText(AdminInputBarang.this, "Update barang berhasil", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(AdminInputBarang.this, "Update barang gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InsertResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
//                    swipeContainer.setRefreshing(false);
                    Toast.makeText(AdminInputBarang.this, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
