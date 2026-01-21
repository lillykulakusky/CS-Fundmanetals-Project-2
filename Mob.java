package student;

import java.util.Objects;

/**
 * A hostile Minecraft mob.
 *
 * @author Ellen Spertus
 * @author Lilly Kulakusky
 */
public class Mob {
    private final String name;
    private final int maxHealth;
    private int health;
    private final int maxStrength;

    /**
     * Creates a new mob.
     *
     * @param name        the name of the mob
     * @param maxHealth   the maximum health level
     * @param maxStrength the maximum attack strength
     */
    public Mob(String name, int maxHealth, int maxStrength) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.maxStrength = maxStrength;
    }

    /**
     * Gets the name of this mob.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the maximum health points of this mob.
     *
     * @return the maximum health points
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Gets this mob's current health points, which can never be below zero.
     *
     * @return the current health points
     */
    public int getHealth() {
        return health;
    }

    /**
     * Returns the name of this mob preceded by "injured", "healthy",
     * or "dead", for example "injured skeleton".
     *
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        if (health == 0) {
            return String.format("%s is dead", name);
        } else if (health < maxHealth) {
            return String.format("injured %s", name);
        } else {
            return String.format("%s is alive", name);
        }
    }

    /**
     * Tests whether this mob is equal to the passed object. This is
     * only the case if the argument is a {@link Mob} constructed
     * with all the same arguments and has taken the same amount of damage.
     *
     * @param other the other object
     * @return {@code true} if they are equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Mob mob = (Mob) other;
        return maxHealth == mob.maxHealth && health == mob.health && maxStrength == mob.maxStrength && Objects.equals(name, mob.name);
    }

    /**
     * Returns a hash code for this object, such that if two instances are
     * equal, so are their hash codes.
     *
     * @return a hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, maxHealth, health, maxStrength);
    }

    /**
     * Checks whether this mob has taken any damage. Note that this
     * returns true for mobs that are dead.
     *
     * @return {@code true} if the mob has taken damage,
     * {@code false} if it is at maximum health
     */
    public boolean isInjured() {
        return health < maxHealth;
    }

    /**
     * Checks whether this mob is alive.
     *
     * @return {@code true} if the current health level is positive,
     * {@code false} if it is zero
     */
    public boolean isAlive() {
        return health > 0;
    }

    /**
     * Applies the specified number of points of damage to this mob
     * (never letting the health level go below zero).
     *
     * @param damage the number of points of damage
     */
    public void takeDamage(int damage) {
        health = Math.max(0, health - damage);
    }

    /**
     * Gets the current strength of attacks by this mob.
     * The strength is determined by scaling the maximum strength
     * by the ratio of the current health points to the maximum
     * health points, rounded up.
     * <p>
     * For example, if a mob has a maximum strength of 15 and its
     * current health level is half its maximum, its
     * attack strength would be 8 (.5 * 15, rounded up).
     *
     * @return this mob's current attack strength
     */
    public int getCurrentStrength() {

        if (health == 0) {
            return 0;
        }

        int currentStrength;
        currentStrength = (health * maxStrength + maxHealth - 1) / maxHealth;
        return currentStrength;
    }


    /**
     * Simulates an attack by this mob onto another mob,
     * generating an appropriate message and possibly
     * decrementing the other mob's health points.
     *
     * @param mob the mob that is being attacked
     * @return a string describing the outcome
     */
    public String attack(Mob mob) {
        if (this == mob) {
            return "A mob cannot attack itself!";
        }

        if (!isAlive()) {
            return String.format("The dead %s cannot attack healthy %s.", name, mob.getName());
        }

        if (!mob.isAlive()) {
            return String.format("The %s is already dead.", mob.getName());
        }

        int damage = getCurrentStrength();
        mob.takeDamage(damage);
        if (mob.isAlive()) {
            return String.format("The %s does %d damage to the %s, which now has a health of %d.", name, damage, mob.getName(), mob.getHealth());
        } else {
            return String.format("The %s does %d damage to the %s, which is now dead.", name, damage, mob.getName());
        }
    }

    /**
     * Simulates a battle to the death between two mobs, printing the
     * result of each attack.
     *
     * @param mob1 the first mob
     * @param mob2 the second mob
     */
    public static void narrateBattle(Mob mob1, Mob mob2) {
        if (mob1 == mob2) {
            System.out.printf("The %s cannot battle itself!", mob1.getName());
            return;
        }
        while (mob1.isAlive() && mob2.isAlive()) {
            System.out.println(mob1.attack(mob2));
            if (mob2.isAlive()) {
                System.out.println(mob2.attack(mob1));
            }
        }
        Mob winner = mob1.isAlive() ? mob1 : mob2;
        System.out.printf("The %s triumphs!\n", winner.getName());
    }

    /**
     * Simulates a battle between a skeleton and a zombie.
     *
     * @param ignored the command-line arguments
     */
    public static void main(String[] ignored) {
        Mob skeleton = new Mob("skeleton", 20, 2);
        Mob zombie = new Mob("zombie", 15, 3);
        narrateBattle(skeleton, zombie);
    }
}
