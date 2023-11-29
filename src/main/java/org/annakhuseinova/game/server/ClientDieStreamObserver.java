package org.annakhuseinova.game.server;

import io.grpc.stub.StreamObserver;
import org.annakhuseinova.game.proto.Die;
import org.annakhuseinova.game.proto.GameState;
import org.annakhuseinova.game.proto.Player;

import java.util.concurrent.ThreadLocalRandom;

public class ClientDieStreamObserver implements StreamObserver<Die> {

    private StreamObserver<GameState> gameStateStreamObserver;
    private Player client;
    private Player server;

    public ClientDieStreamObserver(Player client,
                                   Player server, StreamObserver<GameState> responseObserver) {
        this.gameStateStreamObserver = responseObserver;
        this.client = client;
        this.server = server;
    }

    @Override
    public void onNext(Die die) {
        this.client = this.getNewPlayerPosition(this.client, die.getValue());
        if (this.client.getPosition() != 100){
            this.server = this.getNewPlayerPosition(this.server, getRandomBetweenZeroAndSix());
        }
        this.gameStateStreamObserver.onNext(this.getGameState());
    }

    @Override
    public void onError(Throwable t) {
        System.out.println(t.getMessage());
    }

    @Override
    public void onCompleted() {
        this.gameStateStreamObserver.onCompleted();
    }

    private GameState getGameState(){
        return GameState.newBuilder()
                .addPlayer(this.client)
                .addPlayer(this.server)
                .build();
    }

    private Player getNewPlayerPosition(Player player, int dieValue){
        int newPosition = player.getPosition() + dieValue;
        newPosition = SnakesAndLaddersMap.getPosition(newPosition);
        if (newPosition <= 100){
            player  = player.toBuilder().setPosition(newPosition).build();
        }
        return player;
    }

    private int getRandomBetweenZeroAndSix(){
        return ThreadLocalRandom.current().nextInt(1, 7);
    }
}
