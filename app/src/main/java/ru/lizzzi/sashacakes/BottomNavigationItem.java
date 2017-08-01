package ru.lizzzi.sashacakes;

public class BottomNavigationItem {

    private int label;

    private int defaultIcon;

    private int afterSelectedIcon;
    private int color;

    private int badgeNumber = -1;

    public BottomNavigationItem(int label, int defaultIcon, int afterSelectedIcon) {
        this(label, defaultIcon, afterSelectedIcon, 0);
    }

    public BottomNavigationItem(int label, int defaultIcon, int afterSelectedIcon, int color) {
        this.label = label;
        this.defaultIcon = defaultIcon;
        this.afterSelectedIcon = afterSelectedIcon;
        this.color = color;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public int getDefaultIcon() {
        return defaultIcon;
    }

    public void setDefaultIcon(int defaultIcon) {
        this.defaultIcon = defaultIcon;
    }

    public int getAfterSelectedIcon() {
        return afterSelectedIcon;
    }

    public void setAfterSelectedIcon(int afterSelectedIcon) {
        this.afterSelectedIcon = afterSelectedIcon;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getBadgeNumber() {
        return this.badgeNumber;
    }

    public void setBadgeNumber(int badgeNumber) {
        this.badgeNumber = badgeNumber;
    }

}
