package common;

import java.io.UnsupportedEncodingException;

public class Page {
	private static String CharsetASCIIName = "ASCII";
	private static String defaultCharSet = "UTF-8";
	private static String charSetReg = ";\\s*charset=([^;\\s.]+)";

	private byte[] content;

	public Page(byte[] page) {
		content = page;
	}

	public String getHead() {
		int length = getHeadLength();
		String head = "";

		if (length > 0) {
			try {
				head = new String(content, 0, length, CharsetASCIIName);
			} catch (UnsupportedEncodingException e) {
				// this should not be run
			}
		}
		return head;
	}

	public String getContent() {
		String text = "";
		int startAt = getHeadLength();
		if (startAt > 0 && startAt < content.length) {
			try {
				text = new String(content, startAt, content.length - startAt,
						getCharSet());
			} catch (UnsupportedEncodingException e) {
				// to log char set name is not supported
			}
		}
		return text;
	}

	public int getHeadLength() {
		if (content != null) {
			int length = 0;
			int state = 0;
			char compareChar = '\r';
			for (int index = 0; index < content.length; index++) {
				if (content[index] == compareChar) {
					if (state == 3) {
						length = index + 1;
						break;
					} else {
						state++;
					}
				} else {
					state = 0;
				}
				compareChar = (state == 0 || state == 2) ? '\r' : '\n';
			}
			return length;
		} else {
			return 0;
		}
	}

	public String getCharSet() {
		String charset = RegularExpressHelper.getMatchText(getHead(),
				charSetReg, 1);
		return charset == null || charset.isEmpty() ? defaultCharSet : charset;
	}
}
