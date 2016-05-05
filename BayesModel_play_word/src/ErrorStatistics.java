import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

public class ErrorStatistics {

	private String folder = "C:\\Users\\Nicolette\\OneDrive\\Documents\\EMBRACE\\Analysis\\With user step\\log data by student\\log data with play word";
	private String inputDataFolder = "inputdata/";
	private HashMap<String, Integer> wordToHelpRequests;
	private HashMap<String, Integer> wordToErrors;
	private StudentLogData student;
	private StringBuilder sb = new StringBuilder("");
	private static InitMaps initMaps;

	public static void main(String[] args) {
		ErrorStatistics es = new ErrorStatistics();
		readStoryFiles();
		es.analyze();
	}

	private void analyze() {
		File dir = new File(folder);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			File resultFile = new File(dir.getPath() + "\\Error_Statistics.csv");
			try {
				FileUtils.writeStringToFile(resultFile, "Word,Help Requests,Errors\n", true);
				for (File child : directoryListing) {
					String fileName = child.getName();
					if (fileName.contains("log_data")) {
						File logfile = new File(dir.getPath() + "\\" + fileName);
						if (logfile.exists()) {
							analyzeLogFile(logfile, resultFile);
						} else {
							System.out.println("=======> Log file does not exist..." + logfile.getName());
						}
					}
				}
				FileUtils.writeStringToFile(resultFile, sb.toString(), true);
			} catch (IOException ie) {
				System.out.println(ie.getMessage());
			}

		}
	}

	private void analyzeLogFile(File logFile, File resultFile) {
		// readStudentData(logFile);
		ReadFiles readFiles = new ReadFiles();
		try {
			student = readFiles.readLogData(logFile.getCanonicalPath().toString());
			String studentId = logFile.getName().split("_")[2].split("\\.")[0];
			readInputData(readFiles, studentId);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void readStudentData(File filename) {
		ReadFiles readFiles = new ReadFiles();
		try {
			student = readFiles.readLogData(filename.getCanonicalPath().toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void readInputData(ReadFiles readFiles, String studentid) {
		File dir = new File(inputDataFolder);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				String fileName = child.getName();
				if (fileName.contains(studentid)) {
					File inputDataFile = new File(dir.getPath() + "\\" + fileName);
					if (inputDataFile.exists()) {
						ArrayList<String> inputDataList = readFiles.readInputData(inputDataFile.getAbsolutePath(),
								student);
						processInputData(inputDataList);
						student.setInputData(inputDataList);
						System.out.println(student.getInputData());
					} else {
						System.out.println("=======> Log file does not exist..." + inputDataFile.getName());
					}
				}
			}
		}
	}

	private void processInputData(ArrayList<String> inputDataList) {
		for (String inputData : inputDataList) {
			if (inputData.contains("pen1") || inputData.contains("pen2") || inputData.contains("pen3")
					|| inputData.contains("pen4")) {
				inputData = "pen";
			} else if (inputData.contains("corralDoor") || inputData.contains("corralArea")) {
				inputData = "corral";
			} else if (inputData.contains("pumpkinPatch") || inputData.contains("pumpkin")) {
				inputData = "pumpkins";
			} else if (inputData.contains("farmerFall")) {
				inputData = "farmer";
			} else if (inputData.contains("nearGoat")) {
				inputData = "goat";
			} else if (inputData.contains("trophyS")) {
				inputData = "trophy";
			} else if (inputData.contains("healthyS")) {
				inputData = "healthy	";
			}
		}
	}

	private static void readStoryFiles() {
		ReadFiles readFiles = new ReadFiles();
		initMaps = readFiles.readPropertiesFile(Constants.WORDS_FILE, Constants.ACTION_FILE);
	}
}
