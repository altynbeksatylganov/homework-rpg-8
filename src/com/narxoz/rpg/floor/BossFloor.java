package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import java.util.List;

public class BossFloor extends TowerFloor {

    private final String floorName;
    private Monster boss;

    public BossFloor(String floorName) {
        this.floorName = floorName;
    }

    @Override
    protected void announce() {
        System.out.println("\n=== BOSS FLOOR: " + floorName + " ===");
        System.out.println("The tower trembles as the final guardian descends.");
    }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("[Setup] The Warden of Ash awakens.");
        boss = new Monster("Warden of Ash", 48, 12);
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("[Challenge] The final battle begins.");
        int totalDamageTaken = 0;
        int round = 1;

        while (boss.isAlive() && hasLivingHeroes(party)) {
            System.out.println("Boss round " + round);

            for (Hero hero : party) {
                if (!hero.isAlive() || !boss.isAlive()) {
                    continue;
                }

                hero.startTurn();

                if (hero.isAlive() && hero.canAct()) {
                    hero.attack(boss);
                }

                hero.endTurn();
            }

            Hero target = lastLivingHero(party);
            if (boss.isAlive() && target != null) {
                int before = target.getHp();
                System.out.println(boss.getName() + " attacks " + target.getName() + "!");
                target.receiveAttack(boss.getAttackPower());
                totalDamageTaken += Math.max(0, before - target.getHp());
            }

            round++;
        }

        boolean cleared = !boss.isAlive() && hasLivingHeroes(party);
        String summary = cleared
                ? "The Warden of Ash has fallen."
                : "The party was destroyed on the boss floor.";

        return new FloorResult(cleared, totalDamageTaken, summary);
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        System.out.println("[Loot] The tower heart restores 8 HP to each surviving hero.");
        for (Hero hero : party) {
            if (hero.isAlive()) {
                hero.heal(8);
            }
        }
    }

    @Override
    protected void cleanup(List<Hero> party) {
        System.out.println("[Cleanup] Ash settles as the staircase to freedom opens.");
    }

    @Override
    protected String getFloorName() {
        return floorName;
    }

    private boolean hasLivingHeroes(List<Hero> party) {
        return party.stream().anyMatch(Hero::isAlive);
    }

    private Hero lastLivingHero(List<Hero> party) {
        Hero target = null;
        for (Hero hero : party) {
            if (hero.isAlive()) {
                target = hero;
            }
        }
        return target;
    }
}