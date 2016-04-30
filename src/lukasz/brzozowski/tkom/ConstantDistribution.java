package lukasz.brzozowski.tkom;

public class ConstantDistribution implements INumericalGenerator
{
	public ConstantDistribution(int constant)
	{
		this.constant = constant;
	}
	
	public ConstantDistribution(double constant)
	{
		this.constantD = constant;
	}

	@Override
	public int getNextInt()
	{
		return constant;
	}

	@Override
	public double getNextDouble()
	{
		return constantD;
	}

	private int constant;
	private double constantD;
}
