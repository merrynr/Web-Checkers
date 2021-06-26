<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <meta http-equiv="refresh" content="10">
    <title>${title} | Web Checkers</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
  <div class="page">
  
    <h1>Web Checkers Sign in Page</h1>
    
    <div class="navigation">
      <a href="/">my home</a>
    </div>
    
   <div class="body">
  <p> ${signin_message} </p>
       <form action="./signin" method="POST">
              Enter a username
              <br/>
              <input id = "username" name="username" required pattern = "^[ a-zA-Z0-9]+$"/>
              <br/><br/>
               <button type="submit">Ok</button>
            </form>
    </div>

    
  </div>
</body>
</html>
