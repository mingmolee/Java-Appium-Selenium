package utilities;

import org.apache.commons.text.CaseUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Tools {
  public static String getDate(String dateFormat, int daysToAddOrSubstract) {
    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.add(Calendar.DATE, daysToAddOrSubstract);
    return formatter.format(calendar.getTime());
  }

  public static String toCamelCase(String string) {
    return CaseUtils.toCamelCase(string, false);
  }

  private static String generateRandom(String characters, int count) {
    StringBuilder builder = new StringBuilder();

    while (count-- != 0) {
      int character = (int) (Math.random() * characters.length());
      builder.append(characters.charAt(character));
    }
    return builder.toString();
  }

  public static String getRandomString(int size) {
    return generateRandom("QWERTYUIOPASDFGHJKLZXCVBNM", size);
  }

  public static String getRandomIntString(int size) {
    return generateRandom("123456789", size);
  }

  public static Integer getRandomInt(int size) {
    Random rand = new Random();
    return rand.nextInt(size);
  }
}
