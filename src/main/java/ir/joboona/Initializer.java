package ir.joboona;

import Solutions.Core.SolutionsApplication;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Initializer implements ServletContextListener {

    public void contextInitialized(ServletContextEvent event) {
        // Do stuff during webapp's startup.
        try {
            String[] args = new String[100];
            SolutionsApplication.getInstance().run(args);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
