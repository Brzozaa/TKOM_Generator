package lukasz.brzozowski.tkom;

public class Token
{
	public Token(String s, TokenType type)
	{
		this.s = s;
		this.type = type;
		this.languageConstructNumber = NOT_USED;
	}
	
	public Token(String s, int i, TokenType type)
	{
		this.s = s;
		this.type = type;
		this.languageConstructNumber = i;
	}
	
	public Token(Token t)
	{
		this.s = t.s;
		this.type = t.type;
		this.languageConstructNumber = t.languageConstructNumber;
	}
	
	public String s;
	public TokenType type;
	public int languageConstructNumber;
	private static final int NOT_USED = -1;
	public enum TokenType {KEYWORD, SEPARATOR, IDENTIFIER, NUMERICAL, TEXT, FILE};
}
