<?php

require 'config.php';

$idtransaksi = $_POST['id'];


$sql = "SELECT * FROM tb_transaksi WHERE id=" . $idtransaksi;
$result = mysqli_query($conn, $sql);
$rows = array();

while ($row = $result->fetch_assoc()) {
    $source = $row['tanggal'];
    $date = new DateTime($source);
    $row['tanggal'] = $date->format('Y-m-d H:i');
    array_push($rows, $row);
}
$array = array();

if ($rows != NULL) {
    echo json_encode(array('status_code' => '1', 'status' => "found!", 'result_transaksi' => $rows));
} else {
    echo json_encode(array('status_code' => '0', 'status' => "not found!", 'result_transaksi' => $rows));
}
mysqli_close($conn);
