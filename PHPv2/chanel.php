<?php
session_start();

$method = $_SERVER['REQUEST_METHOD'];
if ('PUT' === $method) {
	parse_str(file_get_contents('php://input'), $_PUT);
	//$_PUT contains put fields https://stackoverflow.com/questions/27941207/http-protocols-put-and-delete-and-their-usage-in-php
}

if (isset($_PUT['login']) && isset($_PUT['chanel'])) {
	$login = $_PUT['login'];
	$chanel = $_PUT['chanel'];
	require_once 'database.php';

	$sql = "SELECT * FROM users WHERE login = :login";
	$query = $db->prepare($sql);
	$query->bindValue(':login', $login, PDO::PARAM_STR);
	$query->execute();
	$count = $query->fetchColumn();

	if ($count == 1) {
		$sql = " UPDATE users SET chanel = :chanel WHERE login = :login";
		$query = $db->prepare($sql);
		$query->execute([':login' => $login, ':chanel' => $chanel]);
		//print_r($query);
		$response["success"] = 1;
		$response["message"] = "Kanal zostal zmieniony.";
		echo json_encode($response);
	} else {
		$response["success"] = 0;
		$response["message"] = "Nie poprawne dane";
		echo json_encode($response);
		exit;
	}
} else {
	$response["success"] = 0;
	$response["message"] = "Nie poprawne dane.";
	echo json_encode($response);
}
