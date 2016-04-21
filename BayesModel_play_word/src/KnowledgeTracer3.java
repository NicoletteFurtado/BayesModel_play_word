import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class KnowledgeTracer3 {

	HashMap<String, Skill> wordToValueMap; // word to skill map
	LinkedHashMap<String, ArrayList<Skill>> skillMap;

	public KnowledgeTracer3() {
		wordToValueMap = new HashMap<String, Skill>();
		skillMap = new LinkedHashMap<String, ArrayList<Skill>>();
	}

	public SkillSet calculateSkill(StudentLogData studentLogData, SkillSet skillSet, InitMaps initMaps) {
		SkillSet skillSet1 = new SkillSet();
		skillMap = skillSet.getSkillMap();
		// temporary variables
		String currSentence = "";
		String currAction = "";
		int currUserStep;
		String currInputData = "";
		String currVerification = "";

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

		// System.out.println("sentence list=" + studentLogData.getSentenceList());
		// ArrayList<String> userStepList = new ArrayList<String>();
		ArrayList<String> wordList = new ArrayList<String>(); // for skills
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

			// add sentence
			// tempSentenceList = sentenceMap.get(sentenceKey);
			// System.err.println(currSentence);
			// System.out.println(tempSentenceList);
			if (sentenceMap.keySet().contains(sentenceKey)) {
				tempSentenceList = sentenceMap.get(sentenceKey);
			} else {
				tempSentenceList = new ArrayList<String>();
				tempSentenceList.add(currSentence);
			}
			tempSentenceList.add(currSentence);
			sentenceMap.put(sentenceKey, tempSentenceList);

			// add action
			// tempActionList = sentenceMap.get(sentenceKey);
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

			// for each sentence
			// for each step update each skill
			// last step update syntax
			// see number of play words
			// update the play words differently than others
		}
		// for (String sKey : actionMap.keySet()) {
		// evaluateSentence(actionMap, sentenceMap, userStepMap, inputDataMap, verificationMap, initMaps,
		// studentLogData);
		// }

		evaluateSentence(actionMap, sentenceMap, userStepMap, inputDataMap, verificationMap, initMaps, studentLogData);
		skillSet1.setSkillMap(skillMap);
		return skillSet1;
	}

	private void evaluateSentence(LinkedHashMap<String, ArrayList<String>> actionMap,
			LinkedHashMap<String, ArrayList<String>> sentenceMap,
			LinkedHashMap<String, ArrayList<Integer>> userStepMap,
			LinkedHashMap<String, ArrayList<String>> inputDataMap,
			LinkedHashMap<String, ArrayList<String>> verificationMap, InitMaps initMaps, StudentLogData student) {

		// ArrayList<String> tempSentenceList;
		// ArrayList<String> tempActionList;
		// ArrayList<Integer> tempUserStepList;
		// ArrayList<String> tempInputDataList;
		// ArrayList<String> tempVerificationList;

		// for each sentence
		for (String sentence : actionMap.keySet()) {
			// for each step update each skill
			// get local arrayLists
			System.err.println("/////////// " + sentence);
			// tempActionList = actionMap.get(sentence);
			// System.out.println("/////////1 " + Arrays.toString(tempActionList.toArray()));
			// tempUserStepList = userStepMap.get(sentence);
			// tempInputDataList = inputDataMap.get(sentence);
			// tempVerificationList = verificationMap.get(sentence);
			// calculateForSentence(sentence, tempActionList, tempUserStepList, tempInputDataList, tempVerificationList,
			// initMaps, student);
			calculateForSentence(sentence, actionMap.get(sentence), userStepMap.get(sentence),
					inputDataMap.get(sentence), verificationMap.get(sentence), initMaps, student);
			// last step update syntax
			// see number of play words
			// update the play words differently than others
		}
	}

	private void evaluateSentence2(LinkedHashMap<String, ArrayList<String>> actionMap,
			LinkedHashMap<String, ArrayList<String>> sentenceMap,
			LinkedHashMap<String, ArrayList<Integer>> userStepMap,
			LinkedHashMap<String, ArrayList<String>> inputDataMap,
			LinkedHashMap<String, ArrayList<String>> verificationMap, InitMaps initMaps, StudentLogData student) {
		System.out.println("action map=" + actionMap);
		for (String sentence : actionMap.keySet()) {

		}
	}

	private void calculateForSentence(String sentence, ArrayList<String> tempActionList,
			ArrayList<Integer> tempUserStepList, ArrayList<String> tempInputDataList,
			ArrayList<String> tempVerificationList, InitMaps initMaps, StudentLogData studentLogData) {

		double skillEvaluated = 0.0;
		double newSkill = 0.0;
		double prevSkillValue = 0.0;
		int prevUserStep = 0;
		String prevAction = "";
		// int currUserStep = 0;

		ArrayList<String> playWordList = new ArrayList<String>();
		for (int i = 0; i < tempActionList.size(); i++) {
			HashMap<String, Boolean> wordToVerif = checkIncorrectSkill2(studentLogData, initMaps, i);
			// if currAction!=play word && prevuserstep!=currUserStep &&
			if (!tempActionList.get(i).equals(Constants.PLAY_WORD)
					&& (!(prevUserStep == studentLogData.getUserStep().get(i).intValue()) || prevAction
							.equals(Constants.PLAY_WORD))) {
				if (tempVerificationList.get(i).equals(Constants.CORRECT)) {
					for (String word : wordToVerif.keySet()) {
						if (wordToValueMap.keySet().contains(word)) { // some words that the student moved to are
							// not skills
							// System.err.println("wordToVerif" + wordToVerif);
							prevSkillValue = wordToValueMap.get(word).getSkillValue();
							// change skills differently if they are in playWords list
							if (playWordList.equals(word))
								skillEvaluated = this.calcCorrectPlayWord(studentLogData, prevSkillValue);
							else
								skillEvaluated = this.calcCorrect(studentLogData, prevSkillValue);
							newSkill = calcNewSkillValue(studentLogData, skillEvaluated);
							updateSkills(studentLogData, newSkill, word, tempVerificationList.get(i), sentence,
									tempActionList.get(i), tempUserStepList.get(i).intValue());
						}
					}
					// System.out.println(wordToVerif);

				} else if (tempVerificationList.get(i).equals(Constants.INCORRECT)) {
					for (String word : wordToVerif.keySet()) {
						if (wordToValueMap.keySet().contains(word)) { // some words that the student moved to are
																		// not skills
							if (wordToVerif.get(word)) {
								prevSkillValue = wordToValueMap.get(word).getSkillValue();
								// skillEvaluated = this.calcCorrect(studentLogData, prevSkillValue);
								if (playWordList.equals(word))
									skillEvaluated = this.calcCorrectPlayWord(studentLogData, prevSkillValue);
								else
									skillEvaluated = this.calcCorrect(studentLogData, prevSkillValue);
								// System.out.println("in incorrect correct " + word);
							} else {
								prevSkillValue = wordToValueMap.get(word).getSkillValue();
								if (playWordList.equals(word))
									skillEvaluated = this.calcIncorrectPlayWord(studentLogData, prevSkillValue);
								else
									skillEvaluated = this.calcIncorrect(studentLogData, prevSkillValue);
								// System.out.println("in incorrect incorrect " + word);
							}
							newSkill = calcNewSkillValue(studentLogData, skillEvaluated);
							// updateSkills(studentLogData, initMaps, newSkill, word, i);
							updateSkills(studentLogData, newSkill, word, tempVerificationList.get(i), sentence,
									tempActionList.get(i), tempUserStepList.get(i).intValue());
							// System.out.println("updated incorrect");
						}
					}
				}
				// prevUserStep = studentLogData.getUserStep().get(i).intValue();
				// prevAction = studentLogData.getActionList().get(i);
			} else if (tempActionList.get(i).equals(Constants.PLAY_WORD)) {
				playWordList.add(tempInputDataList.get(i));
			}
			prevUserStep = studentLogData.getUserStep().get(i).intValue();
			prevAction = studentLogData.getActionList().get(i);
		}
	}

	// check this
	private double calcCorrect(StudentLogData student, double prevSkillValue) {

		return (prevSkillValue * (1 - student.getSlip()))
				/ (prevSkillValue * (1 - student.getSlip()) + (1 - prevSkillValue) * student.getGuess());

	}

	private double calcIncorrect(StudentLogData student, double prevSkillValue) {

		return (prevSkillValue * student.getSlip())
				/ ((student.getSlip() * prevSkillValue) + ((1 - student.getGuess()) * (1 - prevSkillValue)));

	}

	private double calcCorrectPlayWord(StudentLogData student, double prevSkillValue) {

		return (prevSkillValue * (1 - student.getSlip()))
				/ (prevSkillValue * (1 - student.getSlip()) + (1 - prevSkillValue) * student.getGuess());

	}

	private double calcIncorrectPlayWord(StudentLogData student, double prevSkillValue) {

		return (prevSkillValue * student.getSlip())
				/ ((student.getSlip() * prevSkillValue) + ((1 - student.getGuess()) * (1 - prevSkillValue)));

	}

	private double calcNewSkillValue(StudentLogData student, double skillEvaluated) {
		return skillEvaluated + ((1 - skillEvaluated) * student.getTransition());
	}

	private void updateSkills(StudentLogData studentLogData, double newSkill, String word, String verification,
			String sentence, String action, int userStep) {
		// String currAction = studentLogData.getActionList().get(count);
		Skill skill = new Skill();
		skill.setAction(action);
		skill.setSkillValue(newSkill);
		// skill.setWord(wordInList);
		skill.setWord(word);
		skill.setUserStep(userStep);
		skill.setVerification(verification);
		// System.out.println(studentLogData.getSentenceList().get(i));
		skill.setSentence(sentence);
		// skillMap.get(wordInList).add(skill);
		skillMap.get(word).add(skill);
		//
		wordToValueMap.put(word, skill);
		// System.out.println("changed value " + word + " " + newSkill + " for sentence " + skill.getSentence()
		// + " for step " + userStep);
	}

	// set false for the word that the student got wrong
	private HashMap<String, Boolean> checkIncorrectSkill2(StudentLogData student, InitMaps initMaps, int count) {
		// System.err.println("entreed check incorrect 2");
		String objectsMoved[] = student.getInputData().get(count).split(Constants.STUDENT_INPUT_DATA_SEPARATOR);
		System.err.println("objects moved " + Arrays.toString(objectsMoved));
		String currSentence = student.getSentenceList().get(count);
		// List of correct words for that step
		ArrayList<String> actionWords = initMaps.getSentenceToActions()
				.get(AnalysisUtil.convertStringToKey(currSentence)).get(student.getUserStep().get(count) - 1);

		System.err.println(currSentence);
		System.err.println("actionWrods "
				+ initMaps.getSentenceToActions().get(AnalysisUtil.convertStringToKey(currSentence))
						.get(student.getUserStep().get(count) - 1));
		// String currSentence = student.getSentenceList().get(count);
		HashMap<String, Boolean> wordToVerif = new HashMap<String, Boolean>();
		// add syntax to wordToVerif defalut false
		wordToVerif.put(Constants.SYNTAX, false);
		// case for pen
		for (int i = 0; i < objectsMoved.length; i++) {
			if (objectsMoved[i].contains("pen1") || objectsMoved[i].contains("pen2")
					|| objectsMoved[i].contains("pen3") || objectsMoved[i].contains("pen4")) {
				objectsMoved[i] = "pen";
			} else if (objectsMoved[i].contains("corralDoor")) {
				objectsMoved[i] = "corral";
			} else if (objectsMoved[i].contains("pumpkinPatch") || objectsMoved[i].contains("pumpkin")) {
				objectsMoved[i] = "pumpkins";
			} else if (objectsMoved[i].contains("farmerFall")) {
				objectsMoved[i] = "farmer";
			}
		}
		// get the action for this step
		for (int i = 0; i < actionWords.size(); i++) {
			if (actionWords.get(i).equalsIgnoreCase(objectsMoved[i])) {
				// System.out.println("action word=" + actionWords.get(i));
				// System.out.println("ojbjeys moved=" + objectsMoved[i]);
				wordToVerif.put(actionWords.get(i), true);
				// understood everything about the sentence including syntax
				// wordToVerif.put(Constants.SYNTAX, true);
			} else {
				wordToVerif.put(actionWords.get(i), false);
				// if (actionWords.contains(objectsMoved[i])) {
				// if the students moved the correct words in the wrong order
				// wordToVerif.put(Constants.SYNTAX, true);
				// }
			}
		}
		// check if all words in actionWords are present in objectsMoved
		int flag = 99;
		for (int i = 0; i < actionWords.size(); i++) {
			if (actionWords.contains(objectsMoved[i])) {
				flag = 1;
			} else {
				flag = 0;
				break;
			}
		}
		if (flag == 1)
			wordToVerif.put(Constants.SYNTAX, true);
		else if (flag == 0)
			wordToVerif.put(Constants.SYNTAX, false);
		else
			System.err.println("flag=" + flag);
		return wordToVerif;
	}
}