<?php

require 'config.php';

$idtoko = $_POST['idtoko'];
$email = $_POST['email'];
$namatoko = $_POST['namatoko'];
$notelp = $_POST['notelp'];
$alamat = $_POST['alamat'];

$sql = "UPDATE tb_toko SET email='".$email."', 
namatoko='".$namatoko."', notelp='".$notelp."', alamat='".$alamat."' 
WHERE idtoko=".$idtoko;

if ($conn->query($sql) === TRUE) {
    $result = mysqli_insert_id($conn);
    echo json_encode(array('status_code'=>'1', 'status'=>'Success!'));
}else{
    echo json_encode(array('status_code'=>'0', 'status'=>'Failed!'));
}
 
mysqli_close($conn);
