import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class KnowledgeTracer4 {
	HashMap<String, Skill> wordToValueMap; // word to skill map
	LinkedHashMap<String, ArrayList<Skill>> skillMap;

	public KnowledgeTracer4() {
		wordToValueMap = new HashMap<String, Skill>();
		skillMap = new LinkedHashMap<String, ArrayList<Skill>>();
	}

	public SkillSet calculateSkill(StudentLogData studentLogData, SkillSet skillSet, InitMaps initMaps) {
		SkillSet skillSet1 = new SkillSet();
		skillMap = skillSet.getSkillMap();

		// initialize wordToValueMap so that the last skill value of the word is stored
		for (String word : skillMap.keySet()) {
			wordToValueMap.put(word, skillMap.get(word).get(0));
			// System.out.println("wordToValue " + word);
		}
		// add syntax to wordToValue
		wordToValueMap.put(Constants.SYNTAX, skillMap.get(Constants.SYNTAX).get(0));
		System.out.println(wordToValueMap);

		LinkedHashMap<String, ArrayList<String>> actionMap = new LinkedHashMap<String, ArrayList<String>>();
		LinkedHashMap<String, ArrayList<String>> sentenceMap = new LinkedHashMap<String, ArrayList<String>>();
		LinkedHashMap<String, ArrayList<Integer>> userStepMap = new LinkedHashMap<String, ArrayList<Integer>>();
		LinkedHashMap<String, ArrayList<String>> inputDataMap = new LinkedHashMap<String, ArrayList<String>>();
		LinkedHashMap<String, ArrayList<String>> verificationMap = new LinkedHashMap<String, ArrayList<String>>();

		String sentenceKey = "";

		ArrayList<String> tempSentenceList = new ArrayList<String>();
		ArrayList<String> tempActionList = new ArrayList<String>();
		ArrayList<Integer> tempUserStepList = new ArrayList<Integer>();
		ArrayList<String> tempInputDataList = new ArrayList<String>();
		ArrayList<String> tempVerificationList = new ArrayList<String>();

		String currSentence = "";
		String currAction = "";
		int currUserStep;
		String currInputData = "";
		String currVerification = "";

		for (int i = 0; i < studentLogData.getVerificationList().size(); i++) {
			// get the current sentence and action
			// System.out.println("i=" + i);
			// System.out.println(Arrays.toString(studentLogData.getSentenceList().toArray()));
			currSentence = studentLogData.getSentenceList().get(i);
			currAction = studentLogData.getActionList().get(i);
			currUserStep = studentLogData.getUserStep().get(i);
			currInputData = studentLogData.getInputData().get(i);
			currVerification = studentLogData.getVerificationList().get(i);

			sentenceKey = studentLogData.getSentenceList().get(i);

			if (sentenceMap.keySet().contains(sentenceKey)) {
				tempSentenceList = sentenceMap.get(sentenceKey);
			} else {
				tempSentenceList = new ArrayList<String>();
				tempSentenceList.add(currSentence);
			}
			tempSentenceList.add(currSentence);
			sentenceMap.put(sentenceKey, tempSentenceList);

			// add action
			if (actionMap.keySet().contains(sentenceKey)) {
				tempActionList = actionMap.get(sentenceKey);
			} else {
				tempActionList = new ArrayList<String>();
			}
			tempActionList.add(currAction);
			actionMap.put(sentenceKey, tempActionList);

			// add inputdata
			if (inputDataMap.keySet().contains(sentenceKey)) {
				tempInputDataList = inputDataMap.get(sentenceKey);
			} else {
				tempInputDataList = new ArrayList<String>();
			}
			tempInputDataList.add(currInputData);
			inputDataMap.put(sentenceKey, tempInputDataList);

			// add userStep
			if (userStepMap.keySet().contains(sentenceKey)) {
				tempUserStepList = userStepMap.get(sentenceKey);
			} else {
				tempUserStepList = new ArrayList<Integer>();
			}
			tempUserStepList.add(currUserStep);
			userStepMap.put(sentenceKey, tempUserStepList);

			// add verification
			if (verificationMap.keySet().contains(sentenceKey)) {
				tempVerificationList = verificationMap.get(sentenceKey);
			} else {
				tempVerificationList = new ArrayList<String>();
			}
			tempVerificationList.add(currVerification);
			verificationMap.put(sentenceKey, tempVerificationList);
		}
		evaluateSentence(actionMap, sentenceMap, userStepMap, inputDataMap, verificationMap, initMaps, studentLogData);
		return skillSet1;
	}

	private void evaluateSentence(LinkedHashMap<String, ArrayList<String>> actionMap,
			LinkedHashMap<String, ArrayList<String>> sentenceMap,
			LinkedHashMap<String, ArrayList<Integer>> userStepMap,
			LinkedHashMap<String, ArrayList<String>> inputDataMap,
			LinkedHashMap<String, ArrayList<String>> verificationMap, InitMaps initMaps, StudentLogData student) {
		// for each sentence
		for (String sentence : actionMap.keySet()) {
			// for each step update each skill
			// get local arrayLists
			System.err.println("/////////// " + sentence);

		}
	}

}
