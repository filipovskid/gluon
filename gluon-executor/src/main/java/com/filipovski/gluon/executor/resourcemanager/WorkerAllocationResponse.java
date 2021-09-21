package com.filipovski.gluon.executor.resourcemanager;

/**
 * Class carrying the status of worker allocation along with the reason/message
 * explaining the allocation status.
 */

public class WorkerAllocationResponse {

    private Status status;

    private String message;

    private WorkerAllocationResponse(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public boolean isSuccessful() {
        return status == Status.SUCCESS;
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public static WorkerAllocationResponse createSuccessfulResponse() {
        return createSuccessfulResponse("Worker allocated successfully!");
    }

    public static WorkerAllocationResponse createSuccessfulResponse(String message) {
        return new WorkerAllocationResponse(Status.SUCCESS, message);
    }

    public static WorkerAllocationResponse createFailedResponse(String reason) {
        return new WorkerAllocationResponse(Status.FAILURE, reason);
    }

    public enum Status {
        SUCCESS,
        FAILURE
    }
}
