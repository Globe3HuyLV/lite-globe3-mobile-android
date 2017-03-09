package com.globe3.tno.g3_lite_mobile.security;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class G3Encryption {
    public static final String ENC_KEY = "tnoG3";
    public static final String ENC_SALT = "27062016";

    private static final String TAG = "EncryptionUtility";

    private final Builder builder;

    private G3Encryption(Builder builder) {
        this.builder = builder;
    }

    public static G3Encryption getDefault(String key, String salt, byte[] iv) {
        try {
            return Builder.getDefaultBuilder(key, salt, iv).build();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    public String encrypt(String data) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, InvalidKeySpecException, BadPaddingException, IllegalBlockSizeException {
        if (data == null) return null;
        SecretKey secretKey = getSecretKey(hashTheKey(builder.getKey()));
        byte[] dataBytes = data.getBytes(builder.getCharsetName());
        Cipher cipher = Cipher.getInstance(builder.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, builder.getIvParameterSpec(), builder.getSecureRandom());
        return Base64.encodeToString(cipher.doFinal(dataBytes), builder.getBase64Mode());
    }

    public String encryptOrNull(String data) {
        try {
            return encrypt(data);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    public void encryptAsync(String data, final Callback callback) {
        if (callback == null) return;
        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {
                try {
                    String encrypt = encrypt(params[0]);
                    if (encrypt == null) {
                        callback.onError(new Exception("Encrypt return null, it normally occurs when you send a null data"));
                    }
                    return encrypt;
                } catch (Exception e) {
                    callback.onError(e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result != null) callback.onSuccess(result);
            }

        }.execute(data);
    }

    public String decrypt(String data) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        if (data == null) return null;
        byte[] dataBytes = Base64.decode(data, builder.getBase64Mode());
        SecretKey secretKey = getSecretKey(hashTheKey(builder.getKey()));
        Cipher cipher = Cipher.getInstance(builder.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, secretKey, builder.getIvParameterSpec(), builder.getSecureRandom());
        byte[] dataBytesDecrypted = (cipher.doFinal(dataBytes));
        return new String(dataBytesDecrypted);
    }

    public String decryptOrNull(String data) {
        try {
            return decrypt(data);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    public void decryptAsync(String data, final Callback callback) {
        if (callback == null) return;
        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {
                try {
                    String decrypt = decrypt(params[0]);
                    if (decrypt == null) {
                        callback.onError(new Exception("Decrypt return null, it normally occurs when you send a null data"));
                    }
                    return decrypt;
                } catch (Exception e) {
                    callback.onError(e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result != null) callback.onSuccess(result);
            }

        }.execute(data);
    }

    private SecretKey getSecretKey(char[] key) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(builder.getSecretKeyType());
        KeySpec spec = new PBEKeySpec(key, builder.getSalt().getBytes(builder.getCharsetName()), builder.getIterationCount(), builder.getKeyLength());
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), builder.getAlgorithm());
    }

    private char[] hashTheKey(String key) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(builder.getDigestAlgorithm());
        messageDigest.update(key.getBytes(builder.getCharsetName()));
        return Base64.encodeToString(messageDigest.digest(), Base64.NO_PADDING).toCharArray();
    }

    public static interface Callback {
        public void onSuccess(String result);
        public void onError(Exception exception);
    }

    public static class Builder {
        private byte[] mIv;
        private int mKeyLength;
        private int mBase64Mode;
        private int mIterationCount;
        private String mSalt;
        private String mKey;
        private String mAlgorithm;
        private String mCharsetName;
        private String mSecretKeyType;
        private String mDigestAlgorithm;
        private String mSecureRandomAlgorithm;
        private SecureRandom mSecureRandom;
        private IvParameterSpec mIvParameterSpec;

        public static Builder getDefaultBuilder(String key, String salt, byte[] iv) {
            return new Builder()
                    .setIv(iv)
                    .setKey(key)
                    .setSalt(salt)
                    .setKeyLength(128)
                    .setCharsetName("UTF8")
                    .setIterationCount(65536)
                    .setDigestAlgorithm("SHA1")
                    .setBase64Mode(Base64.DEFAULT)
                    .setAlgorithm("AES/CBC/PKCS5Padding")
                    .setSecureRandomAlgorithm("SHA1PRNG")
                    .setSecretKeyType("PBKDF2WithHmacSHA1");
        }

        public G3Encryption build() throws NoSuchAlgorithmException {
            setSecureRandom(SecureRandom.getInstance(getSecureRandomAlgorithm()));
            setIvParameterSpec(new IvParameterSpec(getIv()));
            return new G3Encryption(this);
        }

        private String getCharsetName() {
            return mCharsetName;
        }

        public Builder setCharsetName(String charsetName) {
            mCharsetName = charsetName;
            return this;
        }

        private String getAlgorithm() {
            return mAlgorithm;
        }

        public Builder setAlgorithm(String algorithm) {
            mAlgorithm = algorithm;
            return this;
        }

        private int getBase64Mode() {
            return mBase64Mode;
        }

        public Builder setBase64Mode(int base64Mode) {
            mBase64Mode = base64Mode;
            return this;
        }

        private String getSecretKeyType() {
            return mSecretKeyType;
        }

        public Builder setSecretKeyType(String secretKeyType) {
            mSecretKeyType = secretKeyType;
            return this;
        }

        private String getSalt() {
            return mSalt;
        }

        public Builder setSalt(String salt) {
            mSalt = salt;
            return this;
        }

        private String getKey() {
            return mKey;
        }

        public Builder setKey(String key) {
            mKey = key;
            return this;
        }

        private int getKeyLength() {
            return mKeyLength;
        }

        public Builder setKeyLength(int keyLength) {
            mKeyLength = keyLength;
            return this;
        }

        private int getIterationCount() {
            return mIterationCount;
        }

        public Builder setIterationCount(int iterationCount) {
            mIterationCount = iterationCount;
            return this;
        }

        private String getSecureRandomAlgorithm() {
            return mSecureRandomAlgorithm;
        }

        public Builder setSecureRandomAlgorithm(String secureRandomAlgorithm) {
            mSecureRandomAlgorithm = secureRandomAlgorithm;
            return this;
        }

        private byte[] getIv() {
            return mIv;
        }

        public Builder setIv(byte[] iv) {
            mIv = iv;
            return this;
        }

        private SecureRandom getSecureRandom() {
            return mSecureRandom;
        }

        public Builder setSecureRandom(SecureRandom secureRandom) {
            mSecureRandom = secureRandom;
            return this;
        }

        private IvParameterSpec getIvParameterSpec() {
            return mIvParameterSpec;
        }

        public Builder setIvParameterSpec(IvParameterSpec ivParameterSpec) {
            mIvParameterSpec = ivParameterSpec;
            return this;
        }

        private String getDigestAlgorithm() {
            return mDigestAlgorithm;
        }

        public Builder setDigestAlgorithm(String digestAlgorithm) {
            mDigestAlgorithm = digestAlgorithm;
            return this;
        }
    }

}