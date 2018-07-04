package shop.utils;

public class Custom{

	
	public static final int MYSQL_ERROR 	= 1000;
	public static final int DELETE_ERROR 	= 1100;
	public static final int CREATE_ERROR 	= 1200;
	public static final int UPDATE_ERROR 	= 1300;
	public static final int READ_ERROR 		= 1400;
	public static final int BAD_ROLE_ERROR 	= 1500;
	public static final int UNKNOWN_ERROR 	= 1600;
	public static final int AUTH_ERROR 		= 1700;
	
	
	public final static String getJsonError( int p_error ) {
		
		switch( p_error ) {
		
			case MYSQL_ERROR:
			case DELETE_ERROR:
			case CREATE_ERROR:
			case UPDATE_ERROR:
			case READ_ERROR:
			case BAD_ROLE_ERROR:
			case AUTH_ERROR:
				return "{\"message\": \"error\", \"code\": "+p_error+"}";
				
			default: 
				return "{\"message\": \"error\", \"code\": "+UNKNOWN_ERROR+"}";
				
		}
		
	}
	
	
	public final static String getJsonSuccess() {
		return "{\"message\":\"ok\"}";
	}
	
}
