package com.engoneassessment.game.ui.controls;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.TimeUtils;

public class ClickableUIElementClickListener extends ClickListener {

    boolean isStillOverAfterClicked = false;
    boolean isStillPressed = false;


    /** Called any time the mouse cursor or a finger touch is moved over an actor. On the desktop, this event occurs even when no
     * mouse buttons are pressed (pointer will be -1).
     * @param fromActor May be null.
     * @see ClickListener */
    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        super.enter(event, x, y, pointer, fromActor);
        ClickableUIElement clickableUIElement = (ClickableUIElement) event.getListenerActor();
        if (clickableUIElement.isEnabled) {
            if(!isPressed()){
                clickableUIElement.setButtonUIState(ClickableUIElement.ButtonUIState.hovered);
            }
        }
        else {
            clickableUIElement.setButtonUIState(ClickableUIElement.ButtonUIState.notActivated);
        }
        isStillOverAfterClicked = false;
    }

    /** Called any time the mouse cursor or a finger touch is moved out of an actor. On the desktop, this event occurs even when no
     * mouse buttons are pressed (pointer will be -1).
     * @param toActor May be null.
     * @see ClickListener */
    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        super.exit(event, x, y, pointer, toActor);
        if(isStillOverAfterClicked){
            enter(event, x, y, pointer, toActor);
        }else {
            ClickableUIElement clickableUIElement = (ClickableUIElement) event.getListenerActor();
            if (clickableUIElement.isEnabled){
                if(!isPressed()){
                    clickableUIElement.setButtonUIState(ClickableUIElement.ButtonUIState.normal);
                }
            } else {
                clickableUIElement.setButtonUIState(ClickableUIElement.ButtonUIState.notActivated);
            }
        }
    }

    /** Called when a mouse button or a finger touch goes up anywhere, but only if touchDown previously returned true for the mouse
     * button or touch. The touchUp event is always {@link Event#handle() handled}.
     * @see InputEvent */
    @Override
    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
        ClickableUIElement clickableUIElement = (ClickableUIElement) event.getListenerActor();
        if(clickableUIElement.isEnabled){
            super.touchUp(event,x,y,pointer,button);
        }
    }

    /** Called when a mouse button or a finger touch goes down on the actor. If true is returned, this listener will have
     * {@link Stage#addTouchFocus(EventListener, Actor, Actor, int, int) touch focus}, so it will receive all touchDragged and
     * touchUp events, even those not over this actor, until touchUp is received. Also when true is returned, the event is
     * {@link Event#handle() handled}.
     * @see ClickListener */
    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        ClickableUIElement clickableUIElement = (ClickableUIElement) event.getListenerActor();
        if(clickableUIElement.isEnabled){
            isStillPressed = true;
            if (clickableUIElement.isEnabled) {
                clickableUIElement.setButtonUIState(ClickableUIElement.ButtonUIState.pressed);
            } else {
                clickableUIElement.setButtonUIState(ClickableUIElement.ButtonUIState.notActivated);
            }
        }
        return super.touchDown(event, x, y, pointer, button);
    }

    /** Called when a mouse button or a finger touch is moved anywhere, but only if touchDown previously returned true for the
     * mouse button or touch. The touchDragged event is always {@link Event#handle() handled}.
     * @see ClickListener */
    @Override
    public void touchDragged (InputEvent event, float x, float y, int pointer) {
        super.touchDragged(event, x, y, pointer);
        ClickableUIElement clickableUIElement = (ClickableUIElement) event.getListenerActor();
        if (clickableUIElement.isEnabled) {
            clickableUIElement.setButtonUIState(ClickableUIElement.ButtonUIState.pressed);
        } else {
            clickableUIElement.setButtonUIState(ClickableUIElement.ButtonUIState.notActivated);
        }
    }

    /** Called when a mouse button or a finger touch goes up anywhere, but only if touchDown previously returned true for the mouse
     * button or touch. The touchUp event is always {@link Event#handle() handled}.
     * @see ClickListener */
    @Override
    public void clicked (InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        boolean a = isOver();
        if(isOver()){
            isStillOverAfterClicked = true;
        }
        ClickableUIElement clickableUIElement = (ClickableUIElement) event.getListenerActor();
        if (clickableUIElement.isEnabled) {
            if(isStillOverAfterClicked){
                clickableUIElement.setButtonUIState(ClickableUIElement.ButtonUIState.hovered);
            }
            else{
                clickableUIElement.setButtonUIState(ClickableUIElement.ButtonUIState.normal);
            }
        } else {
            clickableUIElement.setButtonUIState(ClickableUIElement.ButtonUIState.notActivated);
        }
    }
}
