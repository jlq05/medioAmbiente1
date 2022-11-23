package model;

public class NotificacionWhatsApp implements Notificador {
  private WhatsApp apiWhatapp;

  public NotificacionWhatsApp(WhatsApp apiWhatapp) {
    this.apiWhatapp = apiWhatapp;
  }


  public void enviarNotificacion(String msj, Contacto contacto,String asunto) {
    try {
      this.apiWhatapp.enviar(msj, contacto.getTelefono());
    } catch (Exception ex) {
      throw  new WhatappException("Ocurrio un error al enviar el msj");
    }

  }

}