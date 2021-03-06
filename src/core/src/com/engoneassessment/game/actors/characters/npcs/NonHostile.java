package com.engoneassessment.game.actors.characters.npcs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.engoneassessment.game.screens.RoomScreen;

public class NonHostile extends NPC {
    public NonHostile(TextureRegion textureRegion, RoomScreen screen) {
        super(textureRegion, screen, new Texture("Characters/Other/run/run.png"),
                new Texture("Characters/Other/idle/idle.png"));
    }
}
