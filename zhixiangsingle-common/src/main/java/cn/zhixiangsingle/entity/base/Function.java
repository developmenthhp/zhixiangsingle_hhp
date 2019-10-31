package cn.zhixiangsingle.entity.base;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.UUID;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.base
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/21 18:51
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class Function {
    // 得到32位序列
    public static String getUUID() {
        String uuid = Function.ClearTrim(UUID.randomUUID()).replaceAll("-", "");
        return uuid;
    }
    // 随机4位数字
    public static int radomNumber() {
        String str = Math.random() + "";
        str = str.substring(str.length() - 4, str.length());
        int i = getInt(str, 9999);
        return i;
    }
    // 随机n位数字组成的字符串
    public static String radomNumber(int n) {
        String str = Math.random() + "";
        if (n < str.length() - 3) {
            str = str.substring(str.length() - n, str.length());
            return str;
        } else {
            return null;
        }

    }
    /**
     * 判断字符串是否为空
     *
     * @param src
     * @return false:不为空 true:字符串为空
     */
    public static Boolean isNull(String src) {
        if ("".equals(ClearTrim(src))) {
            return true;
        }
        return false;
    }
    // 清除字符串两边的空格
    public static String ClearTrim(Object obj) {
        String result = "";
        if (obj != null)
            result = obj.toString();
        if (result != null)
            result = result.trim();
        return result;
    }
    // 把对象转化为整数，非整数则返回defaultNum
    public static int getInt(Object obj, int defaultNum) {
        String value = ClearTrim(obj);
        int num = defaultNum;
        if (value != null && !value.equals("")) {
            try {
                if (value.lastIndexOf(".") > 0) {
                    value = value.substring(0, value.lastIndexOf("."));
                }
                num = Integer.parseInt(value);
            } catch (Exception ignored) {
                // ignored.printStackTrace();
            }
        }
        return num;
    }
    // 把对象转化为长整数，非整数则返回defaultNum
    public static Long getLong(Object obj, Long defaultNum) {
        String value = ClearTrim(obj);
        Long num = defaultNum;
        if (value != null && !value.equals("")) {
            try {
                num = Long.parseLong(value);
            } catch (Exception ignored) {
            }
        }
        return num;
    }
    // 把对象转化为浮点，非浮点则返回defaultNum
    public static double getFloat(Object obj, Float defaultNum) {
        Float num = defaultNum;
        String value = ClearTrim(obj);
        if (value != null && !value.equals("")) {
            try {
                num = Float.valueOf(ClearTrim(value));
            } catch (Exception ignored) {
            }
        }
        return num;
    }
    // 把对象转化为双精度，非双精度则返回defaultNum
    public static double getDouble(Object obj, Double defaultNum) {
        Double num = defaultNum;
        String value = ClearTrim(obj);
        if (value != null && !value.equals("")) {
            try {
                num = Double.valueOf(ClearTrim(value));
            } catch (Exception ignored) {
            }
        }
        return num;
    }
    // 把浮点数转换成字符串，不自动转化为科学计数法
    public static String doubleToStr(Double defaultNum, String format) {
        if (format == null) {
            format = "###0.0000#";
        }
        DecimalFormat df = new DecimalFormat(format);// 最多保留几位小数，就用几个#，最少位就用0来确定
        String s = df.format(defaultNum);
        return s;
    }
    public static String kexueStrToNormalStr(String oldStr){
        BigDecimal dBigDecimal = new BigDecimal(oldStr);
        String newStr = dBigDecimal.toPlainString();
        return newStr;
    }
    /**
     * 分割字符串
     *
     * @param str
     *            要分割的字符串
     * @param spilit_sign
     *            字符串的分割标志
     * @return 分割后得到的字符串数组
     */
    public static String[] stringSpilit(String str, String spilit_sign) {
        str = ClearTrim(str);
        String[] spilit_string = str.split(spilit_sign);
        if (spilit_string[0].equals("")) {
            String[] new_string = new String[spilit_string.length - 1];
            for (int i = 1; i < spilit_string.length; i++)
                new_string[i - 1] = spilit_string[i];
            return new_string;
        } else {
            return spilit_string;
        }
    }
    public static boolean isExit(String value, String strList) {
        value = Function.ClearTrim(value);
        strList = Function.ClearTrim(strList);
        boolean isOk = false;
        String strArray[] = strList.split(",");
        for (int i = 0; i < strArray.length; i++) {
            if (value.equals(strArray[i])) {
                isOk = true;
                break;
            }
        }
        return isOk;
    }
    // 特殊字符过滤
    public static String CheckReplace(String s) {
        try {
            if (s == null || s.equals(""))
                return "";
            else {
                StringBuffer stringbuffer = new StringBuffer();
                for (int i = 0; i < s.length(); i++) {
                    char c = s.charAt(i);
                    switch (c) {
                        case 10:
                            // stringbuffer.append("<br>");
                            stringbuffer.append(" ");
                            break;
                        case 13:
                            // stringbuffer.append("<br>");
                            stringbuffer.append(" ");
                            break;
                        case 34: // '"'
                            stringbuffer.append("&quot;");
                            break;

                        case 39: // '\''
                            stringbuffer.append("&#039;");
                            break;

                        // case 124: // '|'
                        // stringbuffer.append("");
                        // break;
                        case 92:// '\'
                            stringbuffer.append("/");
                            break;
                        case '&':
                            stringbuffer.append("&amp;");
                            break;

                        case '<':
                            stringbuffer.append("&lt;");
                            break;

                        case '>':
                            stringbuffer.append("&gt;");
                            break;

                        default:
                            stringbuffer.append(c);
                            break;
                    }
                }
                return stringbuffer.toString().trim();
            }
        } catch (Exception e) {
            return "";
        }
    }
    public static String getStrChangeFirstU(String str) {
        str = str.toLowerCase();
        String result = "";
        String strArray[] = Function.ClearTrim(str).split("_");
        for (int i = 0; i < strArray.length; i++) {
            String str1 = strArray[i];
            String content = str1.substring(0, 1).toUpperCase()
                    + str1.substring(1, str1.length());
            result += content;
        }
        return result;

    }
    public static String getStrChangeFirstL(String str) {
        str = str.toLowerCase();
        String result = "";
        String strArray[] = Function.ClearTrim(str).split("_");
        for (int i = 0; i < strArray.length; i++) {
            String str1 = strArray[i];
            String content = "";
            if (i == 0) {
                content = str1.substring(0, 1).toLowerCase()
                        + str1.substring(1, str1.length());
            } else {
                content = str1.substring(0, 1).toUpperCase()
                        + str1.substring(1, str1.length());
            }
            result += content;
        }
        return result;
    }
    /**
     * 两个数字字符串相减,number1-number2
     * */
    public static String subtractionStr(String number1, String number2) {
        Integer newNumber = getInt(number1, 0) - getInt(number2, 0);
        return newNumber.toString();
    }
    /**
     * 两个数字字符串相加,number1-number2
     * */
    public static String additionStr(String number1, String number2) {
        Integer newNumber = getInt(number1, 0) + getInt(number2, 0);
        return newNumber.toString();
    }

    // 特殊字符过滤反转
    public static String CheckReplaceOpposition(String s) {
        try {
            s = s.replace("&lt;", "<");
            s = s.replace("&gt;", ">");
            s = s.replace("&nbsp;", " ");
            s = s.replace("&amp;", "&");
            s = s.replace("&quot;", "\"");
            s = s.replace("&ensp;", " ");
            s = s.replace("&emsp;", "  ");
            s = s.replace("&mdash;", "'");
            return Function.ClearTrim(s);
        } catch (Exception e) {
            return "";
        }
    }
    /**
     * 字符替换
     *
     * @param rStr
     *            被替换的字符串
     * @param rFix
     *            被替代的字符串
     * @param rRep
     *            替代的字符串
     * @return String 将字符串rStr中的字符串rFix替换成rRep
     */
    public static String replaceAll(String rStr, String rFix, String rRep) {
        if (rStr == null)
            return "";
        String strrStr = rStr.toLowerCase();
        String strrFix = rFix.toLowerCase();
        int idx1 = 0;
        int idx2 = strrStr.indexOf(strrFix, idx1);
        int len = strrFix.length();
        StringBuffer sb = new StringBuffer();
        while (true) {
            if (idx2 == -1) {
                sb.append(rStr.substring(idx1, rStr.length()));
                break;
            }
            sb.append(rStr.substring(idx1, idx2));
            sb.append(rRep);
            idx1 = idx2 + len;
            idx2 = strrStr.indexOf(strrFix, idx1);
        }
        String gRtnStr = sb.toString();
        return gRtnStr;
    }
    /**
     *
     * 返回首字母
     *
     * @param strChinese
     *
     * @param bUpCase
     *
     * @return
     */

    public static String getPYIndexStr(String strChinese, boolean bUpCase) {
        try {

            StringBuffer buffer = new StringBuffer();

            byte b[] = strChinese.getBytes("GBK");// 把中文转化成byte数组

            for (int i = 0; i < b.length; i++) {

                if ((b[i] & 255) > 128) {

                    int char1 = b[i++] & 255;

                    char1 <<= 8;// 左移运算符用“<<”表示，是将运算符左边的对象，向左移动运算符右边指定的位数，并且在低位补零。其实，向左移n位，就相当于乘上2的n次方

                    int chart = char1 + (b[i] & 255);

                    buffer.append(getPYIndexChar((char) chart, bUpCase));

                    continue;

                }

                char c = (char) b[i];

                if (!Character.isJavaIdentifierPart(c))// 确定指定字符是否可以是 Java
                    // 标识符中首字符以外的部分。

                    c = 'A';

                buffer.append(c);

            }

            return buffer.toString();

        } catch (Exception e) {

            System.out.println((new StringBuilder())
                    .append("\u53D6\u4E2D\u6587\u62FC\u97F3\u6709\u9519")
                    .append(e.getMessage()).toString());

        }

        return null;

    }
    /**
     *
     * 得到首字母
     *
     * @param strChinese
     *
     * @param bUpCase
     *
     * @return
     */
    private static char getPYIndexChar(char strChinese, boolean bUpCase) {

        int charGBK = strChinese;

        char result;

        if (charGBK >= 45217 && charGBK <= 45252)

            result = 'A';

        else

        if (charGBK >= 45253 && charGBK <= 45760)

            result = 'B';

        else

        if (charGBK >= 45761 && charGBK <= 46317)

            result = 'C';

        else

        if (charGBK >= 46318 && charGBK <= 46825)

            result = 'D';

        else

        if (charGBK >= 46826 && charGBK <= 47009)

            result = 'E';

        else

        if (charGBK >= 47010 && charGBK <= 47296)

            result = 'F';

        else

        if (charGBK >= 47297 && charGBK <= 47613)

            result = 'G';

        else

        if (charGBK >= 47614 && charGBK <= 48118)

            result = 'H';

        else

        if (charGBK >= 48119 && charGBK <= 49061)

            result = 'J';

        else

        if (charGBK >= 49062 && charGBK <= 49323)

            result = 'K';

        else

        if (charGBK >= 49324 && charGBK <= 49895)

            result = 'L';

        else

        if (charGBK >= 49896 && charGBK <= 50370)

            result = 'M';

        else

        if (charGBK >= 50371 && charGBK <= 50613)

            result = 'N';

        else

        if (charGBK >= 50614 && charGBK <= 50621)

            result = 'O';

        else

        if (charGBK >= 50622 && charGBK <= 50905)

            result = 'P';

        else

        if (charGBK >= 50906 && charGBK <= 51386)

            result = 'Q';

        else

        if (charGBK >= 51387 && charGBK <= 51445)

            result = 'R';

        else

        if (charGBK >= 51446 && charGBK <= 52217)

            result = 'S';

        else

        if (charGBK >= 52218 && charGBK <= 52697)

            result = 'T';

        else

        if (charGBK >= 52698 && charGBK <= 52979)

            result = 'W';

        else

        if (charGBK >= 52980 && charGBK <= 53688)

            result = 'X';

        else

        if (charGBK >= 53689 && charGBK <= 54480)

            result = 'Y';

        else

        if (charGBK >= 54481 && charGBK <= 55289)

            result = 'Z';

        else

            result = (char) (65 + (new Random()).nextInt(25));

        if (!bUpCase)

            result = Character.toLowerCase(result);

        return result;

    }
    public static String urlDecodeToUTF8(String str) {
        if (str != null) {
            String strNew = "";
            try {
                strNew = URLDecoder.decode(str, "UTF-8");
                strNew = URLDecoder.decode(strNew, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }
            return strNew;
        } else {
            return "";
        }
    }
    public static String arrayToStr(Object[] array) {
        StringBuffer s = new StringBuffer();
        for (int x = 0; x < array.length; x++) {
            if (x != 0) {
                s.append(",");
            }
            s.append(array[x].toString());
        }
        return s.toString();
    }
    /**
     * 将元数据前补零，补后的总长度为指定的长度，以字符串的形式返回
     *
     * @param sourceDate
     * @param formatLength
     * @return 重组后的数据
     */
    public static String frontCompWithZore(int sourceNumber, int formatLength) {
        /*
         * 0 指前面补充零 formatLength 字符总长度为 formatLength d 代表为正数。
         */
        String newString = String.format("%0" + formatLength + "d",
                sourceNumber);
        return newString;
    }
    /**
     * 将元数据前补零，补后的总长度为指定的长度，以字符串的形式返回
     *
     * @param sourceDate
     * @param formatLength
     * @return 重组后的数据
     */
    public static String frontCompWithZoreRight(String sourceNumber, int formatLength) {
        /*
         * 0 指前面补充零 formatLength 字符总长度为 formatLength d 代表为正数。
         */
        String newString = sourceNumber + String.format("%1$0"+(formatLength-sourceNumber.length())+"d", 0);
        return newString;
    }
    /**
     * 将元数据后补空格，补后的总长度为指定的长度，以字符串的形式返回
     *
     * @param sourceDate
     * @param formatLength
     * @return 重组后的数据
     */
    public static String frontCompWithBlankRight(String sourceNumber, int formatLength) {
        /*
         * 0 指前面补充零 formatLength 字符总长度为 formatLength d 代表为正数。
         */
        String newString = String.format("%-"+formatLength+"s", sourceNumber);
        return newString;
    }
    //18位身份证号 倒数第二位是性别，单数是男，双数是女
    public static String getXbByZJHM(String ZJHM){
        if(ClearTrim(ZJHM).length()!=18){
            return "未知";
        }else{
            String sbStr = ZJHM.substring(16,17);
            int i = getInt(sbStr, 0);
            if(i%2==0){
                return "女";
            }else{
                return "男";
            }
        }
    }
    /**
     * 删除右边的0
     * @param p
     * @return
     */
    public static String deleteRightZero(String p){
        int i = 0;
        while (i < 2) {
            i++;
            if (p.length() > 2 && p.lastIndexOf('0') != -1) {
                p = p.substring(0, p.length() - 1);
            }
        }

        return p;
    }
    /**
     * 翻译地区名称
     * 因为要匹配 地图中各区县的名称，所以需要去掉省市的名称
     * 四川省成都市高新区  ==〉高新区
     * 四川省成都市崇州市  ==〉崇州市
     * @param name
     * @return
     */
    public static String getAreaNameByName(String name) {
        if (name == null)
            return name;

        String [] strs = name.split("市");
        String str = strs[strs.length - 1];

        if (name.endsWith("市"))
            str = str + "市";

        return str;
    }
    public static void main(String[] args){

    }
    public static Integer getIntByDoubleStr(Object object){
        double b = getDouble(object, 0.0);
        Integer c = (int)b;
        return c;
    }
}
