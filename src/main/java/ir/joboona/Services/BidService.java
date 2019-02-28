package ir.joboona.Services;

import Solutions.Data.Exceptions.EntityNotFound;
import ir.joboona.Exceptions.BidExceptions.BudgetOverflow;
import ir.joboona.Exceptions.BidExceptions.IllegalBidException;
import ir.joboona.Exceptions.BidExceptions.InsufficientSkill;
import ir.joboona.Models.Bid;
import ir.joboona.Repositories.BidRepository;
import ir.joboona.Repositories.ProjectRepository;
import ir.joboona.Repositories.UserRepository;

public class BidService {

    private static BidService instance;
    private BidRepository bidRepository = BidRepository.getInstance();
    private ProjectRepository projectRepository = ProjectRepository.getInstance();
    private UserRepository userRepository = UserRepository.getInstance();

    private BidService() {
    }

    public static BidService getInstance() {
        if (instance == null)
            instance = new BidService();
        return instance;
    }

    public Bid create(Bid bid) throws IllegalBidException, EntityNotFound {

        if (!bid.getProject().sufficientSkills(bid.getBiddingUser().getSkills()))
            throw new InsufficientSkill();

        if (bid.getBidAmount() > bid.getProject().getBudget())
            throw new BudgetOverflow();
        bid.getProject().addBid(bid);
        return bidRepository.save(bid);
    }
}
