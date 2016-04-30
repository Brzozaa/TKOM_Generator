package lukasz.brzozowski.tkom;

import java.util.LinkedList;
import java.util.List;

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
	
	public String identifier;
	public List<Iter>iterList;
	public List<Token>concatenationList;
}
