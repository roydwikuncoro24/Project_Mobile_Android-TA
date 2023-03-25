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

import com.example.stocki.AktifitasAdmin.Admin;
import com.example.stocki.AktifitasAdmin.AdminManajemenMitra;
import com.example.stocki.AktifitasToko.Toko;
import com.example.stocki.ModelData.KaryawanModelData;
import com.example.stocki.ModelData.TokoModelData;
import com.example.stocki.ModelResponse.InsertResponse;
import com.example.stocki.R;
import com.example.stocki.service.APIService;
import com.example.stocki.service.RetrofitHelper;

import java.net.SocketTimeoutException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminMitraAdapter extends RecyclerView.Adapter<AdminMitraAdapter.HolderDataMitra> {

    List<TokoModelData> mToko;
    Context context;
    ProgressDialog progressDialog;

    public AdminMitraAdapter(List<TokoModelData> mToko, Context context) {
        this.mToko = mToko;
        this.context = context;
    }

    @Override
    public AdminMitraAdapter.HolderDataMitra onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mitrakaryawan,
                parent, false);
        AdminMitraAdapter.HolderDataMitra holderData = new AdminMitraAdapter.
                HolderDataMitra(v);
        return holderData;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminMitraAdapter.HolderDataMitra holder, int position) {
        TokoModelData tk = mToko.get(position);
        holder.tvNick.setText(tk.getNamatoko().substring(0,1));
        holder.tvNama.setText(tk.getNamatoko().toString());
        holder.tvNotelp.setText(tk.getNotelp().toString());
        holder.tvAlamat.setText(tk.getAlamat().toString());
        holder.idtoko = tk.getIdtoko();
        holder.nampem = tk.getNamapemilik();
    }

    @Override
    public int getItemCount() {
        return mToko.size();
    }

    public class HolderDataMitra extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvNama, tvAlamat, tvNotelp, tvNick;
        String idtoko, nampem, notelp, alamat;
        CardView cardView;

        public HolderDataMitra(View v) {
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
            this.idtoko.toString();
            txTitle.setText("Profil Toko");
            txNamaPem.setText("Pemilik : "+this.nampem);
            txNama.setText("Nama : "+this.tvNama.getText().toString());
            txAlamat.setText("Alamat : "+this.tvAlamat.getText().toString());
            txNoTelp.setText("No. Telp : "+this.tvNotelp.getText().toString());

            dialogBuilder.setNegativeButton("Hapus", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showDialogHapus(idtoko);
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
        mToko.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<TokoModelData> list) {
        mToko.addAll(list);
        notifyDataSetChanged();
    }

    public void updateList(List<TokoModelData> list){
        mToko = list;
        notifyDataSetChanged();
    }

    public void showDialogHapus(final String idtoko){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        hapusToko(idtoko);
                        AdminManajemenMitra.amMitra.finish();
                        Intent intent = new Intent(context, AdminManajemenMitra.class);
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
                .setMessage("Apakah anda yakin akan menghapus toko tersebut?")
                .setPositiveButton("Ya", dialogClickListener)
                .setNegativeButton("Tidak", dialogClickListener).show();
    }

    public void hapusToko(String idtoko){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Harap tunggu...");
        progressDialog.show();
        APIService api = RetrofitHelper.getClient().create(APIService.class);
        Call<InsertResponse> call = api.deleteMitra(idtoko);
        System.out.println("masuk");
        call.enqueue(new Callback<InsertResponse>() {
            @Override
            public void onResponse(Call<InsertResponse> call, Response<InsertResponse> response) {
                System.out.println("re : " + response.body().getStatus_code());
                if (response.body().getStatus_code().equals("1")) {
                    progressDialog.dismiss();
                    Toast.makeText(context, "Hapus toko berhasil", Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(context, "Hapus toko gagal", Toast.LENGTH_SHORT).show();
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
