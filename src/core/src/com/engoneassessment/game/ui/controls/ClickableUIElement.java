package com.engoneassessment.game.ui.controls;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.engoneassessment.game.ui.UIElement;

public class ClickableUIElement extends UIElement {
    TextureRegion normalTexture     ;
    TextureRegion hoveringTexture   ;
    TextureRegion pressedTexture    ;

    ButtonUIState buttonUIState = com.engoneassessment.game.ui.controls.Button.ButtonUIState.normal;

    public ClickableUIElement(TextureRegion normalTexture,TextureRegion hoveringTexture,
                  TextureRegion pressedTexture) {
        super(normalTexture);

        this.normalTexture = normalTexture;
        this.hoveringTexture = hoveringTexture;
        this.pressedTexture = pressedTexture;
    }

    //Switches the texture of the button to be highlighted or not highlighted
    public void setButtonUIState(com.engoneassessment.game.ui.controls.Button.ButtonUIState buttonUIStateInput){
        if(buttonUIStateInput != this.buttonUIState){
            switch (buttonUIState){
                case normal:
                    this.setTextureRegion(normalTexture);
                    break;
                case hovering:
                    this.setTextureRegion(hoveringTexture);
                    break;
                case pressed:
                    this.setTextureRegion(pressedTexture);
                    break;
            }
            this.buttonUIState = buttonUIStateInput;
        }
    }

    enum ButtonUIState{
        normal, hovering, pressed
    }
}