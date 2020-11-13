package com.biji.puppeteer.util;


import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static final String normalTimeFormat = "yyyy-MM-dd HH:mm:ss";
    public static final String normalTimeFormat_HH_mm = "yyyy-MM-dd HH:mm";
    public static final String DateFormat_yyyyMM = "yyyy-MM";
    public static final String normalDateFormat = "yyyy-MM-dd";
    public static final String normalDateFormat2 = "MM-dd";
    public static final String DateFormat_yyyyMMddHHmm = "yyyyMMddHHmm";
    public static final String DateFormat_yyyyMMddHHmmss = "yyyyMMddHHmmss";
    public static final String minuteDateFormat = "yyyy-MM-dd HH:mm";
    public static final String dateFormat_yyyyMMdd = "yyyy/MM/dd";
    public static final String pingyingDateFormat_time = "yyyy年MM月dd日 HH时mm分";
    public static final String pingyingDateFormat_month = "yyyy年MM月";
    public static final String pingyingDateFormat = "yyyy年MM月dd日";
    public static final String nomarlNoSpDateFormat = "yyyyMMdd";
    public static final String normalOnlyTimeFormat = "HH:mm";
    public static final String normalTimeFormatSecond= "HH:mm:ss";
    public static final String FULL_FIELD_FORMAT = "yyyyMMddhhmmssSSS";
    /**
     * 如果format没有传递过来，会使用这个默认的时间戳
     */
    public final static String FORMAT_DEFAULT="yyyy-MM-dd hh:mm:ss";

    public static final String solrTimeFormat = "%sT%sZ";

    public static String formatDate(Date date) {
        String ret = "";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(normalDateFormat);
            ret = sdf.format(date);
        } catch (Exception e) {
        }

        return ret;
    }

    public static String formatTime(Date date) {
        String ret = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(normalTimeFormat);
            ret = sdf.format(date);
        } catch (Exception e) {
        }

        return ret;
    }

    public static String formatDate(Date date, String format) {
        String ret = "";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            ret = sdf.format(date);
        } catch (Exception e) {
        }

        return ret;
    }

    public static Date getBeginDate(Date date) {
        if (date == null) {
            return null;
        }
        final Calendar instance = DateUtil.getCalendar(date);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        return instance.getTime();
    }

    public static Date getEndDate(Date date) {
        if (date == null) {
            return null;
        }
        final Calendar instance = DateUtil.getCalendar(date);
        instance.set(Calendar.HOUR_OF_DAY, 23);
        instance.set(Calendar.MINUTE, 59);
        instance.set(Calendar.SECOND, 59);
        return instance.getTime();
    }

    public static Date parseDate(String date) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(normalDateFormat);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseDate(String date, String format) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    // 通过传入的DepartureTime来判断在不在
    // fliterTime里面，在的话返回true，fliterTime格式为00:00-06:00,DepartureTime格式为0000
    public static boolean compareDepartureTime(String DepartureTime, String fliterTime) {
        String startTime = fliterTime.substring(0, 2) + fliterTime.substring(3, 5);
        String endTime = fliterTime.substring(6, 8) + fliterTime.substring(9, 11);
        if ((Integer.parseInt(DepartureTime) > Integer.parseInt(startTime))
                && (Integer.parseInt(DepartureTime) < Integer.parseInt(endTime))) {
            return true;
        }
        return false;

    }

    public static boolean beforeTime(String sTime, String eTime) throws ParseException {
        DateFormat df = DateFormat.getDateTimeInstance();
        DateFormat dfe = DateFormat.getDateTimeInstance();
        return df.parse(sTime).before(dfe.parse(eTime));
    }

    public static Date stringToDate(String str, String format) {
        DateFormat df = new SimpleDateFormat(format);
        try {
            return df.parse(str);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 以format格式化str，是否成功
     *
     * @param format
     * @return
     */
    public static boolean isDate(String date, String format) {
        DateFormat df = new SimpleDateFormat(format);
        try {
            df.parse(date);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 获取yyyy-MM-dd格式的日期
     *
     * @return String类型 yyyy-mm-dd 2009-01-12
     */
    public static String getYearMonthDay(Calendar dateTime) {
        int year = dateTime.get(Calendar.YEAR);
        int month = dateTime.get(Calendar.MONTH) + 1;
        int day = dateTime.get(Calendar.DATE);
        return String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);
    }

    /**
     * 返回指定日期是一周中的第几天
     *
     * @param date 指定日期
     * @return 返回指定日期是一周中的第几天，周日是第一天
     */
    public static int getWeek(Date date) {
        Calendar calendar = getCalendar(date);
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static String getWeekStr(Date date) {
        String str = null;
        int day = DateUtil.getWeek(date);
        str = Integer.toString(day);
        if ("0".equals(str)) {
            str = "星期日";
        } else if ("1".equals(str)) {
            str = "星期一";
        } else if ("2".equals(str)) {
            str = "星期二";
        } else if ("3".equals(str)) {
            str = "星期三";
        } else if ("4".equals(str)) {
            str = "星期四";
        } else if ("5".equals(str)) {
            str = "星期五";
        } else if ("6".equals(str)) {
            str = "星期六";
        }
        return str;
    }

    /**
     * 获取一个星期的第一天
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date getFirstDayOfWeek(Date date)  {
        Calendar cDay1 = Calendar.getInstance();
        cDay1.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        final int firstDayOfWeek = cDay1.get(Calendar.DAY_OF_WEEK);
        if (1 == firstDayOfWeek) {
            cDay1.add(Calendar.DAY_OF_MONTH, -1);
        }

        cDay1.setFirstDayOfWeek(Calendar.MONDAY);
        int day = cDay1.get(Calendar.DAY_OF_WEEK);
        cDay1.add(Calendar.DATE, cDay1.getFirstDayOfWeek() - day);
        String imptimeBegin = sdf.format(cDay1.getTime());
        try {
            return sdf.parse(imptimeBegin);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取一个月的第一天
     *
     * @param date
     * @return
     * @throws ParseException
     */
    @SuppressWarnings("deprecation")
    public static Date getFirstDayOfMonth(Date date) {
        Calendar cDay1 = Calendar.getInstance();
        cDay1.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        final int firstDay = cDay1.getActualMinimum(Calendar.DAY_OF_MONTH);
        Date firstDate = cDay1.getTime();
        firstDate.setDate(firstDay);
        String c = sdf.format(firstDate);
        try {
            return sdf.parse(c);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date getLastDayOfMonth(Date date) {
        try {
            Date getFirstDate = getFirstDayOfMonth(date);
            Date nextMonthDate = add(getFirstDate, Calendar.MONTH, 1);
            return add(nextMonthDate, Calendar.DAY_OF_YEAR, -1);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取当月第一天
     * yyyyMM格式
     */
    public static Date getFirstDayOfMonth(String date, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date selectDate = sdf.parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectDate);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return selectDate;
    }

    /**
     * 返回当月最后一天的日期
     * yyyy-MM格式
     */
    public static Date getLastDayOfMonth(String date, String format) throws ParseException {
        Date getFirstDate = getFirstDayOfMonth(date, format);
        Date nextMonthDate = add(getFirstDate, Calendar.MONTH, 1);
        return add(nextMonthDate, Calendar.DAY_OF_YEAR, -1);
    }

    /**
     * 将日期转换为日历
     *
     * @param date 日期
     * @return 日历
     */
    private static Calendar convert(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 是否为凌晨
     *
     * @param date 日期
     * @return 是否
     */
    public static Boolean isWeeHours(Date date) {
        Calendar calendar = convert(date);
        int hour = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        return (hour + min + second) == 0;
    }

    /**
     * 返回固定日期
     *
     * @param date yyyy-MM-dd
     * @return 固定日期
     */
    public static Date getFixDate(String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            return dateFormat.parse(date);
        } catch (ParseException e) {
            return new Date();
        }
    }

    /**
     * 给指定的日期增加指定的时间
     *
     * @param date   日期
     * @param field  如Calendar.FIELD
     * @param amount 数目,如1天
     * @return 增加指定时间的日期
     */
    public static Date add(Date date, int field, int amount) {
        Calendar calendar = getCalendar(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }

    /**
     * 返回指定日期的月份
     */
    public static int getMonth(Date date) {
        Calendar calendar = getCalendar(date);
        return calendar.get(Calendar.MONTH);
    }

    /**
     * 返回当前时间的小时数
     * @return 小时
     */
    public static int getHour() {
        Calendar calendar = getCalendar(new Date());
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * Date转换成Calendar
     *
     * @param date Date
     * @return Calendar
     */
    public static Calendar getCalendar(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param startDate 较小的时间
     * @param endDate   较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date startDate, Date endDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            startDate = sdf.parse(sdf.format(startDate));
            endDate = sdf.parse(sdf.format(endDate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(endDate);
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);
            return Integer.parseInt(String.valueOf(between_days));
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 根据星期几和时间,得到具体日期
     * @param alarmDay
     * @param alarmTime
     * @return
     */
    /*public static Date getExecuteTime(int nDay,String sTime)
    {
		Date nowTime = new Date();
		int nDays = DateUtil.getWeek(nowTime);//当前时间是星期几

		Date executeTime =  DateUtil.stringToDate(DateUtil.formatDate(nowTime,DateUtil.normalDateFormat)+" "+sTime, "yyyy-MM-dd HH:mm");

		Calendar calendar =DateUtil.getCalendar(executeTime);

		if(nDays ==0)
		{
			nDays =7;//把星期日,转换为我们传进去的格式
		}

		if(nDays == nDay)//同一天，则比较时间
		{
			if(executeTime.before(nowTime))//同一天，但时间已过去，则加一周时间
			{
				calendar.add(Calendar.DATE, 7);
				executeTime = calendar.getTime();
			}
		}
		else
		{
			if(nDays >nDay)//下周
			{
				calendar.add(Calendar.DATE, 7+nDay-nDays);
			}
			else//本周后面时间
			{
				calendar.add(Calendar.DATE, nDay-nDays);
			}
			executeTime = calendar.getTime();
		}
		return executeTime;
	}*/

    /**
     * 根据时间，得到24小时内的的执行时间
     *
     * @param alarmTime hh:mm
     * @return
     */
    public static Date GetExecuteTime(String alarmTime) {
        Date nowTime = new Date();

        Date executeTime = DateUtil.stringToDate(DateUtil.formatDate(nowTime, DateUtil.normalDateFormat) + " " + alarmTime, "yyyy-MM-dd HH:mm");

        if (executeTime.before(nowTime))//同一天，但时间已过去，则加一周时间
        {
            Calendar calendar = DateUtil.getCalendar(executeTime);
            calendar.add(Calendar.DATE, 1);
            executeTime = calendar.getTime();
        }
        return executeTime;
    }

    public static String monthAndDayText(Date date) {
        String ret = "";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
            ret = sdf.format(date);
        } catch (Exception e) {
        }

        return ret;
    }

    private static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10) {
            retStr = "0" + Integer.toString(i);
        } else {
            retStr = "" + i;
        }
        return retStr;
    }

    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static Date getFirstSaturdayOfWeek() {
        try {
            Date date = new Date();
            Date firstMon = getFirstDayOfWeek(date);
            return add(firstMon,Calendar.DAY_OF_YEAR,-2);
        } catch (Exception e) {
            return add(new Date(),Calendar.DAY_OF_YEAR,-6);
        }
    }

    public static Date getLeaseFutureDate() {
        return getLeaseFutureDate(2);
    }

    public static Date getLeaseFutureDate(int years) {
        return DateUtil.add(new Date(), Calendar.MONTH, years * 12);
    }

    public static Date getFirstDayOfCurYear() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        return calendar.getTime();
    }

    public static String getFirstDayOfCurYear(String fmt) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        return formatDate(calendar.getTime(), fmt);
    }

    public static Date getLastDayOfCurYear(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();
    }

    public static String getLastDayOfCurYear(String fmt){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        return formatDate(calendar.getTime(), fmt);
    }

    public static String toSolrFilterString(Date date) {
        if (date == null) {
            return null;
        }
        String dateStr = formatDate(date,normalDateFormat);
        String timeStr = formatDate(date,normalTimeFormatSecond);
        return String.format(solrTimeFormat,dateStr,timeStr);
    }

    /**
     * 获取一个星期的最后一天
     * @param date Date
     * @return 最后一天
     */
    public static Date getLastOfWeek(Date date) {
        Date firstOfWeek = getFirstDayOfWeek(date);
        if (firstOfWeek == null) {
            return null;
        }
        Calendar cDay1 = Calendar.getInstance();
        cDay1.setTime(firstOfWeek);
        cDay1.add(Calendar.DATE,7);
        return cDay1.getTime();
    }

    /**
     * 获取两个日期相差几个月
     * @param start 开始
     * @param end 结束
     * @return 间隔
     */
    public static int monthBetween(Date start, Date end) {
        if (start.after(end)) {
            Date t = start;
            start = end;
            end = t;
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        Calendar temp = Calendar.getInstance();
        temp.setTime(end);
        temp.add(Calendar.DATE, 1);
        int year = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        int month = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
        if ((startCalendar.get(Calendar.DATE) == Calendar.SUNDAY) && (temp.get(Calendar.DATE) == Calendar.SUNDAY)) {
            return year * 12 + month + 1;
        } else if ((startCalendar.get(Calendar.DATE) != Calendar.SUNDAY) && (temp.get(Calendar.DATE) == Calendar.SUNDAY)) {
            return year * 12 + month;
        } else if ((startCalendar.get(Calendar.DATE) == Calendar.SUNDAY) && (temp.get(Calendar.DATE) != Calendar.SUNDAY)) {
            return year * 12 + month;
        } else {
            return (year * 12 + month - 1) < 0 ? 0 : (year * 12 + month);
        }
    }

    /**
     * 获取两个星期的最后一天
     * @param date Date
     * @return 最后一天
     */
    public static Date getLastOfTwoWeek(Date date) {
        Date firstOfWeek = getLastOfWeek(date);
        Calendar cDay1 = Calendar.getInstance();
        if (firstOfWeek == null) {
            return null;
        }
        cDay1.setTime(firstOfWeek);
        cDay1.add(Calendar.DATE,7);
        return cDay1.getTime();
    }

    public static Date getOneYearLaterDate() {
        return DateUtil.add(Calendar.getInstance().getTime(), Calendar.MONTH, 3);
    }

    public static Date getThreeMonthLaterDate() {
        return DateUtil.add(Calendar.getInstance().getTime(), Calendar.MONTH, 3);
    }

    /**
     * 获取一个月的第一天
     *
     * @param date
     * @return
     */
    public static Date getMonthFirstDay(Date date) {
        Calendar cDay1 = Calendar.getInstance();
        cDay1.setTime(date);
        cDay1.set(Calendar.DAY_OF_MONTH,1);
        cDay1.set(Calendar.HOUR_OF_DAY,0);
        cDay1.set(Calendar.MINUTE,0);
        cDay1.set(Calendar.SECOND,0);
        return cDay1.getTime();
    }

    public static String getWeekIndexInYear(Date date){
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        return formatDate(date, DateUtil.DateFormat_yyyyMM).substring(0, 4) +"第" +calendar.get(Calendar.WEEK_OF_YEAR)+"周";
    }

    /**
     * 将 {@link Date} 转化为 10位的时间戳
     * @param date {@link Date}
     * @return 10位的时间戳
     */
    public static int dateToInt10(Date date){
        return long13To10(date.getTime());
    }

    /**
     * 将13位Linux时间戳转换为10位时间戳
     * @param time 13位Linux时间戳
     * @return 10位Linux时间戳
     */
    public static int long13To10(long time){
        String substring = (time + "").substring(0, 10);
        return Integer.valueOf(substring);
    }

    /**
     * 返回当前13位的Unix时间戳
     *
     * @return 13位Unix时间戳
     * @see Date
     */
    public static long timeForUnix13() {
        Date date = new Date();
        return date.getTime();
    }

    /**
     * 返回当前10位数的Unix时间戳
     *
     * @return Unix时间戳，失败返回0
     */
    public static int timeForUnix10() {
        return long13To10(timeForUnix13());
    }

    /**
     * 传入一个10位的时间戳，返回当前时间戳所在的当天0点的10位时间戳
     *
     * @param time 10位的时间戳
     * @return 当天0点的时间戳。若失败，返回0
     */
    public static int getDateZeroTime(int time) {
        String ls;
        try {
            ls = DateUtil.dateFormat(time, "yyyy-MM-dd");
        } catch (IllegalArgumentException e) {
            return 0;
        }
        return DateUtil.StringToInt(ls + " 00:00:00", "yyyy-MM-dd hh:mm:ss");
    }

    /**
     * 将Linux时间戳变为文字描述的时间
     *
     * @param linuxTime Linux时间戳，10位或者13位
     * @param format    转换格式 ,若不填，默认为yyyy-MM-dd hh:mm:ss {@link #FORMAT_DEFAULT}
     * @return 转换后的日期。如 2016-01-18 11:11:11
     * @throws NotReturnValueException
     */
    public static String dateFormat(long linuxTime, String format) throws IllegalArgumentException {
        int linuxTimeLength = (linuxTime + "").length();
        if (linuxTime == 0 || !(linuxTimeLength == 10 || linuxTimeLength == 13)) {
            throw new IllegalArgumentException("传入的linux时间戳长度错误！当前传入的时间戳：" + linuxTime + ",请传入10或者13位的时间戳");
        } else {
            if (format == null || format.length() == 0) {
                format = FORMAT_DEFAULT;
            }

            if (linuxTimeLength == 10) {
                linuxTime = linuxTime * 1000;
            }
            return new SimpleDateFormat(format).format(new Date(linuxTime));
        }
    }


    /**
     * 将String类型时间转换为10位的linux时间戳
     *
     * @param time   要转换的时间，如2016-02-18 00:00:11
     * @param format 要转换的String的时间格式，如：yyyy-MM-dd HH:mm:ss
     * @return 10位Linux时间戳
     * @throws ParseException
     */
    public static int StringToInt(String time, String format) {
        long d = StringToDate(time, format).getTime();
        if (d == 0) {
            return 0;
        } else {
            return (int) Math.ceil(d / 1000.0);
        }
    }

    /**
     * 将String类型时间转换为Date对象
     *
     * @param time   要转换的时间，如2016-02-18 00:00:11
     * @param format 要转换的String的时间格式，如：yyyy-MM-dd HH:mm:ss
     * @return Date对象 若失败，返回null
     */
    public static Date StringToDate(String time, String format) {
        SimpleDateFormat sFormat = new SimpleDateFormat(format);
        Date date;
        try {
            date = sFormat.parse(time);
        } catch (ParseException e) {
            return new Date();
        }
        return date;
    }

    /**
     * 获取30天前时间
     * @return
     */
    public static Date getDateThirteenDaysBefore() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, -30);
        String endDate = new SimpleDateFormat(normalTimeFormat).format(now.getTime());
        return parseDate(endDate, normalTimeFormat);
    }

    /**
     * 获取指定天数之前时间
     * @return
     */
    public static Date getAssignDaysBefore(int day) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, -day);
        String endDate = new SimpleDateFormat(normalTimeFormat).format(now.getTime());
        return parseDate(endDate, normalTimeFormat);
    }

    public static int hoursBetween(Date start, Date end) {
        if (start == null && end == null) {
            return 0;
        }
        if (end == null) {
            Calendar calendar = getCalendar(start);
            return -calendar.get(Calendar.HOUR_OF_DAY);
        }
        if (start == null) {
            Calendar calendar = getCalendar(end);
            return calendar.get(Calendar.HOUR_OF_DAY);
        }
        long betweenDays = (end.getTime() - start.getTime()) / (1000 * 3600);
        return Integer.parseInt(String.valueOf(betweenDays));
    }

    /**
     * 获取指定时间所在季度的第一天
     *
     * @param date
     */
    public static Date getFirstDayOfQuarter(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;

        int quarter = getQuarter(month);

        String yearStr = year + "";
        String startDay = "";

        switch (quarter) {
            case 1:
                startDay = yearStr + "-01-01 00:00:00";
                break;
            case 2:
                startDay = yearStr + "-04-01 00:00:00";
                break;
            case 3:
                startDay = yearStr + "-07-01 00:00:00";
                break;
            case 4:
                startDay = yearStr + "-10-01 00:00:00";
                break;
        }
        return DateUtil.parseDate(startDay, DateUtil.normalTimeFormat);
    }

    /**
     * 获取指定时间所在季度的最后一天
     *
     * @param date
     */
    public static Date getLastDayOfQuarter(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;

        int quarter = getQuarter(month);

        String yearStr = year + "";
        String endDay = "";

        switch (quarter) {
            case 1:
                endDay = yearStr + "-03-31 23:59:59";
                break;
            case 2:
                endDay = yearStr + "-06-30 23:59:59";
                break;
            case 3:
                endDay = yearStr + "-09-30 23:59:59";
                break;
            case 4:
                endDay = yearStr + "-12-31 23:59:59";
                break;
        }
        return DateUtil.parseDate(endDay, DateUtil.normalTimeFormat);
    }

    private static int getQuarter(int month) {
        int quarter = 0;
        if (month >= 1 && month <= 3) {
            quarter = 1;
        } else if (month >= 4 && month <= 6) {
            quarter = 2;
        } else if (month >= 7 && month <= 9) {
            quarter = 3;
        } else if (month >= 10 && month <= 12){
            quarter = 4;
        }
        return quarter;
    }

    public static Date getFirstDayDateOfYear(Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        final int last = cal.getActualMinimum(Calendar.DAY_OF_YEAR);
        cal.set(Calendar.DAY_OF_YEAR, last);
        return cal.getTime();
    }

    /**
     * 获取几个月前时间，
     * @param month
     * @return
     */
    public static Date getMonthBefore(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -month);
        Date dBefore = calendar.getTime();
        return dBefore;
    }

}
