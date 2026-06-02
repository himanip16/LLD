package lockerManagement.service;

import lockerManagement.model.Package;
public class ConsoleReturnService implements ReturnService {

    public void initiateReturn(Package pkg) {
        System.out.println("RETURN: Initiating return for package " + pkg.packageId
                + " belonging to customer " + pkg.customerId);
    }
}
