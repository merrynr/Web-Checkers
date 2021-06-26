<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <meta http-equiv="refresh" content="5">
    <title>${title} | Web Checkers</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
  <div class="page">
  
    <h1>Web Checkers</h1>
    
    <div class="navigation">

    
      <#if currentPlayer??>
        <a href="/">my home</a>
        </br>
        <a>Current Player: ${currentPlayer}</a>
        <h2>Players Online:</h2>
        <#if userDatabaseString??>
            <#list userDatabaseString as user>
                <#if user != currentPlayer>
                    <#if inGame??>
                      <a>${user}</a>
                    <#else>
                      <a href="/game?opponent=${user}">${user}</a>
                    </#if>
                  </#if>
            </#list>
        <#else>
            <a>No other players are online.</a>
      </#if>
    <#else>
      <a href="/signin">sign in</a>
      <a>Number of players online: ${numberPlayers}</a>
    </#if>
    </div>
    
    <div class="body">
      <p>Welcome to the world of online Checkers.</p>
      <#if currentPlayer??>
        </br>
        <#if game ??>
            <form action="/game" method="GET">
              Your current game:
              <button type="submit">Current Game</button>
            </form>
        </#if>
        <p>To enter Replay Mode click the following link: </p>    <#-- changed to a link to use get (post refreshes to 404) -->
        <a href="/replay">gamelist</a>

      <#-- originally was... -->
      <#-- <form action="./replay" method="POST"><button type="submit">Replay Mode</button></form>-->

          <form action="./signout" method="POST">
                    <br/>
                     <button type="submit">Sign out</button>
        </form>
      
      </#if>
      <#if endGameMessage??>
        <p End of game result: >${endGameMessage}</p>
      </#if>
    </div>
  </div>
</body>
</html>