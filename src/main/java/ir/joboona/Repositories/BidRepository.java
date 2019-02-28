package ir.joboona.Repositories;

import Solutions.Data.ModelRepository;
import ir.joboona.Models.Bid;
import ir.joboona.Models.Project;

import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class BidRepository implements ModelRepository<Bid> {

    private static BidRepository instance;
    private Set<Bid> bids;

    private BidRepository() {
        bids = new HashSet<>();
    }

    public static BidRepository getInstance() {
        if (instance == null)
            instance = new BidRepository();
        return instance;
    }

    @Override
    public Bid save(Bid bid) {
        bids.add(bid);
        return bid;
    }

    @Override
    public Set<Bid> getAll() {
        return bids;
    }


    public Set<Bid> findAllByProject(Project project) {
        return bids.stream().filter(bid -> bid.getProject().equals(project)).collect(toSet());
    }

}
