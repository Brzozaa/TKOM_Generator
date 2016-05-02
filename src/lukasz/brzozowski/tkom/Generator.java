package lukasz.brzozowski.tkom;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import lukasz.brzozowski.tkom.Token.TokenType;

public class Generator
{
	public Generator(SymbolTable symbolTable, String outputTxtName)
	{
		this.symbolTable = symbolTable;
		this.outputTxtname = outputTxtName;
		for(Table t : symbolTable.tableMap.values())
		{
			TableGenerator tableGen = new TableGenerator(t, symbolTable);
			t.initTableGenerator(tableGen);
		}
		try
		{
			this.out = new PrintWriter(outputTxtName);
		}
		catch (FileNotFoundException e)
		{
			Printer.printFileNotFound(outputTxtName);
		}
	}
	
	//prechodzi po tablicach w symbol table dla kazdej tablicy wykonuje tablegen do niej i wstawia ten table gen do tablicy symboli
	// i wtedy generuje dla  nich inserty
	// zalozenie zawsze dziecko ktore ma dziecko jest jako ostanie z dzieci(w sensie dzieci tablicy sa uporzodkowane tak ze najpierw te bez dzieci a potem te z)
	//czyli 1 krok generacja wszystkich gendefow i w nich zaincjalizownaie generatorow dla danych statycznych(nie @id) 
	// dalej generacja danych tabela po tabeli zgodnie z hierarchia rodzic dziecko w strukturze tabeli
	// tabela root kontrolowanie ilosci generacji poprzez pentle a dzieci przy ilosci wywolan rekurencyjnych
	
	//generate through columns
	//increase amount count
	//write to file script
	//call this method for table children
	public void generate(List<Integer> parentLastValues, String tableToGenerate)
	{
		Table t = null;
		if(tableToGenerate == null)
		{
			//search for parent table
			for(Table temp : symbolTable.tableMap.values())
			{
				if(temp.parent.equals(""))
				{
					t = temp;
					break;
				}
			}
		}
		else
		{
			for(Table temp : symbolTable.tableMap.values())
			{
				if(temp.identifier.equals(tableToGenerate))
				{
					t = temp;
					break;
				}
			}
		}
		if(t.parent.equals("")) // jesli nie ma rodzica
		{	
			int parentAmountCount = 0;
			if(t.amountTokens.size() == 1 && t.amountTokens.get(0).type == TokenType.NUMERICAL)
			{
				parentAmountCount = Integer.parseInt(t.amountTokens.get(0).s);
			}
			else if(t.amountTokens.size() == 2)
			{
				int i1 = Integer.parseInt(t.amountTokens.get(0).s);
				int i2 = Integer.parseInt(t.amountTokens.get(1).s);
				parentAmountCount = (new LinearDistribution(i1,i2)).getNextInt();
			}
			for(int iter = 0 ; iter < parentAmountCount; iter++)
			{
				String insertStatement = "INSERT INTO " + t.identifier + " VALUES(";
				int i = 0, j = 0;  //i - numerical values generated count // j = text values generated count
				List<Integer> lastIntValuesGenerated = new ArrayList<Integer>();
				for(Column c : t.columnList)
				{
					if(c.type.languageConstructNumber == 12 || c.type.languageConstructNumber == 14 ) //int, numerical
					{
						if(c.type.languageConstructNumber == 12)
						{
							int intValueGenerated = t.gen.numericalGenerators.get(i).getNextInt();
							insertStatement += Integer.toString(intValueGenerated);
							lastIntValuesGenerated.add(intValueGenerated);
						}
						else
						{
							double doubleValueGenerated = t.gen.numericalGenerators.get(i).getNextDouble();
							insertStatement += Double.toString(doubleValueGenerated);
						}
						i++;
					}
					else
					{
						String textValueGenerated = t.gen.textGenerators.get(j).getNextString();
						j++;
						if(c.generationMethod.get(0).type == TokenType.KEYWORD && c.generationMethod.get(0).languageConstructNumber == 19) //GETDATE
						{
							insertStatement += textValueGenerated;
						}
						else
						{
							insertStatement += "'" + textValueGenerated + "'";
						}
					}
					insertStatement += ",";
				}
				//t.gen.updateLastValues(lastIntValuesGenerated);
				insertStatement = insertStatement.substring(0,insertStatement.length()-1); //remove last ,
				insertStatement += ");";
				writeLineToOutput(insertStatement);
				for(Table childTable : t.childTables)  //call each child as many times as needed
				{
					int howMany = 0;
					if(childTable.amountTokens.size() == 1 && childTable.amountTokens.get(0).type == TokenType.NUMERICAL)
					{
						howMany = Integer.parseInt(childTable.amountTokens.get(0).s);
					}
					else if(childTable.amountTokens.size() == 2)
					{
						int i1 = Integer.parseInt(childTable.amountTokens.get(0).s);
						int i2 = Integer.parseInt(childTable.amountTokens.get(1).s);
						howMany = (new LinearDistribution(i1,i2)).getNextInt();
					}	
					for(int genCount = 0; genCount < howMany ; genCount++ )
					{
						generate(lastIntValuesGenerated, childTable.identifier);
					}
				}
			}
		}
		else
		{
			String insertStatement = "INSERT INTO " + t.identifier + " VALUES(";
			int i = 0, j = 0;  //i - numerical values generated count // j = text values generated count
			List<Integer> lastIntValuesGenerated = new ArrayList<Integer>();
			for(Column c : t.columnList)
			{
				if(c.type.languageConstructNumber == 12 || c.type.languageConstructNumber == 14 ) //int, numerical
				{
					if(c.type.languageConstructNumber == 12)
					{
						int intValueGenerated = t.gen.numericalGenerators.get(i).getNextInt();
						insertStatement += Integer.toString(intValueGenerated);
						lastIntValuesGenerated.add(intValueGenerated);
					}
					else
					{
						double doubleValueGenerated = t.gen.numericalGenerators.get(i).getNextDouble();
						insertStatement += Double.toString(doubleValueGenerated);
					}
					i++;
				}
				else if (c.type.languageConstructNumber == 13 || c.type.languageConstructNumber == 15 )
				{
					String textValueGenerated = t.gen.textGenerators.get(j).getNextString();
					j++;
					if(c.generationMethod.get(0).type == TokenType.KEYWORD && c.generationMethod.get(0).languageConstructNumber == 19) //GETDATE
					{
						insertStatement += textValueGenerated;
					}
					else
					{
						insertStatement += "'" + textValueGenerated + "'";
					}
				}
				else   // 11 handling @ id
				{
					String parentColumnName = c.generationMethod.get(0).s;
					int countI = 0;
					for(Table parentTable : symbolTable.tableMap.values())
					{
						if(parentTable.identifier.equals(t.parent))
						{
							for(Column cParent : parentTable.columnList)
							{
								if(cParent.identifier.equals(parentColumnName)) //found @id indicated column
								{
									break;
								}
								if(cParent.type.languageConstructNumber == 12) // int
								{
									countI++;
								}
							}
							break;
						}
					}
					insertStatement += Integer.toString(parentLastValues.get(countI));
				}
				insertStatement += ",";
			}
			//t.gen.updateLastValues(lastIntValuesGenerated);
			insertStatement = insertStatement.substring(0,insertStatement.length()-1); //remove last ,
			insertStatement += ");";
			writeLineToOutput(insertStatement);
			for(Table childTable : t.childTables)  //call each child as many times as needed
			{
				int howMany = 0;
				if(childTable.amountTokens.size() == 1 && childTable.amountTokens.get(0).type == TokenType.NUMERICAL)
				{
					howMany = Integer.getInteger(childTable.amountTokens.get(0).s);
				}
				else if(childTable.amountTokens.size() == 2)
				{
					int i1 = Integer.getInteger(childTable.amountTokens.get(0).s);
					int i2 = Integer.getInteger(childTable.amountTokens.get(1).s);
					howMany = (new LinearDistribution(i1,i2)).getNextInt();
				}	
				for(int genCount = 0; genCount < howMany ; genCount++ )
				{
					generate(lastIntValuesGenerated, childTable.identifier);
				}
			}
		}
	}
	
	public void writeLineToOutput(String line)
	{
		out.println(line);
	}
	
	String outputTxtname;
	SymbolTable symbolTable;
	private PrintWriter out;
}
