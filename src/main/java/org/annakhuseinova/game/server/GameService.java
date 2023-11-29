package org.annakhuseinova.game.server;

import io.grpc.stub.StreamObserver;
import org.annakhuseinova.game.proto.Die;
import org.annakhuseinova.game.proto.GameServiceGrpc;
import org.annakhuseinova.game.proto.GameState;
import org.annakhuseinova.game.proto.Player;

public class GameService extends GameServiceGrpc.GameServiceImplBase {

    @Override
    public StreamObserver<Die> roll(StreamObserver<GameState> responseObserver) {
        Player client = Player.newBuilder()
                .setName("Client")
                .setPosition(0)
                .build();
        Player server = Player.newBuilder()
                .setName("Server")
                .setPosition(0)
                .build();
        return new ClientDieStreamObserver(client, server, responseObserver);
    }
}
