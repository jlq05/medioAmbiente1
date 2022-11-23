package model;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

public class TwilioWhatsApp implements WhatsApp {
  public static final String ACCOUNT_SID = "xx";
  public static final String AUTH_TOKEN = "xx";

  public static  final  String CEL_FROM = "+14155238886";

  public void enviar(String msj, String cel) {
    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    Message.creator(
            new com.twilio.type.PhoneNumber("whatsapp:" + cel),
            new com.twilio.type.PhoneNumber("whatsapp:" + CEL_FROM),
            msj)
        .create();
  }
}
