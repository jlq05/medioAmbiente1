package domain.services.distancia;

import domain.services.distancia.entities.RespuestaDistancia;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ServicioDistanciaApiInterface {
  @GET("distancia")
  Call<RespuestaDistancia> distancia(
      @Header("Authorization") String token,
      @Query("localidadOrigenId") int localidadOrigenId,
      @Query("calleOrigen") String calleOrigen,
      @Query("alturaOrigen") int alturaOrigen,
      @Query("localidadDestinoId") int localidadDestinoId,
      @Query("calleDestino") String calleDestino,
      @Query("alturaDestino") int alturaDestino
  );
}
