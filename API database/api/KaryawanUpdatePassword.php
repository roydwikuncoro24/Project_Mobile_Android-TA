<?php

require 'config.php';

$idkaryawan = $_POST['idkaryawan'];
$password = $_POST['password'];

$sql = "UPDATE tb_karyawan SET password='".$password."' 
WHERE idkaryawan=".$idkaryawan;

if ($conn->query($sql) === TRUE) {
    $result = mysqli_insert_id($conn);
    echo json_encode(array('status_code'=>'1', 'status'=>'Success!'));
}else{
    echo json_encode(array('status_code'=>'0', 'status'=>'Failed!'));
}
 
mysqli_close($conn);
