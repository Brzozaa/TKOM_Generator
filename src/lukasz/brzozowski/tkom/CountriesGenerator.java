package lukasz.brzozowski.tkom;

public class CountriesGenerator implements ITextGenerator
{
	public CountriesGenerator()
	{
		txt = new TxtOpener("countries.txt");
	}
	@Override
	public String getNextString()
	{	
		return txt.getNextString();
	}
	private final TxtOpener txt;
}
