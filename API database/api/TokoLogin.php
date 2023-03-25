<?php
require 'config.php';

$username = $_POST['username'];
$password = $_POST['password'];


$sql = "SELECT * FROM tb_toko WHERE username = '" . $username . "' AND password = '" . $password . "' AND idadmin>0";
$result = mysqli_query($conn, $sql);
$row = mysqli_fetch_array($result, MYSQLI_ASSOC);

if ($row != NULL) {
    echo json_encode(array('status_code' => 1, 'status' => "User found!", 'result_toko' => array($row)));
} else {
    echo json_encode(array('status_code' => 0, 'status' => "User not found!"));
}

mysqli_close($conn);
