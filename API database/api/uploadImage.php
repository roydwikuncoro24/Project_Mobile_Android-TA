<?php

$destination_path = getcwd() . DIRECTORY_SEPARATOR;
$destination_path =  substr($destination_path, 0, (strlen($destination_path) - 4));
$target_file = $destination_path . 'uploadgambar/' . date("YmdHis") . basename($_FILES["fileToUpload"]["name"]);
$uploadOk = 1;
$imageFileType = strtolower(pathinfo($target_file, PATHINFO_EXTENSION));
// Check if image file is a actual image or fake image
$check = getimagesize($_FILES["fileToUpload"]["tmp_name"]);
if ($check !== false) {
    $uploadOk = 1;
} else {
    $uploadOk = 2;
}

// Check if file already exists
/*if (file_exists($target_file)) {
    echo "Sorry, file already exists.";
    $uploadOk = 0;
}*/

// Check file size 100000 = 100KB
if ($_FILES["fileToUpload"]["size"] > 200000) {
    $uploadOk = 3;
}

// Allow certain file formats
else if ($imageFileType != "jpg" && $imageFileType != "png" && $imageFileType != "jpeg") {
    $uploadOk = 4;
}

// Check if $uploadOk is set to not 1 by an error
if ($uploadOk == 2) {
    echo json_encode(array('status_code' => '2', 'status' => 'File bukan gambar'));
} else if ($uploadOk == 3) {
    echo json_encode(array('status_code' => '3', 'status' => 'Ukuran file melebihi 200KB'));
} else if ($uploadOk == 4) {
    echo json_encode(array('status_code' => '4', 'status' => 'Format file tidak diizinkan'));
    // if everything is ok, try to upload file
} else {
    if (move_uploaded_file($_FILES["fileToUpload"]["tmp_name"], $target_file)) {
        $target_file = preg_split("/usahaskripsi/", $target_file);
        $target_file =  'http://sepatupurwokerto.com/usahaskripsi/' . $target_file[1];
        echo json_encode(array('status_code' => '1', 'url' => $target_file, 'status' => 'Success!'));
    } else {
        echo json_encode(array('status_code' => '0', 'status' => 'Failed!'));
    }
}
