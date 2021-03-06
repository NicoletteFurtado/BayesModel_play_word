import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;

public class ReadFiles {
	public StudentLogData readLogData(String filename) {
		StudentLogData studentLogData = new StudentLogData();
		String line = "";
		ArrayList<String> actionList = new ArrayList<String>();
		ArrayList<String> verificationList = new ArrayList<String>();
		ArrayList<String> sentenceList = new ArrayList<String>();
		ArrayList<Integer> userStep = new ArrayList<Integer>();

		// add default sentence to sentencelist
		// sentenceList.add(Constants.DEFAULT_SENTENCE);
		BufferedReader fileReader;
		try {
			fileReader = new BufferedReader(new FileReader(filename));
			fileReader.readLine();
			while ((line = fileReader.readLine()) != null) {
				String tokens[] = line.split(Constants.CSV_FILE_SEPARATOR);
				if (tokens.length > 0) {
					for (String token : tokens) {
						token = token.trim();
					}
					if (tokens[0].equals("Computer") // don't add computer move actions
							&& (tokens[1].equals(Constants.MOVE_TO_HOTSPOT) || tokens[1]
									.equals(Constants.MOVE_TO_OBJECT)))
						continue;
					else {
						actionList.add(tokens[1]);
						verificationList.add(tokens[2]);
						sentenceList.add(tokens[3]);
						userStep.add(Integer.valueOf(tokens[4]));
					}
				}
			}
			fileReader.close();
			studentLogData.setActionList(actionList);
			studentLogData.setVerificationList(verificationList);
			studentLogData.setSentenceList(sentenceList);
			studentLogData.setUserStep(userStep);
			return studentLogData;
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return studentLogData;
	}

	public InitMaps readPropertiesFile(String wordsPropertiesFile, String actionPropertiesFile) {
		LinkedHashMap<String, String> sentenceToText = new LinkedHashMap<String, String>();
		HashMap<String, ArrayList<String>> sentenceToWords = new HashMap<String, ArrayList<String>>();
		Properties prop = new Properties();
		InitMaps initMaps = new InitMaps();
		String valAdd = "";
		try {
			prop.load(ReadFiles.class.getClassLoader().getResourceAsStream(wordsPropertiesFile));
			for (String key : prop.stringPropertyNames()) {
				String value = prop.getProperty(key);
				String[] valArray = value.split("\\#");
				sentenceToText.put(key.trim(), valArray[0].trim());
				ArrayList<String> wordList = new ArrayList<String>();
				if (valArray[1].contains(",")) {
					String[] types = valArray[1].split(",");
					for (String val : types) {
						valAdd = val.replaceAll("\\s+", "");
						wordList.add(valAdd.trim());
					}
				} else {
					valAdd = valArray[1].replaceAll("\\s+", "");
					wordList.add(valAdd.trim());
				}
				sentenceToWords.put(key, wordList);
			}
			initMaps.setSentenceToText(sentenceToText);
			initMaps.setSentenceToWords(sentenceToWords);
			initMaps.setSentenceToActions(readPropertiesFileAction(actionPropertiesFile));
		} catch (Exception e) {
			System.out.println(e);
		}
		return initMaps;
	}

	public HashMap<String, ArrayList<ArrayList<String>>> readPropertiesFileAction(String propertiesFile) {
		// LinkedHashMap<String, String> sentenceToText = new LinkedHashMap<String, String>();
		HashMap<String, ArrayList<ArrayList<String>>> sentenceToActions = new HashMap<String, ArrayList<ArrayList<String>>>();
		Properties prop = new Properties();
		String valAdd = "";
		try {
			prop.load(ReadFiles.class.getClassLoader().getResourceAsStream(propertiesFile));
			for (String key : prop.stringPropertyNames()) {
				String value = prop.getProperty(key);
				String[] valArray = value.split("\\#"); // split the sentence and all actions
				ArrayList<ArrayList<String>> actionList = new ArrayList<ArrayList<String>>(); // the whole array of all
																								// actions
																								// sentenceToText.put(key.trim(),
																								// valArray[0].trim());
				for (int i = 1; i < valArray.length; i++) {
					ArrayList<String> eachActionList = new ArrayList<String>(); // each action, the value list, comma
																				// separated
					if (valArray[i].contains(",")) {
						// System.out.println("i= " + i + Arrays.toString(valArray));
						String[] types = valArray[i].split(",");
						for (String val : types) {
							valAdd = val.replaceAll("\\s+", "");
							eachActionList.add(valAdd.trim());
						}
					} else {
						valAdd = valArray[i].replaceAll("\\s+", "");
						eachActionList.add(valAdd.trim());
					}
					// actionList.add(valArray[i]);
					actionList.add(eachActionList);
					// sentenceToActions.put(key, actionList);
				}
				sentenceToActions.put(key, actionList);
			}
			// initMaps.setSentenceToText(sentenceToText);
			// initMaps.setSentenceToWords(sentenceToActions);
			// System.out.println("mmmmmmmmmmmm");
			// System.out.println(sentenceToActions);
		} catch (Exception e) {
			System.out.println(e);
		}
		return sentenceToActions;
	}

	// read the inputdata files
	public ArrayList<String> readInputData(String textFile, StudentLogData student) {
		BufferedReader fileReader;
		String line = "";
		String entry = "";
		ArrayList<String> inputData = new ArrayList<String>();
		try {
			fileReader = new BufferedReader(new FileReader(textFile));
			fileReader.readLine();
			while ((line = fileReader.readLine()) != null) {
				line = line.trim();
				if (line != null && line.isEmpty() || line.contains(",") || line.equals(Constants.CORRECT)
						|| line.equals(Constants.INCORRECT))
					continue;
				// line = fileReader.readLine();
				if (line != null && !line.isEmpty() && !line.contains(",") && !line.contains(Constants.AUDIO_E)
						&& !line.equals(Constants.CORRECT) && !line.equals(Constants.INCORRECT)) { // the coordinates
					// contain comma, so
					// don't add that,
					// also differnt
					// case for audio
					// words
					// System.out.println("1 = " + line);
					entry += line.trim() + Constants.STUDENT_INPUT_DATA_SEPARATOR;
				} else if (line.contains(Constants.AUDIO_FILE_ENDING)) {
					entry = line.split(Constants.AUDIO_FILE_ENDING)[0];
					System.out.println("entry1= " + entry);
				}
				line = fileReader.readLine();
				line = fileReader.readLine();
				// System.out.println("////// " + line);
				if (line != null && !line.isEmpty() && !line.contains(",") && !line.contains(Constants.AUDIO_E)
						&& !line.equals(Constants.CORRECT) && !line.equals(Constants.INCORRECT)) {
					// System.out.println("2 = " + line);
					entry += line.trim();
				} else if (line != null && line.contains(Constants.AUDIO_FILE_ENDING)) {
					entry = line.split(Constants.AUDIO_FILE_ENDING)[0];
					System.out.println("entry2= " + entry); // never comes here
				}
				inputData.add(entry);
				// System.out.println("added " + entry);
				entry = "";
			}
			student.setInputData(inputData);
			fileReader.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return inputData;
	}
}
