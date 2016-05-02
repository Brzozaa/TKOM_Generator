package lukasz.brzozowski.tkom;

import java.util.LinkedList;
import java.util.List;

import lukasz.brzozowski.tkom.Token.TokenType;

public class Function
{
	public Function(String identifier)
	{
		this.identifier = identifier;
		this.iterList = new LinkedList<>();
		this.concatenationList = new LinkedList<>();
	}
	
	public void addIter(Iter iter)
	{
		this.iterList.add(iter);
	}
	
	public void addConcatenationToken(Token token)
	{
		this.concatenationList.add(token);
	}
	
	public Function()  //default constructor function, it is used for generating constant value GETDATE()
	{
		this.identifier = "";
		this.iterList = new LinkedList<>();
		this.concatenationList = new LinkedList<>();
		concatenationList.add(new Token("GETDATE()", TokenType.TEXT));
	}
	
	public String identifier;
	public List<Iter>iterList;
	public List<Token>concatenationList;
}
