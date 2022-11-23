package domain.services.distancia;

import domain.services.distancia.entities.RespuestaDistancia;
import domain.services.distancia.entities.Ubicacion;
import java.io.IOException;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServicioDistancia implements ServicioDistanciaInterface {
  private static ServicioDistancia instancia = new ServicioDistancia();
  private static final String urlAPI = "https://ddstpa.com.ar/api/";
  private static final String token = "Bearer lN+M86uBdrbwOo1Nkl7D23+eXz8uBa8ukEoPXEqMKZE=";
  private final Retrofit retrofit;

  private ServicioDistancia() {
    this.retrofit = new Retrofit.Builder()
        .baseUrl(urlAPI)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
  }

  public static ServicioDistancia getInstancia() {
    if (instancia == null) {
      instancia = new ServicioDistancia();
    }
    return instancia;
  }

  @Override
  public float obtenerDistancia(
      Ubicacion origen,
      Ubicacion destino
  ) throws IOException {
    ServicioDistanciaApiInterface servicioDistancia =
        this.retrofit
            .create(ServicioDistanciaApiInterface.class);
    Call<RespuestaDistancia> requestDistancia =
        servicioDistancia
            .distancia(
                token,
                origen.getLocalidadId(), origen.getCalle(), origen.getAltura(),
                destino.getLocalidadId(), destino.getCalle(), destino.getAltura()
            );
    Response<RespuestaDistancia> respuestaDistancia = requestDistancia.execute();
    return respuestaDistancia.body() != null
        ? respuestaDistancia.body().getValorFloat()
        : -1;
  }
}