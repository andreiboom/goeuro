package goeuro;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.Test;
import com.testing.goeuro.JSon2CSV;

import org.junit.Assert;

public class JSon2CSVTest {
	@Test
	/*
	 * Testing private method JSon2CSV.getFilePath using reflection
	 */ 
	public void testGetFilePath(){
		try {
			Class[] cArg = new Class[1];
	        cArg[0] = String.class;
			Method method = JSon2CSV.class.getDeclaredMethod("getFilePath", cArg);
			method.setAccessible(true);
			Assert.assertEquals("Freiburg", method.invoke(JSon2CSV.class, "Freiburg"));
			Assert.assertEquals("Berlin", method.invoke(JSon2CSV.class, "Berlin"));
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}		
	}
}
