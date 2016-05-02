package lukasz.brzozowski.tkom;

import java.util.ArrayList;
import java.util.List;
import lukasz.brzozowski.tkom.Token.TokenType;

public class FunctionGenerator implements ITextGenerator
{
	public FunctionGenerator(Function f)
	{
		this.f = f;
		iters = new ArrayList<>();
		for(Iter i: f.iterList)
		{
			iters.add(new Iter(i));
		}
	}

	@Override
	public String getNextString()
	{
		String toReturn = "";
		for(Token t : f.concatenationList)
		{
			if(t.type == TokenType.TEXT)
			{	
				
				toReturn += t.s.replace("\"", "");
			}
			else
			{
				for(Iter i : this.iters)
				{
					if(i.identifier.equals(t.s) && t.type == TokenType.IDENTIFIER)
					{
						toReturn += i.getNextString();
					}
				}
			}
		}
		return toReturn;
	}
	List<Iter> iters;
	Function f;
}