package step.gallery.app.filters;


import org.json.JSONObject;

import step.gallery.app.util.Db;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

@WebFilter("/*")
public class DbFilter implements Filter {
    private FilterConfig filterConfig = null;
    private String configFilename = "localdb.json";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        res.setCharacterEncoding("UTF-8");

        Db.setConnection(null);
        File config = new File(filterConfig.getServletContext().getRealPath("/WEB-INF/configs/") + "/" + configFilename);

        if (!config.exists()) {
            System.err.println("config/" + configFilename + " not found");
        } else {
            try (InputStream reader = new FileInputStream(config)) {
                int fileLength = (int) config.length();
                byte[] buf = new byte[fileLength];

                if (reader.read(buf) != fileLength)
                    throw new IOException("File read integrity falls");

                JSONObject configData = new JSONObject(new String(buf));

                if (!Db.setConnection(configData))
                    throw new SQLException("Db connection error");
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
        // Checking connection to be opened
        if (Db.getConnection() == null) {
            // No connection - use static mode
            req
                    .getRequestDispatcher("/static.jsp")
                    .forward(req, res);
        } else {
            if(!Db.isTableExists("PICTURES")){
                Db.createGallery();
            }
            filterChain.doFilter(req, res);
            Db.closeConnection();
        }
    }
}
