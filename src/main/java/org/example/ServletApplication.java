package org.example;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import org.example.db.SessionUtil;

@WebListener
public class ServletApplication implements ServletContextListener {
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        SessionUtil.closeSessionFactory();
        AbandonedConnectionCleanupThread.checkedShutdown();
    }
}
