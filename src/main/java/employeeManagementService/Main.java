package employeeManagementService;

/**
 * 4. Driver class to execute the end-to-end flow.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== INITIALIZING EMPLOYEE MANAGEMENT SYSTEM ===\n");
        OrganizationService orgService = new OrganizationServiceImpl();

        // Step 1: Add Employees
        System.out.println("--- 1. Building Initial Hierarchy ---");
        orgService.addEmployee("E100", "Alice (CEO)", null);
        orgService.addEmployee("E101", "Bob (VP Eng)", "E100");
        orgService.addEmployee("E102", "Charlie (VP Product)", "E100");
        orgService.addEmployee("E103", "David (Tech Lead)", "E101");
        orgService.addEmployee("E104", "Eve (Software Engineer)", "E103");

        System.out.println("Initial tree created successfully.");

        // Step 2: Test counting reports
        System.out.println("\n--- 2. Testing Report Counts ---");
        System.out.println("Reports under Bob (VP Eng): " + orgService.getTotalReportsCount("E101"));
        // Bob has David and Eve. Output: 2

        System.out.println("Reports under Alice (CEO): " + orgService.getTotalReportsCount("E100"));
        // Alice has Bob, Charlie, David, and Eve. Output: 4

        // Step 3: Getting an employee's manager
        System.out.println("\n--- 3. Testing Manager Lookup ---");
        Employee bobsManager = orgService.getManager("E101");
        System.out.println("Bob's Manager is: " + bobsManager); // Output: Alice (CEO) (E100)

        Employee evesManager = orgService.getManager("E104");
        System.out.println("Eve's Manager is: " + evesManager); // Output: David (Tech Lead) (E103)

        // Step 4: Move Eve to a new manager (from David to Charlie)
        System.out.println("\n--- 4. Moving Eve under Charlie ---");
        orgService.changeManager("E104", "E102");
        System.out.println("Eve's new manager is: " + orgService.getManager("E104"));
        // Output: Charlie (VP Product) (E102)

        // Verify the report counts changed correctly
        System.out.println("\n--- 5. Verifying Counts After Moving Eve ---");
        System.out.println("Reports under Bob (Old manager's parent): " + orgService.getTotalReportsCount("E101"));
        // Bob now only has David. Output: 1

        System.out.println("Reports under Charlie (New manager): " + orgService.getTotalReportsCount("E102"));
        // Charlie now has Eve. Output: 1

        System.out.println("Reports under Alice (CEO): " + orgService.getTotalReportsCount("E100"));
        // The total count under CEO remains exactly 4.

        // Step 5: Test Cycle Prevention
        System.out.println("\n--- 6. Testing Cycle Prevention Edge Case ---");
        try {
            // Attempting to move Alice (CEO) to report to David (Tech Lead)
            orgService.changeManager("E100", "E103");
        } catch (IllegalArgumentException e) {
            System.out.println("Caught Expected Exception: " + e.getMessage());
        }
    }
}