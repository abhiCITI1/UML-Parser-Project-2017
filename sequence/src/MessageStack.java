import java.util.Stack;

public class MessageStack<E> extends Stack<E>{
	/**
	 * 
	 */
	
	public MessageStack(){}
	//private static final long serialVersionUID = 1L;

	public String getSequenceNumber()
	{
		int len = elementData.length;
		len = findIndex(len);
		return combineElementsIntoSingleValue(len);
	}

	private String combineElementsIntoSingleValue(int len) {
		StringBuffer resultStringBuffer= new StringBuffer();
		for(int i=0;i<len;i++){
			resultStringBuffer.append(elementData[i]);
			if(i!=(len-1)) {
				resultStringBuffer.append( ".");
			}
		}
		return resultStringBuffer.toString();
	}

	private int findIndex(int len) {
		for(int i=0;i<len;i++){
			if(elementData[i] == null)
			{
				len =i;
				break;
			}
		}
		return len;
	}
}
