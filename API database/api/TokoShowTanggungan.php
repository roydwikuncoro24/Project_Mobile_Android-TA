<?php

require 'config.php';

$idtoko = $_POST['idtoko'];


$sql = "SELECT * FROM tb_tanggungan WHERE 
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
    echo json_encode(array('status_code' => '1', 'status' => "found!", 'result_tanggungan' => $rows));
} else {
    echo json_encode(array('status_code' => '0', 'status' => "not found!", 'result_tanggungan' => $rows));
}
mysqli_close($conn);
