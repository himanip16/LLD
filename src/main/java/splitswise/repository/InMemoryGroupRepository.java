package splitswise.repository;
import splitswise.exception.GroupNotFoundException;
import splitswise.model.Group;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryGroupRepository implements GroupRepository {
//    If two users add an expense to the same group at the same exact time,
//    HashMap.put() or Map.compute() operations will conflict, leading to corrupted data,
//    lost updates, or ConcurrentModificationExceptions.

//    FIX: If we want to keep it in-memory, we need to swap those out for ConcurrentHashMap
//    and use atomic operations (like .compute() or lock stripping). If we move to a database,
//    we would need to look into Optimistic Locking (using a @Version field) or
//    Pessimistic Locking (SELECT FOR UPDATE) on the Group/BalanceSheet entity.
private final ConcurrentHashMap<String, Group> groups = new ConcurrentHashMap<>();

    @Override
    public Group findById(String id) {
        Group group = groups.get(id);
        if (group == null) {
            throw new GroupNotFoundException(id);
        }
        return group;
    }

    @Override
    public void save(Group group) {
        if (group != null) {
            groups.put(group.getId(), group);
        }
    }
}