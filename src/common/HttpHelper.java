package common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class HttpHelper {
	private static String httpPrefix = "http://";
	private static String httpPrefixReg = "^HTTP[s]?://";
	private static String validUrlReg = "http[s]?://([^/?&:]+):?(\\d*)(/.*)";
	private static String invalidUrlWithoutRelativeUri = "http[s]?://([^/?&:]+):?(\\d*)";
	private static String invalidUrlOnlyHost = "([^/?&:]+):?(\\d*)";
	private static String invalidUrlWithoutPrefix = "([^/?&:]+):?(\\d*)(/.*)";

	public static String toValideUrl(String shortUrl) {
		if (isValideUrl(shortUrl)) {
			return shortUrl;
		} else if (isInvalidUrlWithoutRelativeUri(shortUrl)) {
			return shortUrl + "/";
		} else if (isInvalideUrlOnlyHost(shortUrl)
				|| isInvalidUrlWithoutPrefix(shortUrl)) {
			String validUrl = "http://" + shortUrl;
			if(!validUrl.endsWith("/")){
				validUrl += "/";
			}
			return validUrl;
		} else {
			return null;
		}
	}

	public static boolean isValideUrl(String url) {
		return RegularExpressHelper.isMatch(url, validUrlReg);
	}

	public static boolean isInvalidUrlWithoutRelativeUri(String url) {
		return RegularExpressHelper.isMatch(url, invalidUrlWithoutRelativeUri);
	}

	public static boolean isInvalideUrlOnlyHost(String url) {
		return RegularExpressHelper.isMatch(url, invalidUrlOnlyHost);
	}

	public static boolean isInvalidUrlWithoutPrefix(String url) {
		return RegularExpressHelper.isMatch(url, invalidUrlWithoutPrefix);
	}

	public static boolean hasHttpPrefix(String url) {
		Pattern pattern = Pattern.compile(httpPrefixReg,
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(url);
		return matcher.find();
	}

	public static String addHttpPrefix(String url) {
		String urlToReturn = url;
		if (!hasHttpPrefix(url)) {
			urlToReturn = httpPrefix + url.trim();
		}
		return urlToReturn;
	}

	public static String getHost(String url) {
		String validUrl = toValideUrl(url);
		if (validUrl != null) {
			return RegularExpressHelper.getMatchText(validUrl, validUrlReg, 1);
		} else {
			return null;
		}
	}

	public static String getRelativeUrl(String url) {
		String validUrl = toValideUrl(url);
		if (validUrl == null) {
			return null;
		} else {
			return RegularExpressHelper.getMatchText(validUrl, validUrlReg, 3);
		}
	}

	public static int getPort(String url) {
		String validUrl = toValideUrl(url);
		if (validUrl == null) {
			return 80;
		} else {
			String port = RegularExpressHelper.getMatchText(validUrl, validUrlReg, 2);
			if (port == null || port.isEmpty()) {
				return 80;
			} else {
				return Integer.parseInt(port);
			}
		}
	}

	public static String removeHttpPrefix(String url) {
		Pattern pattern = Pattern.compile(httpPrefixReg,
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(url);
		return matcher.replaceAll("");
	}

	public static String replaceString(String text, String replacement,
			String regex) {
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(text);
		return matcher.replaceAll(replacement);
	}
}