package edu.upenn.cis350.project.data;

import android.util.Log;

import java.time.LocalDate;

public class HealthScore {

    private LocalDate date;
    private int proteinGoal = 0;
    private int proteinConsumed = 0;
    private int fatGoal = 0;
    private int fatConsumed = 0;
    private int fiberGoal = 0;
    private int fiberConsumed = 0;
    private int saltGoal = 0;
    private int saltConsumed = 0;
    private int sugarGoal = 0;
    private int sugarConsumed = 0;
    private int carbsGoal = 0;
    private int carbsConsumed = 0;
    private int calGoal, calConsumed;

    private int num = 1;

    public HealthScore(int calGoal, int calConsumed) {
        this.calGoal = calGoal;
        this.calConsumed = calConsumed;
    }

    public void setConsumed(int calConsumed, int fatConsumed, int fiberConsumed,
                            int sugarConsumed, int saltConsumed, int proteinConsumed, int carbsConsumed) {
        this.calConsumed = calConsumed;
        this.fiberConsumed = fiberConsumed;
        this.fatConsumed = fatConsumed;
        this.sugarConsumed = sugarConsumed;
        this.saltConsumed = saltConsumed;
        this.proteinConsumed = proteinConsumed;
        this.carbsConsumed = carbsConsumed;
    }

    public void setGoals(int calGoal, int fatGoal, int fiberGoal,
                         int sugarGoal, int saltGoal, int proteinGoal, int carbsGoal) {
        this.calGoal = calGoal;
        this.fiberGoal = fiberGoal;
        this.fatGoal = fatGoal;
        this.sugarGoal = sugarGoal;
        this.saltGoal = saltGoal;
        this.proteinGoal = proteinGoal;
        this.carbsGoal = carbsGoal;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setCalGoal(int calGoal) {
        this.calGoal = calGoal;
    }

    // Health Score is ranked 1 - 10 (where 10 is all goals met)
    public int calculateHealthScore() {
        float protein  = calculateGoalMet(proteinGoal, proteinConsumed);
        float fat = calculateGoalMet(fatGoal, fatConsumed);
        float fiber = calculateGoalMet(fiberGoal, fiberConsumed);
        float salt = calculateGoalMet(saltGoal, saltConsumed);
        float sugar = calculateGoalMet(sugarGoal, sugarConsumed);
        float carbs = calculateGoalMet(carbsGoal, carbsConsumed);
        float cal = calculateGoalMet(calGoal, calConsumed);

        float avg = ((protein + fat + fiber + salt + sugar + carbs + cal) / num);

        return Math.round(avg);
    }

    private float calculateGoalMet(int goal, int consumed) {
        if (goal == 0) {
            return 0.0f;
        }

        if (goal == 0 && consumed == 0) {
            return 1.0f;
        }

         float score_proportion = (float) consumed / (float) goal;

        if (score_proportion > 1) {
            score_proportion = Math.abs(score_proportion - 1.0f);
        }

        return score_proportion * 10.0f;
    }


}
