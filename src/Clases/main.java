package Clases;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import MetodosSql.JavaMail;
import MetodosSql.MetodosSql;
import configs.ReadPropertyFile;

public class main {
	static String rutaArchivo;
	static String horaBBDD;

	static int flagError = 0;
	public static void main(String[] args) {
		Properties props = ReadPropertyFile.getInstance().obtenerPropiedades();
		
		String bases=props.getProperty("bases");
		String ipBase=props.getProperty("ipBase");
		String usuarioBase=props.getProperty("usuarioBase");
		String passwordBase=props.getProperty("passwordBase");
		String [] basesSeparadasPorComa = bases.split(",");
		List<String> listaBases = Arrays.asList(basesSeparadasPorComa);
		String mailRemitentes=props.getProperty("mailRemitentes");
		String [] mailRemitentesSeparadasPorComa = mailRemitentes.split(",");
		List<String> listamailRemitentes = Arrays.asList(mailRemitentesSeparadasPorComa);
		String mailSender = props.getProperty("usuarioMailSender");
		String passwordMAilSender = props.getProperty("passwordMAilSender");
		String rutaBackup= props.getProperty("rutaBackup");	
		String mensaje=props.getProperty("cuerpoMensaje");	
		String baseDefault=props.getProperty("baseDefault");

		MetodosSql baseCCCC = new MetodosSql(ipBase,usuarioBase, passwordBase); 
		baseCCCC.setDatabase(baseDefault);
		String queryHoraBBDD = "select getdate()";
		horaBBDD = baseCCCC.consultarUnaCelda(queryHoraBBDD);
		horaBBDD = horaBBDD.replaceAll(" ", "").replaceAll("-", "").replaceAll(":", "");
		String resultadoBk="";
		String logOK="";
		String LogNook="";
		
		for(int i=0;i<listaBases.size();i++) {
			baseCCCC.setDatabase(listaBases.get(i));
			resultadoBk=backupear(rutaBackup,baseCCCC);
			System.out.println("Backupeando ->" + listaBases.get(i));
			if(resultadoBk.contains("Error")) {
				LogNook=LogNook+resultadoBk+"\n";
			}else {
				logOK=logOK+resultadoBk+"\n";
			}
		}

		
		JavaMail mail = new JavaMail(mailSender, passwordMAilSender,listamailRemitentes);		
				
		
		if(LogNook.equals("")) {
			//todo ok
			mail.setCuerpoMensaje(mensaje+logOK);
			mail.notificar(true);
		}else {
			mail.setCuerpoMensaje(mensaje+LogNook);
			mail.notificar(false);
			
		}

		
	}



	private static String backupear(String rutaBackup,MetodosSql baseCCCC) {
		rutaArchivo = rutaBackup + baseCCCC.getDatabase() + horaBBDD + ".bak";
		String SentenciaSql = "BACKUP DATABASE [" + baseCCCC.getDatabase() + "] TO  DISK = N'" + rutaArchivo + "' WITH NOFORMAT, NOINIT,  NAME = N'" + baseCCCC.getDatabase() + "-Full Database Backup', SKIP, NOREWIND, NOUNLOAD,  STATS = 10";
		String resultadoBk=baseCCCC.ejecutarBackup(SentenciaSql);
		System.out.println("Resultado del backup es "+resultadoBk);
		if (baseCCCC.existeArchivo(rutaArchivo)) {
			return "Archivo "+rutaArchivo+" Generado correctamente";
		} else {
			System.out.println("Archivo "+rutaArchivo+ " no se encuentra...");
			return "Error "+resultadoBk;
		} 
	}
}
