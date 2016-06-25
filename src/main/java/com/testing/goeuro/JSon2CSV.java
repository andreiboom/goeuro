package com.testing.goeuro;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.json.CDL;
import org.json.JSONArray;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import com.testing.goeuro.exceptions.InputValidationException;

/** Description of JSon2CSV 
*
* @author Andrei Filipovich
* @version 1 Build 1 Jun 30, 2016.
*/

public class JSon2CSV {

	private final static String BASE_URL = "http://api.goeuro.com/api/v2/position/suggest/en/";
	private final static String REGEX = "^[a-zA-Z *]+$";

	/* 
	 * main method
	 */
	public static void main(String[] args) {

		if (args.length < 1)
			System.out.println("Insufficient parameters");
		else {

			try {
				String param = args[0].trim();
				String url = getFullUrl(BASE_URL, param);
				ClientResource resource = new ClientResource(url);
				String response = resource.get(String.class);
				
				if (response != null){
					JSONArray jsonArray = new JSONArray(response);
					
					if (jsonArray != null && jsonArray.length() > 0){
						// write to CSV
						// name file with value of parameter
						File file = new File(getFilePath(param));
						String csv = CDL.toString(jsonArray);
						FileUtils.writeStringToFile(file, csv);
						System.out.println("File created: " + file.getAbsolutePath());

					} else {
						// nothing to write
						System.out.println("Not found. Nothing to write");
					}
				}
			} catch (UnsupportedEncodingException e) {
				System.out.println("UnsupportedEncodingException");
			} catch (ResourceException e) {
				System.out.println("ResourceException");
			} catch (IOException e) {
				System.out.println("IOException");
			} catch (InputValidationException e) {
				System.out.println("InputValidationException");
			}
		}
	}

	/* 
	 * validates input and creates 
	 * a full path to the web service
	 */
	private static String getFullUrl(String baseUrl, String param) 
			throws UnsupportedEncodingException, InputValidationException {

		if (param != null && !"".equals(param)){
			if (param.matches(REGEX)) {
				StringBuilder sb = new StringBuilder();
				sb.append(baseUrl);
				sb.append(param);
				return sb.toString();
			} else {
				throw new InputValidationException();
			}
		} else {
			throw new InputValidationException();
		}		
	}
	
	/*
	 * recursive method to generate new file name
	 * if file already exists method appends 
	 * suffix .[n] to file name until path is available 
	 */  
	private static String getFilePath(String pathString){
		Path path;
		if (pathString != null && !pathString.isEmpty()){
			path = Paths.get(pathString);
			int currentCount;
			if (Files.exists(path)){
				// append number to files name and call again
				int indexOfDot = pathString.lastIndexOf('.');
				if (indexOfDot != -1){
					String currentSuffix = pathString.substring(indexOfDot+1);
					currentCount = Integer.valueOf(currentSuffix);
					pathString = pathString.substring(0, indexOfDot) + '.' + ++currentCount;
				} else {
					currentCount = 0;
					pathString = pathString + '.' + ++currentCount;
				}
				return getFilePath(pathString);
			} else if (Files.notExists(path)){
				return pathString;
			} else {
				System.out.println("Unable to verify the path");
				return null; 
			}
		} 
		else {
			System.out.println("Empty path");
			return null;
		}
	}
}
