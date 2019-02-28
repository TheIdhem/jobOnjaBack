package ir.joboona.Repositories;

import Solutions.Data.EntityRepository;
import ir.joboona.Models.Knowledge;

import java.util.HashSet;
import java.util.Set;

public class KnowledgeRepository implements EntityRepository<Knowledge, String> {

    private static KnowledgeRepository instance;

    private Set<Knowledge> knowledgeSet;

    private KnowledgeRepository() {
        knowledgeSet = new HashSet<>();
    }

    public static KnowledgeRepository getInstance() {
        if (instance == null)
            instance = new KnowledgeRepository();
        return instance;
    }

    @Override
    public Knowledge save(Knowledge o) {
        knowledgeSet.add(o);
        return o;
    }

    public void saveAll(Set<Knowledge> knowledgeSet) {
        this.knowledgeSet.addAll(knowledgeSet);
    }

    @Override
    public Set<Knowledge> getAll() {
        return knowledgeSet;
    }
}
