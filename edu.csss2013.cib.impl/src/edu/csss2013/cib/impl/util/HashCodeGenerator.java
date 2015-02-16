package edu.csss2013.cib.impl.util;

public class HashCodeGenerator {
	
	public static int hashCode(Object... elements) {
        if (elements == null)
            return 0;

        int result = 1;
        for (Object element : elements) {
            int elementHash = (int)(element.hashCode() ^ (element.hashCode() >>> 32));
            result = 31 * result + elementHash;
        }

        return result;
    }

}
