package com.monitor.bilibili_monitor.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.apifan.common.random.source.InternetSource;
import io.github.admin4j.http.HttpRequest;
import okhttp3.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


public class ParameterEncryptionUtil {

//    public static void main(String[] args) {
//        String bvid = "BV1wy4y1D7JT/";
//        System.out.println(bvid.substring(bvid.length() -1));
//        bvid = bvid.substring(0, bvid.length() - 1);
//        System.out.println(bvid);
//        Map<String, Object> parameterMap = new HashMap<>();
//        parameterMap.put("mid", "384641605");
//        parameterMap.put("token", "");
//        parameterMap.put("platform", "web");
//        parameterMap.put("web_location", "1550101");
//        encryption(parameterMap);
//        System.out.println(parameterMap);
//    }

    public static String encryption(Map<String, Object> t) {
        String i = "b7f68cd5444d413bb404a0a6ae865fd7";
        String a = "bdaf788453014d499225e2cd3fa82b26";
        String[] wbiKeys = getWbiKeys();
        //获取最新wbiKey
//        if (wbiKeys.length > 0) {
//            i = wbiKeys[0];
//            a = wbiKeys[1];
//        }
        String n = i + a;
        List<String> r = new ArrayList<>();
        List<Integer> d = Arrays.asList(46, 47, 18, 2, 53, 8, 23, 32, 15, 50, 10, 31, 58, 3, 45, 35, 27, 43, 5, 49, 33,
                9, 42, 19, 29, 28, 14, 39, 12, 38, 41, 13, 37, 48, 7, 16, 24, 55, 40, 61, 26, 17, 0, 1, 60, 51, 30, 4,
                22, 25, 54, 21, 56, 59, 6, 63, 57, 62, 11, 36, 20, 34, 44, 52);
        for (Integer integer : d) {
            if (n.charAt(integer) != 0) {
                r.add(String.valueOf(n.charAt(integer)));
            }
        }
        String c = String.join("", r).substring(0, 32);
        int s = (int) Math.round(System.currentTimeMillis() / 1000.0);
        t.put("wts", s);
        List<String> dKeys = new ArrayList<>(t.keySet());
        Collections.sort(dKeys);
        List<String> h = new ArrayList<>();
        String v = "[!'()*]";
        for (String key : dKeys) {
            Object g = t.get(key);
            if (g instanceof String) {
                String strG = (String) g;
                strG = strG.replaceAll(v, "");
                strG = urlEncode(strG);
                g = strG;
            }
            if (g != null) {
                h.add(urlEncode(key) + "=" + g);
            }
        }
        String b = String.join("&", h);
        String md = b + c;
        String w_rid = u(md);
        t.put("w_rid", w_rid);
        return w_rid;
    }

    private static String[] getWbiKeys() {
        HttpRequest httpRequest = HttpRequest.get("https://api.bilibili.com/x/web-interface/nav");
        try {
            Response response = httpRequest.execute();
            if (response.isSuccessful() && null != response.body()) {
                JSONObject jsonObject = JSON.parseObject(response.body().string());
                JSONObject wbi = jsonObject.getJSONObject("data").getJSONObject("wbi_img");
                String[] imgUrls = wbi.getString("img_url").split("/");
                String imgUrl = imgUrls[imgUrls.length - 1].split("\\.")[0];
                String[] subUrls = wbi.getString("sub_url").split("/");
                String subUrl = subUrls[imgUrls.length - 1].split("\\.")[0];
                return new String[]{imgUrl, subUrl};
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new String[]{};
    }

    private static String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return value;
    }

    private static String u(String t) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] encodedHash = digest.digest(t.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getDevId() {
        char[] b = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] s = "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".toCharArray();
        for (int i = 0; i < s.length; i++) {
            if ('-' == s[i] || '4' == s[i]) {
                continue;
            }
            int randomInt = (int) (16 * Math.random());
            if ('x' == s[i]) {
                s[i] = b[randomInt];
            } else {
                s[i] = b[3 & randomInt | 8];
            }
        }
        return new String(s);
    }

    public static String getRandomUserAgent() {
        return InternetSource.getInstance().randomPCUserAgent();
    }


    public static String aidToBvid(int aid) {
        final String TABLE = "fZodR9XQDSUm21yCkr6zBqiveYah8bt4xsWpHnJE7jL5VG3guMTKNPAwcF";
        final int XOR = 177451812;
        final long ADD = 8728348608L;
        long x = (aid ^ XOR) + ADD;
        final int[] S = new int[]{11, 10, 3, 8, 4, 6};
        char[] chars = new char[]{'B', 'V', '1', ' ', ' ', '4', ' ', '1', ' ', '7', ' ', ' '};
        for (int i = 0; i < 6; i++) {
            int pow = (int) Math.pow(58, i);
            long i1 = x / pow;
            int index = (int) (i1 % 58);
            chars[S[i]] = TABLE.charAt(index);
        }
        return String.valueOf(chars);
    }

    public static String bVidToAid(String bvid) {
        bvid = bvid.substring(0, bvid.length() - 1);
        final String TABLE = "fZodR9XQDSUm21yCkr6zBqiveYah8bt4xsWpHnJE7jL5VG3guMTKNPAwcF";
        final int XOR = 177451812;
        final long ADD = 8728348608L;
        final int[] S = new int[]{11, 10, 3, 8, 4, 6};
        final Map<Character, Integer> MAP = new HashMap<>();
        for (int i = 0; i < 58; i++) {
            MAP.put(TABLE.charAt(i), i);
        }
        long r = 0;
        for (int i = 0; i < 6; i++) {
            r += MAP.get(bvid.charAt(S[i])) * Math.pow(58, i);
        }
        return String.valueOf((r - ADD) ^ XOR);
    }
}
