package bizcom.bizcom;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

//The AEScrypt algorithm for hashing passwords to be sent across the network
public class AESCrypt
{
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String KEY = "1Hcfh667adfDDJ57"; //the private key
    private static final String IV = "4e5Wa71fYoT7MFEZ";
    public static String encrypt(String value) throws Exception
    {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(AESCrypt.ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, generateKey(), generateIV());
        byte [] encryptedByteValue = cipher.doFinal(value.getBytes("utf-8"));
        String encryptedValue = Base64.encodeToString(encryptedByteValue, Base64.DEFAULT);
        return encryptedValue;

    }

    public static String decrypt(String value) throws Exception
    {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(AESCrypt.ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedValue64 = Base64.decode(value, Base64.DEFAULT);
        byte [] decryptedByteValue = cipher.doFinal(decryptedValue64);
        String decryptedValue = new String(decryptedByteValue,"utf-8");
        return decryptedValue;

    }

    private static Key generateKey()
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] key = md.digest(IV.getBytes());
            return new SecretKeySpec(key,"AES");
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        return null;
    }
    private static AlgorithmParameterSpec generateIV(){
        try
        {
            return new IvParameterSpec(AESCrypt.IV.getBytes("utf-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
