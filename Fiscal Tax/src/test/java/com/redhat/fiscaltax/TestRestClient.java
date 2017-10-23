package com.redhat.fiscaltax;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.junit.Ignore;
import org.junit.Test;

public class TestRestClient {
	@Test
	@Ignore
	public void test() throws IOException {

		Double aliquota = ExternalResourceClient.consultarAliquota(3304557, 1);
		System.out.println(aliquota);

	}

	@Test
	public void testAliquota() {
		NumberFormat nf = DecimalFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);

		OrdemPagamentoRule ops = new OrdemPagamentoRule();
		ops.setSaldoIRPF(10000.0);
		ops.setVlIRPF(0.0);

		ops.atualizarSaldoIRPF(27.5, 4664.69);
		System.out.println(nf.format(ops.getSaldoIRPF()));
		System.out.println(nf.format(ops.getVlIRPF()));

		System.out.println("------------------------------");
		
		ops.atualizarSaldoIRPF(22.5, 3751.06);
		System.out.println(nf.format(ops.getSaldoIRPF()));
		System.out.println(nf.format(ops.getVlIRPF()));

		System.out.println("------------------------------");
		
		ops.atualizarSaldoIRPF(15.0, 2826.66);
		System.out.println(nf.format(ops.getSaldoIRPF()));
		System.out.println(nf.format(ops.getVlIRPF()));

		System.out.println("------------------------------");
		
		ops.atualizarSaldoIRPF(7.5, 1903.99);
		System.out.println(nf.format(ops.getSaldoIRPF()));
		System.out.println(nf.format(ops.getVlIRPF()));

		System.out.println("------------------------------");
		
		ops.atualizarSaldoIRPF(0.0, 1903.98);
		System.out.println(nf.format(ops.getSaldoIRPF()));
		System.out.println(nf.format(ops.getVlIRPF()));

	}

}
