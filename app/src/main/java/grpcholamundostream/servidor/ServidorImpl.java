package grpcholamundostream.servidor;

import java.util.Scanner;

import com.proto.saludo.SaludoServiceGrpc;
import com.proto.saludo.Holamundo.FileRequest;
import com.proto.saludo.Holamundo.FileResponse;
import com.proto.saludo.Holamundo.SaludoRequest;
import com.proto.saludo.Holamundo.SaludoResponse;

import io.grpc.stub.StreamObserver;

public class ServidorImpl extends SaludoServiceGrpc.SaludoServiceImplBase {
    @Override
    public void saludo(SaludoRequest request, StreamObserver<SaludoResponse> responseObserver) {
        SaludoResponse response = SaludoResponse.newBuilder().setResultado("Hola " + request.getNombre()).build();

        responseObserver.onNext(response);

        responseObserver.onCompleted();
    }

    @Override
    public void saludoStream(SaludoRequest request, StreamObserver<SaludoResponse> responseObserver) {
        for (int i = 0; i <= 10; i++) {
            SaludoResponse response = SaludoResponse.newBuilder()
                .setResultado("Hola " + request.getNombre() + " por " + i + " vez").build();
    
            responseObserver.onNext(response);
        }

        responseObserver.onCompleted();
    }

    @Override
    public void getBigFile(FileRequest request, StreamObserver<FileResponse> responseObserver) {
        try (Scanner scanner = new Scanner(ServidorImpl.class.getResourceAsStream("/" + request.getName() + ".csv"))) {
            while (scanner.hasNextLine()) {
                System.out.println("Procesando archivo...");
                FileResponse response = FileResponse.newBuilder()
                    .setData(scanner.nextLine()).build();

                responseObserver.onNext(response);
            }
            System.out.println("Archivo procesado!");
        } catch(Exception e) {
            FileResponse response = FileResponse.newBuilder()
                .setData("Ocurrió un error al recuperar el archivo").build();

            responseObserver.onNext(response);
        }

        responseObserver.onCompleted();
    }
}
