package Solutions.Core.ApplicationRunner;

import org.springframework.core.Ordered;

/**
 * Classes implementing {@link ApplicationRunner} are executed according to their order {@link Ordered}
 */
public interface ApplicationRunner extends Ordered {

    void run() throws Exception;
}
