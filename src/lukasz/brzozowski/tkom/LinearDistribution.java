package lukasz.brzozowski.tkom;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class LinearDistribution implements INumericalGenerator
{
	public LinearDistribution(int lowerLimit, int upperLimit)
	{
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
		rand = new Random();
	}
	
	public LinearDistribution(double lowerLimit, double upperLimit)
	{
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
		this.precision = 2;
		rand = new Random();
	}
	
	public LinearDistribution(double lowerLimit, double upperLimit, int precision)
	{
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
		this.precision = precision;
		rand = new Random();
	}
	
	@Override
	public int getNextInt()
	{
		int difference = (int) (upperLimit - lowerLimit) + 1;
		return rand.nextInt(difference) + (int)lowerLimit;
	}

	@Override
	public double getNextDouble()
	{
		double toReturn = rand.nextDouble() * (upperLimit - lowerLimit) + lowerLimit;
		BigDecimal bd = new BigDecimal(toReturn).setScale(precision, RoundingMode.HALF_EVEN);
		return bd.doubleValue();
	}

	/*public static void main(String[] args)
	{
		INumericalGenerator dist = new LinearDistribution(10, 20);
		int sum = 0;
		for(int i = 0 ; i < 1000; i++)
		{
			int nextGenerated = dist.getNextInt();
			System.out.println(nextGenerated);
			sum += nextGenerated;
		}
		System.out.println("Sum is " + sum);
	}*/
	/*
	public static void main(String[] args)
	{
		INumericalGenerator dist = new LinearDistribution(4.0, 8.0, 4);
		double sum = 0;
		for(int i = 0 ; i < 1000; i++)
		{
			double nextGenerated = dist.getNextDouble();
			System.out.println(nextGenerated);
			sum += nextGenerated;
		}
		System.out.println("Sum is " + sum);
	}*/
	
	
	private final double lowerLimit;
	private final double upperLimit;
	private int precision;
	private Random rand;
}
