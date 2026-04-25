package com.narxoz.rpg.tower;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.floor.FloorResult;
import com.narxoz.rpg.floor.TowerFloor;
import java.util.List;

public class TowerRunner {

    private final List<TowerFloor> floors;

    public TowerRunner(List<TowerFloor> floors) {
        this.floors = floors;
    }

    public TowerRunResult run(List<Hero> party) {
        int cleared = 0;

        for (TowerFloor floor : floors) {
            printParty(party);

            FloorResult result = floor.explore(party);

            System.out.println("[Floor Result] " + result.getSummary());
            System.out.println("[Floor Result] Damage taken on this floor: " + result.getDamageTaken());

            if (!result.isCleared()) {
                break;
            }

            cleared++;

            if (party.stream().noneMatch(Hero::isAlive)) {
                break;
            }
        }

        int survivors = (int) party.stream().filter(Hero::isAlive).count();
        boolean reachedTop = cleared == floors.size() && survivors > 0;

        return new TowerRunResult(cleared, survivors, reachedTop);
    }

    private void printParty(List<Hero> party) {
        System.out.println("\n[Party Status]");
        for (Hero hero : party) {
            System.out.println("- " + hero.getStatusLine());
        }
    }
}