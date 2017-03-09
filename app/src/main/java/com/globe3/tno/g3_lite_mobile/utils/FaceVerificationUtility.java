package com.globe3.tno.g3_lite_mobile.utils;

import android.os.Environment;

import com.neurotec.util.concurrent.AggregateExecutionException;

import java.util.List;

/**
 * Created by huylv on 22/02/2017.
 */

public class FaceVerificationUtility {

    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String NEUROTECHNOLOGY_DIRECTORY = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + FILE_SEPARATOR + "Neurotechnology";

    public static String getMessages(Throwable throwable) {
        if (throwable == null) throw new NullPointerException("Null Pointer Exception!");
        String msg = "";
        if (throwable instanceof AggregateExecutionException) {
            List<Throwable> causes = ((AggregateExecutionException) throwable).getCauses();
            if (causes.size() > 0) {
                Throwable cause = causes.get(0);
                msg = cause.getMessage() != null ? cause.getMessage() : cause.toString();
            }
        } else {
            msg = throwable.getMessage() != null ? throwable.getMessage() : throwable.toString();
        }
        return msg;
    }

    public static String combinePath(String... folders) {
        String path = "";
        for (String folder : folders) {
            path = path.concat(FILE_SEPARATOR).concat(folder);
        }
        return path;
    }
}
