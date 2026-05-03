package employeeManagementService;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 1. Domain Entity representing an Employee.
 * Maintains bidirectional references: parent pointer (manager) and child pointers (subordinates).
 */

@Setter
@Getter
public class Employee {
    private final String id;
    private final String name;
    private Employee manager; // Parent pointer
    private final List<Employee> subordinates; // Child pointers

    public Employee(String id, String name) {
        if (id == null || id.isBlank() || name == null || name.isBlank()) {
            throw new IllegalArgumentException("Employee ID and Name cannot be null or empty.");
        }
        this.id = id;
        this.name = name;
        this.subordinates = new ArrayList<>();
    }



    public void addSubordinate(Employee subordinate) {
        if (subordinate == null) {
            throw new IllegalArgumentException("Subordinate cannot be null.");
        }
        this.subordinates.add(subordinate);
    }

    public void removeSubordinate(Employee subordinate) {
        this.subordinates.remove(subordinate);
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, id);
    }
}

