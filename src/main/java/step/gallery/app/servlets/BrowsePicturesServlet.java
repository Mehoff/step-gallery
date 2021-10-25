package step.gallery.app.servlets;

import org.json.JSONObject;
import step.gallery.app.models.Picture;
import step.gallery.app.util.Db;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "browsePicturesServlet", value = "/browse-pictures-servlet")
public class BrowsePicturesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        req.getRequestDispatcher("browsePictures.jsp").forward(req, res);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        try {
            InputStream reader = req.getInputStream();
            StringBuilder sb = new StringBuilder();

            int sym;

            while ((sym = reader.read()) != -1) {
                sb.append((char) sym);
            }

            String body = new String(
                    sb.toString().getBytes(
                            StandardCharsets.ISO_8859_1),
                    StandardCharsets.UTF_8
            );

            if (body.contains("?")) {
                resp.getWriter().print("{\"status\":-3}");

                return;
            }

            JSONObject params = new JSONObject(body);

            if (Db.updatePicture(new Picture((String) params.get("id"), (String) params.get("name"), (String) params.get("description"),null))) {
                resp.getWriter().print("{\"status\":1}");
            } else {
                resp.getWriter().print("{\"status\":-1}");
            }
        } catch (Exception ex) {
            System.err.println("GalleryServlet(PUT): " + ex.getMessage());

            resp.getWriter().print("{\"status\":-2}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pictureId = req.getParameter("id");
        JSONObject answer = new JSONObject();

        if (pictureId == null || "".equals(pictureId)) {
            answer.put("status", "-1");
            answer.put("message", "picture id required");
        } else {
            if (Db.deletePictureById(pictureId)) {
                answer.put("success", "true");
                answer.put("message", "picture with id " + pictureId + " was deleted");
            } else {
                answer.put("error", "true");
                answer.put("message", "failed to delete picture");
            }
        }

        resp.setContentType("application/json");
        resp.getWriter().print(answer);
    }
}
