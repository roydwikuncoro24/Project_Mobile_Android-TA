<?php

require 'config.php';

$tanggalTransaksi = date("Y-m-d H:i:s");
$tanggal = date("Y-m-d");
$list_barang = file_get_contents('php://input');
$json_list = json_decode($list_barang, true);
$flag = 1;
$lastidtransaksi = 0;

if ($flag == 1) {
    $sql = "INSERT INTO tb_transaksi (idtoko,tanggal,modal,jual,
    jumlah,petugas,namatoko,tipe) VALUES 
    (" . $json_list['idtoko'] . ",
    '" . $tanggalTransaksi . "',
    '" . $json_list['modal'] . "',
    '" . $json_list['jual'] . "',
    '" . $json_list['jumlah'] . "',
    '" . $json_list['namapetugas'] . "',
    '" . $json_list['namatoko'] . "',
    " . $json_list['tipe'] . ")";
    if ($conn->query($sql) === TRUE) {
        $lastidtransaksi = mysqli_insert_id($conn);
    }
} else {
    $flag = 0;
}

if ($json_list['tipe'] == '1') {
    foreach ($json_list['barangList'] as $item) {
        if ($item['idtipe'] == '1') {
            $sql = "UPDATE tb_barang SET 
            stok=stok-" . $item['stok'] . ",  
            aset=aset-" . $item['stok'] . "*" . $item['hargadasar'] . "
            WHERE id=" . $item['id'];
            if ($conn->query($sql) === TRUE) {
                $flag = 1;
            }
        }
    }
}
if ($json_list['tipe'] == '1') {
    foreach ($json_list['barangList'] as $item) {
        if ($item['idtipe'] == '2') {
            $sql = "UPDATE tb_barang SET 
            stok=stok-" . $item['stok'] . ",  
            aset=aset-" . $item['stok'] . "*" . $item['hargajual'] . "
            WHERE id=" . $item['id'];
            if ($conn->query($sql) === TRUE) {
                $flag = 1;
            }
        }
    }
}

if ($flag == 1) {
    foreach ($json_list['barangList'] as $item) {
        $sql = "INSERT INTO tb_penjualan (idtoko,idtransaksi,tanggal,idbarang,
        idadmin,jumlah,hdasar,hjual,hjualtoko,
        jumhargadasar,jumhargajual,jumhargajualtoko,
        idtipe) VALUES 
        (" . $json_list['idtoko'] . ",
        " . $lastidtransaksi . ",
        '" . $tanggalTransaksi . "',
        '" . $item['id'] . "',
        '" . $item['idadmin'] . "',
        '" . $item['stok'] . "',
        '" . $item['hargadasar'] . "',
        '" . $item['hargajual'] . "',
        '" . $item['hargajualtoko'] . "',
        " . $item['hargadasar'] . "*" . $item['stok'] . ",
        " . $item['hargajual'] . "*" . $item['stok'] . ",
        " . $item['hargajualtoko'] . "*" . $item['stok'] . ",
        '" . $item['idtipe'] . "')";

        if ($item['idtipe'] == '1') {
            $sql2 = "INSERT INTO tb_tanggungan (idtoko,idadmin,tanggal,
            tanggungan) VALUES 
                (" . $json_list['idtoko'] . ",
                '" . $item['idadmin'] . "',
                '" . $tanggal . "',
                " . $item['hargajual'] . "*" . $item['stok'] . ")";
            if ($conn->query($sql2) === TRUE) {
                $flag = 1;
            }
        }
        if ($conn->query($sql) === TRUE) {
            $flag = 1;
        } else {
            $flag = 0;
        }
    }
    if ($flag == 1) {
        echo json_encode(array('status_code' => '1', 'status' => 'Success!', 'idtransaksi' => $lastidtransaksi));
    }
} else {
    echo json_encode(array('status_code' => '0', 'status' => 'Gagal!'));
}
mysqli_close($conn);
