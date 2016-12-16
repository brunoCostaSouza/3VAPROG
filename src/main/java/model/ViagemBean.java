package model;

import java.io.Serializable;
import java.text.NumberFormat;

import anotations.Atributo;
import anotations.Bean;
import br.com.brsiueg.projectWicket.ListaViagem;

@Bean(nome="viagem")
public class ViagemBean extends AbstractBean<Integer> implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Atributo(nome="id", pk=true)
	private Integer id;
	
	@Atributo(nome="destino")
	private String destino;
	
	@Atributo(nome="orcamento")
	private Double orcamento;
	
	@Atributo(nome="dataPartida")
	private String dataPartida;
	
	@Atributo(nome="dataChegada")
	private String dataChegada;
	
	@Atributo(nome="tipoViagem")
	private String tipoViagem;
	
	@Atributo(nome="descricao")
	private String descricao;
	
	@Atributo(nome="celular")
	private Boolean celular;
	
	@Atributo(nome="notebook")
	private Boolean notebook;
	
	@Atributo(nome="tablet")
	private Boolean tablet;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public Double getOrcamento() {
		return orcamento;
	}

	public void setOrcamento(Double orcamento) {
		this.orcamento = orcamento;
	}

	public String getDataPartida() {
		return dataPartida;
	}

	public void setDataPartida(String dataPartida) {
		this.dataPartida = dataPartida;
	}

	public String getDataChegada() {
		return dataChegada;
	}

	public void setDataChegada(String dataChegada) {
		this.dataChegada = dataChegada;
	}

	public String getTipoViagem() {
		return tipoViagem;
	}

	public void setTipoViagem(String tipoViagem) {
		this.tipoViagem = tipoViagem;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public Boolean getCelular() {
		return celular;
	}

	public void setCelular(Boolean celular) {
		this.celular = celular;
	}

	public Boolean getNotebook() {
		return notebook;
	}

	public void setNotebook(Boolean notebook) {
		this.notebook = notebook;
	}

	public Boolean getTablet() {
		return tablet;
	}

	public void setTablet(Boolean tablet) {
		this.tablet = tablet;
	}

	@Override
	public String getPKName() {
		return "id";
	}


	@Override
	public void resetCampos() {
		id 			= null;
		destino 	= null;
		orcamento	= 0.0;
		dataPartida = null;
		dataChegada = null;
		tipoViagem 	= null;
		descricao 	= null;
		celular 	= false;
		notebook 	= false;
		tablet 		= false;
	}

	@Override
	public String getNomeTabela() {
		return "viagem";
	}

	@Override
	public Integer getPK() {
		return this.getId();
	}

	@Override
	public void setPK(Integer pk) {
		this.setId(pk);
	}
	
	@Override
	public String toString() {
		return destino;
	}
	
	@Override
	public String getStringChoice() {
		return destino;
	}
	
	@Override
	public Class<?> getClassList() {
		return ListaViagem.class;
	}
	
	public String getOrcamentoString(){
		return NumberFormat.getCurrencyInstance().format(getOrcamento());
	}
}
