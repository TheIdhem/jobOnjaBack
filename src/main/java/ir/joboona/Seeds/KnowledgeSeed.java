package ir.joboona.Seeds;

import Solutions.Core.ApplicationRunner.ApplicationRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import ir.joboona.HttpClient.HttpGetClient;
import ir.joboona.Models.Knowledge;
import ir.joboona.Repositories.KnowledgeRepository;

import java.util.Set;

public class KnowledgeSeed implements ApplicationRunner {

    private final KnowledgeRepository knowledgeRepository = KnowledgeRepository.getInstance();

    @Override
    public void run() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();

        Set<Knowledge> knowledges = objectMapper.
                readValue(HttpGetClient.getInstance().getRequest("http://142.93.134.194:8000/joboonja/skill", 1000),
                        typeFactory.constructCollectionType(Set.class, Knowledge.class));

        knowledgeRepository.saveAll(knowledges);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
