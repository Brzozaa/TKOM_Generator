package lukasz.brzozowski.tkom;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class NormalDistribution implements INumericalGenerator
{	
	public NormalDistribution(int mean, int standardDeviation)
	{
		rand = new Random();
		this.mean = mean;
		this.standardDeviation = standardDeviation;
	}

	public NormalDistribution(double mean, double standardDeviation, int precision)
	{
		rand = new Random();
		this.mean = mean;
		this.standardDeviation = standardDeviation;
		this.precision = 2;
	}
	
	public NormalDistribution(double mean, double standardDeviation)
	{
		rand = new Random();
		this.mean = mean;
		this.standardDeviation = standardDeviation;
	}
	
	@Override
	public int getNextInt()
	{	
		double result = rand.nextGaussian() * standardDeviation + mean;
		final int toReturn = (int) result;
		return toReturn;
	}
	
	@Override
	public double getNextDouble()
	{
		double toReturn = rand.nextGaussian() * standardDeviation + mean;
		BigDecimal bd = new BigDecimal(toReturn).setScale(precision, RoundingMode.HALF_EVEN);
		return bd.doubleValue();
	}
	
	/*public static void main(String[] args)
	{
		NormalDistribution dist = new NormalDistribution(20, 10);
		int sum = 0;
		for(int i = 0 ; i < 1000; i++)
		{
			int nextGenerated = dist.getNextInt();
			System.out.println(nextGenerated);
			sum += nextGenerated;
		}
		System.out.println("Sum is " + sum);
	}*/
	
	/*public static void main(String[] args)
	{
		NormalDistribution dist = new NormalDistribution(20.0, 10.0, 2);
		double sum = 0;
		for(int i = 0 ; i < 1000; i++)
		{
			double nextGenerated = dist.getNextDouble();
			System.out.println(nextGenerated);
			sum += nextGenerated;
		}
		System.out.println("Sum is " + sum);
	}*/
	
	private final Random rand;
	private int precision;
	private double mean;
	private double standardDeviation;
}
