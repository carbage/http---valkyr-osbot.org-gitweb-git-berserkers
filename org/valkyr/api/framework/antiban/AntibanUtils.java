package org.valkyr.api.framework.antiban;

import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.RS2Interface;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.script.Script;
import org.valkyr.api.framework.script.LoopScript;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Antiban Utils class based on TRiBot's ABCUtils class
 *
 * @author Valkyr
 */

public class AntibanUtils {

    public static int INTERVAL = 1000;
    public static SecureRandom rng = new SecureRandom(String.valueOf(System.currentTimeMillis()).getBytes());
    private Script script;

    public AntibanUtils(LoopScript script) {
        this.script = script;
    }

    public static SecureRandom getRng() {
        rng.setSeed(System.currentTimeMillis());
        return rng;
    }

    public void performRotateCamera() throws InterruptedException {
        if (TIME_TRACKER.ROTATE_CAMERA.next() <= System.currentTimeMillis()) {
            script.getCamera().movePitch(getRng().nextInt(100));
            script.getCamera().moveYaw(getRng().nextInt(100));
            MethodProvider.sleep(getRng().nextInt(INTERVAL));
            TIME_TRACKER.ROTATE_CAMERA.reset();
        }
    }

    public void performPickupMouse() throws InterruptedException {
        if (TIME_TRACKER.PICKUP_MOUSE.next() <= System.currentTimeMillis()) {
            script.mouse.moveSlightly();
            MethodProvider.sleep(getRng().nextInt(INTERVAL));
            TIME_TRACKER.CHECK_XP.reset();

        }
    }

    public void performLeaveGame() throws InterruptedException {
        if (TIME_TRACKER.LEAVE_GAME.next() <= System.currentTimeMillis()) {
            script.mouse.moveOutsideScreen();
            MethodProvider.sleep(getRng().nextInt(INTERVAL));
            TIME_TRACKER.LEAVE_GAME.reset();

        }
    }

    public void performExamineObject() throws InterruptedException {
        if (TIME_TRACKER.EXAMINE_OBJECT.next() <= System.currentTimeMillis()) {
            RS2Object obj = script.objects.getAll().get(getRng().nextInt(script.getObjects().getAll().size()));
            if (obj != null && script.myPosition().distance(obj) < 10 && obj.isVisible())
                obj.interact("Examine");
            MethodProvider.sleep(getRng().nextInt(INTERVAL));
            TIME_TRACKER.EXAMINE_OBJECT.reset();
        }
    }

    public void performRandomRightClick() throws InterruptedException {
        if (TIME_TRACKER.RANDOM_RIGHT_CLICK.next() <= System.currentTimeMillis()) {
            RS2Object obj = script.objects.getAll().get(getRng().nextInt(script.getObjects().getAll().size()));
            if (obj != null && script.myPosition().distance(obj) < 10 && obj.isVisible())
                obj.hover();
            script.mouse.click(true);
            MethodProvider.sleep(getRng().nextInt(INTERVAL));
            script.mouse.moveRandomly();
            MethodProvider.sleep(getRng().nextInt(INTERVAL));
            TIME_TRACKER.RANDOM_RIGHT_CLICK.reset();
        }
    }

    public void performRandomMouseMovement() throws InterruptedException {
        if (TIME_TRACKER.RANDOM_MOUSE_MOVEMENT.next() <= System.currentTimeMillis()) {
            script.mouse.moveRandomly();
            MethodProvider.sleep(getRng().nextInt(INTERVAL));
            TIME_TRACKER.RANDOM_MOUSE_MOVEMENT.reset();

        }
    }

    public void performCombatCheck() throws InterruptedException {
        if (TIME_TRACKER.CHECK_COMBAT.next() <= System.currentTimeMillis()) {
            tabHoverRandom(Tab.ATTACK);
            MethodProvider.sleep(getRng().nextInt(INTERVAL));
            TIME_TRACKER.CHECK_COMBAT.reset();
        }
    }

    public void performXPCheck() throws InterruptedException {
        if (TIME_TRACKER.CHECK_XP.next() <= System.currentTimeMillis()) {
            tabHoverRandom(Tab.SKILLS);
            MethodProvider.sleep(getRng().nextInt(INTERVAL));
            TIME_TRACKER.CHECK_XP.reset();
        }
    }

    public void performEquipmentCheck() throws InterruptedException {
        if (TIME_TRACKER.CHECK_EQUIPMENT.next() <= System.currentTimeMillis()) {
            tabHoverRandom(Tab.EQUIPMENT);
            MethodProvider.sleep(getRng().nextInt(INTERVAL));
            TIME_TRACKER.CHECK_EQUIPMENT.reset();
        }
    }

    public void performFriendsCheck() throws InterruptedException {
        if (TIME_TRACKER.CHECK_FRIENDS.next() <= System.currentTimeMillis()) {
            tabHoverRandom(Tab.FRIENDS);
            MethodProvider.sleep(getRng().nextInt(INTERVAL));
            TIME_TRACKER.CHECK_EQUIPMENT.reset();
        }
    }

    public void performMusicCheck() throws InterruptedException {
        if (TIME_TRACKER.CHECK_MUSIC.next() <= System.currentTimeMillis()) {
            tabHoverRandom(Tab.MUSIC);
            MethodProvider.sleep(getRng().nextInt(INTERVAL));
            TIME_TRACKER.CHECK_MUSIC.reset();
        }
    }

    public void performQuestsCheck() throws InterruptedException {
        if (TIME_TRACKER.CHECK_QUESTS.next() <= System.currentTimeMillis()) {
            tabHoverRandom(Tab.QUEST);
            MethodProvider.sleep(getRng().nextInt(INTERVAL));
            TIME_TRACKER.CHECK_QUESTS.reset();
        }
    }

    public void waitNewOrSwitchDelay(long last_busy_time, boolean combat) {
        if (!combat) {
            try {
                MethodProvider.sleep(getRng().nextInt(INTERVAL));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void tabHoverRandom(Tab tab) throws InterruptedException {
        Tab lastTab = script.getTabs().getOpen();
        if (lastTab != tab)
            script.getTabs().open(tab);
        MethodProvider.sleep(getRng().nextInt(INTERVAL));
        RS2Interface i = script.interfaces.get(script.getTabs().getOpen().getInterfaceParentId());
        i.getChildren()[getRng().nextInt(i.getChildren().length)].hover();
        MethodProvider.sleep(getRng().nextInt(INTERVAL));
        script.getTabs().open(lastTab);
    }

    public boolean performTimedActions(Skill skill) {
        return false;
    }

    public static class SWITCH_TRACKER {
        public static final SWITCH_TRACKER TOO_MANY_PLAYERS = new SWITCH_TRACKER();

        public boolean next(int i) {
            return i <= 5;
        }

    }

    public static class TIME_TRACKER {
        public static final TIME_TRACKER CHECK_XP = new TIME_TRACKER();
        public static final TIME_TRACKER EXAMINE_OBJECT = new TIME_TRACKER();
        public static final TIME_TRACKER ROTATE_CAMERA = new TIME_TRACKER();
        public static final TIME_TRACKER PICKUP_MOUSE = new TIME_TRACKER();
        public static final TIME_TRACKER RANDOM_RIGHT_CLICK = new TIME_TRACKER();
        public static final TIME_TRACKER LEAVE_GAME = new TIME_TRACKER();
        public static final TIME_TRACKER RANDOM_MOUSE_MOVEMENT = new TIME_TRACKER();
        public static final TIME_TRACKER CHECK_QUESTS = new TIME_TRACKER();
        public static final TIME_TRACKER CHECK_FRIENDS = new TIME_TRACKER();
        public static final TIME_TRACKER CHECK_EQUIPMENT = new TIME_TRACKER();
        public static final TIME_TRACKER CHECK_MUSIC = new TIME_TRACKER();
        public static final TIME_TRACKER CHECK_COMBAT = new TIME_TRACKER();
        private static long delay = 0;

        public long next() {
            return delay;
        }

        public void reset() {
            delay = getRng().nextInt(INTERVAL);
        }

    }

    public static class BOOL_TRACKER {
        public static final BOOL_TRACKER GO_TO_ANTICIPATED = new BOOL_TRACKER();
        public static final BOOL_TRACKER HOVER_NEXT = new BOOL_TRACKER();
        public static final BOOL_TRACKER USE_CLOSEST = new BOOL_TRACKER();
        public static final BOOL_TRACKER USE_MINIMAP = new BOOL_TRACKER();
        private static boolean bool = false;

        public boolean next() {
            return bool;
        }

        public void reset() {
            bool = getRng().nextBoolean();
        }

    }

    public static class DELAY_TRACKER {
        public static final DELAY_TRACKER ITEM_INTERACTION = new DELAY_TRACKER();
        public static final DELAY_TRACKER NEW_OBJECT = new DELAY_TRACKER();
        public static final DELAY_TRACKER NEW_OBJECT_COMBAT = new DELAY_TRACKER();
        public static final DELAY_TRACKER SWITCH_OBJECT = new DELAY_TRACKER();
        public static final DELAY_TRACKER SWITCH_OBJECT_COMBAT = new DELAY_TRACKER();
        private static long delay = 0;

        public long next() {
            return delay;
        }

        public void reset() {
            delay = getRng().nextInt(INTERVAL);
        }

    }

    public static class INT_TRACKER {
        public static final INT_TRACKER NEXT_EAT_AT = new INT_TRACKER();
        public static final INT_TRACKER NEXT_RUN_AT = new INT_TRACKER();
        public static final INT_TRACKER WALK_USING_SCREEN = new INT_TRACKER();
        private static final int FIFTY = 50;
        private static final int TWENTY_FIVE = 25;
        private static Random rn = new Random(System.currentTimeMillis());
        private static int next = rn.nextInt(TWENTY_FIVE) + TWENTY_FIVE;

        public int next() {
            return next;
        }

        public void reset() {
            next = getRng().nextInt(TWENTY_FIVE) + TWENTY_FIVE;
        }

    }
}
