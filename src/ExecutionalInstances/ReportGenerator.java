/**
 * 
 */
package ExecutionalInstances;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import ContainersInstance.StateList;
import Model.SystemState;

/**
 * @author mali
 *
 */
public class ReportGenerator {
	private static FileWriter output;
	private static BufferedWriter writer;
	private static FileWriter dataOutput;
	private static BufferedWriter dataWriter;
	public ReportGenerator() {
		generateLogWriter();
	}
	private static void generateLogWriter() {
		try {
			dataOutput = new FileWriter(new File("/Users/mali/Desktop/555Project/data_report.txt"));
			dataWriter = new BufferedWriter(dataOutput);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeLog(String str) {
		try {
			writer.write(str);
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void dataLog(String str) {
		try {
			dataWriter.write(str);
			dataWriter.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void closeLog() {
		try {
			if (dataWriter != null) {

				dataWriter.flush();
				dataOutput.close();
				dataWriter.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void generatingReport() {
		generateLogWriter();
		StateList list = Controller.log;
		while (list != null && !list.isEmpty()) {
			SystemState state = list.popStateOut();
			dataLog(state.toString());
		}
	}

	
}
