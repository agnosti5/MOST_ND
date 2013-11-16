package edu.rutgers.MOST.presentation;

import java.io.File;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.rutgers.MOST.config.LocalConfig;
import edu.rutgers.MOST.data.SBMLConstants;

public class Utilities {

	public String createDateTimeStamp() {
		Date date = new Date();
		Format formatter;
		formatter = new SimpleDateFormat("_yyMMdd_HHmmss");
		String dateTimeStamp = formatter.format(date);
				
		return dateTimeStamp;
	}
	
	// based on http://www.java2s.com/Code/Java/File-Input-Output/DeletefileusingJavaIOAPI.htm
	public void delete(String fileName) {
		// A File object to represent the filename
		File f = new File(fileName);

		// Make sure the file or directory exists and isn't write protected
		if (!f.exists())
			//System.out.println("Delete: no such file or directory: " + fileName);

		if (!f.canRead())
			//System.out.println("Delete: can't read: "+ fileName);
		
		if (!f.canWrite())
			//System.out.println("Delete: write protected: "+ fileName);

		if (!f.canExecute())
			//System.out.println("Delete: can't execute: "+ fileName);
		
		// If it is a directory, make sure it is empty
		if (f.isDirectory()) {
			String[] files = f.list();
			if (files.length > 0) {
				//System.out.println("Delete: directory not empty: " + fileName);
			}				
		}
		
        // Attempt to delete it
        boolean success = f.delete();
        if (success)
        	//System.out.println(fileName + " deletion succeeded");
	    if (!success) {
	    	//System.out.println(fileName + " deletion failed");
	    }	 
	}
	
	public void deleteFileIfExists(String filename) {
		File f = new File(filename);
		if (f.exists()) {
			//System.out.println(filename);
			delete(filename);						
		}
	}
	
	// from http://www.javadb.com/how-to-rename-a-file-change-file-name
	public void renameFile(String file, String toFile) {

        File toBeRenamed = new File(file);

        if (!toBeRenamed.exists() || toBeRenamed.isDirectory()) {

            //System.out.println("File does not exist: " + file);
            return;
        }

        File newFile = new File(toFile);

        //Rename
        if (toBeRenamed.renameTo(newFile)) {
            //System.out.println("File has been renamed.");
        } else {
            //System.out.println("Error renmaing file");
        }
    }
	
	public String makeValidID(String mAbrv) {
		mAbrv = replaceInvalidSBMLCharacters(mAbrv);
		if (mAbrv.contains("[") && mAbrv.contains("]")) {
			mAbrv = mAbrv.replace("[","_");
			mAbrv = mAbrv.replace("]","");
		}
		
		if (!mAbrv.startsWith(SBMLConstants.METABOLITE_ABBREVIATION_PREFIX)) {
			mAbrv = SBMLConstants.METABOLITE_ABBREVIATION_PREFIX + mAbrv;
		}
				
		return mAbrv;

	}
	
	public String makeValidReactionID(String rAbrv) {
		rAbrv = replaceInvalidSBMLCharacters(rAbrv);
		if (rAbrv.contains("[") && rAbrv.contains("]")) {
			rAbrv = rAbrv.replace("[","_");
			rAbrv = rAbrv.replace("]","");
		}
		
		if (!rAbrv.startsWith(SBMLConstants.REACTION_ABBREVIATION_PREFIX)) {
			rAbrv = SBMLConstants.REACTION_ABBREVIATION_PREFIX + rAbrv;
		}
						
		return rAbrv;

	}
	
	public String replaceInvalidSBMLCharacters(String value) {
		String corrected = "";
		
		if (value.contains("-")) {
			value = value.replaceAll("-", "_");
		}
		if (value.contains("*")) {
			value = value.replace("*", SBMLConstants.ASTERIC_REPLACEMENT);
		}
		if (value.contains("(")) {
			value = value.replace("(", SBMLConstants.PARENTHESIS_REPLACEMENT);
		}
		if (value.contains(")")) {
			value = value.replace(")", SBMLConstants.PARENTHESIS_REPLACEMENT);
		}
		if (value.contains(":")) {
			value = value.replace(":", SBMLConstants.COLON_CHARACTER_REPLACEMENT);
		}
		if (value.contains("{")) {
			value = value.replace("{", "[");
		}
		if (value.contains("}")) {
			value = value.replace("}", "]");
		}
		if (value.contains("'")) {
			value = value.replace("'", SBMLConstants.APOSTROPHE_REPLACEMENT);
		}
		if (value.contains("#")) {
			value = value.replace("#", SBMLConstants.NUMBER_SIGN_REPLACEMENT);
		}
		if (value.contains("+")) {
			value = value.replace("+", SBMLConstants.PLUS_SIGN_REPLACEMENT);
		}
		if (value.contains(" ")) {
			value = value.replace(" ", "_");
		}
		corrected = value;
		
		return corrected;
		
	}
	
	public String duplicateSuffix(String value, ArrayList<String> list) {
		String duplicateSuffix = "_1";
		if (list.contains(value + duplicateSuffix)) {
			int duplicateCount = Integer.valueOf(duplicateSuffix.substring(1, duplicateSuffix.length()));
			while (list.contains(value + duplicateSuffix.replace("1", Integer.toString(duplicateCount + 1)))) {
				duplicateCount += 1;
			}
			duplicateSuffix = duplicateSuffix.replace("1", Integer.toString(duplicateCount + 1));
		}
		return duplicateSuffix;
	}
	
}
