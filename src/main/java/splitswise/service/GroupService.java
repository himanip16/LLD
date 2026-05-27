package splitswise.service;


import splitswise.model.Group;
import splitswise.model.SplitType;
import splitswise.model.User;
import splitswise.repository.GroupRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GroupService {
    private GroupRepository groupRepository;
    private ExpenseService expenseService;
    private DebtSimplificationService debtSimplificationService;

    public GroupService(GroupRepository groupRepository, ExpenseService expenseService, DebtSimplificationService debtSimplificationService) {
        this.groupRepository = groupRepository;
        this.expenseService = expenseService;
        this.debtSimplificationService = debtSimplificationService;
    }

    public String createGroup(String name, List<User> members) {
        String id = UUID.randomUUID().toString();
        Group group = new Group(id, name, members);
        groupRepository.save(group);
        return id;
    }

    public void addExpense(String groupId, String description, double amount, User paidBy, List<User> participants, SplitType splitType, Map<User, Double> metadata) {
        Group group = groupRepository.findById(groupId);
        if (group == null) throw new IllegalArgumentException("Group not found");
        expenseService.addExpense(group, description, amount, paidBy, participants, splitType, metadata);
    }

    public void simplifyGroupDebts(String groupId) {
        Group group = groupRepository.findById(groupId);
        if (group == null) throw new IllegalArgumentException("Group not found");
        debtSimplificationService.simplifyDebts(group);
    }

    public Group getGroupDetails(String groupId) {
        return groupRepository.findById(groupId);
    }
}