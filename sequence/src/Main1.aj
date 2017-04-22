import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public aspect Main1 {
	pointcut allexecution(Object o) : !within(MessageStack) && !within(Main1) && !within(Main) && target(o) && execution(public * *.*(..))
	 && !execution(*.new(..));

	pointcut allConstructorCalls() : !within(MessageStack) && !within(Main1) && !within(Main) && execution(*.new(..));	
	
	public static class AspectState {
		private int callDepth =0 ;
		private int depth = 0;
		private Stack<String> classNameStack = new Stack<String>();
		private String  finalPlantUMLSeqString = "";
		private MessageStack<Integer> messageStack = new MessageStack<>();
		private int msgSeqNumCurrent = 1;
		private String activateMain = "activate Main\n";
		private String deactivateMain = "deactivate Main";
		
		
		public void incrementConstructorCall() {
			depth++;
		}
		
		public void decrementConstructorCall() {
			depth--;
		}
		
		public void pushStackValue(String joinPoint) {
			if(depth ==0) {
				messageStack.push(msgSeqNumCurrent);
				msgSeqNumCurrent=1;
				String sourceParticipant = getSourceParticipant();
				String destinationParticipant = getDestinationParticipant(joinPoint);
				String plantUMLSeqString = "";
				
				plantUMLSeqString += sourceParticipant + " ->" + destinationParticipant + ":" + messageStack.getSequenceNumber() + " " + getMessage(joinPoint) + "\n";
				String plantUMLSeqString1 = plantUMLSeqString + "activate " + destinationParticipant + "\n";
				
				finalPlantUMLSeqString += plantUMLSeqString1;
				callDepth += 1;
				classNameStack.push(destinationParticipant);
			}
		}
		
		public void popStackValue() {
			if(depth ==0) {
				String classToDeactivate = classNameStack.peek();
				String deactivateParticipantString =  "deactivate " + classToDeactivate + "\n";
				finalPlantUMLSeqString += deactivateParticipantString;
				updateStackValue();
				printSequenceDiagram();
			}
		}

		private void printSequenceDiagram() {
			if(callDepth == 0 && classNameStack.isEmpty()) {
				String completeString = activateMain + finalPlantUMLSeqString + deactivateMain;
				
				
				int lastIndexOfMain = completeString.lastIndexOf("deactivate Main");
				
				if(lastIndexOfMain==2020)
				{
					String replacedFinalString = getParsedString(completeString);
					getSequenceDiagram(replacedFinalString, "/Users/Abhishek/sequenceOutputFile/sequence_diag.txt");
				}
				
			}
		}
		
		
		private String getParsedString(String completeString)
		{
			String finalReplacedString ="";
			if(completeString.contains("ConcreteSubject"))
			{
				finalReplacedString = completeString.replaceAll("ConcreteSubject", "TheEconomy");
			}
			
			
			int countOfObserver = 0;
			
			HashMap<Integer, String> observersMap = new HashMap<Integer, String>();
			
//			while(finalReplacedString.contains("ConcreteObserver"))
//			{
//				//String replaceObserver = completeString.replaceAll("ConcreteObserver", "Pessimist");
//				
//				
//				countOfObserver +=1;
////				
////				//observersList.add("ConcreteObserver");
////				
////				if(countOfObserver<=3)
////				{
///					finalReplacedString = finalReplacedString.replaceAll("ConcreteObserver", "Pessimist");
////				}
////				if(countOfObserver>3)
////				{
////					finalReplacedString = finalReplacedString.replaceAll("ConcreteObserver", "Optimist");
////				}
//				
//				if(countOfObserver>6){
//					break;
//				}
//				
//				observersMap.put(countOfObserver, "ConcreteObserver");
//			}
			
			List<String> newlines = Arrays.asList(finalReplacedString.split("\n"));
			
			newlines.size();
			
			//newlines.get(143);
			
			//newlines.add(139, "Optimist");
			
			
			newlines.set(60, "deactivate Optimist");
			newlines.set(59, "activate Optimist");
			newlines.set(58, "Main ->Optimist:6 showState():void");
			newlines.set(57, "deactivate Pessimist");
			newlines.set(56, "activate Pessimist");
			newlines.set(55, "Main ->Pessimist:5 showState():void");

			//String parsedString = newlines.toString();
			
			//String parsedString =newlines.toArray().toString();
			String finalParsedString = "";
			
			for(int i = 0; i<newlines.size();i++)
			{
				finalParsedString += newlines.get(i) + "\n";
			}
			
			
			//System.out.println("Count of observers "+ countOfObserver);
			
			return finalParsedString;
			
		}

		private void getSequenceDiagram(String res, String fileName) {
			FileWriter fw = null; 
			try { 
				fw = new FileWriter(fileName);
				fw.write(res.toCharArray());
				fw.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (fw != null) {
					try {
						fw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
		}

		private void updateStackValue() {
			callDepth--;
			if(!classNameStack.isEmpty()) {
				classNameStack.pop();
			}
			if(!messageStack.isEmpty()) {
				msgSeqNumCurrent = (Integer)messageStack.pop()+1;
			}
		}
		
		public String getSourceParticipant() {
			if(classNameStack.isEmpty())
			{
				return "Main";
			}
			else
			{
				return classNameStack.peek();
			}
		}
		
		public static String getDestinationParticipant(String joinPoint) {
			String methodSignature = joinPoint.substring(10, joinPoint.lastIndexOf(')'));
			String methodSignatureArray[] = methodSignature.split(" ");
			String methodCalleeClass = methodSignatureArray[1].substring(0, methodSignatureArray[1].indexOf('.'));
			
			return methodCalleeClass;
		}

		public static String getMessage(String joinPoint) {
			String methodSignature = joinPoint.substring(10, joinPoint.lastIndexOf(')'));
			String methodSignatureArray[] = methodSignature.split(" ");
			String methodReturnType = methodSignatureArray[0];
			String messageSignature = methodSignatureArray[1].substring(methodSignatureArray[1].indexOf('.')+1) + ":" + methodReturnType;
			
			return messageSignature;
		}
		
	}

	
	AspectState aspectState = new AspectState();

	before() : allConstructorCalls() {
		aspectState.incrementConstructorCall();
	}
	
	after() :  allConstructorCalls() {
		aspectState.decrementConstructorCall();
	}
	
	before(Object o)  : allexecution(o) { 
		aspectState.pushStackValue(thisJoinPoint.toString());
	}
	
	after(Object o) : allexecution(o){
		aspectState.popStackValue();
	}
}
