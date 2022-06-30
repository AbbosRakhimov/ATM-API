package uz.pdp.exception;

public class CardBloced extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CardBloced() {
		super("Card is bloced, please contact your Bank");
	}
}
