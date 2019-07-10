package com.example.druid.untils;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class MD5Util {
    protected  MD5Util(){

    }
    private  static  final String ALGORITH_NAME = "md5";
    private static final int HASH_ITERATIONS = 5;

    public static String encrypt(String username,String password){
        String souce = StringUtils.lowerCase(username);
        password = StringUtils.lowerCase(password);
        return new SimpleHash(ALGORITH_NAME,password, ByteSource.Util.bytes(souce),HASH_ITERATIONS).toHex();
    }
}
