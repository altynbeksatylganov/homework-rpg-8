package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import com.narxoz.rpg.state.PoisonedState;
import java.util.List;

public class BattleFloor extends TowerFloor {

    private final String floorName;
    private Monster monster;

    public BattleFloor(String floorName) {
        this.floorName = floorName;
    }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("[Setup] A venomous guardian appears.");
        monster = new Monster("Venom Skeleton", 34, 9);
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("[Challenge] Combat begins on " + floorName + ".");
        int totalDamageTaken = 0;
        int round = 1;

        while (monster.isAlive() && hasLivingHeroes(party)) {
            System.out.println("Round " + round);

            for (Hero hero : party) {
                if (!hero.isAlive() || !monster.isAlive()) {
                    continue;
                }

                hero.startTurn();

                if (hero.isAlive() && hero.canAct()) {
                    hero.attack(monster);
                }

                hero.endTurn();
            }

            Hero target = firstLivingHero(party);
            if (monster.isAlive() && target != null) {
                int before = target.getHp();
                System.out.println(monster.getName() + " attacks " + target.getName() + "!");
                target.receiveAttack(monster.getAttackPower());
                totalDamageTaken += Math.max(0, before - target.getHp());

                if (target.isAlive() && !(target.getState() instanceof PoisonedState)) {
                    System.out.println(target.getName() + " is splashed with venom!");
                    target.setState(new PoisonedState(2, 3));
                }
            }

            round++;
        }

        boolean cleared = !monster.isAlive() && hasLivingHeroes(party);
        String summary = cleared
                ? "The venom skeleton was defeated."
                : "The party was defeated by the venom skeleton.";

        return new FloorResult(cleared, totalDamageTaken, summary);
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        System.out.println("[Loot] The heroes find herbs and recover 4 HP each.");
        for (Hero hero : party) {
            if (hero.isAlive()) {
                hero.heal(4);
            }
        }
    }

    @Override
    protected void cleanup(List<Hero> party) {
        System.out.println("[Cleanup] Bones are scattered across the floor.");
    }

    @Override
    protected String getFloorName() {
        return floorName;
    }

    private boolean hasLivingHeroes(List<Hero> party) {
        return party.stream().anyMatch(Hero::isAlive);
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