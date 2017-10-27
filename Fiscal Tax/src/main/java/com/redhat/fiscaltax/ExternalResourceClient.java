package com.redhat.fiscaltax;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExternalResourceClient implements Serializable {

	private static final long serialVersionUID = -5500209592403158694L;
	protected static Logger logger = LoggerFactory.getLogger(ExternalResourceClient.class);

	public static void adicionarAcumuloIR(String documento, Date data, String tipo, Integer cdMunc, Integer cdServico,
			Double vlImposto, Double aliquota) throws Exception {

//		logger.info("============================================================================");
//		StringBuilder sb = new StringBuilder();
//		sb.append("Entrou no método com os parámetros [").append(documento).append(",".intern()).append(data)
//				.append(",".intern()).append(tipo).append(",".intern()).append(cdMunc).append(",".intern())
//				.append(cdServico).append(",".intern()).append(vlImposto).append(",".intern()).append(aliquota)
//				.append("]");
//		logger.info(sb.toString());
//		logger.info("============================================================================");

		if (aliquota == null)
			aliquota = 0.0;

		String sql = new String(
				"INSERT INTO BASEIR(ID,DOCUMENTO,DTSERVICO,TIPO,VALOR,VERSION,MUNICIPIO_ID,SERVICO_ID,ALIQUOTA) VALUES (NEXTVAL('BASEIR_ID_SEQ'),?,?,?,?,0,?,?,?)");

		Connection connection = recuperaConexaoDB();
		PreparedStatement prepareStatement = connection.prepareStatement(sql);

		configIdMunicipio(cdMunc, connection, prepareStatement);
		configIdServico(cdServico, connection, prepareStatement);

		prepareStatement.setString(1, documento);
		prepareStatement.setDate(2, new java.sql.Date(data.getTime()));
		prepareStatement.setString(3, tipo);
		prepareStatement.setDouble(4, vlImposto);

		prepareStatement.setDouble(7, aliquota);

		prepareStatement.executeUpdate();
		prepareStatement.close();
		connection.close();
	}

	private static void configIdMunicipio(Integer cdMunc, Connection connection, PreparedStatement prepareStatement)
			throws SQLException {
//		logger.info("CONFIG MUNICIPIO [" + cdMunc + "]");
		PreparedStatement stm = connection.prepareStatement("SELECT ID from MUNICIPIO where codigo = ?");
		stm.setInt(1, cdMunc);
		try (ResultSet query = stm.executeQuery()) {
			while (query.next()) {
				int id = query.getInt("ID");
				prepareStatement.setInt(5, id);
			}
		}
		stm.close();
	}

	private static void configIdServico(Integer cdServico, Connection connection, PreparedStatement prepareStatement)
			throws SQLException {
//		logger.info("CONFIG SERVICO [" + cdServico + "]");
		PreparedStatement stm = connection.prepareStatement("SELECT ID from SERVICO where codigo = ?");
		stm.setInt(1, cdServico);
		try (ResultSet query = stm.executeQuery()) {
			while (query.next()) {
				int id = query.getInt("ID");
				prepareStatement.setInt(6, id);
			}
		}
		stm.close();
	}

	private static Connection recuperaConexaoDB() throws NamingException, SQLException {
		InitialContext cxt = new InitialContext();
		DataSource ds = (DataSource) cxt.lookup("java:jboss/datasources/ExampleDS");
		Connection connection = ds.getConnection();

		return connection;
	}

	public static Double consultarAliquota(Integer municipio, Integer servico) throws IOException {
//		logger.info("Consultando aliquota para municipio [" + municipio + "] e serviço [" + servico + "]");
		CloseableHttpClient httpclient = HttpClients.createDefault();
		Double retorno = 5.0;
		try {
			HttpGet httpGet = new HttpGet(
					"http://localhost:8080/fiscaltax-services/rest/aliquota/ISS/" + municipio + "/" + servico);
			CloseableHttpResponse response = httpclient.execute(httpGet);
			try {
//				logger.info(response.getStatusLine().toString());
				if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
					logger.info("Retorno diferente de 200! resolver oq fazer");
				} else {
					HttpEntity entity = response.getEntity();
					String ret = EntityUtils.toString(entity, "UTF-8");
					retorno = new Double(ret);
				}
			} finally {
				response.close();
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpclient.close();
		}
//		logger.info("Retornando " + retorno);
		return retorno;
	}

}
