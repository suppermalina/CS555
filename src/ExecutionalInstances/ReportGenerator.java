/**
 * 
 */
package ExecutionalInstances;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author mali
 *
 */
public class ReportGenerator {
	private static FileWriter output;
	private static BufferedWriter writer;
	private static FileWriter dataOutput;
	private static BufferedWriter dataWriter;
	private static FileWriter generatingSignalReporter;
	private static BufferedWriter generatingSignalWriter;
	private static FileWriter popingSignalReporter;
	private static BufferedWriter popingSignalWriter;
	private static FileWriter logOutput;
	private static BufferedWriter logWriter;
	private static FileWriter serverOut;
	private static BufferedWriter serverLogWriter;
	private static FileWriter queueOut;
	private static BufferedWriter queueLogWriter;
	public ReportGenerator() {
		generateLogWriter();
	}
	public void generateLogWriter() {
		try {
			output = new FileWriter(new File("/Users/mali/Desktop/555Project/report.txt"));
			writer = new BufferedWriter(output);
			dataOutput = new FileWriter(new File("/Users/mali/Desktop/555Project/data_report.txt"));
			dataWriter = new BufferedWriter(dataOutput);
			generatingSignalReporter = new FileWriter(new File("/Users/mali/Desktop/555Project/GeneratingReport.txt"));
			generatingSignalWriter = new BufferedWriter(generatingSignalReporter);
			popingSignalReporter = new FileWriter(new File("/Users/mali/Desktop/555Project/PopingReport.txt"));
			popingSignalWriter = new BufferedWriter(popingSignalReporter);
			logOutput = new FileWriter(new File("/Users/mali/Desktop/555Project/Log.txt"));
			logWriter = new BufferedWriter(logOutput);
			serverOut = new FileWriter(new File("/Users/mali/Desktop/555Project/ServerLog.txt"));
			serverLogWriter = new BufferedWriter(serverOut);
			queueOut = new FileWriter(new File("/Users/mali/Desktop/555Project/QueueLog.txt"));
			queueLogWriter = new BufferedWriter(queueOut);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void flushAll() {
		try {
			queueLogWriter.flush();
			serverLogWriter.flush();
			writer.flush();
			logWriter.flush();
			dataWriter.flush();
			generatingSignalWriter.flush();
			popingSignalWriter.flush();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void closeAllLog() {
		try {
			
			queueLogWriter.close();
			
			serverLogWriter.close();
			
			writer.close();
			
			logWriter.close();
			
			dataWriter.close();
			
			generatingSignalWriter.close();
			
			popingSignalWriter.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void queueLog(String str) {
		try {
			queueLogWriter.write(str);
			queueLogWriter.newLine();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void serverLog(String str) {
		try {
			serverLogWriter.write(str);
			serverLogWriter.newLine();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeRepor(String str) {
		try {
			writer.write(str);
			writer.newLine();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeLog(String str) {
		try {
			logWriter.write(str);
			logWriter.newLine();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void dataLog(String str) {
		try {
			dataWriter.write(str);
			dataWriter.newLine();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void generatingLog(String str) {
		try {
			generatingSignalWriter.write(str);
			generatingSignalWriter.newLine();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void popingLog(String str) {
		try {
			popingSignalWriter.write(str);
			popingSignalWriter.newLine();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
}
