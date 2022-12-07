package model;

import controllers.NotificacionController;

public class NotificacionApp {

  public static void main(String[] arg) {
    NotificacionController notificacionController = new NotificacionController();
    notificacionController.enviarNotificaciones(null, null);
  }

}