package project1;
//response of a search
public class MatchResponse {

	private int line;
	private int col;
	private String word;
	private String lineString;

	

	public MatchResponse(int line, int col, String word, String lineString) {
		super();
		this.line = line;
		this.col = col; //column of the prefix found
		this.word = word;
		this.lineString = lineString;
	}
	


	@Override
	public String toString() {
		return "\n MatchResponse [line=" + line + ", col=" + col + ", word=" + word + ", lineString=" + lineString + "]";
	}
	public String getLineString() {
		return lineString;
	}

	public void setLineString(String lineString) {
		this.lineString = lineString;
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