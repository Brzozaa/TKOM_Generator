package lukasz.brzozowski.tkom;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable
{	
	public SymbolTable()
	{
		this.tableMap = new HashMap<>();
		this.functionMap = new HashMap<>();
	}
	
	public void addTableToSymbolTab(Table t)
	{
		this.tableMap.put(t.identifier, t);
	}
	
	public void addFunctionToSymbolTab(Function f)
	{
		this.functionMap.put(f.identifier, f);
	}
	
	public Table getTableByName(String name)
	{
		return tableMap.get(name);
	}	
	public Function getFunctionByName(String name)
	{
		return functionMap.get(name);
	}
	
	public Map<String, Table> tableMap;
	public Map<String, Function> functionMap;
}
