package generator;

import java.util.Arrays;

public class DateConverter {
    public static String toString(int year, int month, int day) {
        return year + "-" + month + "-" + day;
    }

    public static String toString(int year, int month, int day, int hour, int min, int sec) {
        return year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + sec;
    }

    public static int[] toInt(String date) {
        return Arrays.stream(date.split("-| |:")).mapToInt(Integer::valueOf).toArray();
    }

    public static int dateComparator(String a, String b) {
        if (a.equals(b))
            return 0;

        int[] aVal = toInt(a);
        int[] bVal = toInt(b);

        int len = aVal.length < bVal.length ? aVal.length : bVal.length;
        for (int i = 0; i < len; i++) {
            if (aVal[i] < bVal[i])
                return 1;
            else if (aVal[i] > bVal[i])
                return -1;
        }
        return 0;
    }
}
