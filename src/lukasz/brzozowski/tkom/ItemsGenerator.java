package lukasz.brzozowski.tkom;

public class ItemsGenerator implements ITextGenerator
{
	public ItemsGenerator()
	{
		txt = new TxtOpener("items.txt");
	}
	@Override
	public String getNextString()
	{	
		return txt.getNextString();
	}
	private final TxtOpener txt;
}
