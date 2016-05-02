package lukasz.brzozowski.tkom;

import java.util.LinkedList;
import java.util.List;

import lukasz.brzozowski.tkom.Token.TokenType;

public class TableGenerator
{
	public TableGenerator(Table t, SymbolTable symbolTable)
	{
		textGenerators = new LinkedList<>();
		numericalGenerators = new LinkedList<>();
		for(Column c : t.columnList)
		{
			if(c.type.languageConstructNumber == 12 || c.type.languageConstructNumber == 14 ) //int, numerical
			{
				if(c.generationMethod.size() == 2 && c.generationMethod.get(0).languageConstructNumber == 18)  // constant
				{
					String s = c.generationMethod.get(1).s;
					if(c.type.languageConstructNumber == 12)
					{
						int i = Integer.parseInt(s);
						numericalGenerators.add(new ConstantDistribution(i));
					}
					else
					{
						double d = Double.parseDouble(s);
						numericalGenerators.add(new ConstantDistribution(d));
					}
				}
				else if(c.generationMethod.size() == 3)
				{
					String s1 = c.generationMethod.get(1).s;
					String s2 = c.generationMethod.get(2).s;
					if(c.type.languageConstructNumber == 12)
					{
						int i1 = Integer.parseInt(s1);
						int i2 = Integer.parseInt(s2);
						if(c.generationMethod.get(0).languageConstructNumber == 17) //linear
						{
							numericalGenerators.add(new LinearDistribution(i1,i2));
						}
						else if(c.generationMethod.get(0).languageConstructNumber == 16) //normal
						{
							numericalGenerators.add(new NormalDistribution(i1,i2));
						}	
					}
					else
					{
						double d1 = Double.parseDouble(s1);
						double d2 = Double.parseDouble(s2);
						if(c.generationMethod.get(0).languageConstructNumber == 17) //linear
						{
							numericalGenerators.add(new LinearDistribution(d1,d2));
						}
						else if(c.generationMethod.get(0).languageConstructNumber == 16) //normal
						{
							numericalGenerators.add(new NormalDistribution(d1,d2));
						}
					}
				}
			}
			if(c.type.languageConstructNumber == 13 || c.type.languageConstructNumber == 15) //nvarchar, date
			{
				if(c.generationMethod.get(0).type == TokenType.FILE)
				{
					textGenerators.add(new TxtOpener(c.generationMethod.get(0).s));
				}
				else if(c.generationMethod.get(0).type == TokenType.IDENTIFIER)
				{
					String funcIdentifier = c.generationMethod.get(0).s;
					Function f = symbolTable.functionMap.get(funcIdentifier);
					textGenerators.add(new FunctionGenerator(f));
				}
				else if(c.generationMethod.get(0).type == TokenType.KEYWORD && c.generationMethod.get(0).languageConstructNumber == 19) //GETDATE
				{
					textGenerators.add(new FunctionGenerator(new Function()));
				}
			}
		}
	}
	
	//lista ostatnich wygenerowanych wartosci numerycznych //wazne przy rozwiazywaniu @id
	/*public void updateLastValues(List<Integer> listLastValues)
	{
		this.listLastValues = listLastValues;
	}*/
	
	//lista text i numerical generatorow dla tabeli
	List<ITextGenerator> textGenerators;
	List<INumericalGenerator> numericalGenerators;
	//List<Integer> listLastValues;
}
