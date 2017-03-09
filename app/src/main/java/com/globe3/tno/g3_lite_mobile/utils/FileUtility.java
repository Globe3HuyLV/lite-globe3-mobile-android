package com.globe3.tno.g3_lite_mobile.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.globe3.tno.g3_lite_mobile.globals.Globals;

import static com.globe3.tno.g3_lite_mobile.globals.Globals.APP_NAME;

public class FileUtility {
    public static boolean createAppFolders(Activity activity){
        try {
            System.setProperty("jna.nounpack", "true");
            System.setProperty("java.io.tmpdir", activity.getCacheDir().getAbsolutePath());

            File dir = new File(Globals.GLOBE3_DIR, "data");
            if (!dir.exists()) { dir.mkdirs(); }
            dir = new File(Globals.GLOBE3_DIR, "timelog");
            if (!dir.exists()) { dir.mkdirs(); }
            dir = new File(Globals.GLOBE3_DIR, "images");
            if (!dir.exists()) { dir.mkdirs(); }
            dir = new File(Globals.GLOBE3_DIR, "db");
            if (!dir.exists()) { dir.mkdirs(); }
            dir = new File(Globals.GLOBE3_DIR, "project_photos");
            if (!dir.exists()) { dir.mkdirs(); }
            return true;
        } catch (Exception e) {
            Log.e(APP_NAME, "Exception", e);
            return false;
        }
    }
    public static byte[] getFileBlob(String filepath){
        FileInputStream fileInputStream=null;
        File file = new File(filepath);

        if(file.exists()){
            byte[] bFile = new byte[(int) file.length()];
            try {
                fileInputStream = new FileInputStream(file);
                fileInputStream.read(bFile);
                fileInputStream.close();

                for (int i = 0; i < bFile.length; i++) {
                    System.out.print((char)bFile[i]);
                }
            }catch(Exception e){
                e.printStackTrace();
                return null;
            }

            return bFile;
        }else{
            return null;
        }
    }

    public static String getBase64(String filePath){
        Bitmap bitmapOrg = BitmapFactory.decodeFile(filePath);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 100, bao);
        byte [] ba = bao.toByteArray();
        String b64= Base64.encodeToString(ba, Base64.NO_WRAP);

        return b64;
    }

    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public static byte[] getImage(String b64){
        if(b64.length() > 0){
            byte[] decodedString = Base64.decode(b64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            ByteArrayOutputStream imageByteArray = new ByteArrayOutputStream();

            try{
                decodedByte.compress(Bitmap.CompressFormat.JPEG, 100, imageByteArray);

                byte[] imageByte = imageByteArray.toByteArray();

                imageByte[13] = 00000001;
                imageByte[14] = 00000001;
                imageByte[15] = (byte) 244;
                imageByte[16] =  00000001;
                imageByte[17] = (byte) 244;

                return imageByte;
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }

        }else{
            return null;
        }
    }

    public static Bitmap decodeFile(File f) {
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            final int REQUIRED_SIZE=512;

            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }

    public static void fileDelete(String path){
        File file = new File(path);
        file.delete();
    }
}
