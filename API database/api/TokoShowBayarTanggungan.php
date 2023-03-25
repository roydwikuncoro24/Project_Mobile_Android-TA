<?php

require 'config.php';

$idtoko = $_POST['idtoko'];


$sql = "SELECT * FROM tb_bayartanggungan WHERE 
idtoko=" . $idtoko;
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
    echo json_encode(array('status_code' => '1', 'status' => "found!", 'result_bayartanggungan' => $rows));
} else {
    echo json_encode(array('status_code' => '0', 'status' => "not found!", 'result_bayartanggungan' => $rows));
}
mysqli_close($conn);
