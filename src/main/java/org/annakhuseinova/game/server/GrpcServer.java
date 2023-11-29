package org.annakhuseinova.game.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServer {

    public static void main(String[] args) throws InterruptedException, IOException {
        Server server = ServerBuilder.forPort(6565)
                .addService(new GameService()) // adding webservice to the server
                .build();

        server.start();

        // awaitTermination() - makes the server wait for the manual termination. Otherwise - the server will stop
        // listening once the main method completes execution
        server.awaitTermination();

    }
}
