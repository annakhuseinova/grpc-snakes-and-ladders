package org.annakhuseinova.game.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.annakhuseinova.game.proto.Die;
import org.annakhuseinova.game.proto.GameServiceGrpc;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameClientTest {

    private GameServiceGrpc.GameServiceStub asyncStub;

    @BeforeAll
    void setUp(){
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6565)
                .usePlaintext()
                .build();
        this.asyncStub = GameServiceGrpc.newStub(channel);
    }

    @Test
    public void clientGame() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        GameStateResponseStreamObserver responseStreamObserver = new GameStateResponseStreamObserver(latch);
        StreamObserver<Die> clientDieStreamObserver = this.asyncStub.roll(responseStreamObserver);
        responseStreamObserver.setClientDieStreamObserver(clientDieStreamObserver);
        responseStreamObserver.roll();
        latch.await();
    }
}
