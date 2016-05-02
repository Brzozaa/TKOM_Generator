package lukasz.brzozowski.tkom;

import java.util.ArrayList;
import java.util.List;

public class Table
{
	public Table(String identifier, String parent)
	{
		this.identifier = identifier;
		this.parent = parent;
		this.columnList = new ArrayList<Column>();
		this.childTables = new ArrayList<Table>();
		this.amountTokens = null;
	}

	public void addColumn(Column column)
	{
		this.columnList.add(column);
	}
	
	public void addChildTable(Table table)
	{
		this.childTables.add(table);
	}
	
	public void addamountTokens(List<Token> tokens)
	{
		this.amountTokens = tokens;
	}
	
	public void initTableGenerator(TableGenerator gen)
	{
		this.gen = gen;
	}
	
	public String identifier;
	public String parent;
	public List<Column>columnList;
	public List<Table>childTables;
	public List<Token> amountTokens;
	public TableGenerator gen;
}
