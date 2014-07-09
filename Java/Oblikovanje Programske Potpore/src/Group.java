package pagru_v05;

import java.util.List;

public class Group {

    List<String> groupMembers; 
    	
    @Override
    public boolean equals(Object o) {
    	Group otherGroup = (Group) o;
    		
    	if (groupMembers.equals(otherGroup.groupMembers))
    		return true;
    	else
    		return false;
    }
}
