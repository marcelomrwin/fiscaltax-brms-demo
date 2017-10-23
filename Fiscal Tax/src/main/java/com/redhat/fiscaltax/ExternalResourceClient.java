package com.redhat.fiscaltax;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;

public class ExternalResourceClient implements Serializable {

	private static final long serialVersionUID = -5500209592403158694L;

	@PersistenceContext(unitName = "com.redhat:FiscalTax")
	protected static EntityManager em;

	protected static void config() {
		if (em == null) {
			try {
				em = (EntityManager) new InitialContext().lookup("java:comp/env/jpa/FiscalTax");
			} catch (Exception e) {
				LoggerFactory.getLogger(ExternalResourceClient.class).error("Não obteve o entity manager!", e);
			}
		}
	}

	public static EntityManager getEntityManager() {
		config();
		return em;
	}

	public static void adicionarAcumuloIR(String documento, Date data, String tipo, Integer cdMunc, Integer cdServico,
			Double vlImposto, Double aliquota) throws Exception {
		if (aliquota == null)
			aliquota = 0.0;

		BaseIR base = new BaseIR();
		base.setAliquota(aliquota);
		base.setDocumento(documento);
		base.setDtservico(data);
		base.setMunicipio(new Long(cdMunc));
		base.setServico(new Long(cdServico));
		base.setTipo(tipo);
		base.setValor(vlImposto);
		
		getEntityManager().persist(base);
	}

	public static Double consultarAliquota(Integer municipio, Integer servico) throws IOException {
		System.out.println("Consultando aliquota para municipio [" + municipio + "] e serviço [" + servico + "]");
		CloseableHttpClient httpclient = HttpClients.createDefault();
		Double retorno = 5.0;
		try {
			HttpGet httpGet = new HttpGet(
					"http://localhost:8080/fiscaltax-services/rest/aliquota/ISS/" + municipio + "/" + servico);
			CloseableHttpResponse response = httpclient.execute(httpGet);
			try {
				System.out.println(response.getStatusLine());
				if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
					System.out.println("Retorno diferente de 200! resolver oq fazer");
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
		System.out.println("Retornando " + retorno);
		return retorno;
	}

}
