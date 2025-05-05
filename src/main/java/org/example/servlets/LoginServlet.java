package org.example.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.models.JsonReader;
import org.example.models.User;
import org.example.service.UserService;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name="Login", value="/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        /* Set cors headers to avoid CORS error */
        setCorsHeaders(res);
        /* Set content type */
        res.setContentType("application/json");

        try {
            /* Transform json */
            JSONObject json = JsonReader.readFromRequest(req);

            /* Divide into parts and save in db */
            String username = json.getString("username");
            String password = json.getString("password");

            UserService userService = new UserService();
            /* Check if user is present */
            User found = userService.found(username);
            if (found == null) {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            /* Check if password is correct */
            if (! password.equals(found.getPassword())) {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            /* No needed to close connection - crash
            * follows after the line below; */
//            userService.closeConnection();

            /* Setting SESSION ID to Cookies */
            HttpSession session = req.getSession();
            /* Saving 'username' to session */
            session.setAttribute("username", username);

            /* Send response-code as "Ok" */
            res.setStatus(HttpServletResponse.SC_OK);

            /* build userId to json format
             *  example: { "success": BOOLEAN } **/
            JSONObject result = new JSONObject();
            result.put("success", true);

            /* return json */
            PrintWriter out = res.getWriter();
            out.print(result);
            out.flush();
        } catch (JSONException e) {
            System.out.println(e);
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse res) {
        setCorsHeaders(res);
        res.setStatus(200);
    }

    private void setCorsHeaders(HttpServletResponse res) {
        res.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type");
        res.setHeader("Access-Control-Allow-Credentials", "true");
    }
}
