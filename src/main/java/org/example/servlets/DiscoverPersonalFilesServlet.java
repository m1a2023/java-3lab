package org.example.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.models.LocalFile;
import org.example.models.User;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name="DiscoverPersonalFiles", value="/personal/files")
public class DiscoverPersonalFilesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException
    {
        /* Get user by session */
        HttpSession session = req.getSession();
        String username = (String) session.getAttribute("username");

        if (username == null) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not authenticated");
            return;
        }

        /* Build root path for user */
        String rootPathStr = new File(System.getProperty("user.home")).toPath().getParent() + "/" + username;
        rootPathStr = rootPathStr.replace("\\", "/");
        Path rootPath = Paths.get(rootPathStr).normalize();

        /* Build request path */
        String pathParam = req.getParameter("path");
        Path requestPath;
        if (pathParam == null) {
            requestPath = rootPath;
        } else {
            pathParam = pathParam.replace("\\", "/");
            requestPath = Paths.get(pathParam).normalize();
        }

        /* Validate request path */
        if (!requestPath.startsWith(rootPath)) {
            requestPath = rootPath;
        }

        File requestDirectory = requestPath.toFile();
        if (!requestDirectory.exists() || !requestDirectory.isDirectory()) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND, "Directory not found");
            return;
        }

        /* Debug */
        System.out.printf("\n\nRequest path: %s\n\n", requestPath);

        /* Get user files */
        List<LocalFile> files = new ArrayList<>();
        File[] listedFiles = requestDirectory.listFiles();
        if (listedFiles != null) {
            for (File file : listedFiles) {
                files.add(new LocalFile(file));
            }
        }

        /* Build previous directory */
        Path previousPath = requestPath.getParent();
        String previousPathStr;
        if (previousPath == null || !previousPath.startsWith(rootPath)) {
            previousPathStr = rootPath.toString().replace("\\", "/");
        } else {
            previousPathStr = previousPath.toString().replace("\\", "/");
        }

        /* Debug */
        System.out.printf("Previous path: %s\n", previousPathStr);

        /* Set attributes and forward */
        req.setAttribute("previousPath", previousPathStr);
        req.setAttribute("currentPath", requestPath.toString().replace("\\", "/"));
        req.setAttribute("Items", files);

        req.getRequestDispatcher("/page.jsp").forward(req, res);
    }
}
