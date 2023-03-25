package com.example.stocki.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stocki.AktifitasToko.Toko;
import com.example.stocki.AktifitasToko.TokoManajemenKaryawan;
import com.example.stocki.ModelData.KaryawanModelData;
import com.example.stocki.ModelResponse.InsertResponse;
import com.example.stocki.R;
import com.example.stocki.service.APIService;
import com.example.stocki.service.RetrofitHelper;

import java.net.SocketTimeoutException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TokoKaryawanAdapter extends RecyclerView.Adapter<TokoKaryawanAdapter.HolderDataKaryawan> {

    List<KaryawanModelData> mKaryawan;
    Context context;
    ProgressDialog progressDialog;

    public TokoKaryawanAdapter(List<KaryawanModelData> mKaryawan, Context context) {
        this.mKaryawan = mKaryawan;
        this.context = context;
    }

    @Override
    public TokoKaryawanAdapter.HolderDataKaryawan onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mitrakaryawan,
                parent, false);
        TokoKaryawanAdapter.HolderDataKaryawan holderData = new TokoKaryawanAdapter.
                HolderDataKaryawan(v);
        return holderData;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderDataKaryawan holder, int position) {
        KaryawanModelData mdKar = mKaryawan.get(position);
        holder.tvNick.setText(mdKar.getNamakaryawan().substring(0,1));
        holder.tvNama.setText(mdKar.getNamakaryawan().toString());
        holder.tvNotelp.setText(mdKar.getNotelp().toString());
        holder.tvAlamat.setText(mdKar.getAlamat().toString());
        holder.idkaryawan = mdKar.getIdkaryawan();
    }

    @Override
    public int getItemCount() {
        return mKaryawan.size();
    }

    public class HolderDataKaryawan extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvNama, tvAlamat, tvNotelp, tvNick;
        String idkaryawan, notelp, alamat;
        CardView cardView;

        public HolderDataKaryawan(View v) {
            super(v);
            v.setOnClickListener(this);
            tvNama = (TextView) v.findViewById(R.id.tx_namaps);
            tvAlamat = (TextView) v.findViewById(R.id.tx_alamatps);
            tvNotelp = (TextView) v.findViewById(R.id.tx_notelpps);
            tvNick = (TextView) v.findViewById(R.id.tx_nickps);
            cardView = (CardView) v.findViewById(R.id.cardMitraKaryawan);
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            View dialogView = inflater.inflate(R.layout.dialog_mitrakaryawan, null);
            dialogBuilder.setView(dialogView);

            TextView txTitle = (TextView) dialogView.findViewById(R.id.tx_dialog_title);
            final TextView txNama = (TextView) dialogView.findViewById(R.id.tx_nama_pu);
            final TextView txNamaPem = (TextView) dialogView.findViewById(R.id.tx_nama_pemilik);
            final TextView txNoTelp = (TextView) dialogView.findViewById(R.id.tx_notelp_pu);
            final TextView txAlamat = (TextView) dialogView.findViewById(R.id.tx_alamat_pu);
            this.idkaryawan.toString();
            txTitle.setText("Profil Karyawan");
            txNamaPem.setVisibility(View.GONE);
            txNama.setText("Nama : "+this.tvNama.getText().toString());
            txAlamat.setText("Alamat : "+this.tvAlamat.getText().toString());
            txNoTelp.setText("No. Telp : "+this.tvNotelp.getText().toString());

            dialogBuilder.setNegativeButton("Hapus", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showDialogHapus(idkaryawan);
                }
            });
            dialogBuilder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
        }
    }

    public void clear() {
        mKaryawan.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<KaryawanModelData> list) {
        mKaryawan.addAll(list);
        notifyDataSetChanged();
    }

    public void updateList(List<KaryawanModelData> list){
        mKaryawan = list;
        notifyDataSetChanged();
    }

    public void showDialogHapus(final String idkaryawan){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        hapusKaryawan(idkaryawan);
                        TokoManajemenKaryawan.tmKaryawan.finish();
                        Intent intent = new Intent(context, TokoManajemenKaryawan.class);
                        context.startActivity(intent);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        dialogBuilder
                .setTitle("Perhatian")
                .setMessage("Apakah anda yakin akan menghapus karyawan tersebut?")
                .setPositiveButton("Ya", dialogClickListener)
                .setNegativeButton("Tidak", dialogClickListener).show();
    }

    public void hapusKaryawan(String idkaryawan){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Harap tunggu...");
        progressDialog.show();
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<InsertResponse> call = api.deleteKaryawan(idkaryawan);
        System.out.println("masuk");
        call.enqueue(new Callback<InsertResponse>() {
            @Override
            public void onResponse(Call<InsertResponse> call, Response<InsertResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code().equals("1")) {
                    progressDialog.dismiss();
                    Toast.makeText(context, "Hapus karyawan berhasil", Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(context, "Hapus karyawan gagal", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<InsertResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    progressDialog.dismiss();
                    Toast.makeText(context, "Harap periksa koneksi internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
