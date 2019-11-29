<?php
session_start();

if (isset($_GET['chanel'])) {
	$chanel = $_GET['chanel'];
	require_once 'database.php';

	$sql = "SELECT text, time, login FROM messages LEFT JOIN users ON users.id = messages.user_id ORDER BY time DESC LIMIT 100;";
	$query = $db->prepare($sql);
	$query->bindValue(':chanel', $chanel, PDO::PARAM_STR);
	$query->execute();

	$response["success"] = 1;
	$response["message"] = [];
	while ($message = $query->fetch(PDO::FETCH_ASSOC)) {
		$response["message"][] = "{$message['login']}: {$message['text']}: ||{$message['time']} </br>";
	}
	echo json_encode($response);

} else {
	$response["success"] = 0;
	$response["message"] = "Nie poprawny kanal.";
	echo json_encode($response);
}
