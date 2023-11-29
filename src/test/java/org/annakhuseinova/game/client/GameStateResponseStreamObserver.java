package org.annakhuseinova.game.client;

import com.google.common.util.concurrent.Uninterruptibles;
import io.grpc.stub.StreamObserver;
import org.annakhuseinova.game.proto.Die;
import org.annakhuseinova.game.proto.GameState;
import org.annakhuseinova.game.proto.Player;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class GameStateResponseStreamObserver implements StreamObserver<GameState> {

    private CountDownLatch latch;
    private StreamObserver<Die> clientDieStreamObserver;

    public GameStateResponseStreamObserver(CountDownLatch latch){
        this.latch = latch;
    }

    public void setClientDieStreamObserver(StreamObserver<Die> clientDieStreamObserver){
        this.clientDieStreamObserver = clientDieStreamObserver;
    }

    @Override
    public void onNext(GameState value) {
        List<Player> list = value.getPlayerList();
        list.forEach(player -> {
            System.out.println(player.getName() + " : " + player.getPosition());
        });
        boolean isGameOver = list.stream().anyMatch(player -> player.getPosition() == 100);
        if (isGameOver){
            this.clientDieStreamObserver.onCompleted();
        } else {
            Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
            this.roll();
        }
        System.out.println("----------------------------");
    }

    @Override
    public void onError(Throwable t) {
        this.latch.countDown();
    }

    @Override
    public void onCompleted() {
        this.latch.countDown();
    }

    public void roll(){
        int dieValue = ThreadLocalRandom.current().nextInt(1, 7);
        Die die = Die.newBuilder()
                .setValue(dieValue)
                .build();
        this.clientDieStreamObserver.onNext(die);
    }
}
