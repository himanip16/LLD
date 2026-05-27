package splitswise.strategy;

import splitswise.model.Split;
import splitswise.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PercentageSplitStrategy implements SplitStrategy {
    @Override
    public List<Split> split(double totalAmount, List<User> participants, Map<User, Double> metadata) {
        double totalPercentage = 0;
        for (double p : metadata.values()) {
            totalPercentage += p;
        }
        if (Math.abs(totalPercentage - 100.0) > 0.01) {
            throw new IllegalArgumentException("Total percentage must equal 100%");
        }

        List<Split> splits = new ArrayList<>();
        for (User user : participants) {
            double percentage = metadata.get(user);
            double share = (totalAmount * percentage) / 100.0;
            splits.add(new Split(user, share));
        }
        return splits;
    }
}