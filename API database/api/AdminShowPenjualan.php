<?php

require 'config.php';

$idadmin = $_POST['idadmin'];


$sql = "SELECT * FROM tb_toko JOIN tb_barang ON
tb_toko.idtoko = tb_barang.idtoko JOIN tb_penjualan ON 
tb_penjualan.idbarang = tb_barang.id WHERE 
tb_penjualan.idadmin=" . $idadmin;
$result = mysqli_query($conn, $sql);
$rows = array();

while ($row = $result->fetch_assoc()) {
    $source = $row['tanggal'];
    $date = new DateTime($source);
    $row['tanggal'] = $date->format('d-m-Y');
    array_push($rows, $row);
}

$array = array();
if ($rows != NULL) {
    echo json_encode(array('status_code' => '1', 'status' => "found!", 'result_penjualan' => $rows));
} else {
    echo json_encode(array('status_code' => '0', 'status' => "not found!", 'result_penjualan' => $rows));
}
mysqli_close($conn);
