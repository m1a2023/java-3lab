package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.discover.LocalFile;
import org.example.discover.User;

import java.io.File;
import java.io.IOException;
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
        User u = (User) session.getAttribute("user");

        /* If session is expired */
        if (u == null) {
            res.sendRedirect("/logout");
        }

        /* Build path */
        String path = req.getParameter("path");
        if (path == null) {
            path = System
                    .getProperty("user.home")
                    .replace("\\", "/");

        }

        { // Debug
            System.out.printf("\n\npath: %s\n\n", path);
        }
        /* Build root path for user */
        String rootPath = (new File(path))
                .toPath()
                .getParent() + "/" + u.getUsername();
        rootPath = rootPath.replace("\\", "/");

        { // Debug
            System.out.printf("\n\nroot path: %s\n\n", rootPath);
        }

        File rootDirectory = new File(rootPath); // Build

        if (!rootDirectory.exists()) { // Check if present
            res.sendError(HttpServletResponse.SC_NOT_FOUND, "Directory not found");
        }

        /* Build requested directory */
        if (!path.contains(rootPath)) {
            // Check if path has correct root
            path = rootPath;
        }

        File requestDirectory = new File(path); // Build
        if (!requestDirectory.exists() || !requestDirectory.isDirectory()) {
            // Check if present
            res.sendError(HttpServletResponse.SC_NOT_FOUND, "Directory not found");
        }

        /* Get user files */
        List <LocalFile> files = new ArrayList<>();
        for (File file : requestDirectory.listFiles()) {
            files.add(new LocalFile(file));
        }

        /* Build previous directory */
        String previousPath = requestDirectory
                .toPath()
                .getParent()
                .toString()
                .replace("\\", "/");

        { // Debug
            System.out.printf("\n\nprev path: %s\n\n", previousPath);
        }

        if (!previousPath.contains(rootPath)) {
            // Check if path has correct root
            previousPath = rootPath;
        }

        { // Debug
            System.out.printf("\n\npath: %s\nprevpath:%s\nreqpath: %s",
                    path, previousPath, requestDirectory.toString());
        }
        req.setAttribute("previousPath", previousPath);
        req.setAttribute("currentPath", path);
        req.setAttribute("Items", files);

        req.getRequestDispatcher("/page.jsp").forward(req, res);
    }
}
