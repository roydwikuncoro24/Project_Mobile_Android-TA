<?php
require 'config.php';

$idbarang = $_POST['id'];

$sql = "DELETE FROM tb_barang WHERE id=" . $idbarang;
if ($conn->query($sql) === TRUE) {
    $last_id = mysqli_insert_id($conn);
    echo json_encode(array('status_code' => '1', 'status' => 'Success!'));
} else {
    echo json_encode(array('status_code' => '0', 'status' => 'Failed!'));
}

mysqli_close($conn);
