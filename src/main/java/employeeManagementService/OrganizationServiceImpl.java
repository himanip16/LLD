package employeeManagementService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 3. Thread-safe implementation of organizational management.
 */
class OrganizationServiceImpl implements OrganizationService {
    // Stores all nodes for O(1) direct node access
    private final Map<String, Employee> employeeRegistry = new ConcurrentHashMap<>();

    @Override
    public synchronized void addEmployee(String id, String name, String managerId) {
        if (employeeRegistry.containsKey(id)) {
            throw new IllegalArgumentException("Employee with ID " + id + " already exists.");
        }

        Employee newEmployee = new Employee(id, name);

        if (managerId != null && !managerId.isBlank()) {
            Employee manager = employeeRegistry.get(managerId);
            if (manager == null) {
                throw new IllegalArgumentException("Manager with ID " + managerId + " does not exist.");
            }
            // Link parent and child bidirectionally
            manager.addSubordinate(newEmployee);
            newEmployee.setManager(manager);
        }

        // Register in our global hash map
        employeeRegistry.put(id, newEmployee);
    }

    @Override
    public Employee getManager(String employeeId) {
        Employee emp = employeeRegistry.get(employeeId);
        if (emp == null) {
            throw new IllegalArgumentException("Employee with ID " + employeeId + " does not exist.");
        }
        return emp.getManager();
    }

    @Override
    public synchronized void changeManager(String employeeId, String newManagerId) {
        Employee emp = employeeRegistry.get(employeeId);
        Employee newManager = employeeRegistry.get(newManagerId);

        if (emp == null || newManager == null) {
            throw new IllegalArgumentException("Employee or New Manager ID does not exist.");
        }

        if (emp.getId().equals(newManagerId)) {
            throw new IllegalArgumentException("An employee cannot be their own manager.");
        }

        // Cycle Detection: Make sure the new manager isn't a direct/indirect report of the moving employee
        Employee ancestorCheck = newManager;
        while (ancestorCheck != null) {
            if (ancestorCheck.getId().equals(employeeId)) {
                throw new IllegalArgumentException("Error: Cyclic structure detected! " +
                        "Cannot move a manager to report under their own direct or indirect subordinate.");
            }
            ancestorCheck = ancestorCheck.getManager();
        }

        // 1. Decouple from existing manager
        Employee oldManager = emp.getManager();
        if (oldManager != null) {
            oldManager.removeSubordinate(emp);
        }

        // 2. Link to the new manager
        newManager.addSubordinate(emp);
        emp.setManager(newManager);
    }

    @Override
    public int getTotalReportsCount(String managerId) {
        Employee manager = employeeRegistry.get(managerId);
        if (manager == null) {
            throw new IllegalArgumentException("Manager with ID " + managerId + " does not exist.");
        }
        return countReportsRecursive(manager);
    }

    private int countReportsRecursive(Employee node) {
        int count = 0;
        for (Employee subordinate : node.getSubordinates()) {
            count += 1 + countReportsRecursive(subordinate);
        }
        return count;
    }
}
