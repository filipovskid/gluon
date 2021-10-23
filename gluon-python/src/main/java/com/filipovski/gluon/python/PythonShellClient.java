package com.filipovski.gluon.python;

import com.filipovski.gluon.python.proto.ExecutionRequest;
import com.filipovski.gluon.python.proto.ExecutionResponse;
import com.filipovski.gluon.python.proto.PythonShellDriverGrpc;
import com.filipovski.gluon.python.proto.PythonShellDriverGrpc.PythonShellDriverBlockingStub;
import com.filipovski.gluon.python.proto.PythonShellDriverGrpc.PythonShellDriverStub;
import io.grpc.Context;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class PythonShellClient {

    private final Logger logger = LogManager.getLogger(PythonShellClient.class);

    private final ManagedChannel channel;

    private final PythonShellDriverBlockingStub blockingStub;

    private final PythonShellDriverStub asyncStub;

    public PythonShellClient(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        blockingStub = PythonShellDriverGrpc.newBlockingStub(channel).withWaitForReady();
        asyncStub = PythonShellDriverGrpc.newStub(channel).withWaitForReady();
    }

    public void execute(String code, Consumer<ExecutionResponse> executionOutputHandler) {
        ExecutionRequest request = ExecutionRequest.newBuilder()
                .setCode(code)
                .build();
        Context ctx = Context.current().fork();

        ctx.run(() -> {
            this.asyncStub.execute(request, new StreamObserver<ExecutionResponse>() {
                @Override
                public void onNext(ExecutionResponse executionResponse) {
                    executionOutputHandler.accept(executionResponse);
                }

                @Override
                public void onError(Throwable throwable) {
                    logger.error("An error occurred while consuming python shell output stream [{}].",
                            throwable.toString());
                }

                @Override
                public void onCompleted() {
                    logger.info("Python shell output stream consumed !");
                }
            });
        });
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(2, TimeUnit.SECONDS);
    }
}
