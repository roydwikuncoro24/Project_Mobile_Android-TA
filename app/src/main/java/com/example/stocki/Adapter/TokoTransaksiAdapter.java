package com.example.stocki.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.stocki.AktifitasKaryawan.Karyawan;
import com.example.stocki.AktifitasKaryawan.KaryawanTransaksi;
import com.example.stocki.AktifitasToko.TokoTransaksi;
import com.example.stocki.ModelData.TransaksiProses;
import com.example.stocki.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TokoTransaksiAdapter extends RecyclerView.Adapter<TokoTransaksiAdapter.HolderData> implements Filterable {
    List<TransaksiProses> transaksiList;
    List<TransaksiProses> mFilteredList = new ArrayList<>();
    Activity activity;
    int tipe = 0;

    public TokoTransaksiAdapter(List<TransaksiProses> transaksiList, Activity activity) {
        this.transaksiList = transaksiList;
        this.mFilteredList = transaksiList;
        this.activity = activity;
    }
    public TokoTransaksiAdapter(List<TransaksiProses> transaksiList, Activity activity, int tipe) {
        this.transaksiList = transaksiList;
        this.mFilteredList = transaksiList;
        this.activity = activity;
        this.tipe = tipe;
    }

    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = (tipe == 0) ?
                LayoutInflater.from(parent.getContext()).inflate
                        (R.layout.item_barang2, parent, false) :
                LayoutInflater.from(parent.getContext()).inflate
                        (R.layout.item_barang, parent, false);
        return new HolderData(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        TransaksiProses transaksi = mFilteredList.get(position);
        holder.id = transaksi.getId();
        holder.txNama.setText(transaksi.getNamabarang());
        holder.txStok.setText(transaksi.getStok());
        holder.txHarga.setText("Rp " + doubleToStringNoDecimal(Double.parseDouble(transaksi.getJualtoko())));
        holder.txBeli.setText(transaksi.getJumlah());
        if (tipe!=0){
            //list
            holder.txBeli.setVisibility(View.VISIBLE);
            holder.imgBarang.setVisibility(View.GONE);
            holder.imgBarang2.setVisibility(View.VISIBLE);
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .error(R.drawable.ic_picture)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH);
            Glide.with(activity.getApplicationContext())
                    .load(transaksi.getImage())
                    .apply(options)
                    .into(holder.imgBarang2);
        }  else {
            holder.imgBarang.setVisibility(View.VISIBLE);
            holder.imgBarang2.setVisibility(View.GONE);
        }

        if (tipe == 0){
            holder.txBeli.setVisibility(View.VISIBLE);
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .error(R.drawable.ic_picture)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH);
            Glide.with(activity.getApplicationContext())
                    .load(transaksi.getImage())
                    .apply(options)
                    .into(holder.imgBarang);
        }
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();

                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilteredList = transaksiList;
                } else {
                    List<TransaksiProses> resultData = new ArrayList<>();
                    for(TransaksiProses transaksi: transaksiList){
                        if (transaksi.getNamabarang().toLowerCase().contains(charString.toLowerCase())) {
                            resultData.add(transaksi);
                        }
                        else if(transaksi.getKode().toLowerCase().contains(charString.toLowerCase())){
                            resultData.add(transaksi);
                        }
                    }
                    mFilteredList = resultData;
                }
                filterResults.count = mFilteredList.size();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                mFilteredList = (List<TransaksiProses>) filterResults.values;
                notifyDataSetChanged();

            }
        };
    }

    public class HolderData extends RecyclerView.ViewHolder implements View.OnClickListener {

        public  TextView txBeli, txNama, txHarga, txStok;
        public ImageView imgBarang, imgBarang2;
        int jum = 0;
        String id;

        public HolderData(View itemView) {
            super(itemView);
            txBeli = (TextView)itemView.findViewById(R.id.tx_belitrans);
            txNama = (TextView)itemView.findViewById(R.id.tx_namatrans);
            txHarga = (TextView)itemView.findViewById(R.id.tx_hargatrans);
            txStok = (TextView)itemView.findViewById(R.id.tx_stoktrans);
            imgBarang = (ImageView) itemView.findViewById(R.id.img_dbarang);
            imgBarang2 = (ImageView) itemView.findViewById(R.id.img_dbarang2);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (!txStok.getText().equals("0")){
                int jumlah = Integer.parseInt(mFilteredList.get(position).getJumlah()) + 1;
                txBeli.setText(String.valueOf(jumlah));
                int stok = Integer.parseInt(mFilteredList.get(position).getStok()) - 1;
                txStok.setText(String.valueOf(stok));
                if (stok>=0) {
                    mFilteredList.get(position).setStok(String.valueOf(stok));
                    mFilteredList.get(position).setJumlah(String.valueOf(jumlah));
                    if (activity instanceof TokoTransaksi) {
                        ((TokoTransaksi) activity).addTransaksi(mFilteredList.get(position));
                    } else if (activity instanceof KaryawanTransaksi){
                        ((KaryawanTransaksi)activity).addTransaksi(mFilteredList.get(position));
                    }
                }else{
                    Toast.makeText(v.getContext(),"Stok "+txNama.getText().toString()+" habis",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(v.getContext(),"Stok "+txNama.getText().toString()+" habis",Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        transaksiList.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<TransaksiProses> list) {
        mFilteredList.addAll(list);
        notifyDataSetChanged();
    }

    public void updateList(List<TransaksiProses> list){
        transaksiList = list;
        notifyDataSetChanged();
    }

    public static String doubleToStringNoDecimal(double d) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter .applyPattern("#,###");
        return formatter.format(d).replace(",",".");
    }
}
