package lukasz.brzozowski.tkom;

public class NamesGenerator implements ITextGenerator
{
	public NamesGenerator()
	{
		txt = new TxtOpener("CSV_Database_of_Last_Names.txt");
	}
	@Override
	public String getNextString()
	{	
		return txt.getNextString();
	}
	private final TxtOpener txt;
}
