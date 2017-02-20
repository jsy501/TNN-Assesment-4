package me.lihq.game.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import me.lihq.game.ConversationManager;
import me.lihq.game.people.Npc;
import me.lihq.game.people.Player;

/**
 * window for getting interaction option from the player. The options are question, accuse and ignore.
 */
public class InteractionSelectionBubble extends SpeechBubble{

    public InteractionSelectionBubble(Player player, Skin skin, ConversationManager conversationManager, Gui gui) {
        super(player, skin);

        TextButton questionButton = new TextButton("Question", skin, "buttonBubble");
        questionButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //interaction halts when there is no clue in the inventory
                if (player.getInventory().getCollectedClues().size == 0){
                    gui.displayInfo("You don't have any clues!");
                    conversationManager.setFinished(true);
                }

                //if the npc is falsely accused, he/she will refuse to be questioned
                else if (conversationManager.getInteractingCharacter().isFalseAccused()){
                    Npc interactingCharacter = conversationManager.getInteractingCharacter();
                    SpeechBubble speechBubble = new ConversationSpeechBubble(interactingCharacter,
                            interactingCharacter.getDialogue().getFailResponseArray().random(), skin);
                    conversationManager.addSpeechBubble(speechBubble);
                    conversationManager.nextSpeechBubble();
                }

                //otherwise open question window
                else {
                    gui.getClueSelectionWindow().show(gui.getGuiStage());
                }
            }
        });
        addButton(questionButton);

        TextButton accuseButton = new TextButton("Accuse", skin, "buttonBubble");
        addButton(accuseButton);

        TextButton ignoreButton = new TextButton("Ignore", skin, "buttonBubble");
        ignoreButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                 conversationManager.setFinished(true);
            }
        });
        addButton(ignoreButton);

    }

    private void addButton(TextButton button){
        getContentTable().add(button).row();
    }
}