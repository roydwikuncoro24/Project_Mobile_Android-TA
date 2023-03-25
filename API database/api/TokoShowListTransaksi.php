<?php

require 'config.php';

$idtoko = $_POST['idtoko'];
$idtransaksi = $_POST['idtransaksi'];

$sql = "SELECT * FROM tb_barang JOIN tb_penjualan ON
tb_barang.id = tb_penjualan.idbarang 
WHERE tb_penjualan.idtoko=" . $idtoko . " AND tb_penjualan.idtransaksi=" . $idtransaksi;
$result = mysqli_query($conn, $sql);
$rows = array();

while ($row = $result->fetch_assoc()) {
    array_push($rows, $row);
}

$array = array();

if ($rows != NULL) {
    echo json_encode(array('status_code' => 1, 'status' => "found!", 'result_penjualan' => $rows));
} else {
    echo json_encode(array('status_code' => 0, 'status' => "not found!", 'result_penjualan' => $rows));
}
mysqli_close($conn);
