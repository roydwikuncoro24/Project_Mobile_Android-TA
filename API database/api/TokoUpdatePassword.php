<?php

require 'config.php';

$idtoko = $_POST['idtoko'];
$password = $_POST['password'];

$sql = "UPDATE tb_toko SET password='".$password."' 
WHERE idtoko=".$idtoko;

if ($conn->query($sql) === TRUE) {
    $result = mysqli_insert_id($conn);
    echo json_encode(array('status_code'=>'1', 'status'=>'Success!'));
}else{
    echo json_encode(array('status_code'=>'0', 'status'=>'Failed!'));
}
 
mysqli_close($conn);
