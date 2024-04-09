package kr.easw.lesson5;
import java.util.function.Consumer;

public class SimpleIdleRpgExample {
    private static final ItemData POTION = new ItemData("포션", 0, 0, data -> {
        data.hp += 25;
    });

    private static final ItemData LARGE_POTION = new ItemData("대형 포션", 0, 0, data -> {
        data.hp += 50;
    });

    private static final ItemData POISON = new ItemData("독극물", 0, 0, data -> {
        data.hp -= 10;
    });

    private static final ItemData SWORD = new ItemData("검", 5, 1, data -> {
        data.score += 5;
    });

    private static final ItemData SHIELD = new ItemData("방패", 1, 20, data -> {
        data.score += 5;
    });

    private static final ItemData REWARD = new ItemData("보상 아이템", 0, 0, data -> {
        data.score += 5;
    });


    private static final EnemyData[] enemies = new EnemyData[]{new EnemyData(15, 8, new ItemData[]{LARGE_POTION, POTION, SWORD, SWORD, SHIELD, SHIELD,}), new EnemyData(25, 17, new ItemData[]{POTION, POISON, SHIELD, SWORD, POTION, REWARD}), new EnemyData(50, 7, new ItemData[]{LARGE_POTION, POTION, REWARD, REWARD, SHIELD, SWORD, SHIELD, SWORD}), new EnemyData(100, 16, new ItemData[]{}),};


    public static void main(String[] args) throws InterruptedException {
        CharacterData characterData = new CharacterData();
        InventoryData inventoryData = new InventoryData();
        for (EnemyData enemyData : enemies) {
            while (enemyData.hp > 0) {
                characterData.isAwarding = false;
                try {
                    getActionController().onPlayerTurn(characterData, inventoryData, enemyData);
                    int damageDealt = (characterData.weapon == null ? 1 : characterData.weapon.power);
                    characterData.score += damageDealt;
                    enemyData.hp = Math.max(0, enemyData.hp - damageDealt);
                    System.out.println(damageDealt + "의 피해를 입힙니다. 적의 남은 체력 " + enemyData.hp);
                    if (characterData.weapon != null && characterData.weapon.durability <= 1) {
                        characterData.weapon = null;
                        System.out.println("무기가 부숴졌습니다.");
                    } else {
                        if (characterData.weapon != null) characterData.weapon.durability -= 1;
                    }
                } catch (IllegalStateException ex) {
                    System.out.println(ex.getMessage());
                }

                if (enemyData.hp != 0) {
                    int damage = Math.max(0, enemyData.power - (characterData.weapon == null ? 0 : characterData.weapon.defence));
                    characterData.score += damage;
                    characterData.hp = Math.max(0, characterData.hp - damage);
                    System.out.println(damage + "의 피해를 입었습니다. 남은 체력 " + characterData.hp);
                    if (characterData.hp <= 0) {
                        System.out.println("패배하였습니다.");
                        System.out.println("최종 점수: " + characterData.score);
                        return;
                    }
                } else {
                    System.out.println("적을 처치하였습니다.");
                    characterData.isAwarding = true;
                    getActionController().onAward(characterData, inventoryData, enemyData.getDrops());
                }
                Thread.sleep(650L);
            }
        }
        System.out.println("승리하였습니다!");
        System.out.println("최종 점수: " + (characterData.score + characterData.hp * 1.5));
    }

    private static PlayerActionController getActionController() {
        return new MyActionController();
    }

    private static class PlayerActionController {
    	public static int count;  
      
      public void onPlayerTurn(CharacterData characterData, InventoryData inventoryData, EnemyData enemyData) {
            count++;
            System.out.println(count + "번째 턴입니다.");
        }

        public void onAward(CharacterData characterData, InventoryData data, ItemData[] itemData) {
            System.out.println("보상을 받았습니다.");
            
        }
    }

    static class MyActionController extends PlayerActionController {

        @Override
        public void onPlayerTurn(CharacterData characterData, InventoryData inventoryData, EnemyData enemyData) {
        		super.onPlayerTurn(characterData,inventoryData, enemyData);
        }

        @Override
        public void onAward(CharacterData characterData, InventoryData data, ItemData[] itemData) {
        		super.onAward(characterData,data, itemData);	
        }
    }
  
    private static class CharacterData {
        private boolean isAwarding;

        private int hp = 50;

        private ItemData weapon = new ItemData("목검", 3, 5, null);

        private int score = 0;

        public int getHp() {
            return hp;
        }

        public ItemData getWeapon() {
            return weapon;
        }

        public int getScore() {
            return score;
        }
    }

    private static class ItemData implements Cloneable {
        private final String itemName;

        private final int power;

        private final int defence;

        private int durability = 5;

        private Consumer<CharacterData> action;

        public ItemData(String itemName, int power, int defence, Consumer<CharacterData> itemAction) {
            this.itemName = itemName;
            this.power = power;
            this.defence = defence;
            this.action = itemAction;
        }

        public int getDurability() {
            return durability;
        }

        public int getDefence() {
            return defence;
        }

        public int getPower() {
            return power;
        }

        public String getItemName() {
            return itemName;
        }

        public Consumer<CharacterData> getAction() {
            return action;
        }

        @Override
        protected ItemData clone() {
            ItemData data = new ItemData(itemName, power, defence, action);
            data.durability = durability;
            return data;
        }
    }

    private static class EnemyData {
        private int hp;

        private int power;

        private ItemData[] drops;

        public EnemyData(int hp, int power, ItemData[] drops) {
            this.hp = hp;
            this.power = power;
            this.drops = drops;
        }

        public ItemData[] getDrops() {
            return drops;
        }

        public int getPower() {
            return power;
        }

        public int getHp() {
            return hp;
        }

        private void setPower(int power) {
            this.power = power;
        }

        private void setHp(int hp) {
            this.hp = hp;
        }

        private void setDrops(ItemData[] drops) {
            this.drops = drops;
        }
    }

    private static class InventoryData {
        private final ItemData[] items = new ItemData[10];

        /**
         * 주어진 칸에 있는 장비를 장착합니다.
         * 해당 칸에 장비가 없으면 턴을 낭비합니다.
         */
        public void equip(CharacterData data, int slot) {
            if (items[slot] == null) {
                throw new IllegalStateException("해당 칸에 장비가 없습니다! 턴을 낭비합니다.");
            }
            ItemData equipped = data.weapon;
            data.weapon = items[slot].clone();
            items[slot] = equipped;
            if (!data.isAwarding) throw new IllegalStateException("장비 " + data.weapon.itemName + "을 장착합니다.");
        }

        /**
         * 주어진 칸에 있는 장비를 사용합니다.
         * 해당 칸에 장비가 없으면 턴을 낭비합니다.
         */
        public void consume(CharacterData data, int slot) {
            if (data.isAwarding) {
                System.out.println("보상 수령중에는 사용이 불가능합니다.");
                return;
            }
            if (items[slot] == null) {
                throw new IllegalStateException("해당 칸에 장비가 없습니다! 턴을 낭비합니다.");
            }

            if (items[slot].getAction() == null) {
                throw new IllegalStateException("소모 불가능한 장비입니다! 턴을 낭비합니다.");
            }
            ItemData item = items[slot];
            item.getAction().accept(data);
            items[slot] = null;
            throw new IllegalStateException("아이템 " + item.getItemName() + "을 사용합니다.");
        }

        /**
         * 보상 배열에서 아이템을 획득합니다.
         * 전투 도중에 사용시 턴을 낭비합니다.
         */
        public void acquire(CharacterData data, ItemData[] rewardArray, int slot, int targetSlot) {
            if (!data.isAwarding) {
                throw new IllegalStateException("보상 수령중이 아닐때 아이템 획득을 시도하였습니다. 턴이 낭비됩니다.");
            }
            if (items[targetSlot] != null) {
                System.out.println("아이템 " + items[targetSlot].getItemName() + "을 버렸습니다.");
            }
            ItemData itemTarget = rewardArray[slot];
            if (itemTarget == null) {
                return;
            }
            rewardArray[slot] = null;
            items[targetSlot] = itemTarget.clone();
            System.out.println("아이템 " + itemTarget.getItemName() + "을 얻었습니다.");
        }

        /**
         * 대상 위치에 있는 아이템을 반환합니다.
         */
        public ItemData getItemsAt(int index) {
            return items[index];
        }


        /**
         * 인벤토리의 최대 크기를 받습니다.
         */
        public int getMaxSize() {
            return items.length;
        }


        /**
         * 대상 위치에 있는 아이템을 삭제합니다.
         */
        public void remove(CharacterData data, int slot) {
            if (items[slot] != null) {
                ItemData item = items[slot];
                items[slot] = null;
                if (!data.isAwarding) throw new IllegalStateException("아이템 " + item.getItemName() + "을 버렸습니다.");
            }
        }
    }

}
