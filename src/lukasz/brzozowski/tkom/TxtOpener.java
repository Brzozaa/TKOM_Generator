package lukasz.brzozowski.tkom;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TxtOpener implements ITextGenerator
{
	public TxtOpener(String fileName)
	{
		this.fileName = fileName;
		try
		{
			 in = new BufferedReader(new FileReader(this.fileName));
		}
		catch (FileNotFoundException e)
		{
			Printer.printFileNotFound(fileName);
		}
	}
	
	@Override
	public String getNextString()
	{
		String line="";
		try
		{
			line = in.readLine();
			if(line == null)
			{
				in.close();
				in = new BufferedReader(new FileReader(this.fileName));
				line = in.readLine();
			}	
			return line;
		}
		catch (IOException e)
		{
			Printer.printErrorWhileReadingFile(fileName);
			return "";
		}
	}
	private String fileName;
	private BufferedReader in;	
	/*public static void main(String[] args)
	{
		TxtOpener txt = new TxtOpener("countries.txt");
		for (int i=0;i<1000;i++)
		{
			System.out.println(txt.getNextString());
		}
	}*/
}
