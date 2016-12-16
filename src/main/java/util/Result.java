package util;

public class Result {
	
	private  String msg;
	private boolean result;
	private String acao;
	
	public Result() {}
	
	public Result(String msg, boolean result, String acao) {
		this.msg = msg;
		this.result = result;
		this.acao = acao;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getAcao() {
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}
	
}
