package project1;

public class MatchResponse {
	
	private int line;
	private int col;
	private String word;
	
	
	
	public MatchResponse(int line, int col, String word) {
		super();
		this.line = line;
		this.col = col;
		this.word = word;
	}
	
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	
	
	

}