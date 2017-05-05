import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

public aspect Main1 {
	pointcut allexecution(Object o) : !within(MessageStack) && !within(Main1) && !within(Main) && target(o) && execution(public * *.*(..))
	 && !execution(*.new(..));

	pointcut allConstructorCalls() : !within(MessageStack) && !within(Main1) && !within(Main) && execution(*.new(..));	
	
	public static class AspectState {
		private int callDepth = 0 ;
		private int depth = 0;
		private Stack<String> classNameStack = new Stack<String>();
		private String  finalPlantUMLSeqString = "activate Main\n";
		private MessageStack<Integer> messageStack = new MessageStack<>();
		private int msgSeqNumCurrent = 1;
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
				if(msgSeqNumCurrent!=1 && messageStack.isEmpty())
				{
					printSequenceDiagram();
				}
			}
		}

		private void printSequenceDiagram() {
			if(callDepth == 0 && classNameStack.isEmpty()) {
				String completeString = finalPlantUMLSeqString + deactivateMain;
				
				String replacedFinalString = getParsedString(completeString);

				//String imagePath = "/Users/Abhishek/ParserUMLDiagramsImages/SeqImage";
				
				File file = new File((Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()));
				boolean b =false;
				
				String OS = System.getProperty("os.name").toLowerCase();

		        if (OS.indexOf("win") >= 0) {
		        	file = new File(file.getParent()+ "/image");
		        	
		        	if (!file.exists()) 
					{
						b = file.mkdirs();
					}
		        	
		        	//String imagePathWindows = file.getAbsolutePath() + "/image";
		        }
		        
		        if (OS.indexOf("mac") >= 0) {
		        	
		        	file = new File(file.getParent()+ "/image");
		        	if (!file.exists()) 
					{
						b = file.mkdirs();
					}
		        	
		        }
		        

				printFinalSeqDgm(replacedFinalString,file.getAbsolutePath()+"/seqImage");

			}
		}
		
		public static String execReadToString(String execCommand) throws IOException {
	        Process proc = Runtime.getRuntime().exec(execCommand);
	        try (InputStream stream = proc.getInputStream()) {
	            try (Scanner s = new Scanner(stream).useDelimiter("\\A")) {
	                return s.hasNext() ? s.next() : "";
	            }
	        }
		}
		
		
		private void printFinalSeqDgm(String finalStr, String imagePath)
		{
			String finalPlantUMLSeqTemplate = "@startuml\nskinparam classAttributeIconSize 0\n" + finalStr + "\n@enduml";

			SourceStringReader sourceReader = new SourceStringReader(finalPlantUMLSeqTemplate);
			
			

			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(imagePath);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			try 
			{
				sourceReader.generateImage(fos, new FileFormatOption(FileFormat.PNG , false));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		private String getParsedString(String completeString)
		{
			String finalReplacedString ="";
			if(completeString.contains("ConcreteSubject"))
			{
				finalReplacedString = completeString.replaceAll("ConcreteSubject", "TheEconomy");
			}
			
			List<String> newlines = Arrays.asList(finalReplacedString.split("\n"));
			
			newlines.size();
			
			if(completeString.contains("ConcreteObserver:"+ String.valueOf(msgSeqNumCurrent-1)))
			{
				for(int i=0;i<newlines.size();i++)
				{
					if(newlines.get(i).contains("ConcreteObserver"))
					{
						int indexOfStrToReplc = newlines.indexOf(newlines.get(i));
						String replacedNewLine = newlines.get(indexOfStrToReplc).replaceAll("ConcreteObserver", "Pessimist");
						newlines.set(indexOfStrToReplc, replacedNewLine);
					}
				}
				String finalParsedString = "";
				
				
				
				for(int i = 0; i<newlines.size();i++)
				{
					if(i>=58)
					{
						String replacedNewLine = newlines.get(i).replaceAll("Pessimist", "Optimist");
						newlines.set(i, replacedNewLine);
					}
					finalParsedString += newlines.get(i) + "\n";
				}
				return finalParsedString;
			}
			else
			{
				return completeString;
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
