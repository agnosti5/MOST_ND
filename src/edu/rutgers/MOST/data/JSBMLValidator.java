package edu.rutgers.MOST.data;

import java.util.ArrayList;

public class JSBMLValidator {

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
		if (value.contains("&")) {
			System.out.println("and");
			value = value.replace("&", SBMLConstants.AMPERSAND_REPLACEMENT);
			System.out.println(value);
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
