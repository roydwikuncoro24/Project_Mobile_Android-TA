<?php
require 'config.php';

$idtoko = $_POST['idtoko'];


$sql = "SELECT * FROM tipebarang JOIN tb_barang ON
tipebarang.idtipe = tb_barang.idtipe JOIN tb_penjualan ON
tb_barang.id = tb_penjualan.idbarang 
WHERE tb_penjualan.idtoko=" . $idtoko;
$result = mysqli_query($conn, $sql);
$rows = array();

while ($row = $result->fetch_assoc()) {
    $source = $row['tanggal'];
    $date = new DateTime($source);
    $row['tanggal'] = $date->format('Y-m-d');
    array_push($rows, $row);
}
$array = array();

if ($rows != NULL) {
    echo json_encode(array('status_code' => 1, 'status' => "found!", 'result_penjualan' => $rows));
} else {
    echo json_encode(array('status_code' => 0, 'status' => "not found!", 'result_penjualan' => $rows));
}
mysqli_close($conn);
