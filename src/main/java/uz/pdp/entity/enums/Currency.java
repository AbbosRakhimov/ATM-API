package uz.pdp.entity.enums;

import java.util.LinkedList;
import java.util.Random;

public enum Currency {

	HUNDRED(100), FIFTY(50), TWENTY(20), TEN(10), FIVE(5), ONE(1);
	
	private int currency;
	
	Currency(int currency){
		this.currency=currency;
	}

	public int getCurrency() {
		return currency;
	}
	
	 private static final Currency[] VALUES = values();
	  private static final int SIZE = VALUES.length;
	  private static final Random RANDOM = new Random();

	  public static Currency getRandomLetter()  {
	    return VALUES[RANDOM.nextInt(SIZE)];
	  }
	  

}
