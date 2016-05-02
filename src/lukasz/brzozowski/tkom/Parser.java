package lukasz.brzozowski.tkom;

import java.util.ArrayList;
import java.util.List;
import lukasz.brzozowski.tkom.Token.TokenType;

public class Parser
{
	//TODO fix columns bug with last semicolon in last column def
	//TODO dodaj rozklad Poissona
	public Parser(String fileName)
	{
		this.scanner = new Scanner(fileName);
		this.symbolTable = new SymbolTable();
	}
	
	public void startParsing()
	{
		Token token;
		while((token = scanner.getNextToken()).languageConstructNumber == 2)  //FUNCTION
		{
			Function fun = parseFunction();
			symbolTable.addFunctionToSymbolTab(fun);
		}
		if(token.languageConstructNumber != 0)  //GENERATE
		{
			Printer.printExpectedDifferent("GENERATE", token.s);
		}
		do
		{
			Table table = parseGendef("");  //empty parent
			symbolTable.addTableToSymbolTab(table);
			for(Table t : table.childTables)
			{
				symbolTable.addTableToSymbolTab(t);
			}
		}
		while((token = scanner.getNextToken())!= null && token.languageConstructNumber == 0); // GENERATE
	}
	
	private Table parseGendef(String parent)
	{
		Token token = scanner.getNextToken();
		if(token.languageConstructNumber != 1)  // INTO
		{
			Printer.printExpectedDifferent("INTO", token.s);
			return null;
		}
		token = scanner.getNextToken();
		if(token.type != Token.TokenType.IDENTIFIER)
		{
			Printer.printExpectedDifferent("identifier", token.s);
			return null;
		}
		Table mainTable = new Table(token.s, parent);
		if((token = scanner.getNextToken()).languageConstructNumber != 4) //WITH
		{
			Printer.printExpectedDifferent("WITH", token.s);
			return null;
		}
		if((token = scanner.getNextToken()).languageConstructNumber != 5) //COLUMNS
		{
			Printer.printExpectedDifferent("COLUMNS", token.s);
			return null;
		}
		if((token = scanner.getNextToken()).type != TokenType.SEPARATOR || token.languageConstructNumber != 3) //:
		{
			Printer.printExpectedDifferent("colon", token.s);
			return null;
		}
		do
		{
			Column column = parseColumn();
			mainTable.addColumn(column);
		}
		while((token = scanner.getNextToken()).languageConstructNumber == 1 && token.type == TokenType.SEPARATOR); //;
		if(token.languageConstructNumber != 3 && token.languageConstructNumber != 7) //end albo foreachdef
		{
			Printer.printExpectedDifferent("END or FOREACH", token.s);
		}
		if(token.languageConstructNumber == 7) //FOREACH
		{
			if((token = scanner.getNextToken()).type != TokenType.IDENTIFIER)
			{
				Printer.printExpectedDifferent("identifier", token.s);
				return null;
			}
			if(!token.s.equals(mainTable.identifier))
			{
				Printer.printNameFromContextError(mainTable.identifier, token.s);
				return null;
			}
			token = scanner.getNextToken();
			if(token.languageConstructNumber != 0)  //GENERATE
			{
				Printer.printExpectedDifferent("GENERATE", token.s);
			}
			do
			{
				Table table = parseGendef(mainTable.identifier);
				mainTable.addChildTable(table);
				symbolTable.addTableToSymbolTab(table);
			}
			while((token = scanner.getNextToken()).languageConstructNumber == 0); // GENERATE
			if(token.languageConstructNumber != 3) //END
			{
				Printer.printExpectedDifferent("END", token.s);
				return null;
			}
			List<Token> amountTokens = parseAmountDef();
			mainTable.addamountTokens(amountTokens);
		}
		else // 3, END
		{
			List<Token> amountTokens = parseAmountDef();
			mainTable.addamountTokens(amountTokens);
		}
		return mainTable;
	}

	private List<Token> parseAmountDef()
	{
		Token token;
		ArrayList<Token> tokens = new ArrayList<>();
		if((token = scanner.getNextToken()).languageConstructNumber != 5) //COLUMNS
		{
			Printer.printExpectedDifferent("COLUMNS", token.s);
			return null;
		}
		if((token = scanner.getNextToken()).languageConstructNumber != 20) //AMOUNT
		{
			Printer.printExpectedDifferent("AMOUNT", token.s);
			return null;
		}
		if((token = scanner.getNextToken()).type != TokenType.KEYWORD && token.type != TokenType.NUMERICAL)
			return null;
		if(token.type == Token.TokenType.NUMERICAL)
		{
			if(token.s.contains("."))
			{
				Printer.printExpectedDifferent("numerical int", token.s);
			}
			else
			{
				tokens.add(token);
				return tokens;
			}
		}
		else if(token.languageConstructNumber == 17)  //linear
		{
			if((token = scanner.getNextToken()).type != TokenType.SEPARATOR && token.languageConstructNumber != 4) //(
			{
				Printer.printExpectedDifferent("(", token.s);
			}
			if((token = scanner.getNextToken()).type == TokenType.NUMERICAL && !token.s.contains("."))
			{
				tokens.add(token);
				if((token = scanner.getNextToken()).languageConstructNumber == 6) // ,
				{
					if((token = scanner.getNextToken()).type == TokenType.NUMERICAL && !token.s.contains("."))
					{
						tokens.add(token);
					}
					if((token = scanner.getNextToken()).languageConstructNumber != 5)  // )
					{
						Printer.printExpectedDifferent(")", token.s);
					}
				}
				else
				{
					Printer.printExpectedDifferent(",", token.s);
				}
			}
			return tokens;
		}
		return null;
	}
	
	private Column parseColumn()
	{
		Token token;
		String name;
		List<Token>generationMethod = new ArrayList<>();
		if((token = scanner.getNextToken()).type != TokenType.IDENTIFIER)
		{
			Printer.printExpectedDifferent("IDENTIFIER", token.s);
		}
		name = token.s;
		if((token = scanner.getNextToken()).type != TokenType.SEPARATOR && token.languageConstructNumber != 1) //;
		{
			Printer.printExpectedDifferent("semicolon", token.s);
		}
		if((token = scanner.getNextToken()).type != TokenType.KEYWORD && (token.languageConstructNumber < 11 || token.languageConstructNumber > 15))
		{
			Printer.printExpectedDifferent("column type", token.s);
		}
		Token typeToken = new Token(token);
		if((token = scanner.getNextToken()).type != TokenType.SEPARATOR && token.languageConstructNumber != 1) //;
		{
			Printer.printExpectedDifferent("semicolon", token.s);
		}
		//TYPE ->  „int” | „nvarchar” | „numeric” | „date” | „@id”
		//GENERATION_METHODE -> constant ‘(‘ real ‘)’ | normal ‘(‘real’,’ real’)’ | linear ‘(‘ real’,’ real’)’ | FNAME | „GETDATE” | CNAME | FILE
		if((token = scanner.getNextToken()).type != TokenType.KEYWORD && token.type != TokenType.IDENTIFIER && token.type != TokenType.FILE)
		{
			Printer.printExpectedDifferent("generation methode", token.s);
		}
		if(!(typeToken.languageConstructNumber == 15 && (token.languageConstructNumber == 19 || token.type == TokenType.IDENTIFIER)) //date -> GETDATE|FNAME
			&& !(typeToken.languageConstructNumber == 13 && (token.type == TokenType.FILE || token.type == TokenType.IDENTIFIER))  // nvarchar ->FNAME | FILE
			&& !(typeToken.languageConstructNumber == 11 && token.type == TokenType.IDENTIFIER)  // @id ->CNAME
			&& !((typeToken.languageConstructNumber == 12 || typeToken.languageConstructNumber == 14)  && (token.languageConstructNumber >= 16 && token.languageConstructNumber <= 18))  // int|numeric ->linear|constant|real
		  )
		{
			Printer.printColumnTypeGenerationError(typeToken.s, token.s);
		}
		generationMethod.add(token);
		if(typeToken.languageConstructNumber == 15 || typeToken.languageConstructNumber == 11 || typeToken.languageConstructNumber == 13)
		{
			return new Column(name, typeToken, generationMethod);
		}
		if((token = scanner.getNextToken()).type != TokenType.SEPARATOR && token.languageConstructNumber != 4) // (
		{
			Printer.printExpectedDifferent("(", token.s);
		}
		if((token = scanner.getNextToken()).type != TokenType.NUMERICAL)
		{
			Printer.printExpectedDifferent("numerical", token.s);
		}
		generationMethod.add(token);
		if((token = scanner.getNextToken()).type != TokenType.SEPARATOR)
		{
			Printer.printExpectedDifferent("separator", token.s);
		}
		if(token.languageConstructNumber == 5)  // )
		{
			return new Column(name, typeToken, generationMethod);
		}
		else if(token.languageConstructNumber == 6)  // ,
		{
			if((token = scanner.getNextToken()).type != TokenType.NUMERICAL)
			{
				Printer.printExpectedDifferent("numerical", token.s);
			}
			generationMethod.add(token);
		}
		if((token = scanner.getNextToken()).languageConstructNumber == 5)  // )
		{
			return new Column(name, typeToken, generationMethod);
		}
		return null;
	}

	private Function parseFunction()
	{
		Token token = scanner.getNextToken();
		if(token.type != Token.TokenType.IDENTIFIER)
		{
			Printer.printExpectedDifferent("identifier", token.s);
			return null;
		}
		Function fun = new Function(token.s);
		token = scanner.getNextToken();
		if(token.languageConstructNumber != 8)  //FOR
		{
			Printer.printExpectedDifferent("FOR", token.s);
		}
		if((token = scanner.getNextToken()).languageConstructNumber != 4 && token.type != TokenType.SEPARATOR) // (
		{
			Printer.printExpectedDifferent("(", token.s);
		}
		while((token = scanner.getNextToken()).type == TokenType.IDENTIFIER)
		{
			Iter iter = parseIterdef(token.s);
			fun.addIter(iter);
		}
		if(token.type != TokenType.SEPARATOR && token.languageConstructNumber != 5)
		{
			Printer.printExpectedDifferent(")", token.s);
		}
		if((token = scanner.getNextToken()).languageConstructNumber != 6) //RETURN
		{
			Printer.printExpectedDifferent("RETURN", token.s);
			return null;
		}
		for(Token t : parseReturnExpression())
		{
			fun.addConcatenationToken(t);
		}
		return fun;
	}
	
	private List<Token> parseReturnExpression()
	{
		List<Token> tokens = new ArrayList<>();
		Token token = parseConstruct();
		tokens.add(token);
		while((token = scanner.getNextToken()).type == TokenType.SEPARATOR && token.languageConstructNumber == 2) //+
		{
			tokens.add(parseConstruct());
		}
		if(token.languageConstructNumber != 1) //;
		{
			Printer.printExpectedDifferent("semicolon",token.s);
		}
		return tokens;
	}
	
	private Token parseConstruct()
	{
		Token token;
		if((token = scanner.getNextToken()).type == TokenType.TEXT || token.type == TokenType.IDENTIFIER) 
			return token;
		return null;
	}
	
	private Iter parseIterdef(String identifier)
	{
		Token token;
		if((token = scanner.getNextToken()).languageConstructNumber != 9) //FROM
		{
			Printer.printExpectedDifferent("FROM", token.s);
			return null;
		}
		token = scanner.getNextToken();
		if(token.s.length() != 1 || (token.type != Token.TokenType.IDENTIFIER && token.type != Token.TokenType.NUMERICAL))
		{
			Printer.printInvalidIterName(token.s);
			return null;
		}
		char lowerBound = token.s.charAt(0);
		if((token = scanner.getNextToken()).languageConstructNumber != 10) //TO
		{
			Printer.printExpectedDifferent("TO", token.s);
			return null;
		}
		token = scanner.getNextToken();
		if(token.s.length() != 1 || (token.type != Token.TokenType.IDENTIFIER && token.type != Token.TokenType.NUMERICAL))
		{
			Printer.printInvalidIterName(token.s);
			return null;
		}
		char upperBound = token.s.charAt(0);
		if((token = scanner.getNextToken()).type != Token.TokenType.SEPARATOR || !token.s.equals(";")) //;
		{
			Printer.printExpectedDifferent("semicolon", token.s);
			return null;
		}
		return new Iter(identifier, lowerBound, upperBound);	
	}

	public static void main(String[] args)
	{
		Parser parser = new Parser("input2.txt");
		parser.startParsing();
		Generator gen = new Generator(parser.symbolTable, "generationscript.txt");
		gen.generate(new ArrayList<Integer>(), null);
	}
	
	public SymbolTable symbolTable;
	private Scanner scanner;
}