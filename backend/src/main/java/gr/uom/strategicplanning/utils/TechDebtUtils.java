package gr.uom.strategicplanning.utils;

public class TechDebtUtils {
    public static final int DEV_WAGE_PER_HOUR = 30;
    public static final int WORK_HOURS_PER_WEEK = 40;
    public static final int WEEKS_PER_MONTH = 4;
    public static final int WORK_HOURS_PER_MONTH = WORK_HOURS_PER_WEEK * WEEKS_PER_MONTH;

    private TechDebtUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static double convertTDToHours(Number techDebtInMinutes) {
        return techDebtInMinutes.doubleValue() / 60.0;
    }

    public static double calculateTechDebtForAllHours(Number totalTechDebtInHours) {
        return totalTechDebtInHours.doubleValue() * DEV_WAGE_PER_HOUR;
    }

    public static double calculateTechDebtCostPerMonth(Number totalTechDebtCost, Number totalTechDebtInHours) {
        boolean techDebtIsLessThanOneMonth = totalTechDebtInHours.doubleValue() <= WORK_HOURS_PER_MONTH;
        if (techDebtIsLessThanOneMonth) return totalTechDebtCost.doubleValue();

        return totalTechDebtCost.doubleValue() / WORK_HOURS_PER_MONTH;
    }
}
