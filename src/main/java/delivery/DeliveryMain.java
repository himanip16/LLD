//package delivery;
//
//import delivery.context.DeliveryWorkflow;
//import delivery.domain.PackageDetails;
//import delivery.domain.StateTransitionEvent;
//import delivery.factory.ConcreteStateFactory;
//import delivery.factory.StateFactory;
//import delivery.observer.WorkflowObserver;
//import delivery.service.OtpVerificationService;
//
//public class DeliveryMain {
//
//    public static void main(String[] args) {
//        // 1. Initializing clean, stateless, decoupled primitives
//        StateFactory stateFactory = new ConcreteStateFactory();
//        OtpVerificationService otpService = (packageId, inputOtp) -> "9988".equals(inputOtp);
//
//        WorkflowObserver operationalLog = new WorkflowObserver() {
//            @Override public void onStateChange(String pkgId, String state, PackageDetails d) {}
//            @Override
//            public void onActionFailure(String pkgId, String action, String reason) {
//                System.out.println("[AUDIT REJECTION] Action: " + action + " failed. Reason: " + reason);
//            }
//        };
//
//        // Poisonous observer to explicitly simulate fault isolation
//        WorkflowObserver unstableWebhookNotifier = new WorkflowObserver() {
//            @Override
//            public void onStateChange(String pkgId, String state, PackageDetails d) {
//                if ("SCANNED".equals(state)) {
//                    throw new RuntimeException("Network timeout exception connecting to Slack.");
//                }
//            }
//            @Override public void onActionFailure(String pkgId, String action, String reason) {}
//        };
//
//        PackageDetails highValuePackage = new PackageDetails("PKG-777", "+15559999", true);
//        DeliveryWorkflow workflow = new DeliveryWorkflow(highValuePackage, otpService, stateFactory);
//
//        workflow.registerObserver(operationalLog);
//        workflow.registerObserver(unstableWebhookNotifier);
//
//        // 2. Simulating sequential runner action chain out in the field
//        System.out.println("Beginning Track Execution: " + workflow.getCurrentStateName());
//
//        workflow.scanPackage("PKG-777");
//
//        // Invalid input attempt
//        workflow.verifyOtp("0000");
//        // Secure identity verification matching correct hash
//        workflow.verifyOtp("9988");
//
//        // Final secure visual capture step
//        workflow.capturePhoto("https://s3.aws.storage/proofs/pkg-777.jpg");
//
//        System.out.println("Terminal Track Execution State: " + workflow.getCurrentStateName());
//
//        // 3. Inspecting Event Sourced Audit Ledger Records
//        System.out.println("\n--- COMPILING ENTERPRISE EVENT SOURCED RECORDS ---");
//        for (StateTransitionEvent event : workflow.getAuditHistory()) {
//            System.out.printf("[%s] Transitioned from %s to %s | Context: %s%n",
//                    event.getTimestamp(), event.getFromState(), event.getToState(), event.getMetadata());
//        }
//    }
//}
//
