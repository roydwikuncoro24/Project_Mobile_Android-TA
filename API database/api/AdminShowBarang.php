<?php
require 'config.php';

$idadmin = $_POST['idadmin'];


$sql = "SELECT * FROM tb_toko JOIN tb_barang ON
tb_toko.idtoko = tb_barang.idtoko WHERE 
tb_barang.idadmin=" . $idadmin;
$result = mysqli_query($conn, $sql);
$rows = array();

while ($row = $result->fetch_assoc()) {
    array_push($rows, $row);
}
$array = array();

if ($rows != NULL) {
    echo json_encode(array('status_code' => 1, 'status' => "found!", 'result_barang' => $rows));
} else {
    echo json_encode(array('status_code' => 0, 'status' => "not found!", 'result_barang' => $rows));
}
mysqli_close($conn);
