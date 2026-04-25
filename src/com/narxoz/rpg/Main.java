package com.narxoz.rpg;
import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.floor.BattleFloor;
import com.narxoz.rpg.floor.BossFloor;
import com.narxoz.rpg.floor.RestFloor;
import com.narxoz.rpg.floor.TrapFloor;
import com.narxoz.rpg.floor.TowerFloor;
import com.narxoz.rpg.state.PoisonedState;
import com.narxoz.rpg.tower.TowerRunResult;
import com.narxoz.rpg.tower.TowerRunner;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Hero arin = new Hero("Arin", 38, 10, 3);
        Hero mira = new Hero("Mira", 34, 8, 4, new PoisonedState(1, 2));

        List<Hero> party = Arrays.asList(arin, mira);

        List<TowerFloor> floors = Arrays.asList(
                new BattleFloor("Floor 1 - Venom Hall"),
                new TrapFloor("Floor 2 - Rune Corridor"),
                new BossFloor("Floor 3 - Ashen Summit"),
                new RestFloor("Floor 4 - Moon Shrine")
        );

        TowerRunner runner = new TowerRunner(floors);
        TowerRunResult result = runner.run(party);

        System.out.println("\n===== FINAL TOWER RESULT =====");
        System.out.println("Floors cleared: " + result.getFloorsCleared());
        System.out.println("Heroes surviving: " + result.getHeroesSurviving());
        System.out.println("Reached top: " + result.isReachedTop());

    }
}
