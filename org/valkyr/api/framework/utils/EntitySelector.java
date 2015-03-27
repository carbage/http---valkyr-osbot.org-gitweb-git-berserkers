package org.valkyr.api.framework.utils;

import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.InteractableObject;
import org.osbot.rs07.input.mouse.EntityDestination;
import org.valkyr.api.framework.script.LoopScript;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class EntitySelector {

    private ArrayList<Entity> entities = new ArrayList<>();

    public EntitySelector(final LoopScript script) {
        script.getBot().getCanvas().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent evt) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        for (Entity e : script.getAllEntities()) {
                            if (e instanceof InteractableObject)
                                if (new EntityDestination(script.getBot(), e).getBoundingBox().contains(evt.getPoint())) {
                                    if (!entities.contains(e))
                                        entities.add(e);
                                    else entities.remove(e);
                                }
                        }

                    }
                });
            }
        });
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

}
