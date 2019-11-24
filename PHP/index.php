<?php
session_start();
if(isset($_SESSION['result']))
{
   echo $_SESSION['result'];
   unset($_SESSION['result']);
   exit;
}
?>

<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <title>API</title>
    <meta name="description" content="Używanie PDO - API APlikacji">
    <meta name="keywords" content="php, PDO, połączenie, MySQL, android">
    <meta http-equiv="X-Ua-Compatible" content="IE=edge">
    <link rel="stylesheet" href="main.css">
</head>

<body>
    <div class="container">

        <header>
            <h1>Logowanie</h1>
        </header>

        <main>
            <article>
                <form action="login.php" method="get">
                    <label>Podaj Login:
                        <input type="text" name="login">
                    </label>
					<label>Podaj Hasło:
                        <input type="password" name="password">
                    </label>
                    <input type="submit" value="Zaloguj się!">
                </form>
            </article>
        </main>
    </div>
</body>
</html>