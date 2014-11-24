package com.piscen.huakai.common;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class StringUtils {

	public static String trim(String str) {
		return null == str ? null : str.trim();
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static String join(Collection<String> collection, String separator) {
		if (collection == null) {
			return null;
		}
		return join(collection.iterator(), separator);
	}

	public static String join(Iterator<String> iterator, String separator) {

		// handle null, zero and one elements before building a buffer
		if (iterator == null) {
			return null;
		}
		if (!iterator.hasNext()) {
			return "";
		}
		Object first = iterator.next();
		if (!iterator.hasNext()) {
			return first == null ? "" : first.toString();
		}

		// two or more elements
		StringBuffer buf = new StringBuffer(256); // Java default is 16,
		// probably too small
		if (first != null) {
			buf.append(first);
		}

		while (iterator.hasNext()) {
			if (separator != null) {
				buf.append(separator);
			}
			Object obj = iterator.next();
			if (obj != null) {
				buf.append(obj);
			}
		}
		return buf.toString();
	}

	public static String join(Object[] array, String separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}

	public static String join(Object[] array, String separator, int startIndex,
			int endIndex) {
		if (array == null) {
			return null;
		}
		if (separator == null) {
			separator = "";
		}

		// endIndex - startIndex > 0: Len = NofStrings *(len(firstString) +
		// len(separator))
		// (Assuming that all Strings are roughly equally long)
		int bufSize = (endIndex - startIndex);
		if (bufSize <= 0) {
			return "";
		}

		bufSize *= ((array[startIndex] == null ? 16 : array[startIndex]
				.toString().length()) + separator.length());

		StringBuffer buf = new StringBuffer(bufSize);

		for (int i = startIndex; i < endIndex; i++) {
			if (i > startIndex) {
				buf.append(separator);
			}
			if (array[i] != null) {
				buf.append(array[i]);
			}
		}
		return buf.toString();
	}

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static String[] split(String str, String separatorChars) {
		return splitWorker(str, separatorChars, -1, false);
	}

	private static String[] splitWorker(String str, String separatorChars,
			int max, boolean preserveAllTokens) {
		// Performance tuned for 2.0 (JDK1.4)
		// Direct code is quicker than StringTokenizer.
		// Also, StringTokenizer uses isSpace() not isWhitespace()

		if (str == null) {
			return null;
		}
		int len = str.length();
		if (len == 0) {
			return new String[0];
		}
		List<String> list = new ArrayList<String>();
		int sizePlus1 = 1;
		int i = 0, start = 0;
		boolean match = false;
		boolean lastMatch = false;
		if (separatorChars == null) {
			// Null separator means use whitespace
			while (i < len) {
				if (Character.isWhitespace(str.charAt(i))) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				}
				lastMatch = false;
				match = true;
				i++;
			}
		} else if (separatorChars.length() == 1) {
			// Optimise 1 character case
			char sep = separatorChars.charAt(0);
			while (i < len) {
				if (str.charAt(i) == sep) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				}
				lastMatch = false;
				match = true;
				i++;
			}
		} else {
			// standard case
			while (i < len) {
				if (separatorChars.indexOf(str.charAt(i)) >= 0) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				}
				lastMatch = false;
				match = true;
				i++;
			}
		}
		if (match || (preserveAllTokens && lastMatch)) {
			list.add(str.substring(start, i));
		}
		return list.toArray(new String[list.size()]);
	}

	public static String MD5(String str) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	public static String formatDate(Date date) {
		SimpleDateFormat dateformat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return dateformat.format(date);
	}

	/**
	 * 截取
	 * 
	 * @param t
	 * @return
	 */
	public static <T> String execute(T t) {
		String s = t.getClass().getName();
		int i = s.lastIndexOf(".");
		String className = s.substring(i + 1);
		return className;
	}

	public static long strDateToMs(String strDate) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date date = df.parse(strDate);
		return date.getTime();
	}

	public static String formatDate(String l) {
		// System.out.println("time:"+l);

		long timeMS;
		try {
			timeMS = strDateToMs(l);

			long MS = 86400000;// 一天的毫秒数

			long currentMS = System.currentTimeMillis();// 当前毫秒
			long todayMS = 0;// 当天毫秒数
			long yesterdayMS = 0;// 昨天毫秒数
			long byesterdayMS = 0;// 前天毫秒数
			Date toDay = new Date(currentMS);
			DateFormat format1 = new SimpleDateFormat("yyyy.MM.dd");
			DateFormat format2 = new SimpleDateFormat("HH:mm");
			String toDayStr = format1.format(toDay);

			toDay = format1.parse(toDayStr);
			todayMS = toDay.getTime();
			yesterdayMS = todayMS - MS;
			byesterdayMS = yesterdayMS - MS;
			if (timeMS >= todayMS) {
				// 今天
				Date d = new Date(timeMS);
				return "今天 " + format2.format(d);
			} else if (timeMS >= yesterdayMS) {
				// 昨天
				Date d = new Date(timeMS);
				return "昨天 " + format2.format(d);
			} else if (timeMS >= byesterdayMS) {
				// 前天
				Date d = new Date(timeMS);
				return "前天 " + format2.format(d);
			} else {
				// 正常
				Date d = new Date(timeMS);
				return format1.format(d);
			}

		} catch (ParseException e) {
			e.printStackTrace();

		}
		return null;
	}
	public static String getNameFromlist(List<String> name){
		StringBuilder sb = new StringBuilder();
		if(0 != name.size()){
			for (int i = 0; i < name.size(); i++) {
				if(!name.get(i).toString().equals("")){
					if(i == name.size()-1){
						sb.append(name.get(i));
					}else{
						sb.append(name.get(i)+",");
					}
					}
			}
			return sb.toString();
		}
		return null;
	}
	public static String getNewImageName(String oldImageName) {
		if (null != oldImageName) {
			StringBuilder s1 = new StringBuilder(oldImageName);
			s1.insert(0, "a");
			if (null != s1.toString()) {
				return s1.toString();
			}
		}
		return null;
	}

	public static String getNewMiddlerImageName(String oldImageName) {
		if (null != oldImageName) {
			StringBuilder s1 = new StringBuilder(oldImageName);
			s1.insert(0, "b");
			if (null != s1.toString()) {
				return s1.toString();
			}
		}
		return null;
	}

	public static String getSmallImageName(String name) {
		String imageName = name.substring(name.lastIndexOf("/") + 1,
				name.length());
		String[] s = imageName.split("-");
		return s[2];
	}

	public static int getIndex(String[] arr,String s){
		if(null==s)return 0;
		for(int i=0;i<arr.length;i++){
			if(arr[i].equals(s))return i;
		}
		return 0;
	}

	public static  String broken_width[] = new String[] {"155", "165", "175", "185", "195","205","P205","LT205","215","P215","LT215",
		"225","P225","LT225","235","P235","LT235","245","P245","LT245","255","P255","LT255",
		"265","P265","LT265","275","P275","LT275","285","P285","LT285","295","305","315","325",
		"LT325","335","345","355","30*9.50","31*10.50","37*12.50"};
	public static String flat_width[] = new String[] {"30", "35", "40", "45", "50","55","60","65","70","75","80","85","无"};
	public static String tyre_width[] = new String[] {"R", "RF", "ZR", "ZRF"};
	public static String tyre2_width[] = new String[] {"12", "13", "14", "15", "16","17","18","19","20","21"};
}
