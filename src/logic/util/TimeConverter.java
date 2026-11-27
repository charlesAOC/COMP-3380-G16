package logic.util;

/**
 * TimeConverter
 * class for time convertions
 */
public class TimeConverter {

    /**
     * COnverts lap times to integer milliseconds
     * 
     * @param time in mm:ss.ss format to convert
     * @return integer milliseconds or null for invalid input
     */
    public static Integer ConvertToMilliSec(String time) {

        // split time using the :
        String[] spltTime = time.split(":");
        Integer milliSec = null;

        // check that its a valid format
        if (spltTime.length > 1) {

            try {
                // extract data
                int min = Integer.parseInt(spltTime[0].trim());
                float sec = Float.parseFloat(spltTime[1].trim());

                // convert data
                milliSec = (int) ((min * 60 * 1000) + (sec * 1000));
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
                milliSec = null;
            }

        }

        return milliSec;
    }
}
