package ir.joboona.Presentation.Controllers;

import Solutions.Core.Dispatcher.RequestMethod;
import Solutions.Presentation.Controller.HtmlController;
import Solutions.Presentation.Controller.PathVariable;
import Solutions.Presentation.Controller.RequestMapping;
import ir.joboona.Models.User;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


@HtmlController(basePath = "/user")
public class UserController {

    @RequestMapping(path = "/{userId}", method = RequestMethod.GET, template = "user.html")
    public void get(@PathVariable(value = "userId") User user, Document document) {

        Element ul = document.selectFirst("ul");
        ul.child(0).append(user.getId());
        ul.child(1).append(user.getFirstName());
        ul.child(2).append(user.getJobTitle());
        ul.child(3).append(user.getJobTitle());
        ul.child(4).append(user.getBio());

    }
}
