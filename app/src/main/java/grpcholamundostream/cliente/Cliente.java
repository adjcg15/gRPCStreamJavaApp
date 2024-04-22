package grpcholamundostream.cliente;

import com.proto.saludo.SaludoServiceGrpc;
import com.proto.saludo.Holamundo.FileRequest;
import com.proto.saludo.Holamundo.SaludoRequest;
import com.proto.saludo.Holamundo.SaludoResponse;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Cliente {
    public static void main(String[] args) {
        String host = "localhost";

        int port = 8080;

        ManagedChannel ch = ManagedChannelBuilder
            .forAddress(host, port)
            .usePlaintext()
            .build();
        
        // saludarUnario(ch);

        // saludarStream(ch);

        getBigFile(ch);

        System.out.println("Apagando...");
        ch.shutdown();
    }

    public static void saludarUnario(ManagedChannel ch) {
        SaludoServiceGrpc.SaludoServiceBlockingStub stub = SaludoServiceGrpc.newBlockingStub(ch);

        SaludoRequest request = SaludoRequest.newBuilder().setNombre("Angel").build();

        SaludoResponse response = stub.saludo(request);

        System.out.println("Respuesta RPC: " + response.getResultado());
    }

    public static void saludarStream(ManagedChannel ch) {
        SaludoServiceGrpc.SaludoServiceBlockingStub stub = SaludoServiceGrpc.newBlockingStub(ch);

        SaludoRequest request = SaludoRequest.newBuilder().setNombre("Angel").build();

        stub.saludoStream(request).forEachRemaining(response -> {
            System.out.println("Respuesta RPC: " + response.getResultado());
        });
    }

    public static void getBigFile(ManagedChannel ch) {
        SaludoServiceGrpc.SaludoServiceBlockingStub stub = SaludoServiceGrpc.newBlockingStub(ch);

        FileRequest request = FileRequest.newBuilder().setName("archivote").build();

        stub.getBigFile(request).forEachRemaining(response -> {
            System.out.println(response.getData());
        });
    }
}
