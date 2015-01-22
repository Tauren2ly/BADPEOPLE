package common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpressHelper {
	public static boolean isMatch(String text, String patternStr) {
		return text.matches(patternStr);
	}

	public static boolean hasSubStringMatch(String text, String patternStr) {
		Pattern pattern = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(text);
		return matcher.find();
	}
	
	public static String getMatchText(String text, String pattern, int groupNum) {
		Pattern patt = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		Matcher matcher = patt.matcher(text);
		if (matcher.find() && groupNum <= matcher.groupCount()) {
			return matcher.group(groupNum);
		} else {
			return null;
		}
	}
}
