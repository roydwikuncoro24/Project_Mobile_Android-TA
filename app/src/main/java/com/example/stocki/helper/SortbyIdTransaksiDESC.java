package com.example.stocki.helper;

import com.example.stocki.ModelData.TransaksiModelData;

import java.util.Comparator;

public class SortbyIdTransaksiDESC implements Comparator<TransaksiModelData> {
    // Used for sorting in ascending order of
    // roll number
    public int compare(TransaksiModelData a, TransaksiModelData b)
    {
        return Integer.parseInt(b.getId()) - Integer.parseInt(a.getId());
    }
}
