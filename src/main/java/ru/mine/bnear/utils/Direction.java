package ru.mine.bnear.utils;

import ru.mine.bnear.BNear;

import java.util.ArrayList;
import java.util.Date;

public class Direction {

    private BNear main = (BNear)BNear.getPlugin(BNear.class);

    public Direction(BNear main) {
        this.main = main;
    }

    private String straight = "⬆";
    private String left = "⬅";
    private String right = "➡";
    private String back = "⬇";
    private String straightLeft = "⬉";
    private String straightRight = "⬈";
    private String backLeft = "⬋";
    private String backRight = "⬊";

    public String getdirection(int yaw) {


        switch (yaw) {
            case (0):
                return straight;
            case(1):
                return straightLeft;
            case(2):
                return left;
            case(3):
                return backLeft;
            case(4):
                return back;
            case(5):
                return backRight;
            case(-1):
                return right;
            case(-2):
                return backRight;
            case(-3):
                return backRight;
            case(-4):
                return back;
            case(-5):
                return left;
            case(6):
                return straightRight;
            case(7):
                return straight;
            case(-6):
                return straightLeft;
            case(-7):
                return straight;
            case(-8):
                return straight;
            case(-9):
                return straightRight;
            case(-10):
                return backRight;
            case(-11):
                return backRight;
            case(-12):
                return back;
            case(-13):
                return backLeft;
            case(-14):
                return straightLeft;
            case(-15):
                return straightLeft;

            default:
                ArrayList<String> exception = (ArrayList<String>) main.getConfig().getList("Exception");
                Date data = new Date();
                exception.add(data + " yaw:" + yaw + "");
                main.getConfig().set("Exception", exception);
                main.saveConfig();
                return new String("§cÎøèáêà! Ñîîáùèòå àäìèíèñòðàöèè\n");
        }
    }
}
