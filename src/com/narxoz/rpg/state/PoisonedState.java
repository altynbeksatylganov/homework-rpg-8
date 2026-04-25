package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class PoisonedState implements HeroState {

    private int turnsRemaining;
    private final int poisonDamage;

    public PoisonedState(int turnsRemaining, int poisonDamage) {
        this.turnsRemaining = turnsRemaining;
        this.poisonDamage = poisonDamage;
    }

    @Override
    public String getName() {
        return "Poisoned";
    }

    @Override
    public int modifyOutgoingDamage(int basePower) {
        return Math.max(1, (int) Math.floor(basePower * 0.8));
    }

    @Override
    public int modifyIncomingDamage(int rawDamage) {
        return Math.max(1, (int) Math.ceil(rawDamage * 1.2));
    }

    @Override
    public void onTurnStart(Hero hero) {
        if (!hero.isAlive()) {
            return;
        }

        System.out.println(hero.getName() + " suffers " + poisonDamage + " poison damage.");
        hero.takePureDamage(poisonDamage);
    }

    @Override
    public void onTurnEnd(Hero hero) {
        turnsRemaining--;
        if (turnsRemaining <= 0 && hero.isAlive()) {
            hero.setState(new NormalState());
        }
    }

    @Override
    public boolean canAct() {
        return true;
    }
}