import model.Contacto;
import model.NotificacionWhatsApp;
import model.TwilioWhatsApp;
import model.WhatsApp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;


public class TestNotificacionWhatsApp {
  private WhatsApp apiWhatapp;
  private NotificacionWhatsApp noti;

  @BeforeEach
  void initServicioDistancia() {
    apiWhatapp = mock(WhatsApp.class);
    noti = new NotificacionWhatsApp(apiWhatapp);
  }
  @Test
  public void apiWhatappOK()  {
    Contacto contacto =new Contacto("pepe", "11", null);
    String asunto = "Asunto del mensaje";
    noti.enviarNotificacion("", contacto,asunto);
    verify(apiWhatapp, times(1)).enviar(anyString(), anyString());
  }

  //--------------------------
  //SI SE CORRE ESTE TEST SE COBRA UN DOLAR!!!
  //---------------------------
  @Test
  @Disabled
  public void pruebaTest() {
    TwilioWhatsApp api = new TwilioWhatsApp();
    NotificacionWhatsApp noti = new NotificacionWhatsApp(api);
    String asunto = "Asunto del mensaje";
    Contacto contacto =new Contacto("pepe", "+5491167097068", null);
    noti.enviarNotificacion("hola", contacto,asunto);

  }

/*  @Test
  public void apiWhatappError()  {
   doThrow(new Exception()).when(apiWhatapp).enviar(any(), any());
    Contacto contacto =new Contacto("pepe", "11", null);
    WhatappException thrown = Assertions
        .assertThrows(WhatappException.class, () -> {
          noti.enviarNotificacion("", contacto);
        });
    Assertions
        .assertEquals("Ocurrio un error al enviar el msj",
            thrown.getMessage());
    verify(apiWhatapp, times(1)).enviar(anyString(), anyString());
  }*/
}