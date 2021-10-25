<%@ page import="step.gallery.app.util.Db" %>
<%@ page import="step.gallery.app.models.Picture" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% ArrayList<Picture> Pictures = Db.getPictures(); %>
<html>
<head>
    <title>Title</title>
    <link href="./css/gallery.css" rel="stylesheet">
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


        <% if(Pictures == null) { %>
        <h2>Unfortunately, no pictures were found in database, add a new one <a href="addPicture.jsp">here</a> 😊</h2>

        <% } else { %>
        <h2>Here is some pictures that we got 😊</h2>

        <% for(Picture pic : Pictures){ %>

        <div class="col-sm picture mb-5">
            <div class="card shadow-sm">
                <img class="rounded img-fluid" src="uploads/<%= pic.getPicturePath() %>" alt="<%= pic.getName() %>" />
                <div class="card-body">
                    <h2><%= pic.getName() %></h2>
                    <p><%= pic.getDescription() %></p>
                    <tt class="d-none"><%= pic.getId() %></tt>
                    <div class="crud-buttons">
                        <button type="button" class="btn btn-sm btn-outline-light tool-button tool-edit" title="Edit"></button>
                        <button type="button" class="btn btn-sm btn-outline-light tool-button tool-download" title="Download"></button>
                        <button type="button" class="btn btn-sm btn-outline-light tool-button tool-delete" title="Delete"></button>
                    </div>
                    <small class="text-muted"> <%= pic.getMoment() %> </small>
                </div>
            </div>
        </div>
        <% } }%>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
</body>
<script src="js/gallery.js"></script>
</html>
