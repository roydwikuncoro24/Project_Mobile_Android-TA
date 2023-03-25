<?php
require 'config.php';

$idtoko = $_POST['idtoko'];

$sql = "DELETE FROM tb_toko WHERE idtoko=" . $idtoko;
if ($conn->query($sql) === TRUE) {
    $last_id = mysqli_insert_id($conn);
    echo json_encode(array('status_code' => '1', 'status' => 'Success!'));
} else {
    echo json_encode(array('status_code' => '0', 'status' => 'Failed!'));
}

mysqli_close($conn);
