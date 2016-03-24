import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class KnowledgeTracer2 {

	HashMap<String, Skill> wordToValueMap; // word to skill map
	LinkedHashMap<String, ArrayList<Skill>> skillMap;

	public KnowledgeTracer2() {
		wordToValueMap = new HashMap<String, Skill>();
		skillMap = new LinkedHashMap<String, ArrayList<Skill>>();
	}

	public SkillSet calculateSkill(StudentLogData studentLogData, SkillSet skillSet, InitMaps initMaps) {
		SkillSet skillSet1 = new SkillSet();
		double skillEvaluated = 0.0;
		double newSkill = 0.0;
		double prevSkillValue = 0.0;
		int prevUserStep = 0;
		String prevSentence = "";
		skillMap = skillSet.getSkillMap();
		// temporary variables
		String currSentence = "";
		String currAction = "";
		String word1 = "";
		ArrayList<String> playWords = new ArrayList<String>();
		// initialize wordToValueMap so that the last skill value of the word is stored
		for (String word : skillMap.keySet()) {
			wordToValueMap.put(word, skillMap.get(word).get(0));
			// System.out.println("wordToValue " + word);
		}
		// add syntax to wordToValue
		wordToValueMap.put(Constants.SYNTAX, skillMap.get(Constants.SYNTAX).get(0));
		System.out.println(wordToValueMap);
		for (int i = 0; i < studentLogData.getVerificationList().size(); i++) {
			// get the current sentence and action
			currSentence = studentLogData.getSentenceList().get(i);
			currAction = studentLogData.getActionList().get(i);
			// update only for the first attempt of every step
			if (!(prevSentence.equalsIgnoreCase(currSentence) && (prevUserStep == studentLogData.getUserStep().get(i)
					.intValue()))
					&& currAction.equals(Constants.PLAY_WORD)) {
				System.out.println("entered for step " + studentLogData.getUserStep().get(i) + " for sentence "
						+ currSentence);
				// get the first(or any) word from the current sentence and its skill value
				word1 = initMaps.getSentenceToWords().get(AnalysisUtil.convertStringToKey(currSentence)).get(0);
				if (!word1.equalsIgnoreCase(Constants.DEFAULT_WORD)) {// because sentence to words can contain XYZ
					// prevSkillValue = s.getSkillValue(); // this must be used only if correct
					prevSentence = currSentence;
					prevUserStep = studentLogData.getUserStep().get(i);
					// if correct
					if (studentLogData.getVerificationList().get(i).equals(Constants.CORRECT)) {
						// if correct, one by one increment all skills
						HashMap<String, Boolean> wordToVerif = checkIncorrectSkill2(studentLogData, initMaps, i);
						System.out.println(wordToVerif);
						for (String wordSkill : wordToVerif.keySet()) {
							// System.err.println("wordToVerif" + wordToVerif);
							prevSkillValue = wordToValueMap.get(wordSkill).getSkillValue();
							// change skills differently if they are in playWords list
							if (playWords.contains(wordSkill))
								skillEvaluated = this.calcCorrectPlayWord(studentLogData, prevSkillValue);
							else
								skillEvaluated = this.calcCorrect(studentLogData, prevSkillValue);
							newSkill = calcNewSkillValue(studentLogData, skillEvaluated);
							updateSkills(studentLogData, newSkill, wordSkill, i);
						}
						// clear all entries in playWords
						playWords.clear();
					}
					// if incorrect
					else if (studentLogData.getVerificationList().get(i).equals(Constants.INCORRECT)) {
						HashMap<String, Boolean> wordToVerif = checkIncorrectSkill2(studentLogData, initMaps, i);
						String objectsMoved[] = studentLogData.getInputData().get(i)
								.split(Constants.STUDENT_INPUT_DATA_SEPARATOR);
						// System.out.println("Objects moved = " + Arrays.toString(objectsMoved));
						System.out.println(wordToVerif);
						for (String word : wordToVerif.keySet()) {
							if (wordToValueMap.keySet().contains(word)) { // some words that the student moved to are
																			// not skills
								if (wordToVerif.get(word)) {
									prevSkillValue = wordToValueMap.get(word).getSkillValue();
									// skillEvaluated = this.calcCorrect(studentLogData, prevSkillValue);
									if (playWords.contains(word))
										skillEvaluated = this.calcCorrectPlayWord(studentLogData, prevSkillValue);
									else
										skillEvaluated = this.calcCorrect(studentLogData, prevSkillValue);
									// System.out.println("in incorrect correct " + word);
								} else {
									prevSkillValue = wordToValueMap.get(word).getSkillValue();
									// skillEvaluated = this.calcIncorrect(studentLogData, prevSkillValue);
									if (playWords.contains(word))
										skillEvaluated = this.calcIncorrectPlayWord(studentLogData, prevSkillValue);
									else
										skillEvaluated = this.calcIncorrect(studentLogData, prevSkillValue);
									// System.out.println("in incorrect incorrect " + word);
								}
								newSkill = calcNewSkillValue(studentLogData, skillEvaluated);
								// updateSkills(studentLogData, initMaps, newSkill, word, i);
								updateSkills(studentLogData, newSkill, word, i);
								System.out.println("updated incorrect");
							}
						}
						// clear all entries in playWords
						playWords.clear();
					} else {
						// verification is empty. which means play word action
						playWords.add(studentLogData.getInputData().get(i));
					}
				}
			} else {
				System.out.println(i + " ignored step " + prevUserStep + "for sentence " + prevSentence);
				continue;
			}
		}
		skillSet1.setSkillMap(skillMap);
		return skillSet1;
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

	private void updateSkills(StudentLogData studentLogData, double newSkill, String word, int count) {
		String currAction = studentLogData.getActionList().get(count);
		Skill skill = new Skill();
		skill.setAction(currAction);
		skill.setSkillValue(newSkill);
		// skill.setWord(wordInList);
		skill.setWord(word);
		skill.setUserStep(studentLogData.getUserStep().get(count).intValue());
		skill.setVerification(studentLogData.getVerificationList().get(count));
		// System.out.println(studentLogData.getSentenceList().get(i));
		skill.setSentence(studentLogData.getSentenceList().get(count));
		// skillMap.get(wordInList).add(skill);
		skillMap.get(word).add(skill);
		//
		wordToValueMap.put(word, skill);
		System.out.println("changed value " + word + " " + newSkill + " for sentence " + skill.getSentence()
				+ " for step " + studentLogData.getUserStep().get(count));
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
			if (objectsMoved[i].contains("pen2")) {
				objectsMoved[i] = "pen";
			} else if (objectsMoved[i].contains("corralDoor")) {
				objectsMoved[i] = "corral";
			} else if (objectsMoved[i].contains("pumpkinPatch")) {
				objectsMoved[i] = "pumpkins";
			} else if (objectsMoved[i].contains("farmerFall")) {
				objectsMoved[i] = "farmer";
			}
		}
		// get the action for this step
		// ArrayList<String> actionWords = initMaps.getSentenceToActions()
		// .get(AnalysisUtil.convertStringToKey(currSentence)).get(student.getUserStep().get(count) - 1);
		// System.out.println("actionWrods " + Arrays.toString(actionWords.toArray()));
		for (int i = 0; i < actionWords.size(); i++) {
			if (actionWords.get(i).equalsIgnoreCase(objectsMoved[i])) {
				// System.out.println("action word=" + actionWords.get(i));
				// System.out.println("ojbjeys moved=" + objectsMoved[i]);
				wordToVerif.put(actionWords.get(i), true);
				// understood everything about the sentence including syntax
				// wordToVerif.put(Constants.SYNTAX, true);
			} else {
				wordToVerif.put(actionWords.get(i), false);
				if (actionWords.contains(objectsMoved[i])) {
					// if the students moved the correct words in the wrong order
					// wordToVerif.put(Constants.SYNTAX, true);
				}
			}
		}
		// check if all words in actionWords are presetn in objectsMoved
		boolean flag = false;
		for (int i = 0; i < actionWords.size(); i++) {
			if (actionWords.contains(objectsMoved[i])) {
				flag = true;
			} else {
				flag = false;
				break;
			}
		}
		wordToVerif.put(Constants.SYNTAX, flag);
		return wordToVerif;
	}
}