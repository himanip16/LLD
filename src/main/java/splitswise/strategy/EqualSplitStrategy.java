package splitswise.strategy;


import splitswise.model.Split;
import splitswise.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EqualSplitStrategy implements SplitStrategy {
    @Override
    public List<Split> split(double totalAmount, List<User> participants, Map<User, Double> metadata) {
        List<Split> splits = new ArrayList<>();
        double share = totalAmount / participants.size();
        for (User user : participants) {
            splits.add(new Split(user, share));
        }
        return splits;
    }
}
