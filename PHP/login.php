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
	$count = $query->fetchColumn();

	if ($count != 1) {
		$response["success"] = 0;
		$response["message"] = "Niepoprawny login.";
		$_SESSION["result"] = json_encode($response);
		header('Location: index.php');
		exit;
	}

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

	if (password_verify($password, $hash) && $count == 1) {
		$query = $db->query("UPDATE users SET is_online = 1 WHERE login = '{$login}' AND pass = '{$hash}'");
		$response["success"] = 1;
		$response["message"] = "Zostałeś zalogowany.";
		echo json_encode($response);
	} else {
		$response["success"] = 0;
		$response["message"] = "Niepoprawne hasło.";
		$_SESSION["result"] = json_encode($response);
		header('Location: index.php');
		exit;
	}
} else {
	$response["success"] = 0;
	$response["message"] = "Nie podano loginu bądź hasła.";
	$_SESSION["result"] = json_encode($response);
	header('Location: index.php');
}
