package lukasz.brzozowski.tkom;

import java.util.List;

public class Column
{
	public Column(String identifier, Token type, List<Token> generationMethod)
	{
		this.identifier = identifier;
		this.type = type;
		this.generationMethod = generationMethod;
	}
	public String identifier;
	public Token type;
	public List<Token> generationMethod;
}
