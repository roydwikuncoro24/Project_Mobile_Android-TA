<?php

require 'config.php';

$idbarang = $_POST['idbarang'];


$sql = "SELECT * FROM tb_barang WHERE idbarang=" . $idbarang;
$result = mysqli_query($conn, $sql);
$rows = array();

while ($row = $result->fetch_assoc()) {
    array_push($rows, $row);
}

$array = array();

if ($rows != NULL) {
    echo json_encode(array('status_code' => '1', 'status' => "found!", 'result_barang' => $rows));
} else {
    echo json_encode(array('status_code' => '0', 'status' => "not found!", 'result_barang' => $rows));
}
mysqli_close($conn);
