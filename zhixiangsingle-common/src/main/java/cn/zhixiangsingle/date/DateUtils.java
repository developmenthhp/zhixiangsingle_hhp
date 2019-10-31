package cn.zhixiangsingle.date;

import cn.zhixiangsingle.entity.base.Function;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.date
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/21 18:46
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class DateUtils {
    public static final String yyyy = "yyyy";
    public static final String yyyy_MM = "yyyy-MM";
    public static final String yyyy_MM_dd = "yyyy-MM-dd";
    public static final String HHmmss = "HH:mm:ss";
    public static final String YYYY_MM_DDHHmmss = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DDTHHmmss = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String YYYY_MM_DDHHmmssSSS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String yyyy_MM_ddTHHmmssSSSZ = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final SimpleDateFormat DATE_FORMAT_YYYY = new SimpleDateFormat(yyyy);
    public static final SimpleDateFormat DATE_FORMAT_YYYY_MM = new SimpleDateFormat(yyyy_MM);
    public static final SimpleDateFormat DATE_FORMAT_YYYY_MM_DD = new SimpleDateFormat(yyyy_MM_dd);
    public static final SimpleDateFormat DATE_FORMAT_HH_MM_SS = new SimpleDateFormat(HHmmss);
    public static final SimpleDateFormat DATE_FORMAT_YYYY_MM_DDkgHHmhMMmmss = new SimpleDateFormat(YYYY_MM_DDHHmmss);
    private static SimpleDateFormat fromat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static  SimpleDateFormat fromathour=new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static String formatUdbaDate(Date date) {
        return formatDate(date, YYYY_MM_DDHHmmss);
    }
    /**
     * 获取指定格式的日期
     * @param date 日期
     * @param format 日期格式(如：yyyy-MM-dd)
     * @return
     */
    public static String formatDate(Date date, String format){
        return new SimpleDateFormat(format).format(date);
    }
    /**
     * 获取当前的日期
     * @return
     */
    public static Date getCurrDate(){
        return Calendar.getInstance().getTime();
    }
    /**
     * 获取指定格式的当前日期
     * @param format 日期格式(如：yyyy-MM-dd)
     * @return
     */
    public static String getCurrDate(String format){
        return new SimpleDateFormat(format).format(Calendar.getInstance().getTime());
    }
    /**
     * 获取前一天的日期
     * @return
     */
    public static Date getPrevDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }
    /**
     * 获取指定日期前一天的日期
     * @param date 日期
     * @return
     */
    public static Date getPrevDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }
    /**
     * 获取指定日期下一天的日期
     * @param date 日期
     * @return
     */
    public static Date getNextDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }
    /**
     * 获取指定日期下一天的日期字符串
     * @param date 日期(格式：yyyy-MM-dd)
     * @return 返回日期格式yyyy-MM-dd
     * @throws IllegalArgumentException 如果传入日期的格式不是 'yyyy-MM-dd'
     */
    public static String getNextDate(String date){
        if(date == null || date.length() != 10){
            throw new IllegalArgumentException("传入的日期格式不正确：\""+date+"\"");
        }

        String[] array = date.split("-");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(array[0]), Integer.parseInt(array[1])-1, Integer.parseInt(array[2]));
        calendar.add(Calendar.DATE, 1);

        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }
    /**
     * 获取指定格式的昨天的日期
     * @param format 日期格式(如：yyyy-MM-dd)
     * @return
     */
    public static String getPrevDate(String format){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        return new SimpleDateFormat(format).format(calendar.getTime());
    }
    /**
     * 根据格式Format转换成日期
     * @param str
     * @param format
     * @return
     * @throws ParseException
     */
    public static Date getStringToDate(String str, String format) throws ParseException{
        if(str == null || "".equals(str)){
            return null;
        }
        return new SimpleDateFormat(format).parse(str);
    }
    /**
     * 获取最大的日期
     * @return
     */
    public static Date getMaxDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 9999);
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DATE, 31);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }
    /**
     * 获取当前年的字符串表示，格式：yyyy
     * @return
     */
    public static String getCurrYear(){
        return DATE_FORMAT_YYYY.format(Calendar.getInstance().getTime());
    }
    /**
     * 获取当前年的字符串表示，格式：yyyy <br/>
     * 每月指定日期之后的时间，记入下一月
     * @param point 日期分界点
     * @return
     */
    public static String getCurrYear(int point) {
        Calendar calendar = Calendar.getInstance();

        if(calendar.get(Calendar.DATE) > point){
            calendar.add(Calendar.MONTH, 1);
        }

        return DATE_FORMAT_YYYY.format(calendar.getTime());
    }
    public static Date getPrevMonth(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        return calendar.getTime();
    }
    public static Date getPrevYear(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        return calendar.getTime();
    }
    /**
     * 获取去年的字符串表示，格式：yyyy
     * @return
     */
    public static String getPrevYearStr(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        return DATE_FORMAT_YYYY.format(calendar.getTime());
    }
    /**
     * 获取起始日期到截至日期之间的天数,起始日期和截止日期为同一天时返回1.
     * @param beginDate 起始日期(格式：yyyy-MM-dd)
     * @param endDate 截止日期(格式：yyyy-MM-dd)
     * @return 起始日期到截止日期之间的天数
     * @throws IllegalArgumentException 如果传入日期的格式不是 'yyyy-MM-dd'
     */
    public static int getBetweenDay(String beginDate, String endDate){
        long beginTime = java.sql.Date.valueOf(beginDate).getTime();
        long endTime = java.sql.Date.valueOf(endDate).getTime();
        long betweenDay = (endTime-beginTime)/(24*3600*1000)+1;
        return (int)betweenDay;
    }
    /**
     * 获取起始日期到截至日期之间的天数,起始日期和截止日期为同一天时返回1.
     * @param bdate
     * @param edate
     * @return
     * @throws ParseException
     */
    public static int getBetweenDay(Date bdate, Date edate) throws ParseException{
        return Integer.parseInt(String.valueOf((edate.getTime()-bdate.getTime())/(1000*3600*24))) + 1;
    }
    /**
     * 获取指定月份开始日期的字符串表示，格式：yyyy-MM-dd
     * @param yearMonth 年月(格式：yyyyMM)
     * @param point 日期分界点
     * @return
     */
    public static String getBeginDate(String yearMonth, int point){
        if(yearMonth == null || yearMonth.length() != 6){
            throw new IllegalArgumentException("传入的年月格式不正确：\""+yearMonth+"\"");
        }

        int year = Integer.parseInt(yearMonth.substring(0,4));
        int month = Integer.parseInt(yearMonth.substring(4,6));

        if(year <= 0 || month <= 0 || month > 12){
            throw new IllegalArgumentException("传入的年月格式不正确：\""+yearMonth+"\"");
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month-1, point);
        calendar.add(Calendar.MONTH, -1);
        calendar.add(Calendar.DATE, 1);

        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }
    /**
     * 获取指定月份结束日期的字符串表示，格式：yyyy-MM-dd
     * @param yearMonth 年月(格式：yyyyMM)
     * @param point 日期分界点
     * @return
     */
    public static String getEndDate(String yearMonth, int point){
        if(yearMonth == null || yearMonth.length() != 6){
            throw new IllegalArgumentException("传入的年月格式不正确：\""+yearMonth+"\"");
        }

        int year = Integer.parseInt(yearMonth.substring(0,4));
        int month = Integer.parseInt(yearMonth.substring(4,6));

        if(year <= 0 || month <= 0 || month > 12){
            throw new IllegalArgumentException("传入的年月格式不正确：\""+yearMonth+"\"");
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month-1, point);

        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }
    /**
     * 获取两个时间的小时差
     * @param Date date比较的时间
     * @param mydata 现在世界
     * @return 返回相差小时数
     * @author 贾锐
     */
    public  static Long getXcXs(Date date ,Date  mydata) {
        Long xs= ((mydata.getTime()-date.getTime())/(60*60*1000));
        //System.out.println( "相差的日期: " + xs);

        return xs;

    }
    /**
     * 一个时间相加小时
     * @param Date date  时间
     * @param day 要相加的相加
     * @return 返回相加后的时间
     * @author 贾锐
     */
    public static Date addDateByHouse(Date date, int hours) {
        return addDateByFidld(date, Calendar.HOUR, hours);
    }
    /**
     * 一个时间相加年数
     * @param Date date  时间
     * @param day 要相加的年数
     * @return 返回相加后的时间
     * @author 贾锐
     */
    public static Date addDateByYear(Date date, int year) {
        return addDateByFidld(date, Calendar.YEAR, year);
    }
    public static Date addDateByMonth(Date date, int month) {
        return addDateByFidld(date, Calendar.MONTH, month);
    }
    /**
     * 一个时间相加天数
     * @param Date date  时间
     * @param day 要相加的天数
     * @return 返回相加后的时间
     * @author 贾锐
     */
    public static Date addDateByDay(Date date, int day) {
        return addDateByFidld(date, Calendar.DAY_OF_MONTH, day);
    }
    public static Date addDateByMinute(Date date, int minute) {
        return addDateByFidld(date, Calendar.MINUTE, minute);
    }
    public static Date addDateBySecond(Date date, int second) {
        return addDateByFidld(date, Calendar.SECOND, second);
    }
    /**
     * 根据类型加减时间值
     * @param date
     * @param field
     * @param num
     * @return
     */
    public static Date addDateByFidld(Date date, int field, int num) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(field, num);
        return ca.getTime();
    }
    /**
     * 根据年龄获取出生日期
     * @param age
     * @return
     */
    public static Date getDateByAge(int age){
        Date date = new Date();
        Date newDate = addDateByYear(date,-age);
        return newDate;
    }
    public static String pyDate(String date,int py)
    {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
        String str = "";
        try {
            Date parse = sdf.parse(date);
            Calendar ca=Calendar.getInstance();
            ca.setTime(parse);
            ca.add(Calendar.HOUR_OF_DAY, py);
            parse = ca.getTime();
            str = sdf.format(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
    //返回两个日期时间段
    public static String timeBetween(Date date1, Date date2)
    {
        if(date1==null||date2==null) return "";
        String resultStr="";
        long miaocount=0;
        long mincount=0;
        long hourCount=0;
        long dayCount=0;
        long DAY = 24L * 60L * 60L * 1000L;
        long HOUR=60L * 60L * 1000L;
        long MIN=60L * 1000L;
        long MIAO=1000L;
        long haomiao = 0L;
        long miaolen=date2.getTime()-date1.getTime();
        if(miaolen<1000){haomiao = miaolen;}
        if(miaolen>60)miaocount=(miaolen / MIAO)%60;
        mincount=(miaolen / MIN)%60;
        hourCount=(miaolen / HOUR)%24;
        dayCount=miaolen / DAY;
        resultStr=dayCount+"天"+hourCount+"小时"+mincount+"分"+miaocount+"秒";
        if(dayCount==0)
            resultStr=hourCount+"小时"+mincount+"分"+miaocount+"秒";
        if(dayCount==0&&hourCount==0)
            resultStr=mincount+"分"+miaocount+"秒";
        if(dayCount==0&&hourCount==0&&mincount==0)
            resultStr=miaocount+"秒";
        if(dayCount==0&&hourCount==0&&mincount==0&&miaocount==0){
            resultStr=haomiao+"毫秒";
        }
        return resultStr;
    }
    /**
     * 使用yyyy-MM-dd HH:mm:ss格式解析时间
     * @param dateValue
     * @return
     */
    public static Date parseDate(String dateValue){
        return parseDate(dateValue, YYYY_MM_DDHHmmss);
    }
    /**
     * 使用指定格式解析时间
     * @param dateValue
     * @param strFormat
     * @return
     */
    public static Date parseDate(String dateValue, String strFormat){
        if(dateValue == null){
            return null;
        }
        if(strFormat == null){
            strFormat = YYYY_MM_DDHHmmss;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(strFormat);
        Date newDate = null;
        try {
            newDate = dateFormat.parse(dateValue);
        } catch (ParseException e) {
            newDate = null;
        }
        return newDate;
    }
    //2015-08-31T15:14:39.000Z 格式化数据库时间判断
    public static String DataDateFormat(String str,String formatStr) {
        String string = Function.ClearTrim(str);
        if(string.length()==24 && string.indexOf("T")==10 && string.indexOf("Z")==23){
            // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
            SimpleDateFormat format = new SimpleDateFormat(yyyy_MM_ddTHHmmssSSSZ);
            try {
                format.setLenient(false);
                Date date = format.parse(string);
                if(formatStr==null){
                    formatStr = YYYY_MM_DDHHmmssSSS;
                }
                string = formatDate(date, formatStr);
            } catch (ParseException e) {
                string="";
            }
        }
        return string;
    }
    public static Date parseDateByUDBDateStr(String str) {
        String string = Function.ClearTrim(str);
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat(
                yyyy_MM_ddTHHmmssSSSZ);
        try {
            format.setLenient(false);
            return format.parse(string);
        } catch (ParseException e) {
            return null;
        }
    }
    //yyyy-MM-dd HH:mm:ss 转化为2015-08-31T15:14:39.000Z
    public static String addTZFormat(String str,String formatStr) {
        if(formatStr == null){
            formatStr = YYYY_MM_DDHHmmss;
        }
        Date date = parseDate(str, formatStr);
        String string = Function.ClearTrim(str);
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        string = formatDate(date, yyyy_MM_ddTHHmmssSSSZ);
        return string;
    }
    //日期  转化为2015-08-31T15:14:39.000Z
    public static String DateFormatTZ(Date date) {
        String string = "";
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        string = formatDate(date, yyyy_MM_ddTHHmmssSSSZ);
        return string;
    }
    // 格式化时间
    public static String format(Date aTs_Datetime, String as_Pattern) {
        if (aTs_Datetime == null || as_Pattern == null) {
            return null;
        } else {
            SimpleDateFormat dateFromat = new SimpleDateFormat();
            dateFromat.applyPattern(as_Pattern);
            return dateFromat.format(aTs_Datetime);
        }
    }
    public static Date isValidDate(String str){
        Date flag = null;
        String geshi = "yyyy/MM/dd HH:mm:ss";
        if(str.indexOf("-")>0)geshi = YYYY_MM_DDHHmmss;
        SimpleDateFormat format = new SimpleDateFormat(geshi);
        format.setLenient(false);
        try {
            flag = format.parse(str);
        } catch (ParseException e) {
            flag = null;
        }
        return flag;
    }
    //Long 类型转Date   yyyy_MM_ddTHHmmssSSSZ
    public static String longToDate(String dateFormat, Long millSec){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date = longToDate(millSec);
        if(dateFormat==null){
            dateFormat = YYYY_MM_DDHHmmss;
        }
        return sdf.format(date);
    }
    /**
     * udb毫秒数转换为日期对象， udb毫秒数是以0时区为标准的
     * @param millSec
     * @return
     */
    public static Date longToDate(Long millSec){
        Calendar ca = Calendar.getInstance();
        Date date = new Date(millSec);
        ca.setTime(date);
        ca.add(Calendar.HOUR_OF_DAY, -8);
        return ca.getTime();
    }
    /**
     * 得到指定日期所在月的最大天数
     * @param date
     * @return
     */
    public static int getMaxDayForMonth(Date date) {
        Date nextMonth = DateUtils.addDateByMonth(date, 1);
        Date maxDay = DateUtils.addDateByDay(nextMonth, -1);

        Calendar ca = Calendar.getInstance();
        ca.setTime(maxDay);
        return ca.get(Calendar.DAY_OF_MONTH);
    }
    /**
     * 获取当前的季度
     * @return
     */
    public static int getNowJidu(){
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH)+1;
        if(currentMonth>=1 && currentMonth<=3){
            return 1;
        }else if(currentMonth>=4 && currentMonth<=6){
            return 2;
        }else if(currentMonth>=7 && currentMonth<=9){
            return 3;
        }else{
            return 4;
        }
    }
    /**
     * 根据日期获取季度
     * @return
     */
    public static int getJiduByDate(Date date){
        Calendar c = dateToCalendar(date);
        int currentMonth = c.get(Calendar.MONTH)+1;
        if(currentMonth>=1 && currentMonth<=3){
            return 1;
        }else if(currentMonth>=4 && currentMonth<=6){
            return 2;
        }else if(currentMonth>=7 && currentMonth<=9){
            return 3;
        }else{
            return 4;
        }
    }
    /**
     * 根据日期获取季度
     * @return
     */
    public static String getJiduStrByDate(Date date){
        Calendar c = dateToCalendar(date);
        int currentMonth = c.get(Calendar.MONTH)+1;
        String jStr = "";
        if(currentMonth>=1 && currentMonth<=3){
            jStr = "第一季度";
        }else if(currentMonth>=4 && currentMonth<=6){
            jStr = "第二季度";
        }else if(currentMonth>=7 && currentMonth<=9){
            jStr = "第三季度";
        }else{
            jStr = "第四季度";
        }
        String suStr = format(date, "yyyy")+"年"+jStr;
        return suStr;
    }
    //Date 转 Calendar
    public static Calendar dateToCalendar(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
    // Calendar 转 Date
    public static Date calendarToDate(Calendar calendar){
        Date date = calendar.getTime();
        return date;
    }
    /**
     * 获得本周的第一天，周一
     *
     * @return
     */
    public static Date getCurrentWeekDayStartTime() {
        Calendar c = dateToCalendar(new Date());
        SimpleDateFormat longSdf = new SimpleDateFormat(YYYY_MM_DDHHmmss);
        SimpleDateFormat shortSdf = new SimpleDateFormat(yyyy_MM_dd);
        try {
            int weekday = c.get(Calendar.DAY_OF_WEEK) - 2;
            c.add(Calendar.DATE, -weekday);
            c.setTime(longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }
    /**
     * 获得本周的最后一天，周日
     *
     * @return
     */
    public static Date getCurrentWeekDayEndTime() {
        Calendar c = dateToCalendar(new Date());
        SimpleDateFormat longSdf = new SimpleDateFormat(YYYY_MM_DDHHmmss);
        SimpleDateFormat shortSdf = new SimpleDateFormat(yyyy_MM_dd);
        try {
            int weekday = c.get(Calendar.DAY_OF_WEEK);
            c.add(Calendar.DATE, 8 - weekday);
            c.setTime(longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }
    /**
     * 获得本天的开始时间，即2012-01-01 00:00:00
     *
     * @return
     */
    public static Date getCurrentDayStartTime() {
        SimpleDateFormat shortSdf = new SimpleDateFormat(yyyy_MM_dd);
        Date now = new Date();
        try {
            now = shortSdf.parse(shortSdf.format(now));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }
    /**
     * 获得本天的结束时间，即2012-01-01 23:59:59
     *
     * @return
     */
    public static Date getCurrentDayEndTime() {
        SimpleDateFormat longSdf = new SimpleDateFormat(YYYY_MM_DDHHmmss);
        SimpleDateFormat shortSdf = new SimpleDateFormat(yyyy_MM_dd);
        Date now = new Date();
        try {
            now = longSdf.parse(shortSdf.format(now) + " 23:59:59");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }
    /**
     * 获得本小时的开始时间，即2012-01-01 23:59:59
     *
     * @return
     */
    public static Date getCurrentHourStartTime() {
        SimpleDateFormat longHourSdf = new SimpleDateFormat("yyyy-MM-dd HH");
        Date now = new Date();
        try {
            now = longHourSdf.parse(longHourSdf.format(now));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }
    /**
     * 获得本小时的结束时间，即2012-01-01 23:59:59
     *
     * @return
     */
    public static Date getCurrentHourEndTime() {
        SimpleDateFormat longSdf = new SimpleDateFormat(YYYY_MM_DDHHmmss);
        SimpleDateFormat longHourSdf = new SimpleDateFormat("yyyy-MM-dd HH");
        Date now = new Date();
        try {
            now = longSdf.parse(longHourSdf.format(now) + ":59:59");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }
    /**
     * 获得本月的开始时间，即2012-01-01 00:00:00
     *
     * @return
     */
    public static Date getCurrentMonthStartTime() {
        SimpleDateFormat shortSdf = new SimpleDateFormat(yyyy_MM_dd);
        Calendar c = dateToCalendar(new Date());
        Date now = null;
        try {
            c.set(Calendar.DATE, 1);
            now = shortSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }
    /**
     * 当前月的结束时间，即2012-01-31 23:59:59
     *
     * @return
     */
    public static Date getCurrentMonthEndTime() {
        SimpleDateFormat longSdf = new SimpleDateFormat(YYYY_MM_DDHHmmss);
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = dateToCalendar(new Date());
        Date now = null;
        try {
            c.set(Calendar.DATE, 1);
            c.add(Calendar.MONTH, 1);
            c.add(Calendar.DATE, -1);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }
    /**
     * 当前年的开始时间，即2012-01-01 00:00:00
     *
     * @return
     */
    public static Date getCurrentYearStartTime() {
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = dateToCalendar(new Date());
        Date now = null;
        try {
            c.set(Calendar.MONTH, 0);
            c.set(Calendar.DATE, 1);
            now = shortSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }
    /**
     * 当前年的结束时间，即2012-12-31 23:59:59
     *
     * @return
     */
    public static Date getCurrentYearEndTime() {
        SimpleDateFormat longSdf = new SimpleDateFormat(YYYY_MM_DDHHmmss);
        SimpleDateFormat shortSdf = new SimpleDateFormat(yyyy_MM_dd);
        Calendar c = dateToCalendar(new Date());
        Date now = null;
        try {
            c.set(Calendar.MONTH, 11);
            c.set(Calendar.DATE, 31);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }
    /**
     * 当前季度的开始时间，即2012-01-1 00:00:00
     *
     * @return
     */
    public static Date getCurrentQuarterStartTime() {
        SimpleDateFormat longSdf = new SimpleDateFormat(YYYY_MM_DDHHmmss);
        SimpleDateFormat shortSdf = new SimpleDateFormat(yyyy_MM_dd);
        Calendar c = dateToCalendar(new Date());
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                c.set(Calendar.MONTH, 0);
            else if (currentMonth >= 4 && currentMonth <= 6)
                c.set(Calendar.MONTH, 3);
            else if (currentMonth >= 7 && currentMonth <= 9)
                c.set(Calendar.MONTH, 4);
            else if (currentMonth >= 10 && currentMonth <= 12)
                c.set(Calendar.MONTH, 9);
            c.set(Calendar.DATE, 1);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }
    /**
     * 当前季度的结束时间，即2012-03-31 23:59:59
     *
     * @return
     */
    public static Date getCurrentQuarterEndTime() {
        SimpleDateFormat longSdf = new SimpleDateFormat(YYYY_MM_DDHHmmss);
        SimpleDateFormat shortSdf = new SimpleDateFormat(yyyy_MM_dd);
        Calendar c = dateToCalendar(new Date());
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                c.set(Calendar.MONTH, 2);
                c.set(Calendar.DATE, 31);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                c.set(Calendar.MONTH, 8);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }
    /**
     * 获取前/后半年的开始时间
     * @return
     */
    public static Date getHalfYearStartTime(){
        SimpleDateFormat longSdf = new SimpleDateFormat(YYYY_MM_DDHHmmss);
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = dateToCalendar(new Date());
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 6){
                c.set(Calendar.MONTH, 0);
            }else if (currentMonth >= 7 && currentMonth <= 12){
                c.set(Calendar.MONTH, 6);
            }
            c.set(Calendar.DATE, 1);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;

    }
    /**
     * 获取前/后半年的结束时间
     * @return
     */
    public static Date getHalfYearEndTime(){
        SimpleDateFormat longSdf = new SimpleDateFormat(YYYY_MM_DDHHmmss);
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = dateToCalendar(new Date());
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 6){
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            }else if (currentMonth >= 7 && currentMonth <= 12){
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }
    /**
     * 获得两个日期之间所有的月份
     * @param minDate
     * @param maxDate
     * @return
     */
    public static List<String> getMonthBetween(Date minDate, Date maxDate){
        List<String> result = new ArrayList<String>();

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        min.setTime(minDate);
        min.set(min.get(Calendar.YEAR),min.get(Calendar.MONTH), 1);

        max.setTime(maxDate);
        max.set(max.get(Calendar.YEAR),max.get(Calendar.MONTH), 2);

        Calendar curr = min;
        while(curr.before(max)){
            result.add(format(curr.getTime(), yyyy_MM));
            curr.add(Calendar.MONTH, 1);
        }
        return result;
    }
    /**
     * 时间日期（yyyy-MM-dd HH:mm:ss）转换为毫秒数
     * @param str
     * @return
     */
    public static long hmsConvertMillisAll(String times){
        SimpleDateFormat sdf = DATE_FORMAT_YYYY_MM_DDkgHHmhMMmmss;
        long time = 0;
        try {
            time= sdf.parse(times).getTime();
        } catch (ParseException e) {
            System.out.println("错误代码是："+e);
            e.printStackTrace();
        }
        return time;
    }
    public static String getFromattime(long time){
        String ti=fromat.format(time);
        return ti;
    }
    public static String getFormattimeHour(long time){
        String tc=fromathour.format(System.currentTimeMillis());
        return tc;
    }
}
