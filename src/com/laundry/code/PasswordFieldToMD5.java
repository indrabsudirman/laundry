/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.laundry.code;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;

public class PasswordFieldToMD5 {

//    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException { 
//                   
//        char[] password ={ 'l', 'a', 'u', 'n', 'd', 'r', 'i', 'k', 'u'};
//        byte [] salt = getSalt();
//        String saltInHex = byteArrayToHexString (salt);
//        byte [] saltTwo = hexStringToByteArray(saltInHex);
//        
//        String passwordMD5WithSalt = digest (password, salt);
//        System.out.println("Your plaintext password is : " + new String(password));
//        System.out.println("Your password MD5 with salt is : " + passwordMD5WithSalt);
//        System.out.println("Nilai salt adalah : " +saltInHex);
//        System.out.println("Nilai salt dalam byte adalah : " +Arrays.toString(saltTwo));
//
//    }
    
    public static String digest (char [] in, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        
        ArrayList<Byte> list = new ArrayList<Byte>();
        for (int i = 0; i<in.length; i++) {
            byte b = (byte) in[i];
                    list.add(b); 
        }
        byte[] inputInByte = new byte[list.size()];
        for (int i = 0; i<list.size(); i++) {
            inputInByte[i] = list.get(i);
        }
        messageDigest.update(salt);
        messageDigest.update(inputInByte);
        
        byte byteData[] = messageDigest.digest();
        
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < byteData.length; i++) {
            String hex = Integer.toHexString(0xff & byteData[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    public static byte[] getSalt() throws NoSuchAlgorithmException, NoSuchProviderException {
        //Always use a SecureRandom generator
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
        //Create array for salt
        byte [] salt = new byte[16];
        //Get a random salt
        secureRandom.nextBytes(salt);
        //return salt
        return salt;
    }
    
    final protected static char [] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public static String byteArrayToHexString (byte [] bytes) {
        char [] hexChars = new char [bytes.length * 2];
        int v;
        
        for (int i = 0; i <bytes.length; i++) {
            v = bytes[i] & 0xff;
            hexChars [ i * 2] = hexArray [v >>> 4];
            hexChars [ i * 2 + 1] = hexArray [v & 0x0f];
        }
        return new String (hexChars);
    }
    
    public static byte [] hexStringToByteArray (String string) {
        int len = string.length();
        byte [] data = new byte [len / 2];
        
        for (int i = 0; i < len; i += 2) {
            data [i/2] = (byte) ((Character.digit(string.charAt(i), 16) << 4) + Character.digit(string.charAt(i + 1), 16));
        }
        return data;
    }
}
