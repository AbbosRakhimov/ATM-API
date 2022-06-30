package uz.pdp.entity.enums;

import java.util.Random;

public enum KindOfCard {

	HUMO, UZCARD, VISA;
	
	 private static final KindOfCard[] VALUES = values();
	  private static final int SIZE = VALUES.length;
	  private static final Random RANDOM = new Random();

	  public static KindOfCard getRandomLetter()  {
	    return VALUES[RANDOM.nextInt(SIZE)];
	  }
}
