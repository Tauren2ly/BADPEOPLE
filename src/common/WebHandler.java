package common;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;

public class WebHandler {

	public static String UriRegularExpression = "";
	public String Url;

	private Map<String, String> headers = new HashMap<String, String>();

	public WebHandler() {

	}

	public WebHandler(String url) {
		Url = url;
		headers.put(HttpHeadKey.Host, HttpHelper.getHost(url));
	}

	public byte[] Request(RequestMethod method,
			Map<String, String> headOptions, String params)
			throws UnknownHostException, UnsupportedEncodingException {
		String request = GenerateRequest(method, headOptions, params);
		return SocketHelper.SendMsgAsync(HttpHelper.getHost(Url), HttpHelper.getPort(Url),
				request.getBytes("ASCII"));
	}

	private String GenerateRequest(RequestMethod requestMethod,
			Map<String, String> headOptions, String params) {
		StringBuilder request = new StringBuilder();

		// add request line to first line in request string
		request.append(GetMethodString(requestMethod) + " "
				+ HttpHelper.getRelativeUrl(Url) + " HTTP/1.1\r\n");

		// add headers
		request.append(GenerateHeader(headOptions));

		if (requestMethod == RequestMethod.POST) {
			request.append(params + "\r\n");
		}

		request.append("\r\n");
		return request.toString();
	}

	private String GetMethodString(RequestMethod method) {
		return method.name().toUpperCase();
	}

	private String GenerateHeader(Map<String, String> params) {
		StringBuilder headerText = new StringBuilder();

		Class<HttpHeadKey> keyType = HttpHeadKey.class;
		for (Field filed : keyType.getDeclaredFields()) {
			if (headers.containsKey(filed.getName())) {
				headerText.append(filed.getName() + ": "
						+ headers.get(filed.getName()));
			}
		}

		// add the end blank line
		headerText.append("\r\n");
		return headerText.toString();
	}
}
