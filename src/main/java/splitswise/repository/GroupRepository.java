package splitswise.repository;


import splitswise.model.Group;

public interface GroupRepository {
    Group findById(String id);
    void save(Group group);
}