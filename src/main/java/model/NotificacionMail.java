package model;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class NotificacionMail implements Notificador {

  @Override
  public void enviarNotificacion(String mensaje, Contacto contacto,String asuntoMail) {
    
    try {
      // Propiedades de la conexión
      Properties props = new Properties();
      props.setProperty("mail.smtp.host", "smtp.gmail.com");
      props.setProperty("mail.smtp.starttls.enable", "true");
      props.setProperty("mail.smtp.port", "587");
      props.setProperty("mail.smtp.user", "diseniodesistemas2022@gmail.com");
      props.setProperty("mail.smtp.auth", "true");

      // Preparamos la sesion
      Session session = Session.getDefaultInstance(props);

      // Construimos el mensaje
      MimeMessage message = new MimeMessage(session);
      message.setFrom(new InternetAddress("diseniodesistemas2022@gmail.com"));
      message.addRecipient(
          Message.RecipientType.TO,
          new InternetAddress(contacto.getMail()));
      message.setSubject(asuntoMail);
      message.setText(
          "Este mail es para probar que funciona el envio de mail");

      // Lo enviamos.
      Transport t = session.getTransport("smtp");
      t.connect("diseniodesistemas2022@gmail.com", "asdf1234@");
      t.sendMessage(message, message.getAllRecipients());

      // Cierre.
      t.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
