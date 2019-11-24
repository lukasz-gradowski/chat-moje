<?php
session_start();

if (isset($_GET['login']) && isset($_GET['password'])) {

	$login = $_GET['login'];
	$password = $_GET['password'];
	// $hash = password_hash($password, PASSWORD_DEFAULT);
	// echo($hash);

	require_once 'database.php';
	$sql = "SELECT * FROM users WHERE login = :login";
	$query = $db->prepare($sql);
	$query->bindValue(':login', $login, PDO::PARAM_STR);
	$query->execute();
	$result = $query->fetch();
	$hash = $result['pass'];

	// $sql = "SELECT * FROM users WHERE login = :login AND pass = :password";
	// $query = $db->prepare($sql);
	// $query->execute([':login' => $login, ':password' => $password]);
	// $count = $query->fetchColumn();
	// echo($count);

	if (password_verify($password, $hash)) {
		$sql = "SELECT * FROM users WHERE login = '{$login}'";
		$query = $db->query($sql);
		$count = $query->fetchColumn();
		echo ($count);
		$response["success"] = 1;
        	$response["message"] = "Zostałeś zalogowany.";
		echo json_encode($response);

	} else {
		//Nie poprawne dane
		$response["success"] = 0;
        	$response["message"] = "Niepoprawne dane.";
		echo json_encode($response);
		header('Location: index.php');
	}

	if ($count == 1) {
		$query = $db->query("UPDATE users SET is_online = 1 WHERE login = '{$login}' AND pass = '{$hash}'");
	} else {
		//Nie poprawne dane
		$response["success"] = 0;
        	$response["message"] = "Niepoprawne dane.";
		echo json_encode($response);
		header('Location: index.php');
	}
} else {
	//Nie poprawne dane
		$response["success"] = 0;
        	$response["message"] = "Niepoprawne dane.";
		echo json_encode($response);
		header('Location: index.php');
}
