package model;

import java.io.Serializable;

import anotations.Atributo;
import anotations.Bean;
import br.com.brsiueg.projectWicket.ListaGasto;

@Bean(nome="gasto")
public class GastoBean extends AbstractBean<Integer> implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Atributo(nome="id", pk=true)
	private Integer id;
	
	@Atributo(nome="idViagem")
	private Integer idViagem;
	
	private String nomeViagem;
	
	@Atributo(nome="valorGasto")
	private Double valorGasto;
	
	@Atributo(nome="dataGasto")
	private String dataGasto;
	
	@Atributo(nome="local")
	private String local;
	
	@Override
	public String getPKName() {
		return "id";
	}

	@Override
	public Integer getPK() {
		return id;
	}

	@Override
	public void setPK(Integer pk) {
		this.id = pk;
	}

	@Override
	public void resetCampos() {
		this.id = null;
		this.idViagem = null;
		this.valorGasto = 0.0;
		this.dataGasto = null;
		this.local = null;
	}

	@Override
	public String getNomeTabela() {
		return "gasto";
	}

	public Integer getIdViagem() {
		return idViagem;
	}
	
	public void setIdViagem(Integer idViagem) {
		this.idViagem = idViagem;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getValorGasto() {
		return valorGasto;
	}

	public void setValorGasto(Double valorGasto) {
		this.valorGasto = valorGasto;
	}

	public String getDataGasto() {
		return dataGasto;
	}

	public void setDataGasto(String dataGasto) {
		this.dataGasto = dataGasto;
	}
	
	public String getNomeViagem() {
		return nomeViagem;
	}
	
	public void setNomeViagem(String nomeViagem) {
		this.nomeViagem = nomeViagem;
	}
	
	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}
	
	@Override
	public String getStringChoice() {
		return nomeViagem;
	}
	
	@Override
	public Class<?> getClassList() {
		return ListaGasto.class;
	}
	
	
}
