package com.engoneassessment.game.actors.charactors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.engoneassessment.game.actors.CustomActor;

public class Character extends CustomActor implements ICharacter {
    public enum FacingDirection{
        LEFT, RIGHT, UP, DOWN
    }

    public enum MovementState{
        IDLE, Walking, Running, Teleporting
    }

    Array<Texture> actionTextures;

    private int speed; // i set the speed as integer initially, because using double may be more complicated
    private String ability; // every hostile has a ability, abilities such as be invisible, speeding up...

    private Double health = 100.0;
    private String position; // variable type has to be changed to what we define later
    private FacingDirection direction = FacingDirection.DOWN;
    private MovementState movementState = MovementState.IDLE;
    private CharacterCollisionDetector collisionDetector = new CharacterCollisionDetector();

    public Character(TextureRegion textureRegion) {
        super(textureRegion);
    }

    public String getPos() {
        return position;
    }

    public void setPos(String pos) {
        position = pos;
    }

    public void setHealth(Double hp) {
        health = hp;
    }

    public Double getHealth() {
        return health;
    }

    public void setSpeed(int s) {
        speed = s;
    }


    /*
    0:stand
    1:stand_left
     */
    //public void movement_keyboard_event(){
        //if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
        //    this.changeX(- 1200 * Gdx.graphics.getDeltaTime());
        //    //this.setTexture();
        //}
        //if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
        //    this.changeX(1200 * Gdx.graphics.getDeltaTime());
        //    //this.setTexture();
        //}
        //if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
        //    this.changeY(1200 * Gdx.graphics.getDeltaTime());
        //    //this.setTexture();
        //}
        //if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
        //    this.changeY(-1200 * Gdx.graphics.getDeltaTime());
        //    //this.setTexture();
        //}
    //}


    /**
     * logic handler of the actor
     * we need handle the collision detection here.
     *
     * @param delta
     *		the change of time from the last rendered frame to the current rendering frame,
     *	    or we call it the rendering time step / time difference.
     *	    the unite is second.
     */
    @Override
    public void act(float delta) {
        super.act(delta);
        //you can put actions of the character here.
        if(movementState == MovementState.Walking || movementState == MovementState.Running)
        {
            if(collisionDetector.checkCollision(this.getBounds(),direction)){
                collisionDetector.handleCollisionIssue(this,this.direction);
            }
        }
    }

}
