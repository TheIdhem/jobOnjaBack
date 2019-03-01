package ir.joboona.Seeds;

import Solutions.Core.ApplicationRunner.ApplicationRunner;
import ir.joboona.Models.Knowledge;
import ir.joboona.Models.Skill;
import ir.joboona.Models.User;
import ir.joboona.Repositories.KnowledgeRepository;
import ir.joboona.Repositories.UserRepository;
import org.springframework.core.Ordered;

import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.of;

public class DefaultUserSeed implements ApplicationRunner {

    private final UserRepository userRepository = UserRepository.getInstance();

    private final KnowledgeRepository knowledgeRepository = KnowledgeRepository.getInstance();

    @Override
    public void run() {

        Knowledge html = knowledgeRepository.getById("HTML")
                .orElseGet(() -> knowledgeRepository.save(new Knowledge("HTML")));

        Knowledge js = knowledgeRepository.getById("Javascript")
                .orElseGet(() -> knowledgeRepository.save(new Knowledge("Javascript")));

        Knowledge cpp = knowledgeRepository.getById("C++")
                .orElseGet(() -> knowledgeRepository.save(new Knowledge("C++")));

        Knowledge java = knowledgeRepository.getById("Java")
                .orElseGet(() -> knowledgeRepository.save(new Knowledge("Java")));

        User user = new User(
                "1",
                "علی",
                "شریف زاده",
                of(new Skill(html, 5), new Skill(js, 4),
                        new Skill(cpp, 2), new Skill(java, 3)).collect(toSet()),
                "برنامه نویس وب",
                "روی سنگ قبرم بنویسید: خدابیامرز میخواست خیلی کار بکنه ولی پول نداشت",
                "https://imagesvc.timeincapp.com/v3/mm/image?url=https%3A%2F%2Ftimedotcom.files.wordpress.com%2F2015%2F04%2Fayatollah-khamenei.jpg&w=800&c=sc&poi=face&q=85"
        );

        userRepository.save(user);

    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
