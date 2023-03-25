package com.example.stocki.helper;

import com.example.stocki.ModelData.TransaksiProses;

import java.util.Comparator;

public class SortbyIdBarangTransaksiProsesDESC implements Comparator<TransaksiProses> {
    // Used for sorting in ascending order of
    // roll number
    public int compare(TransaksiProses a, TransaksiProses b)
    {
        return Integer.parseInt(b.getId()) - Integer.parseInt(a.getId());
    }
}
