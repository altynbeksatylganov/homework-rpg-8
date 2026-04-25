package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.state.NormalState;
import java.util.List;

public class RestFloor extends TowerFloor {

    private final String floorName;

    public RestFloor(String floorName) {
        this.floorName = floorName;
    }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("[Setup] A sanctuary room glows with quiet blue fire.");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("[Challenge] There is no enemy here. The party rests.");

        for (Hero hero : party) {
            if (hero.isAlive()) {
                hero.heal(6);

                if (!(hero.getState() instanceof NormalState)) {
                    System.out.println(hero.getName() + " calms down and returns to normal.");
                    hero.setState(new NormalState());
                }
            }
        }

        return new FloorResult(true, 0, "The party safely rested.");
    }

    @Override
    protected boolean shouldAwardLoot(FloorResult result) {
        System.out.println("[Hook] shouldAwardLoot() -> false on a rest floor.");
        return false;
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        System.out.println("[Loot] This should never print because shouldAwardLoot() returns false.");
    }

    @Override
    protected void cleanup(List<Hero> party) {
        System.out.println("[Cleanup] The sanctuary door closes softly.");
    }

    @Override
    protected String getFloorName() {
        return floorName;
    }
}