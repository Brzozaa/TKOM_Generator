package lukasz.brzozowski.tkom;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Scanner
{
	public Scanner(String fileName)
	{
		this.fileName = fileName;
		try
		{
			 in = new BufferedReader(new FileReader(fileName));
		}
		catch (FileNotFoundException e)
		{
			Printer.printFileNotFound(fileName);
		}
		languageSet = new HashMap<String,Integer>();
		languageSet.put("GENERATE", 0); languageSet.put("INTO", 1); languageSet.put("FUNCTION", 2); languageSet.put("END", 3); languageSet.put("WITH", 4);
		languageSet.put("COLUMNS", 5); languageSet.put("RETURN", 6); languageSet.put("FOREACH", 7); languageSet.put("FOR", 8); languageSet.put("FROM", 9); 
		languageSet.put("TO", 10); languageSet.put("@id", 11); languageSet.put("int", 12); languageSet.put("nvarchar", 13); languageSet.put("numeric", 14); 
		languageSet.put("date", 15); languageSet.put("normal", 16);languageSet.put("linear", 17); languageSet.put("constant", 18); languageSet.put("GETDATE", 19);
		languageSet.put("AMOUNT", 20);
		helpConstructsSet = new HashMap<Character,Integer>();
		helpConstructsSet.put(';',1); helpConstructsSet.put('+',2); helpConstructsSet.put(':',3); helpConstructsSet.put('(',4); helpConstructsSet.put(')',5);
		helpConstructsSet.put(',',6);
	}	
	
	Token getNextToken()
	{
		try
		{
			String s="";
			char c;
			int tmp;
			while((tmp = in.read()) != -1 && Character.isWhitespace( (char) tmp))  	// omin biale znaki
				;
			if(tmp==-1)
				return null;
			c = (char) tmp;  // pierwszy niebialy znak
			in.mark(2);
			s += c;
			if(helpConstructsSet.containsKey(c))
			{
				return new Token(s, helpConstructsSet.get(c) ,Token.TokenType.SEPARATOR);
			}
			boolean firstDigitIsZero = false;
			if(c =='0')
			{
				firstDigitIsZero = true;
			}
			if(c >= '0' && c <='9') // przetwarzaj jako cyfra lub rzuc error     TODO: check for correctness
			{
				boolean flag = false;
				while((tmp = in.read()) != -1 && !Character.isWhitespace( (char) tmp) && !helpConstructsSet.keySet().contains((char) tmp) )
				{
					c = (char) tmp;
					s += c;
					if(firstDigitIsZero)
					{
						if(c != '.')
						{
							Printer.printAfterZeroError(s);
							return null;
						}
						firstDigitIsZero = false;
					}
					if(!(c >= '0' && c <= '9'))
					{
						if(c == '.' && flag == false)
						{
							flag = true;
						}
						else
						{
							Printer.printNumericOnlyOneDot(s);
							return null;
						}
					}
					in.mark(2);
				}
				if(helpConstructsSet.keySet().contains((char) tmp))
					in.reset();
				return new Token(s,Token.TokenType.NUMERICAL);
			}
			
			if(c == '\"') // przetwarzaj jako tekst a jak nie to error
			{
				while((tmp = in.read()) != -1 && tmp != '\"')
				{
					s += (char) tmp;
				}
				if((char) tmp != '\"')  // jesli wyszedlem bo koniec pliku
				{
					Printer.printBadTextLiteral(s);
					return null;
				}
				else 
				{
					s += (char) tmp;
					return new Token(s,Token.TokenType.TEXT);
				}
			}			
			boolean canBeFile = false, canBeIdentifier = true;
			if(!isAllowedCharacterForFirstCharacterForTextOrFile(c))
			{
				canBeIdentifier = false;
			}
			while ((tmp = in.read()) != -1 && !Character.isWhitespace( (char) tmp) && !helpConstructsSet.keySet().contains((char) tmp)) // przetwarzaj jako identifier lub file lub slowo kluczowe
			{
				c = (char) tmp;
				if(!isAllowedCharacterForTextOrFile(c))
				{
					canBeIdentifier = false;
				}
				if(c == '.')
				{
					canBeIdentifier = false;
					if(canBeFile == false)   // jesli to pierwsza kropka
					{
						canBeFile = true;
					}
					else
					{
						Printer.printSecondDotError(s);
						return null;
					}
				}
				s += c;
				in.mark(2);
			}
			if(helpConstructsSet.keySet().contains((char) tmp))
				in.reset();
			
			if(canBeFile)
			{
				return new Token(s,Token.TokenType.FILE);
			}
			if(languageSet.containsKey(s))
			{
				return new Token(s, languageSet.get(s) ,Token.TokenType.KEYWORD);
			}
			if(canBeIdentifier)
			{
				return new Token(s,Token.TokenType.IDENTIFIER);
			}
			else  //niepoprawny token
			{
				Printer.printBadToken(s);
				System.exit(-1);
			}
			return null;
		}
		catch (IOException e)
		{
			Printer.printErrorWhileReadingFile(fileName);
			return null;
		}
	}
	
	boolean isAllowedCharacterForTextOrFile(char c)
	{
		if(c == '_' || c == '.' || (c >= 'A' && c <='Z') || (c >= 'a' && c <='z') || (c >= '0' && c <='9'))
			return true;
		return false;
	}
	
	boolean isAllowedCharacterForFirstCharacterForTextOrFile(char c)
	{
		if( (c >= 'A' && c <='Z') || (c >= 'a' && c <='z'))
			return true;
		return false;
	}
	
	/*public static void main(String[] args)
	{
		Scanner scanner = new Scanner("input2.txt");
		Token token=null;
		do
		{
			token = scanner.getNextToken();
		}
		while (token != null);

	}*/
	
	private Map<String, Integer> languageSet;
	private Map<Character, Integer> helpConstructsSet;
	private BufferedReader in;
	private String fileName;
}