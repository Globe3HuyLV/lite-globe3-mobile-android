package com.neurotec.report;

import android.text.format.DateFormat;
import android.util.Log;

import com.neurotec.util.EnvironmentUtils;

import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;

import java.io.File;
import java.io.PrintWriter;
import java.util.Date;

public final class CrashReporter implements ReportSender {

	// ===========================================================
	// Private static fields
	// ===========================================================

	private static final String TAG = CrashReporter.class.getSimpleName();


	// ===========================================================
	// Public methods
	// ===========================================================

	@Override
	public void send(CrashReportData report) throws ReportSenderException {
		PrintWriter writer = null;
		try {
			File directory = new File(EnvironmentUtils.REPORTS_DIRECTORY_PATH);
			if (directory.exists()) {
				String fileName = String.format("report_%s.txt", DateFormat.format(EnvironmentUtils.DATE_FORMAT, new Date()));
				File file = new File(directory, fileName);
				if (file != null) {
					writer = new PrintWriter(file);
					for (ReportField field : report.keySet()) {
						writer.println(field + ": " + report.getProperty(field));
					}
				}
			} else {
				Log.e(TAG, "Could not create reports directory.");
			}
		} catch (Exception e) {
			Log.e(TAG, "Exception", e);
		} finally {
			if (writer != null) {
				writer.close();
				writer = null;
			}
		}
	}
}
