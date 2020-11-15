package com.engoneassessment.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.engoneassessment.game.actors.characters.Player;
import com.engoneassessment.game.screens.RoomScreen;
import com.engoneassessment.game.screens.main.GameScreen;
import com.engoneassessment.game.screens.rooms.*;
import com.engoneassessment.game.screens.setting.SettingScreen;
import com.engoneassessment.game.screens.start.StartScreen;
import com.engoneassessment.game.ui.hud.HUDStage;

import java.util.Random;

/**
 * Since it may not only be a single screen/stage game,
 * then we should extend the whole game based on Game class,
 * instead of extending from ApplicationAdaptor interface.
 * This allows it to switch the screens/stages,
 * and control their rendering process from this top class.
 *
 * - Zhikang Liu 2020/11/01
 */

public class GameEntry extends Game {

    public static final float VIEW_WIDTH = 1920;
    public static final float VIEW_HEIGHT = 1080;

    private Player auber;
    private Screen CurrentScreen;
    public HUDStage hudStage;

    private InputListener inputHandler;

    //Screens Objects
    private StartScreen startScreen;
    private SettingScreen settingScreen;
    private GameScreen gameScreen;


    //All the room screens
    private CargoScreen cargoScreen;
    private CommandScreen commandScreen;
    private ElectricalScreen electricalScreen;
    private EngineScreen engineScreen;
    private HangerScreen hangerScreen;
    private InfirmaryScreen infirmaryScreen;
    private OxygenScreen oxygenScreen;
    private QuartersScreen quartersScreen;
    private WeaponsScreen weaponsScreen;
    private BrigScreen brigScreen;

    private static Random random;

    /**
     * Called when the game is first created.
     */
    @Override
    public void create() {
        //Used for generating random numbers
        random = new Random();
        //Creates the input handler for keyboard based events
        inputHandler = new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if(keycode == Input.Keys.NUM_1) {
                    setScreen(cargoScreen);
                }

                if(keycode == Input.Keys.NUM_2) {
                    setScreen(commandScreen);
                }

                if(keycode == Input.Keys.NUM_3) {
                    setScreen(electricalScreen);
                }

                if(keycode == Input.Keys.NUM_4) {
                    setScreen(engineScreen);
                }

                if(keycode == Input.Keys.NUM_5) {
                    setScreen(hangerScreen);
                }

                if(keycode == Input.Keys.NUM_6) {
                    setScreen(infirmaryScreen);
                }

                if(keycode == Input.Keys.NUM_7) {
                    setScreen(oxygenScreen);
                }

                if(keycode == Input.Keys.NUM_8) {
                    setScreen(quartersScreen);
                }

                if(keycode == Input.Keys.NUM_9) {
                    setScreen(weaponsScreen);
                }

                if(keycode == Input.Keys.NUM_0) {
                    setScreen(brigScreen);
                }

                return super.keyDown(event, keycode);
            }
        };

        //Creates the initial auber
        auber = new Player(new TextureRegion(new Texture("Characters/auber/idle/idle.gif")));
        hudStage = new HUDStage(new StretchViewport(this.VIEW_WIDTH, this.VIEW_HEIGHT),auber);
        // Create StartScreen
        startScreen = new StartScreen(this);

        // Create MainGameScreen
        gameScreen = new GameScreen(this);

        //Create Cargo Bay Screen
        cargoScreen = new CargoScreen(this,"Cargo");

        //Create Command Screen
        commandScreen = new CommandScreen(this,"Command");

        //Create Electrical Screen
        electricalScreen = new ElectricalScreen(this,"Electrical");

        //Create Engine Screen
        engineScreen = new EngineScreen(this,"Engine");

        //Create Hanger Screen
        hangerScreen = new HangerScreen(this,"Hanger");

        //Create Infirmary Screen
        infirmaryScreen = new InfirmaryScreen(this,"Infirmary");

        //Create Oxygen Screen
        oxygenScreen = new OxygenScreen(this,"Oxygen");

        //Create Quarters Screen
        quartersScreen = new QuartersScreen(this,"Brig");

        //Create Weapons Screen
        weaponsScreen = new WeaponsScreen(this,"Weapons");

        //Create Brig Screen
        brigScreen = new BrigScreen(this, "Brig");

        setScreen(startScreen);
    }

    //Changes the current screen to the one passed in
    @Override
    public void setScreen(Screen nextScreen){
        super.setScreen(nextScreen);
        if( CurrentScreen != nextScreen &&
                RoomScreen.class.isInstance(nextScreen)){
            hudStage.updateRoomName((RoomScreen) nextScreen);
        }
    }

    public Screen getCurrentScreen(){
        return CurrentScreen;
    }


    @Override
    public void dispose() {
        // super.dispose() I think you can't remove this line,
        // because there's some essential action in superclass's method.
        super.dispose();


        // When the game finished, dispose everything
        if (startScreen != null) {
            startScreen.dispose();
            startScreen = null;
        }
        if (gameScreen != null) {
            gameScreen.dispose();
            gameScreen = null;
        }
    }

    public CargoScreen getCargoScreen() {
        return cargoScreen;
    }

    public CommandScreen getCommandScreen() {
        return commandScreen;
    }

    public ElectricalScreen getElectricalScreen() {
        return electricalScreen;
    }

    public EngineScreen getEngineScreen() {
        return engineScreen;
    }

    public HangerScreen getHangerScreen() {
        return hangerScreen;
    }

    public InfirmaryScreen getInfirmaryScreen() {
        return infirmaryScreen;
    }

    public OxygenScreen getOxygenScreen() {
        return oxygenScreen;
    }

    public QuartersScreen getQuartersScreen() {
        return quartersScreen;
    }

    public WeaponsScreen getWeaponsScreen() {
        return weaponsScreen;
    }

    public InputListener getKeyboardInputHandler() {
        return inputHandler;
    }

    public Player getAuber() {
        return auber;
    }

    public void setAuber(Player auber) {
        this.auber = auber;
    }

    public static Random getRandom() {
        return random;
    }
}
