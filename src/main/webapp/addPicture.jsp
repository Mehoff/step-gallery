<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Picture</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
</head>
<body>
<div class="container">
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <a class="navbar-brand" href="index.jsp">Gallery</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item"><a class="nav-link" href="add-picture-servlet">Add Picture</a></li>
                <li class="nav-item"><a class="nav-link" href="browse-pictures-servlet">Browse pictures</a></li>
            </ul>
        </div>
    </nav>
    <div class="text-center mt-5">
        <h2>Add picture 😊</h2>
    </div>
    <form method="post" enctype="multipart/form-data">
        <div class="form-group">
            <label for="nameInput">Image name</label>
            <input type="text" class="form-control" id="nameInput" name="name" placeholder="Enter picture name">
        </div><br/>
        <div class="form-group">
            <label for="descriptionInput">Image description</label>
            <input type="text" class="form-control" id="descriptionInput" name="description" placeholder="Enter description">
        </div><br/>
        <div class="form-group">
            <label for="pictureInput"></label>
            <input type="file" class="form-control" id="pictureInput" name="picture">
        </div><br/>
        <button type="submit" class="btn btn-primary text-center">Отправить картинку</button>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
</body>
</html>
