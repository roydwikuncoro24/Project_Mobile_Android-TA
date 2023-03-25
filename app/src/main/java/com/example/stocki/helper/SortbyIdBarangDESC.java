package com.example.stocki.helper;

import com.example.stocki.ModelData.BarangModelData;

import java.util.Comparator;

public class SortbyIdBarangDESC implements Comparator<BarangModelData> {
    // Used for sorting in ascending order of
    // roll number
    public int compare(BarangModelData a, BarangModelData b)
    {
        return Integer.parseInt(b.getId()) - Integer.parseInt(a.getId());
    }
}