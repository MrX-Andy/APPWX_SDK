package com.appwx.sdk.md5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

	/**
	 * º”√‹
	 * @param s
	 * @return
	 */
	public final static String compile(String s) {
        char[] hex = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        byte[] tmp = s.getBytes();
        try {
            MessageDigest dig = MessageDigest.getInstance("MD5");
            dig.update(tmp);
            byte[] md = dig.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hex[byte0 >>> 4 & 0xf];
                str[k++] = hex[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return null;
        }
    }
	
}
