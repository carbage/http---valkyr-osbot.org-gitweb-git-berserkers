package org.valkyr.api.framework.antiban;

import org.osbot.rs07.api.def.NPCDefinition;
import org.osbot.rs07.api.def.ObjectDefinition;
import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.Model;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.utility.ConditionalSleep;
import org.valkyr.api.framework.script.LoopScript;

import java.awt.*;
import java.util.List;

/**
 * Utility class that allows for simple implementation of Anti-ban Compliance.
 * <p/>
 * Create a new ABC object in your script and reference it wherever you need to place anti-ban compliance methods.
 * <p/>
 * When any a.b.c. method is called, the seed is checked. If the next action to be performed should be performed, it will be. Otherwise the method will return.
 *
 * @author Starfox
 */

/**
 * Converted to OSB by Valkyr
 */
public class AntibanHelper {

    private final AntibanUtils abutil;
    private final ObjectUtil objutil;
    private final LoopScript script;
    private int resources_won;
    private int resources_lost;

    /**
     * Creates a new ABC. By default, sets the use of general a.b.c. to be true.
     */
    public AntibanHelper(LoopScript script) {
        this.abutil = new AntibanUtils(script);
        this.objutil = new ObjectUtil(script);
        this.resources_won = 0;
        this.resources_lost = 0;
        this.script = script;
    }

    /**
     * Gets the AntibanUtils object.
     *
     * @return The AntibanUtils object.
     */
    public final AntibanUtils getAntibanUtils() {
        return this.abutil;
    }

    /**
     * Gets the amount of resources won.
     *
     * @return The amount.
     */
    public final int getResourcesWon() {
        return this.resources_won;
    }

    /**
     * Sets the amount of resources won to the specified amount.
     *
     * @param amount The amount to set.
     */
    public final void setResourcesWon(int amount) {
        this.resources_won = amount;
    }

    /**
     * Gets the amount of resources lost.
     *
     * @return The amount.
     */
    public final int getResourcesLost() {
        return this.resources_lost;
    }

    /**
     * Sets the amount of resources lost to the specified amount.
     *
     * @param amount The amount to set.
     */
    public final void setResourcesLost(int amount) {
        this.resources_lost = amount;
    }

    /**
     * Checks to see whether or not the specified bool tracker is ready.
     *
     * @param tracker The tracker to check.
     * @return True if it is ready, false otherwise.
     */
    public final boolean isBoolTrackerReady(AntibanUtils.BOOL_TRACKER tracker) {
        tracker.reset();
        return tracker.next();
    }

    /**
     * Checks to see whether or not the specified switch tracker is ready.
     *
     * @param tracker     The tracker to check.
     * @param playerCount The player count.
     * @return True if it is ready, false otherwise.
     */
    public final boolean isSwitchTrackerReady(AntibanUtils.SWITCH_TRACKER tracker, int playerCount) {
        return tracker.next(playerCount);
    }

    /**
     * Gets the time (in milliseconds) until the next action will be performed for the specified delay tracker.
     *
     * @param tracker The tracker.
     * @return The time (in milliseconds) until the next action will be performed.
     */
    public final long getTimeUntilNext(AntibanUtils.DELAY_TRACKER tracker) {
        tracker.reset();
        return tracker.next() - System.currentTimeMillis();
    }

    /**
     * Gets the time (in milliseconds) until the next action will be performed for the specified int tracker.
     *
     * @param tracker The tracker.
     * @return The time (in milliseconds) until the next action will be performed.
     */
    public final long getTimeUntilNext(AntibanUtils.INT_TRACKER tracker) {
        tracker.reset();
        return tracker.next() - System.currentTimeMillis();
    }

    /**
     * Gets the time (in milliseconds) until the next action will be performed for the specified time tracker.
     *
     * @param tracker The tracker.
     * @return The time (in milliseconds) until the next action will be performed.
     */
    public final long getTimeUntilNext(AntibanUtils.TIME_TRACKER tracker) {
        tracker.reset();
        return tracker.next() - System.currentTimeMillis();
    }

    /**
     * Gets use closest boolean based off the int tracker.
     *
     * @return The next run energy.
     */
    public final boolean getUseClosest() {
        AntibanUtils.BOOL_TRACKER.USE_CLOSEST.reset();
        return AntibanUtils.BOOL_TRACKER.USE_CLOSEST.next();
    }

    /**
     * Gets the next run at based off the int tracker.
     *
     * @return The next run energy.
     */
    public final int getNextRun() {
        AntibanUtils.INT_TRACKER.NEXT_RUN_AT.reset();
        return AntibanUtils.INT_TRACKER.NEXT_RUN_AT.next();
    }

    /**
     * Gets the next eat percent based off the int tracker.
     *
     * @return The next eat percent.
     */
    public final int getNextHpPercent() {
        AntibanUtils.INT_TRACKER.NEXT_EAT_AT.reset();
        return AntibanUtils.INT_TRACKER.NEXT_EAT_AT.next();
    }

    /**
     * Randomly moves the camera. Happens only if the time tracker for camera movement is ready.
     */
    public final void moveCamera() {
        try {
            abutil.performRotateCamera();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks the xp of a random skill. Happens only if the time tracker for checking xp is ready.
     */
    public void checkXp() {
        try {
            abutil.performXPCheck();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Activates run. Happens only when run is off & your run energy is >=
     */
    public final void activateRun() {
        if (script.getSettings().getRunEnergy() >= getNextRun() && !script.getSettings().isRunning()) {
            if (script.getSettings().setRunning(true)) {
                AntibanUtils.INT_TRACKER.NEXT_RUN_AT.reset();
            }
        }
    }

    /**
     * Picks up the mouse. Happens only if the time tracker for picking up the mouse is ready.
     */
    public final void pickUpMouse() {
        try {
            abutil.performPickupMouse();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Leaves the game. Happens only if the time tracker for leaving the game is ready.
     */
    public void leaveGame() {
        try {
            abutil.performLeaveGame();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Examines a random object. Happens only if the time tracker for examining a random object is ready.
     */
    public final void examineObject() {
        try {
            abutil.performExamineObject();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Right clicks a random spot. Happens only if the time tracker for right clicking a random spot is ready.
     */
    public final void rightClick() {
        try {
            abutil.performRandomRightClick();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Randomly moves the mouse. Happens only if the time tracker for randomly moving the mouse is ready.
     */
    public final void mouseMovement() {
        try {
            abutil.performRandomMouseMovement();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Performs a combat tab check. Happens only if the time tracker for combat tab checking is ready.
     */
    public final void combatCheck() {
        try {
            abutil.performCombatCheck();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Performs an equipment tab check. Happens only if the time tracker for equipment tab checking is ready.
     */
    public final void equipmentCheck() {
        try {
            abutil.performEquipmentCheck();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Performs a friends tab check. Happens only if the time tracker for friends tab checking is ready.
     */
    public final void friendsCheck() {
        try {
            abutil.performFriendsCheck();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Performs a music tab check. Happens only if the time tracker for friends tab checking is ready.
     */
    public final void musicCheck() {
        try {
            abutil.performMusicCheck();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Performs a quest tab check. Happens only if the time tracking for friends tab checking is ready.
     */
    public final void questCheck() {
        try {
            abutil.performQuestsCheck();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sleeps for the switch object delay time.
     */
    public final void waitSwitchObjectDelay() {
        try {
            script.wait(AntibanUtils.DELAY_TRACKER.SWITCH_OBJECT.next());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AntibanUtils.DELAY_TRACKER.SWITCH_OBJECT.reset();
    }

    /**
     * Sleeps for the switch object combat delay time.
     */
    public final void waitSwitchObjectCombatDelay() {
        try {
            MethodProvider.sleep(AntibanUtils.DELAY_TRACKER.SWITCH_OBJECT_COMBAT.next());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AntibanUtils.DELAY_TRACKER.SWITCH_OBJECT_COMBAT.reset();
    }

    /**
     * Sleeps for the new object delay time.
     */
    public final void waitNewObjectDelay() {
        try {
            MethodProvider.sleep(AntibanUtils.DELAY_TRACKER.NEW_OBJECT.next());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AntibanUtils.DELAY_TRACKER.NEW_OBJECT.reset();
    }

    /**
     * Sleeps for the new object combat delay time.
     */
    public final void waitNewObjectCombatDelay() {
        try {
            MethodProvider.sleep(AntibanUtils.DELAY_TRACKER.NEW_OBJECT_COMBAT.next());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AntibanUtils.DELAY_TRACKER.NEW_OBJECT_COMBAT.reset();
    }

    /**
     * Sleeps for the interaction delay time.
     */
    public final void waitInteractionDelay() {
        try {
            MethodProvider.sleep(AntibanUtils.DELAY_TRACKER.ITEM_INTERACTION.next());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AntibanUtils.DELAY_TRACKER.ITEM_INTERACTION.reset();
    }

    /**
     * Executes the NEW_OBJECT/SWITCH_OBJECT delay, to be executed right before clicking objects/NPCs/players/tiles/ground items/minimap tiles/items. Not to be
     * used between item clicks; only if we have to wait for an item to popup in the inventory before clicking on it, and if we do not know the exact time at
     * which the item will appear in the inventory. In other words, this is the reaction delay when responding to a changing environment where we do not know
     * the exact time at which the environment will change to the way we want.
     *
     * @param last_busy_time The timestamp at which the player was last working/mining/woodcutting/fighting/fishing/crafting/etc. The timestamp beings when we
     *                       have to move on to the next resource.
     * @param combat         True if the player is in combat, or the script is one which the player is constantly performing actions, and requires the player to have
     *                       very fast actions (such as sorceress's garden).
     */
    public void waitNewOrSwitchDelay(final long last_busy_time, final boolean combat) {
        abutil.waitNewOrSwitchDelay(last_busy_time, combat);
    }

    /**
     * Hovers the next available object if applicable. YOU MUST RESET THE TRACKER YOURSELF AFTER THE CURRENT OBJECT IS GONE/DEPLETED.
     *
     * @param currentlyInteracting The object you are currently interacting with.
     * @param objectName           The name of the object you wish to hover.
     */
    public final void hoverNextObject(final RS2Object currentlyInteracting, final String objectName) {
        if (currentlyInteracting == null || objectName == null) {
            return;
        }
        final List<RS2Object> objects = script.objects.filter(new Filter<RS2Object>() {
            @Override
            public boolean match(RS2Object o) {
                if (o == null) {
                    return false;
                }
                final ObjectDefinition def = o.getDefinition();
                if (def != null) {
                    final String name = def.getName();
                    if (name != null) {
                        return name.equalsIgnoreCase(objectName) && !o.getPosition().equals(currentlyInteracting.getPosition()) && o.isVisible();
                    }
                }
                return false;
            }
        });
        if (objects.size() <= 0) {
            return;
        }
        if (AntibanUtils.BOOL_TRACKER.HOVER_NEXT.next()) {
            final RS2Object next = objects.get(0);
            if (next != null) {
                if (!objutil.isHovering(next) && next.hover()) {
                    new ConditionalSleep(100) {

                        @Override
                        public boolean condition() {
                            return objutil.isHovering(next);
                        }
                    };
                }
            }
        }
    }

    /**
     * Gets an object near you with the specified id. This object will either be the nearest, or second nearest depending on the tracker.
     *
     * @param id       The id of the object you are looking to find.
     * @param distance The distance range.
     * @return An object with the specified id near you. Null if no objects were found.
     */
    public final RS2Object getUseClosestObject(final int id, final int distance) {
        final List<RS2Object> objects = script.objects.filter(new Filter<RS2Object>() {

            @Override
            public boolean match(RS2Object rs2Object) {
                return script.myPosition().distance(rs2Object) <= distance && rs2Object.getId() == id;
            }
        });
        if (objects.size() < 1) {
            return null;
        }
        RS2Object object_to_click = objects.get(0);
        if (objects.size() > 1 && AntibanUtils.BOOL_TRACKER.USE_CLOSEST.next()) {
            if (objects.get(1).getPosition().distance(objects.get(0)) < 3.0) {
                object_to_click = objects.get(1);
            }
        }
        AntibanUtils.BOOL_TRACKER.USE_CLOSEST.reset();
        return object_to_click;
    }

    /**
     * Gets an object near you with the specified name. This object will either be the nearest, or second nearest depending on the tracker.
     *
     * @param name     The name of the object you are looking to find.
     * @param distance The distance range.
     * @return An object with the specified name near you. Null if no objects were found.
     */
    public final RS2Object getUseClosestObject(final String name, final int distance) {
        final List<RS2Object> objects = script.objects.filter(new Filter<RS2Object>() {

            @Override
            public boolean match(RS2Object rs2Object) {
                return script.myPosition().distance(rs2Object) <= distance && rs2Object.getName() == name;
            }
        });
        if (objects.size() < 1) {
            return null;
        }
        RS2Object object_to_click = objects.get(0);
        if (objects.size() > 1 && AntibanUtils.BOOL_TRACKER.USE_CLOSEST.next()) {
            if (objects.get(1).getPosition().distance(objects.get(0)) < 3.0) {
                object_to_click = objects.get(1);
            }
        }
        AntibanUtils.BOOL_TRACKER.USE_CLOSEST.reset();
        return object_to_click;
    }

    /**
     * Gets an object near you with the specified id. This object will either be the nearest, or second nearest depending on the tracker. A distance range of
     * 100 will be used as the default.
     *
     * @param id The id of the object you are looking to find.
     * @return An object with the specified id near you. Null if no objects were found.
     */
    public final RS2Object getUseClosestObject(final int id) {
        return getUseClosestObject(id, 100);
    }

    /**
     * Gets an object near you with the specified name. This object will either be the nearest, or second nearest depending on the tracker. A distance range of
     * 100 will be used as the default.
     *
     * @param name The name of the object you are looking to find.
     * @return An object with the specified name near you. Null if no objects were found.
     */
    public final RS2Object getUseClosestObject(final String name) {
        return getUseClosestObject(name, 100);
    }

    /**
     * Hovers the next available object if applicable. YOU MUST RESET THE TRACKER YOURSELF AFTER THE CURRENT OBJECT IS GONE/DEPLETED.
     *
     * @param currentlyInteracting The object you are currently interacting with.
     * @param objectName           The name of the object you wish to hover.
     */
    public final void hoverNextNPC(final NPC currentlyInteracting, final String objectName) {
        if (currentlyInteracting == null || objectName == null) {
            return;
        }
        final List<NPC> npcs = script.npcs.filter(new Filter<NPC>() {
            @Override
            public boolean match(NPC o) {
                if (o == null) {
                    return false;
                }
                final NPCDefinition def = o.getDefinition();
                if (def != null) {
                    final String name = def.getName();
                    if (name != null) {
                        return name.equalsIgnoreCase(objectName) && !o.getPosition().equals(currentlyInteracting.getPosition()) && o.isVisible();
                    }
                }
                return false;
            }
        });
        if (npcs.size() <= 0) {
            return;
        }
        if (AntibanUtils.BOOL_TRACKER.HOVER_NEXT.next()) {
            final NPC next = npcs.get(0);
            if (next != null) {
                next.hover();
            }
        }
    }

    /**
     * Gets an NPC near you with the specified id. This object will either be the nearest, or second nearest depending on the tracker.
     *
     * @param id       The id of the object you are looking to find.
     * @param distance The distance range.
     * @return An object with the specified id near you. Null if no objects were found.
     */
    public final NPC getUseClosestNPC(final int id, final int distance) {
        final List<NPC> npcs = script.npcs.filter(new Filter<NPC>() {

            @Override
            public boolean match(NPC rs2Object) {
                return script.myPosition().distance(rs2Object) <= distance && rs2Object.getId() == id;
            }
        });
        if (npcs.size() < 1) {
            return null;
        }
        NPC object_to_click = npcs.get(0);
        if (npcs.size() > 1 && AntibanUtils.BOOL_TRACKER.USE_CLOSEST.next()) {
            if (npcs.get(1).getPosition().distance(npcs.get(0)) < 3.0) {
                object_to_click = npcs.get(1);
            }
        }
        AntibanUtils.BOOL_TRACKER.USE_CLOSEST.reset();
        return object_to_click;
    }

    /**
     * Gets an object near you with the specified name. This object will either be the nearest, or second nearest depending on the tracker.
     *
     * @param names    The names of the objects you are looking to find.
     * @param distance The distance range.
     * @return An object with the specified name near you. Null if no objects were found.
     */
    public final NPC getUseClosestNPC(final String[] names, final int distance) {
        final List<NPC> npcs = script.npcs.filter(new Filter<NPC>() {

            @Override
            public boolean match(NPC rs2Object) {
                for (String s : names)
                    if (rs2Object.getName().contains(s))
                        return script.myPosition().distance(rs2Object) <= distance;
                return false;
            }
        });
        if (npcs.size() < 1) {
            return null;
        }
        NPC object_to_click = npcs.get(0);
        if (npcs.size() > 1 && AntibanUtils.BOOL_TRACKER.USE_CLOSEST.next()) {
            if (npcs.get(1).getPosition().distance(npcs.get(0)) < 3.0) {
                object_to_click = npcs.get(1);
            }
        }
        AntibanUtils.BOOL_TRACKER.USE_CLOSEST.reset();
        return object_to_click;
    }

    /**
     * Gets an object near you with the specified id. This object will either be the nearest, or second nearest depending on the tracker. A distance range of
     * 100 will be used as the default.
     *
     * @param id The id of the object you are looking to find.
     * @return An object with the specified id near you. Null if no objects were found.
     */
    public final NPC getUseClosestNPC(final int id) {
        return getUseClosestNPC(id, 100);
    }

    /**
     * Gets an object near you with the specified name. This object will either be the nearest, or second nearest depending on the tracker. A distance range of
     * 100 will be used as the default.
     *
     * @param names The names of the objects you are looking to find.
     * @return An object with the specified name near you. Null if no objects were found.
     */
    public final NPC getUseClosestNPC(final String... names) {
        return getUseClosestNPC(names, 100);
    }

    /**
     * Goes to the next anticipated resource. You must calculate which resource is the next anticipated resource yourself. Note that you may use any
     * Positionable object (e.g. rsnpc, rsplayer, rstile, rsobject etc.) as your next anticipated resource.
     *
     * @param anticipated   The next anticipated resource.
     * @param random_offset The distance away from the object that can be randomized.
     * @return true if the player moved to the resource; false otherwise.
     */
    public final boolean goToAnticipated(Entity anticipated, int random_offset) {
        if (anticipated != null && AntibanUtils.BOOL_TRACKER.GO_TO_ANTICIPATED.next()) {
            if (script.getLocalWalker().walk(anticipated)) {
                AntibanUtils.BOOL_TRACKER.GO_TO_ANTICIPATED.reset();
                return true;
            }
        }
        AntibanUtils.BOOL_TRACKER.GO_TO_ANTICIPATED.reset();
        return false;
    }

    /**
     * Checks to see if the player should switch resources. YOU MUST RESET THE TRACKER YOURSELF AFTER SWITCHING RESOURCES SUCCESSFULLY.
     *
     * @param player_count    The amount of players gathering resources.
     * @param last_time_moved The last time you moved to a new resource.
     * @return true if your player should switch; false otherwise.
     */
    public final boolean shouldSwitchResources(int player_count, long last_time_moved) {
        double win_percent = ((double) (resources_won + resources_lost) / (double) resources_won);
        return win_percent < 50.0 && AntibanUtils.SWITCH_TRACKER.TOO_MANY_PLAYERS.next(player_count);
    }

    /**
     * Checks all of the actions that should be done when idle. If any are ready, it will perform them.
     *
     * @param skill The skill to check the xp of. Null if no xp is being gained.
     * @return true if any actions were performed; false otherwise.
     */
    public final boolean timedActions(Skill skill) {
        return abutil.performTimedActions(skill);
    }

    /**
     * @author Starfox
     */
    private class ObjectUtil {

        private LoopScript script;

        public ObjectUtil(LoopScript script) {
            this.script = script;
        }

        private Polygon getObjectArea(final RS2Object object) {
            if (object == null) {
                return null;
            }
            final Model model = object.getModel();
            if (model != null) {
                return new Polygon(model.getVertexXIndices(), model.getVertexYIndices(), model.getVerticesCount());
            }
            return null;
        }

        public boolean isHovering(final RS2Object object) {
            final Polygon area = getObjectArea(object);
            return area != null && area.contains(script.getMouse().getPosition());
        }
    }
}
