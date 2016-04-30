package lukasz.brzozowski.tkom;

public class ForeNamesGenerator implements ITextGenerator
{
	public ForeNamesGenerator()
	{
		txt = new TxtOpener("CSV_Database_of_First_Names.txt");
	}
	@Override
	public String getNextString()
	{	
		return txt.getNextString();
	}
	private final TxtOpener txt;
}
