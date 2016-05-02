package lukasz.brzozowski.tkom;

public class Iter
{
	public Iter(String identifier, char lowerBound, char upperBound)
	{
		this.identifier = identifier;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.iter = lowerBound;
	}
	
	public Iter(Iter i)
	{
		this.identifier = i.identifier;
		this.lowerBound = i.lowerBound;
		this.upperBound = i.upperBound;
		this.iter = lowerBound;
	}
	
	public String identifier;
	public char lowerBound, upperBound, iter;
	
	public String getNextString()
	{
		String toReturn = "";
		toReturn += iter;
		if(iter == upperBound)
		{
			iter = lowerBound;
		}
		else
		{
			iter +=1;
		}
		return toReturn;
	}
}
