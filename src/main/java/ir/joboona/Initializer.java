package ir.joboona;

import Solutions.Core.SolutionsApplication;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Initializer implements ServletContextListener {
    private SolutionsApplication solutionsApplication;

    public void contextInitialized(ServletContextEvent event) {
        // Do stuff during webapp's startup.
        try {
            String[] args = new String[100];
            solutionsApplication = SolutionsApplication.getInstance();
            solutionsApplication.run(args);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        if (solutionsApplication != null)
            solutionsApplication.tearDown();
    }
}