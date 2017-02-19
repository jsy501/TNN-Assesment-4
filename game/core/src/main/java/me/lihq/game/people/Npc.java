package me.lihq.game.people;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import me.lihq.game.models.Clue;

/**
 * The class which is responsible for the non-playable characters within the game that the player will meet.
 */
public class Npc extends AbstractPerson
{
    private NpcDialogue dialogue;

    private boolean isKiller = false;
    private boolean isVictim = false;

    private boolean isFalseAccused = false;

    /**
     * Used in questioning for more info check Interaction class
     */
    private Array<Clue> exhaustedClues = new Array<>();

    private Personality personality;

    /**
     * The time that Npc will either stand still or walk
     */
    private float randomTimeLimit = 0;

    /**
     * The time sum that Npc has either stood still or walk
     */
    private float randomTimeSum = 0;

    /**
     * Toggle for random walk/stand
     */
    private boolean randomMoveToggle = false;

    /**
     * Define an Npc
     *
     * @param spriteSheet - Sprite sheet for this Npc
     */
    public Npc(JsonValue jsonData, TextureAtlas spriteSheet)
    {
        super(jsonData, spriteSheet);

        dialogue = new NpcDialogue(this);

        Json json = new Json();
        this.personality = json.readValue("personality", Personality.class, jsonData);
    }

    @Override
    public void act(float delta) {
        if (isCanMove()) {
            randomTimeSum += delta;
            if (randomTimeLimit <= randomTimeSum) {
                randomTimeSum = 0;

                if (randomMoveToggle) {
                    setState(PersonState.WALKING);
                    setDirection(getRandomDirection());

                    randomTimeLimit = MathUtils.random(0.5f, 1.5f);
                } else {
                    setState(PersonState.STANDING);

                    randomTimeLimit = MathUtils.random(5, 10);
                }
                randomMoveToggle = !randomMoveToggle;
            }
        }
        super.act(delta);
    }

    private Direction getRandomDirection(){
        int randomInt = MathUtils.random(3);
        switch (randomInt){
            case 0:
                return Direction.EAST;
            case 1:
                return Direction.WEST;
            case 2:
                return Direction.NORTH;
            case 3:
                return Direction.SOUTH;
        }
        return null;
    }

    public boolean isKiller()
    {
        return isKiller;
    }

    public boolean isVictim()
    {
        return isVictim;
    }

    public void setKiller(boolean killer)
    {
        isKiller = killer;
        System.out.println(getName() + " is the killer");
    }

    public void setVictim(boolean victim)
    {
        isVictim = victim;
        System.out.println(getName() + " is the victim");
    }

    @Override
    public NpcDialogue getDialogue() {
        return dialogue;
    }

    /**
     * This method returns the NPCs personality
     *
     * @return (Personality) the NPCs personality {@link me.lihq.game.people.Personality}
     */
    @Override
    public Personality getPersonality()
    {
        return this.personality;
    }

    public Array<Clue> getExhaustedClues() {return this.exhaustedClues;}

    public void addExhaustedClue(Clue clue) {
        this.exhaustedClues.add(clue);
    }


    @Override
    public String toString() {
        return this.getName();
    }

    public void setFalseAccused(boolean falseAccused) {
        isFalseAccused = falseAccused;
    }

    public boolean isFalseAccused() { return isFalseAccused;}
}