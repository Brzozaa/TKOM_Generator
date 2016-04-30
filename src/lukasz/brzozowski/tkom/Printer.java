package lukasz.brzozowski.tkom;

public class Printer
{
	public static void printFileNotFound(String fileName)
	{
		System.out.println("Could not find file with name " + fileName);
	}
	
	public static void printErrorWhileReadingFile(String fileName)
	{
		System.out.println("Error while reading file with name " + fileName);
	}
	
	public static void printBadToken(String token)
	{
		System.out.println("Token " + token + " is not recognized as an allowed token");
	}
	
	public static void printBadTextLiteral(String s)
	{
		System.out.println("Did not end text literal: " + s + "with quotation mark");
	}
	
	public static void printSecondDotError(String s)
	{
		System.out.println("Unexpected dot comes after string: " + s);
	}
	
	public static void printNumericOnlyOneDot(String s)
	{
		System.out.println("Numerical can contain only one dot besides digits and input is: " + s);
	}
	
	public static void printAfterZeroError(String s)
	{
		System.out.println("After 0 in a numerical token must come . and input is: " + s);
	}
	
	public static void printInvalidIterName(String s)
	{
		System.out.println("The length of an identifier should be 1 and input is: " + s);
	}
	
	public static void printExpectedDifferent(String expected, String s)
	{
		System.out.println("Expected " + expected + " and input is: " + s);
	}
	
	public static void printNameFromContextError(String context, String s)
	{
		System.out.println("From outer generate table contex expected table name" + context + " and input is: " + s);
	}
	
	public static void printColumnTypeGenerationError(String columntType, String generationMethode)
	{
		System.out.println("After this column type " + columntType + "cannot come this generathion methode " + generationMethode);
	}
}
