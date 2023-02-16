package studentmanagement.utils;

import java.util.HashMap;
import java.util.Map;

public class AcademicNorms {
    public static Map<String, Integer> grade_to_number = new HashMap<>();
    public static Float minMaxSemCredits = 24.0f;
    public static Float minCreditReqGraduation = 120.0f;

    static {
        grade_to_number.put("A", 10);
        grade_to_number.put("A-", 9);
        grade_to_number.put("B", 8);
        grade_to_number.put("B-", 7);
        grade_to_number.put("C", 6);
        grade_to_number.put("C-", 5);
        grade_to_number.put("D", 4);
        grade_to_number.put("F", 0);
    }

    // setter for minSemCredits
    public static void setMinMaxSemCredits(Float minSemCredits) {
        AcademicNorms.minMaxSemCredits = minSemCredits;
    }

}
