<?php
session_start();

if (isset($_POST['login']) && isset($_POST['password'])) {
	$login = $_POST['login'];
	$password = $_POST['password'];
	require_once 'database.php';

	$sql = "SELECT * FROM users WHERE login = :login";
	$query = $db->prepare($sql);
	$query->bindValue(':login', $login, PDO::PARAM_STR);
	$query->execute();
	$count = $query->fetchColumn();

	if ($count == 0) { //Poprawna walidacja
		$hash = password_hash($password, PASSWORD_DEFAULT);
		$data  = date("Y-m-d H:i:s");
		$sql = "INSERT INTO users VALUES (NULL, :login, :password, 0, '$data')";
		$query = $db->prepare($sql);
		$query->execute([':login' => $login, ':password' => $hash]);
		//print_r($query);
		$response["success"] = 1;
		$response["message"] = "Zostałeś zarejestrowany.";
		echo json_encode($response);
	} else {
		//Nie poprawne dane
		$response["success"] = 0;
		$response["message"] = "Istnieje użytkownik o takim loginie.";
		$_SESSION["result"] = json_encode($response);
		header('Location: index.php');
		exit;
	}
} else {
	//Nie poprawne dane
	$response["success"] = 0;
	$response["message"] = "Nie podano loginu bądź hasła.";
	$_SESSION["result"] = json_encode($response);
	header('Location: index.php');
}
