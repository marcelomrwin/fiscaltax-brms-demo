Fiscal Tax - processador de regras para calculo fiscal
=======================
1) Encontrar o tipo de serviço (Colunas Z + AK)
2) Encontrar o município de retenção (se tipo de serviço (item 1) for devido no local do prestador então é o município do prestador (Coluna E) senão é o município do local da prestação do serviço (Coluna AH) )
3) Se a unidade está estabelecida no município da retenção (Item 2) então próximo passo senão terminou e ISS = 0 (O serviço é quem dita se está)
4) Encontrar o tipo de serviço (item 2 + Coluna AK)
5) Se incentivo ISS (Coluna W) for 74 então aplicar redução da base de calculo de 40% senão Se Isenção ISS = S então terminou e ISS = 0 senão próximo passo fim-se fim-se
6) Encontrar alíquota ISS
7) Se incentivo ISS (Coluna W) for 75 então terminou e ISS = 0 fim-se
8) Aplicar Alíquota ISS na base de calculo
9) Se tipo de serviço (item 1) tem retenção de IR e for Pessoa Física então aplicar tabela progressiva IRRF 
10) Se tipo de serviço (item 1) tem retenção de INSS e for Pessoa Física então encontrar alíquota do INSS 
11) Aplicar Alíquota INSS na base de calculo limitado ao valor do teto máximo
