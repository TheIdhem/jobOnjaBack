package ir.joboona.Seeds;

import Solutions.Core.ApplicationRunner.ApplicationRunner;
import Solutions.Data.EntityManager;
import ir.joboona.Models.Knowledge;
import ir.joboona.Models.User;
import ir.joboona.Models.UserSkill;
import ir.joboona.Services.UserService;
import org.springframework.core.Ordered;

import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.of;

public class DefaultUserSeed implements ApplicationRunner {

    private final EntityManager entityManager = EntityManager.getInstance();
    private final UserService userService = UserService.getInstance();

    @Override
    public void start() throws Exception {

        Knowledge html = entityManager.find(Knowledge.class, "HTML").get();
        Knowledge js = entityManager.find(Knowledge.class,"Javascript").get();
        Knowledge cpp = entityManager.find(Knowledge.class,"C++").get();
        Knowledge java = entityManager.find(Knowledge.class,"Java").get();
        User user = new User(
                "alish",
                "alish",
                "علی",
                "شریف زاده",
                of(new UserSkill(1, html, 5), new UserSkill(2, js, 4),
                        new UserSkill(3, cpp, 2), new UserSkill(4, java, 3)).collect(toSet()),
                "برنامه نویس وب",
                "روی سنگ قبرم بنویسید: خدابیامرز میخواست خیلی کار بکنه ولی پول نداشت",
                "https://imagesvc.timeincapp.com/v3/mm/image?url=https%3A%2F%2Ftimedotcom.files.wordpress.com%2F2015%2F04%2Fayatollah-khamenei.jpg&w=800&c=sc&poi=face&q=85"
        );

        User user1 = new User(
                "mryf",
                "mryf",
                "محمد رضا",
                "یزدانیفر",
                of(new UserSkill(5, html, 5), new UserSkill(6, js, 4),
                        new UserSkill(7, cpp, 2), new UserSkill(8, java, 3)).collect(toSet()),
                "برنامه نویس وب",
                "آبجکت",
                "https://imagesvc.timeincapp.com/v3/mm/image?url=https%3A%2F%2Ftimedotcom.files.wordpress.com%2F2015%2F04%2Fayatollah-khamenei.jpg&w=800&c=sc&poi=face&q=85"
        );

        User user2 = new User(
                "golnar",
                "golnar",
                "گلناز",
                "ادیب",
                of(new UserSkill(9, html, 5), new UserSkill(10, js, 4),
                        new UserSkill(11, cpp, 2), new UserSkill(12, java, 3)).collect(toSet()),
                "برنامه نویس وب",
                "عبداالله",
                "https://imagesvc.timeincapp.com/v3/mm/image?url=https%3A%2F%2Ftimedotcom.files.wordpress.com%2F2015%2F04%2Fayatollah-khamenei.jpg&w=800&c=sc&poi=face&q=85"
        );


        userService.registerUser(user);
        userService.registerUser(user1);
        userService.registerUser(user2);

    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
