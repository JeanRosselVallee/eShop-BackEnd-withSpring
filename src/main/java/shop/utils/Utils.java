package shop.utils;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Random;

public class Utils {

	public Utils() {}
	
	public static String newApiKey() {
		
		Random generator = new Random();
		
		String[] tab = {
				"bananes", 
				"poires", 
				"peches", 
				"pommes",
				"kiwis",
				"cerises",
				"abricots",
				"melons",
				"pasteques",
				"fraises",
				"figues",
				"raisins",
				"oranges"
		};
		
		int index = generator.nextInt(tab.length - 1);
		int rand = generator.nextInt(1000000000);
		String current = tab[index] + generator.nextInt( (int) System.currentTimeMillis() ) + rand;
		
		//return current;
		return Utils.encrypt(current);
	}
	
	public static String encrypt(String p_value) {
		
		try {
			p_value = "gyzedzedze4868d1e4_" + p_value;
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.reset();
			byte[] encoded = md.digest( p_value.getBytes("UTF-8") );
			return byteToHex(encoded);
			
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	private static String byteToHex(final byte[] hash)
	{
	    Formatter formatter = new Formatter();
	    for (byte b : hash)
	    {
	        formatter.format("%02x", b);
	    }
	    String result = formatter.toString();
	    formatter.close();
	    return result;
	}

	
// Logging
	
    private static String getClassAndMethodNames(int stackIndex) {
    	String methodName = Thread.currentThread().getStackTrace()[stackIndex].getMethodName();
    	String className = Thread.currentThread().getStackTrace()[stackIndex].getClassName();
    	return className + "." + methodName ; 
    }

    public static void logMethodName() {
    	int callIndex = 3; // there are 3 levels to access the method to log    		
   		System.out.println("\n===> " + getClassAndMethodNames( callIndex ) + "() ");
    }
    	
    	

    
    
	
}
