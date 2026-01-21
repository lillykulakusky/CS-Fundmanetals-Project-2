package student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MobTest {
    Mob skeleton;
    Mob zombie;

    @BeforeEach
    public void setup() {
        skeleton = new Mob("skeleton", 20, 2);
        zombie = new Mob("zombie", 15, 3);
    }

    @Test
    public void getNameReturnsName() {
        assertEquals("skeleton", skeleton.getName());
        assertEquals("zombie", zombie.getName());
    }

    @Test
    public void getMaxHealthReturnsMaxHealth() {
        Mob mob = new Mob("skeleton", 20, 2);
        assertEquals(20, mob.getMaxHealth());
    }

    @Test
    public void getHealthReturnsCurrentHealth() {
        Mob mob = new Mob("skeleton", 20, 2);
        assertEquals(20, mob.getHealth());
    }

    @Test
    public void getHealthAfterDamage() {
        Mob mob = new Mob("skeleton", 20, 2);
        mob.takeDamage(5);
        assertEquals(15, mob.getHealth());
    }

    @Test
    public void toStringReturnsCorrectStatus() {
        Mob healthyMob = new Mob("skeleton", 20, 2);
        Mob injuredMob = new Mob("skeleton", 20, 2);
        Mob deadMob = new Mob("skeleton", 20, 2);

        injuredMob.takeDamage(5);
        deadMob.takeDamage(20);

        assertEquals("skeleton is alive", healthyMob.toString());
        assertEquals("injured skeleton", injuredMob.toString());
        assertEquals("skeleton is dead", deadMob.toString());
    }

    @Test
    public void equalsReturnsIdenticalMobs() {
        Mob mob1 = new Mob("skeleton", 20, 2);
        Mob mob2 = new Mob("skeleton", 20, 2);
        assertEquals(mob1, mob2);
    }

    @Test
    public void equalsReturnsDifferentNames() {
        Mob mob1 = new Mob("skeleton", 20, 2);
        Mob mob2 = new Mob("zombie", 20, 2);
        assertNotEquals(mob1, mob2);
    }

    @Test
    public void equalsReturnsDifferentHealth() {
        Mob mob1 = new Mob("skeleton", 20, 2);
        Mob mob2 = new Mob("skeleton", 15, 2);
        assertNotEquals(mob1, mob2);
    }

    @Test
    public void isInjuredAfterDamage() {
        Mob mob = new Mob("skeleton", 20, 2);
        mob.takeDamage(5);
        assertTrue(mob.isInjured());
    }

    @Test
    public void isInjuredWhenDead() {
        Mob mob = new Mob("skeleton", 20, 2);
        mob.takeDamage(20);
        assertTrue(mob.isInjured());
    }

    @Test
    public void isAliveWhenHealthAboveZero() {
        Mob mob = new Mob("skeleton", 20, 2);
        assertTrue(mob.isAlive());
    }

    @Test
    public void isAliveWhenHealthZero() {
        Mob mob = new Mob("skeleton", 20, 2);
        mob.takeDamage(20);
        assertFalse(mob.isAlive());
    }

    @Test
    public void takeDamageReducesHealth() {
        Mob mob = new Mob("skeleton", 20, 2);
        mob.takeDamage(5);
        assertEquals(15, mob.getHealth());
    }

    @Test
    public void getCurrentStrengthReturnsCurrentStrength() {
        Mob mob = new Mob("skeleton", 20, 10);
        mob.takeDamage(10);
        assertEquals(5, mob.getCurrentStrength());
    }

    @Test
    public void attackWontLetMobAttackItself() {
        assertEquals("A mob cannot attack itself!", skeleton.attack(skeleton));
    }

    @Test
    public void attackRejectsDeadAttacker() {
        // Kill skeleton.
        skeleton.takeDamage(skeleton.getMaxHealth());

        assertEquals(String.format("The dead %s cannot attack healthy %s.", skeleton.getName(), zombie.getName()),
                skeleton.attack(zombie));
    }

    @Test
    public void attackRejectsDeadVictim() {
        // Kill zombie.
        zombie.takeDamage(zombie.getMaxHealth());

        assertEquals(String.format("The %s is already dead.", zombie.getName()),
                skeleton.attack(zombie));
    }

    @Test
    public void attackDamagesOpponent() {
        String description = skeleton.attack(zombie);
        int expectedHealth = zombie.getMaxHealth() - skeleton.getCurrentStrength();
        String expectedResponse = String.format("The %s does %d damage to the %s, which now has a health of %d.",
                skeleton.getName(), skeleton.getCurrentStrength(), zombie.getName(), expectedHealth);
        assertEquals(expectedResponse, description);
        assertEquals(expectedHealth, zombie.getHealth());
    }

    @Test
    public void attackDoesNotDamageAttacker() {
        skeleton.attack(zombie);
        assertEquals(skeleton.getMaxHealth(), skeleton.getHealth());
    }

    @Test
    public void attackDamagesVictim() {
        skeleton.attack(zombie);
        assertEquals(zombie.getMaxHealth() - skeleton.getCurrentStrength(), zombie.getHealth());
    }

    @Test
    public void attackKillsVictim() {
        // Weaken zombie so one attack will finish it.
        zombie.takeDamage(zombie.getMaxHealth() - skeleton.getCurrentStrength());

        String description = skeleton.attack(zombie);
        assertEquals(
                String.format("The %s does %d damage to the %s, which is now dead.", skeleton.getName(), skeleton.getCurrentStrength(), zombie.getName()),
                description);
        assertEquals(0, zombie.getHealth());
        assertFalse(zombie.isAlive());
    }

    @Test
    public void attackDoesNotMakeVictimsHealthNegative() {
        zombie.takeDamage(zombie.getMaxHealth() - 1);

        String description = skeleton.attack(zombie);
        assertEquals(
                String.format("The %s does %d damage to the %s, which is now dead.", skeleton.getName(), skeleton.getCurrentStrength(), zombie.getName()),
                description);
        assertEquals(0, zombie.getHealth());
        assertFalse(zombie.isAlive());
    }
}
