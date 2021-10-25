package step.gallery.app.servlets;


import step.gallery.app.models.Picture;
import step.gallery.app.util.Db;
import step.gallery.app.util.Hasher;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

@WebServlet(name = "addPictureServlet", value = "/add-picture-servlet")
@MultipartConfig
public class AddPictureServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        req.getRequestDispatcher("addPicture.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession();
        Part filePart = req.getPart("picture");

        String uploadMessage = "";
        String name = req.getParameter("name");
        String description = req.getParameter("description");

        String attachedFilename = null;
        String contentDisposition = filePart.getHeader("content-disposition");

        if (contentDisposition != null) {
            for (String part : contentDisposition.split("; ")) {
                if (part.startsWith("filename")) {
                    attachedFilename = part.substring(10, part.length() - 1);
                    break;
                }
            }
        }
        if (attachedFilename != null) {
            String extension;

            int dotPosition = attachedFilename.lastIndexOf(".");

            if (dotPosition > -1) {
                extension = attachedFilename.substring(dotPosition);

                String[] allowedExtensions = new String[] {".jpg", ".jpeg", ".png"};

                if(!Arrays.asList(allowedExtensions).contains(extension)) {
                    session.setAttribute("uploadMessage", "This file extension not allowed");

                    resp.sendRedirect(req.getRequestURI());
                    return;
                }

                String path = req.getServletContext().getRealPath("/uploads");
                File destination;
                String filename, savedFilename;

                // If file with this name exists regenerate
                do {
                    // Generate random file name and set extension
                    savedFilename = Hasher.hash(attachedFilename) + extension;

                    // Full path
                    filename = path + "\\" + savedFilename;
                    destination = new File(filename);
                    attachedFilename = savedFilename;
                } while (destination.exists());

                Files.copy(
                        filePart.getInputStream(),
                        destination.toPath(),
                        StandardCopyOption.REPLACE_EXISTING
                );

                path = "E:\\Code\\Repositories\\StepGalleryApplication\\src\\main\\webapp\\uploads";
                filename = path + "\\" + savedFilename;
                destination = new File(filename);

                Files.copy(
                        filePart.getInputStream(),
                        destination.toPath(),
                        StandardCopyOption.REPLACE_EXISTING
                );

                // Add file path to db
                if (Db.addPicture(new Picture(name, description, savedFilename))) {
                    uploadMessage = "Image successfully uploaded";
                } else {
                    uploadMessage = "Failed to upload image";
                }
            } else {
                attachedFilename = "no file extension";
            }
        }

        session.setAttribute("uploadMessage", uploadMessage);
        session.setAttribute("description", description);

        resp.sendRedirect(req.getRequestURI());
    }
}
