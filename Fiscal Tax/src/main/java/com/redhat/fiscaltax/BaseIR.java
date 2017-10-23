package com.redhat.fiscaltax;

/**
 * This class was automatically generated by the data modeler tool.
 */

@javax.persistence.Entity
@org.kie.api.definition.type.Label("Base de dados IR")
public class BaseIR implements java.io.Serializable
{

   static final long serialVersionUID = 1L;

   @javax.persistence.GeneratedValue(generator = "BASEIR_ID_GENERATOR", strategy = javax.persistence.GenerationType.AUTO)
   @javax.persistence.Id
   @javax.persistence.SequenceGenerator(name = "BASEIR_ID_GENERATOR", sequenceName = "BASEIR_ID_SEQ")
   private java.lang.Long id;

   private java.lang.String documento;

   private java.util.Date dtservico;

   private java.lang.String tipo;

   private java.lang.Double valor = 0.0;

   private java.lang.Long municipio;

   private java.lang.Long servico;

   private java.lang.Double aliquota = 0.0;

   public BaseIR()
   {
   }

   public java.lang.Long getId()
   {
      return this.id;
   }

   public void setId(java.lang.Long id)
   {
      this.id = id;
   }

   public java.lang.String getDocumento()
   {
      return this.documento;
   }

   public void setDocumento(java.lang.String documento)
   {
      this.documento = documento;
   }

   public java.util.Date getDtservico()
   {
      return this.dtservico;
   }

   public void setDtservico(java.util.Date dtservico)
   {
      this.dtservico = dtservico;
   }

   public java.lang.String getTipo()
   {
      return this.tipo;
   }

   public void setTipo(java.lang.String tipo)
   {
      this.tipo = tipo;
   }

   public java.lang.Double getValor()
   {
      return this.valor;
   }

   public void setValor(java.lang.Double valor)
   {
      this.valor = valor;
   }

   public java.lang.Long getMunicipio()
   {
      return this.municipio;
   }

   public void setMunicipio(java.lang.Long municipio)
   {
      this.municipio = municipio;
   }

   public java.lang.Long getServico()
   {
      return this.servico;
   }

   public void setServico(java.lang.Long servico)
   {
      this.servico = servico;
   }

   public java.lang.Double getAliquota()
   {
      return this.aliquota;
   }

   public void setAliquota(java.lang.Double aliquota)
   {
      this.aliquota = aliquota;
   }

   public BaseIR(java.lang.Long id, java.lang.String documento,
         java.util.Date dtservico, java.lang.String tipo,
         java.lang.Double valor, java.lang.Long municipio,
         java.lang.Long servico, java.lang.Double aliquota)
   {
      this.id = id;
      this.documento = documento;
      this.dtservico = dtservico;
      this.tipo = tipo;
      this.valor = valor;
      this.municipio = municipio;
      this.servico = servico;
      this.aliquota = aliquota;
   }

}