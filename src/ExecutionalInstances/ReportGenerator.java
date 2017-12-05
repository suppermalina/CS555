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
	public ReportGenerator() {
		generateLogWriter();
	}
	private void generateLogWriter() {
		try {
			output = new FileWriter(new File("/Users/mali/Desktop/555Project/report.txt"));
			writer = new BufferedWriter(output);
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

	public static void exportLog() {
		try {
			if (writer != null) {
				writer.flush();
				output.close();
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void generatingReport() {
		generateLogWriter();
		StateList list = Controller.log;
		while (list != null && !list.isEmpty()) {
			SystemState state = list.popStateOut();
			writeLog(state.toString());
		}
		exportLog();
	}

	
}
