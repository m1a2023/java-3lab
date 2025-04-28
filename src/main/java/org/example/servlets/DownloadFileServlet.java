package org.example.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet(name="DownloadFile", value="/personal/download")
public class DownloadFileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException
    {
        String path = req.getParameter("path");
        File file = new File(path);

        if (!file.exists() || !file.isFile()) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found");
            return;
        }

        res.setContentType("application/octet-stream");
        res.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

        try (FileInputStream in = new FileInputStream(file);
             OutputStream out = res.getOutputStream())
        {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }
}
