<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <meta http-equiv="refresh" content="5">
    <title>${title} | Web Checkers</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
  <div class="page">

    <h1>Replay List</h1>

    <div class="navigation">
      <#if currentPlayer??>
        <a href="/">my home</a>
        </br>
        <a>Current Player: ${currentPlayer}</a>
      <#else>
       <a>Number of players online: ${numberPlayers}</a>
      </#if>
    </div>
    <div class="body">
        <h2>Past Games:</h2>
            <#if currentPlayer ??>
                <#if gamelist??>
                    <#list gamelist as game>
                        <a href="/replay/game?gameID=${game.getGameID()}">Game ${game?index+1}: ${game.getPlayer().getUsername()} vs. ${game.getOpponent().getUsername()} </a>
                        <br/>
                    </#list>
                <#else>
                    <a>No games have been played yet</a>
                </#if>
            </#if>
    </div>
  </div>
</body>

</html>