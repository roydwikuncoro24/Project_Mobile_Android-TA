package com.example.stocki.AktifitasToko;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stocki.Login;
import com.example.stocki.ModelData.TokoModelData;
import com.example.stocki.R;
import com.example.stocki.helper.TinyDB;

import dmax.dialog.SpotsDialog;

public class Toko extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout mDrawer;
    NavigationView navigationView;
    String versi;
    Fragment fragment = null;
    Class fragmentClass = null;
    TextView nama;
    SpotsDialog dialogtoko;
    TinyDB tinyDB;
    public static Activity tokoActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toko);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tokoActivity = this;
        getSupportActionBar().setTitle("Dashboard");
        dialogtoko = new SpotsDialog(Toko.this, "Loading...");
        tinyDB = new TinyDB(this);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        View header=navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        assert navigationView != null;

        nama = (TextView) header.findViewById(R.id.nama);
        try {
            nama.setText(((TokoModelData) tinyDB.getObject("toko_login", TokoModelData.class)).getNamatoko());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //mendapatkan versi
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            versi = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        fragmentClass = TokoBeranda.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit();
        navigationView.setCheckedItem(R.id.nav_home);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Toko.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            TokoBeranda frberanda = new TokoBeranda();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, frberanda);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_profiltoko) {
            Intent intent = new Intent(Toko.this, TokoProfil.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_barangtoko) {
            Intent intent = new Intent(Toko.this, TokoBarang.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_transaksitoko) {
            Intent intent = new Intent(Toko.this, TokoTransaksi.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_penjualantoko) {
            Intent intent = new Intent(Toko.this, TokoPenjualan.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_tanggungan) {
            Intent intent = new Intent(Toko.this, TokoTanggungan.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_karyawan) {
            Intent intent = new Intent(Toko.this, TokoManajemenKaryawan.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_tentang) {
            new AlertDialog.Builder(this)
                    .setTitle("STOCKI")
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage("versi "+versi+"\n\n" +
                            "Tentang Aplikasi.\n" +"")
//                            "Stoki merupakan aplikasi penjualan yang membantu distributor dan toko penjualan barang " +
//                            "dalam pengelolaan barang dagang dan hasil penjualan.\n\n")
                    .setCancelable(false)
                    .setNegativeButton("OK", null)
                    .show();
        } else if (id == R.id.nav_logout) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            startActivity(new Intent(Toko.this, Login.class));
                            finish();
                            tinyDB.putObject("toko_login",null);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(Toko.this);
            builder.setMessage("Apakah anda yakin logout ?")
                    .setPositiveButton("Ya", dialogClickListener)
                    .setNegativeButton("Tidak", dialogClickListener).show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Toko.this,
                            new String[]{Manifest.permission.CAMERA},
                            2);
                } else {
                    Toast.makeText(Toko.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case 2: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Toko.this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            3);
                } else {
                    Toast.makeText(Toko.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
//            case 3: {
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    startActivity(new Intent(Toko.this, Toko.class));
//                    finish();
//                } else {
//                    Toast.makeText(Admin.this, "Permission denied", Toast.LENGTH_SHORT).show();
//                }
//                return;
//            }
        }
    }
}
