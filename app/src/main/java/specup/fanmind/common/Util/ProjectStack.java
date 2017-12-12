package specup.fanmind.common.Util;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.vo.ProjectStackVO;

/**
 * Created by DEV-06 on 2016-04-05.
 */
public class ProjectStack {

    private List<ProjectStackVO> stackList = new ArrayList<ProjectStackVO>();
    private int top = 0;
    private boolean pushAfterfirst = false;

    public int push(ProjectStackVO val) {
        try {

            stackList.add(top, val);
            pushAfterfirst = true;
        } catch (Exception e) {
            e.printStackTrace();
            top--;
        }
        top++;
        return top;
    }

    public ProjectStackVO pop() {

        if(pushAfterfirst){
            top-=2;
            pushAfterfirst = false;
        }else{
            top--;
        }
        try {
            return stackList.get(top);
        } catch (Exception e) {
            top++;
            return null;
        }

    }
}
