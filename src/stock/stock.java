package stock;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import common.Page;
import common.RequestMethod;
import common.WebHandler;

public class stock {
	public static void main(String[] argv) {
		WebHandler handler = new WebHandler("http://cn.bing.com/", 80);
		try {
			byte[] result;
			result = handler.Request(RequestMethod.GET, null, "");

			Page pg = new Page(result);
			System.out.println(pg.getCharSet());
			System.out.println(pg.getHeadLength());
			System.out.println(pg.getHead());
			System.out.println(pg.getContent());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
