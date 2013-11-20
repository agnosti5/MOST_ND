package edu.rutgers.MOST.data;

import java.util.ArrayList;

public class JSBMLValidator {

	public String makeValidID(String mAbrv) {
		mAbrv = replaceInvalidSBMLIdCharacters(mAbrv);
		if (mAbrv.contains("[") && mAbrv.contains("]")) {
			mAbrv = mAbrv.replace("[","_");
			mAbrv = mAbrv.replace("]","");
		}
		
		if (mAbrv.contains("+")) {
			mAbrv = mAbrv.replace("+", SBMLConstants.PLUS_SIGN_REPLACEMENT);
		}
		
		if (!mAbrv.startsWith(SBMLConstants.METABOLITE_ABBREVIATION_PREFIX)) {
			mAbrv = SBMLConstants.METABOLITE_ABBREVIATION_PREFIX + mAbrv;
		}
				
		return mAbrv;

	}
	
	public String makeValidReactionID(String rAbrv) {
		rAbrv = replaceInvalidSBMLIdCharacters(rAbrv);
		if (rAbrv.contains("[") && rAbrv.contains("]")) {
			rAbrv = rAbrv.replace("[","_");
			rAbrv = rAbrv.replace("]","");
		}
		
		if (!rAbrv.startsWith(SBMLConstants.REACTION_ABBREVIATION_PREFIX)) {
			rAbrv = SBMLConstants.REACTION_ABBREVIATION_PREFIX + rAbrv;
		}
						
		return rAbrv;

	}
	
	public String replaceInvalidSBMLIdCharacters(String value) {		
//		for  (int i = 0; i < value.length(); i++) {
//			char c = value.charAt(i);
//			// replace all characters not A-Z, a-z, 0-9, []
//			if (!((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '_' || c == '[' || c == ']')) {
//				value.replace(c, '_');
//			}					
//		}
		// http://stackoverflow.com/questions/1805518/replacing-all-non-alphanumeric-characters-with-empty-strings/1805527#1805527
		value = value.replaceAll("[^A-Za-z0-9 ]", "_");
		if (value.contains(" ")) {
			value = value.replace(" ", "_");
		}
		// for some reason all of these make it through the above code
//		if (value.contains("-")) {
//			value = value.replaceAll("-", "_");
//		}
//		if (value.contains("(")) {
//			value = value.replace("(", SBMLConstants.PARENTHESIS_REPLACEMENT);
//		}
//		if (value.contains(")")) {
//			value = value.replace(")", SBMLConstants.PARENTHESIS_REPLACEMENT);
//		}
//		if (value.contains(" ")) {
//			value = value.replace(" ", "_");
//		}
//		if (value.contains("{")) {
//			value = value.replace("{", "[");
//		}
//		if (value.contains("}")) {
//			value = value.replace("}", "]");
//		}
//		if (value.contains(":")) {
//			value = value.replace(":", SBMLConstants.COLON_CHARACTER_REPLACEMENT);
//		}
//		if (value.contains(";")) {
//			value = value.replace(";", SBMLConstants.COLON_CHARACTER_REPLACEMENT);
//		}
//		if (value.contains("*")) {
//			value = value.replace("*", SBMLConstants.ASTERIC_REPLACEMENT);
//		}
//		if (value.contains("~")) {
//			value = value.replace("~", SBMLConstants.ASTERIC_REPLACEMENT);
//		}
//		value = replaceInvalidSBMLCharacters(value);
		// http://stackoverflow.com/questions/10574289/remove-non-ascii-characters-from-string-in-java
		String fixed = value.replaceAll("[^\\x20-\\x7e]", "");
		value = fixed;

		return value;
		
	}
	
	public String replaceInvalidSBMLCharacters(String value) {
		if (value.contains("'")) {
			value = value.replace("'", SBMLConstants.APOSTROPHE_REPLACEMENT);
		}
		if (value.contains("#")) {
			value = value.replace("#", SBMLConstants.NUMBER_SIGN_REPLACEMENT);
		}
		if (value.contains("&")) {
			value = value.replace("&", SBMLConstants.AMPERSAND_REPLACEMENT);
		}
		// http://stackoverflow.com/questions/10574289/remove-non-ascii-characters-from-string-in-java
		String fixed = value.replaceAll("[^\\x20-\\x7e]", "");
		value = fixed;
		
		return value;
		
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
