package com.narxoz.rpg.combatant;
import com.narxoz.rpg.state.HeroState;
import com.narxoz.rpg.state.NormalState;
public class Hero {

    private final String name;
    private int hp;
    private final int maxHp;
    private final int attackPower;
    private final int defense;
    private HeroState state;

    public Hero(String name, int hp, int attackPower, int defense) {
        this(name, hp, attackPower, defense, new NormalState());
    }

    public Hero(String name, int hp, int attackPower, int defense,HeroState initialState) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.attackPower = attackPower;
        this.defense = defense;
        this.state = initialState;
    }

    public String getName()        { return name; }
    public int getHp()             { return hp; }
    public int getMaxHp()          { return maxHp; }
    public int getAttackPower()    { return attackPower; }
    public int getDefense()        { return defense; }
    public HeroState getState()    { return state; }
    public boolean isAlive()       { return hp > 0; }

    public void setState(HeroState newState) {
        if (newState == null) {
            throw new IllegalArgumentException("Hero state cannot be null");
        }

        String oldName = state == null ? "None" : state.getName();
        this.state = newState;
        System.out.println("[STATE] " + name + ": " + oldName + " -> " + newState.getName());
    }

    public void startTurn() {
        state.onTurnStart(this);
    }

    public void endTurn() {
        state.onTurnEnd(this);
    }

    public boolean canAct() {
        return state.canAct();
    }
    public void attack(Monster monster) {
        if (!isAlive()) {
            return;
        }

        int dealtDamage = state.modifyOutgoingDamage(attackPower);
        monster.takeDamage(dealtDamage);

        System.out.println(name + " attacks " + monster.getName() + " for " + dealtDamage + " damage.");
        System.out.println(monster.getName() + " now has " + monster.getHp() + " HP.");
    }
    public void receiveAttack(int rawAttackPower) {
        int reducedDamage = Math.max(1, rawAttackPower - defense);
        int finalDamage = state.modifyIncomingDamage(reducedDamage);

        hp = Math.max(0, hp - finalDamage);
        System.out.println(name + " receives " + finalDamage + " damage. HP: " + hp + "/" + maxHp);
    }
    public void takeDamage(int amount) {
        hp = Math.max(0, hp - amount);
    }

    public void takePureDamage(int amount) {
        hp = Math.max(0, hp - amount);
        System.out.println(name + " now has " + hp + "/" + maxHp + " HP.");
    }
    public void heal(int amount) {
        int oldHp = hp;
        hp = Math.min(maxHp, hp + amount);
        System.out.println(name + " heals " + (hp - oldHp) + " HP. HP: " + hp + "/" + maxHp);
    }

    public String getStatusLine() {
        return name + " [HP=" + hp + "/" + maxHp + ", State=" + state.getName() + "]";
    }
}



