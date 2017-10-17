[condition]Houver uma ordem de pagamento com = $ope : OrdemPagamentoEntrada()
[condition][Retorno]Houver uma ordem de pagamento Pronta para aplicar ISS= $ret:Retorno(vlBaseISS != null)
[condition]Ordem de pagamento isenta de ISS = $ope : OrdemPagamentoEntrada(Isen_ISS == 'S' || Incent_ISS == '75') $ret : Retorno()
[condition]Ordem de pagamento com incentivo ISS Redução de base= $ope : OrdemPagamentoEntrada(Incent_ISS == '74') $ret : Retorno()
[condition]Ordem de pagamento com incentivo ISS zero = $ope : OrdemPagamentoEntrada(Incent_ISS == '75') $ret : Retorno()
[condition]Serviço for devido no local do prestador = $ope : OrdemPagamentoEntrada() 
$ops : OrdemPagamentoRule( servDevLocPrest == true ) 
$ret : Retorno()
[condition]Serviço não for devido no local do prestador = $ope : OrdemPagamentoEntrada() 
$ops : OrdemPagamentoRule( servDevLocPrest == false ) 
$ret : Retorno()
[condition]SAS não estabelecida no municipio da retenção = $ops : OrdemPagamentoRule(sasEstbMunic == false) $ret : Retorno()
[condition]Aliquota ISS não encontrada = $ops : OrdemPagamentoRule() $ret : Retorno(aliquotaISS == null) $ope : OrdemPagamentoEntrada() 
[condition]Serviço executado por pessoa física =$ope: OrdemPagamentoEntrada(FJ == 'F')
[condition]Serviço possui retenção de IR =$ops: OrdemPagamentoRule( reterIR == true )
[condition]Serviço possui retenção de INSS =$ops: OrdemPagamentoRule( reterINSS == true )
[condition]Prestador é pessoa Juridica =$ope : OrdemPagamentoEntrada(FJ == 'J') $ret : Retorno() $ops : OrdemPagamentoRule()
[condition]Houver uma ordem de pagamento para pessoa fisica ou juridica =$ope : OrdemPagamentoEntrada(FJ == 'F' || FJ == 'J') $ret : Retorno() $ops : OrdemPagamentoRule()
[condition]Houver uma ordem de pagamento = $ope : OrdemPagamentoEntrada() $ops : OrdemPagamentoRule() $ret : Retorno()
[condition]com serviço habilitado para retenção de INSS = OrdemPagamentoRule(reterINSS == true)
[condition]prestador é pessoa física = OrdemPagamentoEntrada(FJ == 'F')
[consequence]Utilizar municipio do prestador = $ops.setCdMunicipio(new Integer($ope.getCodMunic())); 
$ret.setVlBaseISS($ope.getVlItem()); 
update($ret); 
update ($ops);
[consequence]Utilizar municipio do local da prestação do serviço = $ops.setCdMunicipio(new Integer($ope.getCodMunicServ())); 
$ret.setVlBaseISS($ope.getVlItem()); 
update($ret); 
update ($ops);
[consequence]Aplicar ISS igual a zero = $ret.setVlBaseISS(0.0); $ret.setVlImpostoISS(0.0); $ret.setAliquotaISS(0.0); update ($ret);
[consequence]Aplicar aliquota do ISS = $ret.setVlImpostoISS(($ret.getVlBaseISS() * $ret.getAliquotaISS())/100); update ($ret);
[consequence]Aplicar redução de base em {percentual} = $ret.setVlBaseISS($ope.getVlItem() - ( $ope.getVlItem() * ({percentual}/100)));update ($ret);
[consequence]Consultar e Aplicar aliquota para o Municipio e Servico do item = $ret.setAliquotaISS(SimpleRestClient.consultarAliquota($ops.getCdMunicipio(), $ops.getCdServicoDestino())); 
update ($ret);
$ops.setSaldoIRPF($ope.getVlItem());
[consequence]Print = System.out.println("OK");
[consequence]Logar {message} = System.out.println({message});
[consequence]Aplicar aliquota de {aliquota} do Imposto de Renda = $ret.setVlBaseIR($ope.getVlItem());
$ret.setVlImpostoIR(($ope.getVlItem() * {aliquota})/100);
$ops.setAliquotaIRPF({aliquota});
$ret.setAliquotaIR({aliquota});
update($ret);
[consequence]Acumular valor pago Imposto de Renda = SimpleRestClient.adicionarAcumuloIR($ope.getDocumento(),$ope.getDtEmissao(),$ope.getFJ(),$ops.getCdMunicipio(),$ops.getCdServicoDestino(),$ret.getVlImpostoIR(),$ret.getAliquotaIR());
[consequence]Aplicar a aliquota de {aliquota} do INSS = $ret.setVlBaseINSS($ope.getVlItem());
$ret.setVlImpostoINSS(($ope.getVlItem() * {aliquota})/100);
update($ret);
[consequence]Limitar o teto máximo de {valor} para o INSS = $ret.getVlImpostoINSS()>{valor}?$ret.setVlImpostoINSS({valor}):$ret.getVlImpostoINSS();
[consequence]Aplicar a aliquota de {aliquota} do PIS = $ret.setVlImpostoPIS(($ope.getVlItem() * {aliquota})/100);
$ret.setVlBasePIS($ope.getVlItem());
update($ret);
[consequence]Aplicar a aliquota de {aliquota} do COFINS = $ret.setVlImpostoCOFINS(($ope.getVlItem() * {aliquota})/100);
$ret.setVlBaseCOFINS($ope.getVlItem());
update($ret);