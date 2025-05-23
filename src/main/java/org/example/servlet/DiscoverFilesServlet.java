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

@WebServlet(name="DiscoverFiles", value = "/files")
public class DiscoverFilesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException
    {
        String path = req.getParameter("path");
        if (path == null) {
            path = System.getProperty("user.home")
                    .replace("\\", "/");
        }

        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND, "Directory not found");
            return;
        }

        List<LocalFile> items = new ArrayList<>();
        for (File file : dir.listFiles()) {
            items.add(new LocalFile(file));
        }

        File prevDir = dir.toPath().getParent().toFile();
        String previousPath;

        if (prevDir.exists()) {
            previousPath = prevDir
                .getAbsolutePath()
                .replace("\\", "/");
        }
        else {
            previousPath = System.getProperty("user.home")
                    .replace("\\", "/");
        }

        req.setAttribute("previousPath", previousPath);
        req.setAttribute("currentPath", path);
        req.setAttribute("Items", items);

        req.getRequestDispatcher("page.jsp").forward(req, res);
    }
}
