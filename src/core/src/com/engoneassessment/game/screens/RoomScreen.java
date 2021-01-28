package com.engoneassessment.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.engoneassessment.game.GameEntry;
import com.engoneassessment.game.actors.buildings.Teleporter;
import com.engoneassessment.game.actors.characters.Player;
import com.engoneassessment.game.actors.characters.npcs.Hostile;
import com.engoneassessment.game.actors.characters.npcs.NonHostile;
import com.engoneassessment.game.actors.rooms.Room;
import com.engoneassessment.game.ui.hud.HUDStage;

public class RoomScreen implements Screen {
    private final GameEntry gameEntry;
    protected String name;

    private Room floor;
    private Room walls;
    private Teleporter teleporter;
    public Stage stage;
    public Player auber;
    final int minX;
    final int minY;
    final int maxX;
    final int maxY;
    public Array<NonHostile> nonHostiles;
    public Array<Hostile> hostiles;
    public HUDStage hudStage;
    private boolean sabotaged;

    //since we have two stages - the stage and the UIStage, we need multiple InputProcessor to handle the listener events.
    InputMultiplexer multiplexer;

    /**
     *
     * @param gameEntry the main gameEntry class that stores a lot of the information
     * @param name The name of the room
     * @param numNPCs The number of NPCs to spawn in the room
     */
    public RoomScreen(GameEntry gameEntry, String name, int numNPCs){
        this.gameEntry = gameEntry;
        //Sets the boundaries of the room
        minX = 424;
        minY = 14;
        maxX = 1451;
        maxY = 795;

        sabotaged = false;

        //Sets the name of the room for use in the UI
        this.name = name;


        //Initialises nonHostiles and hostiles Arrays
        nonHostiles = new Array<>();
        hostiles = new Array<>();

        stage = new Stage(new StretchViewport(GameEntry.VIEW_WIDTH, GameEntry.VIEW_HEIGHT));

        //Creates the walls and floors of the rooms
        floor = new Room(new TextureRegion(new Texture("Rooms/General Square/Floor.png")));
        walls = new Room(new TextureRegion(new Texture("Rooms/General Square/Wall.png")));
        teleporter = new Teleporter();

        stage.addActor(floor);
        stage.addActor(walls);
        stage.addActor(teleporter);

        teleporter.setScale(2f,2f);
        teleporter.setPosition(floor.getWidth()/2-teleporter.getWidth()/2,floor.getHeight()/2-teleporter.getHeight()/2);

        //Creates the non hostiles in the rooms and gives them a random starting position
        for (int i = 1;i <= numNPCs; i ++){
            nonHostiles.add(new NonHostile(new TextureRegion(new Texture("Characters/Other/idle/idle.gif")),this));
            nonHostiles.get(nonHostiles.size-1).setPosition(GameEntry.getRandom().nextInt(this.getMaxX()-this.getMinX())+this.getMinX(), GameEntry.getRandom().nextInt(this.getMaxY()-this.getMinY())+this.getMinY());
            stage.addActor(nonHostiles.get(nonHostiles.size-1));
        }

        hudStage = gameEntry.hudStage;


        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(hudStage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void show() {
        //Gets the auber from game entry and sets it to the centre of the room
        auber = gameEntry.getAuber();
        stage.addActor(auber);
        auber.setPosition(stage.getWidth()/2-auber.getWidth()/2,stage.getHeight()/2-auber.getHeight()/2);
        auber.setCurrentScreen(this);

        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(hudStage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    /**
     *
     * @param delta the time in seconds since the screen was last rendered
     */
    @Override
    public void render(float delta) {
        //Checks if the player is on top of the teleporter
        if(this == gameEntry.getCurrentRoomScreen()){
            gameEntry.hudStage.isTeleportEnabled(this.auber.getBounds().overlaps(teleporter.getBounds()));
        }
        //Checks for movement keys being held
        movement();
        //Runs a function to spawn hostiles randomly in different rooms
        sabotage();
        //Moves the hostiles and non hostiles
        moveNPCS();
        //Uses the hostile abilities
        useAbilities();
        //Checks if the player is ontop of the teleporter
        teleporter.checkOverlap(auber,hudStage);
        //Runs any passive effects for the room like healing or taking damage
        passiveEffects(delta);
        //Sets the camera position to the centre of the player
        stage.getViewport().getCamera().position.set(auber.getX()+auber.getWidth()/2,auber.getY()+auber.getHeight()/2,0);
        stage.getViewport().getCamera().update();
        //Clears the screen
        Gdx.gl.glClearColor(0, 0, 0, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Acts and draws the stage
        stage.act();
        stage.draw();
        hudStage.act();
        hudStage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        //gameEntry.setAuber(auber);
    }

    @Override
    public void dispose() {

    }

    public void movement(){
        if(gameEntry.isDemo()){
            auber.autoMove(gameEntry.getRandom(),gameEntry);
        }
        else {
            //Moves the auber around
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                auber.moveBy(0, auber.getSpeed() * Gdx.graphics.getDeltaTime());
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                auber.moveBy(-auber.getSpeed() * Gdx.graphics.getDeltaTime(), 0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                auber.moveBy(0, -auber.getSpeed() * Gdx.graphics.getDeltaTime());
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                auber.moveBy(auber.getSpeed() * Gdx.graphics.getDeltaTime(), 0);
            }
        }

        //If the auber is out of bounds, move him back in
        if(auber.getY() > maxY){
            auber.setY(maxY);
        }
        if(auber.getY() < minY){
            auber.setY(minY);
        }
        if(auber.getX() < minX){
            auber.setX(minX);
        }
        if(auber.getX() > maxX){
            auber.setX(maxX);
        }
    }

    public void moveNPCS(){
        //Goes through every non hostile and hostile and moves them
        for(NonHostile nonHostile:nonHostiles){
            nonHostile.randomMove();
        }

        for(Hostile hostile:hostiles) {
            hostile.randomMove();
        }
    }

    public void sabotage(){
        //If the time since the last sabotage is more than 15 seconds sabotages a system
        if(System.currentTimeMillis() - gameEntry.getSpawnTime() > 10000) {
            gameEntry.sabotage();
            gameEntry.setSpawnTime(System.currentTimeMillis());
        }
    }

    public void useAbilities(){
        for(Hostile hostile:hostiles) {
            //If the ability is on cooldown decrease the cooldown by one, otherwise use the ability
            if(hostile.getAbilityCooldown()!=0){
                hostile.setAbilityCooldown(hostile.getAbilityCooldown()-1);
            }

            else {
                hostile.useAbility(auber, GameEntry.getRandom());
            }

            if(hostile.getAbilityTimer()!=0){
                hostile.setAbilityTimer(hostile.getAbilityTimer()-1);
            }

            else if(hostile.getAbilityActivated()){
                hostile.deactivateAbility();
            }

        }
    }

    public int getMinX() {
        return minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public String getName(){
        return name;
    }

    /**
     *
     * @param floor new floor texture
     */
    public void  setFloorTexture(TextureRegion floor) {
        this.floor.setTextureRegion(floor);
    }

    /**
     *
     * @param walls new wall texture
     */
    public void setWallsTexture(TextureRegion walls) {
        this.walls.setTextureRegion(walls);
    }

    /**
     *
     * @param delta time in seconds since last call
     */
    public void passiveEffects(float delta){
        if (gameEntry.getOxygenScreen().isSabotaged()) {
            auber.setHealth(auber.getHealth()-delta*2);
            gameEntry.hudStage.updateHealth();
        }
    }

    public void setSabotaged(boolean sabotaged) {
        this.sabotaged = sabotaged;
    }

    public boolean isSabotaged() {
        return sabotaged;
    }

    public Teleporter getTeleporter() {
        return teleporter;
    }

    public Room getFloor() {
        return floor;
    }

    public Room getWalls() {
        return walls;
    }

}
