package MetodosSql;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import configs.ReadPropertyFile;



public class JavaMail {    

    private String usuario;
    private String pass;
    List<String> destinatarios;
	public String cuerpoMensaje;
	Properties props = ReadPropertyFile.getInstance().obtenerPropiedades();
	String asuntoError=props.getProperty("asuntoError");
	String asuntoOK=props.getProperty("asuntoOK");

    public JavaMail(String usuario, String pass,List<String> destinatarios){
        this.usuario=usuario;
        this.pass=pass;
        this.destinatarios=destinatarios;
    }

    private void enviaStartTLS(String from, List<String> to, String subject, String text){
        final String username = usuario;
        final String password = pass;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
          });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
          //  message.setRecipients(Message.RecipientType.TO,  InternetAddress.parse(to));
            for(int i=0;i<to.size();i++)
            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to.get(i)));
            message.setSubject(subject);
            message.setText(text);

            Transport.send(message);

            System.out.println("Enviado");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

	public void notificar(boolean todoOk) {
		
		if(todoOk) {
			enviaStartTLS(usuario, destinatarios, asuntoOK, cuerpoMensaje);
		}else {
			enviaStartTLS(usuario, destinatarios, asuntoError, cuerpoMensaje);
		}
		
		
	}

	public List<String> getDestinatarios() {
		return destinatarios;
	}

	public void setDestinatarios(List<String> destinatarios) {
		this.destinatarios = destinatarios;
	}

	public String getCuerpoMensaje() {
		return cuerpoMensaje;
	}

	public void setCuerpoMensaje(String cuerpoMensaje) {
		this.cuerpoMensaje = cuerpoMensaje;
	}


}