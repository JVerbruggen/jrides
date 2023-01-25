package com.jverbruggen.jrides.models.properties;

public class FrameRange {
    private final Frame lower;
    private final Frame upper;
    private final int cycle;

    public FrameRange(Frame lower, Frame upper, int cycle) {
        this.lower = lower;
        this.upper = upper;
        this.cycle = cycle;
    }

    public Frame getInBetween(double percentage){
        int lowerValue = lower.getValue();
        int upperValue = upper.getValue();

        int frameIndex;
        if(lowerValue < upperValue){
            int delta = upperValue-lowerValue;
            frameIndex = (int) (lowerValue+(delta*percentage));
        }
        else {
            int lowerValuePart = cycle - lowerValue;
            int upperValuePart = upperValue;
            int totalParts = lowerValuePart + upperValuePart;
            double distribution = (double)lowerValuePart / (double)totalParts;
            if(percentage > distribution){
                //                         = (0.8        - 0.6         ) / 0.4 = 0.5 (50%)
                double upperPartPercentage = (percentage - distribution) / upperValuePart;
                frameIndex = (int) (upperValue*upperPartPercentage);
            }else{
                //                         = 0.4 / 0.6 = 0.66 (66%)
                double lowerPartPercentage = percentage / distribution;
                frameIndex = (int) (lowerValue + (lowerValuePart * lowerPartPercentage));
            }
        }

        return new SimpleFrame(frameIndex);
    }
}
