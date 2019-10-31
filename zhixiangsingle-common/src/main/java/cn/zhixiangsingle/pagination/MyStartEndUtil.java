package cn.zhixiangsingle.pagination;

import com.google.common.collect.Lists;

import java.util.ArrayList;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.pagination
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/9/20 12:31
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class MyStartEndUtil {
    public static ArrayList<ArrayList<Integer>> getStartEndArray(ArrayList<Integer> totalAll, int startNum, int endNum){
        ArrayList<ArrayList<Integer>> startEndNums = Lists.newArrayList();
        int count1=0;
        for(Integer totalSingle:totalAll){
            count1=count1+totalSingle;
            if(count1>startNum){
                ArrayList<Integer> startEndNum = Lists.newArrayList();
                int startNumIndbNo=totalSingle-(count1-startNum);
                if(startNumIndbNo<0){
                    startNumIndbNo = 0;
                }
                startEndNum.add(startNumIndbNo);
                int endNumIndbNo = 0;
                if(endNum>count1){
                    endNumIndbNo=totalSingle;
                    startEndNum.add(endNumIndbNo);
                    startEndNums.add(startEndNum);
                }else{
                    endNumIndbNo=totalSingle-(count1-endNum);
                    startEndNum.add(endNumIndbNo);
                    startEndNums.add(startEndNum);
                    break;
                }
            }else{
                ArrayList<Integer> startEndNum = new ArrayList<>();
                startEndNum.add(0);
                startEndNum.add(0);
                startEndNums.add(startEndNum);
            }
        }
        return startEndNums;
    }
}
