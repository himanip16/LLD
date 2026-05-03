package employeeManagementService;

/**
 * 2. Service Interface defining the LLD operations.
 */
interface OrganizationService {
    void addEmployee(String id, String name, String managerId);

    int getTotalReportsCount(String managerId);

    Employee getManager(String employeeId);

    void changeManager(String employeeId, String newManagerId);
}
