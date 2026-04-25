package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.state.StunnedState;
import java.util.List;

public class TrapFloor extends TowerFloor {

    private final String floorName;

    public TrapFloor(String floorName) {
        this.floorName = floorName;
    }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("[Setup] The corridor is filled with unstable runes.");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("[Challenge] Arcane bolts strike the front hero.");
        Hero target = firstLivingHero(party);

        if (target == null) {
            return new FloorResult(false, 0, "No heroes were left to face the trap.");
        }

        int damageTaken = 0;

        int before = target.getHp();
        target.receiveAttack(11);
        damageTaken += Math.max(0, before - target.getHp());

        if (target.isAlive()) {
            System.out.println(target.getName() + " is shocked by the trap and becomes stunned.");
            target.setState(new StunnedState(1));
        }

        for (Hero hero : party) {
            if (hero != target && hero.isAlive()) {
                int otherBefore = hero.getHp();
                hero.receiveAttack(6);
                damageTaken += Math.max(0, otherBefore - hero.getHp());
            }
        }

        boolean cleared = party.stream().anyMatch(Hero::isAlive);
        return new FloorResult(cleared, damageTaken, "The rune trap has been disarmed.");
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        System.out.println("[Loot] The party finds a protective charm and heals 2 HP each.");
        for (Hero hero : party) {
            if (hero.isAlive()) {
                hero.heal(2);
            }
        }
    }

    @Override
    protected void cleanup(List<Hero> party) {
        System.out.println("[Cleanup] The runes fade away behind the party.");
    }

    @Override
    protected String getFloorName() {
        return floorName;
    }

    private Hero firstLivingHero(List<Hero> party) {
        for (Hero hero : party) {
            if (hero.isAlive()) {
                return hero;
            }
        }
        return null;
    }
}