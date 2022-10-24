package ca.rttv.malum.util.helper;

import ladysnake.effective.client.Effective;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.*;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static net.minecraft.util.math.MathHelper.sqrt;

public final class DataHelper {
    public static Vec3d fromBlockPos(BlockPos pos) {
        return new Vec3d(pos.getX(), pos.getY(), pos.getZ());
    }

    public static Vec3f fromBlockPosVec3f(BlockPos pos) {
        return new Vec3f(pos.getX(), pos.getY(), pos.getZ());
    }

    public static Vec3d randPos(BlockPos pos, RandomGenerator rand, double min, double max) {
        double x = MathHelper.nextDouble(rand, min, max) + pos.getX();
        double y = MathHelper.nextDouble(rand, min, max) + pos.getY();
        double z = MathHelper.nextDouble(rand, min, max) + pos.getZ();
        return new Vec3d(x, y, z);
    }

    public static Identifier prefix(String path) {
        return new Identifier(Effective.MODID, path);
    }

    public static <T, K extends Collection<T>> K reverseOrder(Supplier<K> reversed, Collection<T> items) {
        ArrayList<T> original = new ArrayList<>(items);
        K newCollection = reversed.get();
        for (int i = items.size() - 1; i >= 0; i--) {
            newCollection.add(original.get(i));
        }
        return newCollection;
    }

    public static String toTitleCase(String givenString, String regex) {
        String[] stringArray = givenString.split(regex);
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : stringArray) {
            stringBuilder.append(Character.toUpperCase(string.charAt(0))).append(string.substring(1)).append(regex);
        }
        return stringBuilder.toString().trim().replaceAll(regex, " ").substring(0, stringBuilder.length() - 1);
    }

    public static void writeNbt(NbtCompound nbt, DefaultedList<ItemStack> stacks, String key) {
        NbtList nbtList = new NbtList();

        for(int i = 0; i < stacks.size(); ++i) {
            ItemStack itemStack = stacks.get(i);
            if (!itemStack.isEmpty()) {
                NbtCompound nbtCompound = new NbtCompound();
                nbtCompound.putByte("Slot", (byte)i);
                itemStack.writeNbt(nbtCompound);
                nbtList.add(nbtCompound);
            }
        }
        nbt.put(key, nbtList);
    }

    public static void readNbt(NbtCompound nbt, DefaultedList<ItemStack> stacks, String key) {
        NbtList nbtList = nbt.getList(key, 10);

        for(int i = 0; i < nbtList.size(); ++i) {
            NbtCompound nbtCompound = nbtList.getCompound(i);
            int j = nbtCompound.getByte("Slot") & 255;
            if (j < stacks.size()) {
                stacks.set(j, ItemStack.fromNbt(nbtCompound));
            }
        }

    }

    public static int[] nextInts(RandomGenerator rand, int count, int range) {
        int[] ints = new int[count];
        for (int i = 0; i < count; i++) {
            while (true) {
                int nextInt = rand.nextInt(range);
                if (Arrays.stream(ints)
                          .noneMatch(j -> j == nextInt)) {
                    ints[i] = nextInt;
                    break;
                }
            }
        }
        return ints;
    }

    public static <T> boolean hasDuplicate(T[] things) {
        Set<T> thingSet = new HashSet<>();
        return !Arrays.stream(things)
                      .allMatch(thingSet::add);
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> Collection<T> takeAll(Collection<? extends T> src, T... items) {
        List<T> ret = Arrays.asList(items);
        for (T item : items) {
            if (!src.contains(item)) {
                return Collections.emptyList();
            }
        }
        if (!src.removeAll(ret)) {
            return Collections.emptyList();
        }
        return ret;
    }

    public static <T> Collection<T> takeAll(Collection<T> src, Predicate<T> pred) {
        List<T> ret = new ArrayList<>();

        Iterator<T> iterable = src.iterator();
        while (iterable.hasNext()) {
            T item = iterable.next();
            if (pred.test(item)) {
                iterable.remove();
                ret.add(item);
            }
        }

        if (ret.isEmpty()) {
            return Collections.emptyList();
        }
        return ret;
    }

    @SafeVarargs
    public static <T> Collection<T> getAll(Collection<? extends T> src, T... items) {
        return List.copyOf(getAll(src, t -> Arrays.stream(items)
                                                  .anyMatch(tAgain -> tAgain.getClass().isInstance(t))));
    }

    public static <T> Collection<T> getAll(Collection<T> src, Predicate<T> pred) {
        return src.stream()
                  .filter(pred)
                  .collect(Collectors.toList());
    }

    public static Vec3d circlePosition(Vec3d pos, float distance, float current, float total) {
        double angle = current / total * (Math.PI * 2);
        double dx2 = (distance * Math.cos(angle));
        double dz2 = (distance * Math.sin(angle));

        Vec3d vector = new Vec3d(dx2, 0, dz2);
        double x = vector.x * distance;
        double z = vector.z * distance;
        return pos.add(new Vec3d(x, 0, z));
    }

    public static Vec3d rotatedCirclePosition(Vec3d pos, float distance, float current, float total, long gameTime, float time, float tickDelta) {
        return rotatedCirclePosition(pos, distance, distance, current, total, gameTime, time, tickDelta);
    }

    public static Vec3d rotatedCirclePosition(Vec3d pos, float distanceX, float distanceZ, float current, float total, long gameTime, float time, float tickDelta) {
        double angle = current / total * (Math.PI * 2);
        angle += (((gameTime % time) + tickDelta) / time) * (Math.PI * 2);
        double dx2 = (distanceX * Math.cos(angle));
        double dz2 = (distanceZ * Math.sin(angle));

        Vec3d vector2f = new Vec3d(dx2, 0, dz2);
        double x = vector2f.x * distanceX;
        double z = vector2f.z * distanceZ;
        return pos.add(x, 0, z);
    }

    public static ArrayList<Vec3d> blockOutlinePositions(World world, BlockPos pos) {
        ArrayList<Vec3d> arrayList = new ArrayList<>();
        double d0 = 0.5625D;
        RandomGenerator random = world.random;
        for (Direction direction : Direction.values()) {
            BlockPos blockpos = pos.offset(direction);
            if (!world.getBlockState(blockpos).isOpaqueFullCube(world, blockpos)) {
                Direction.Axis direction$axis = direction.getAxis();
                double d1 = direction$axis == Direction.Axis.X ? 0.5D + d0 * (double) direction.getOffsetX() : (double) random.nextFloat();
                double d2 = direction$axis == Direction.Axis.Y ? 0.5D + d0 * (double) direction.getOffsetY() : (double) random.nextFloat();
                double d3 = direction$axis == Direction.Axis.Z ? 0.5D + d0 * (double) direction.getOffsetZ() : (double) random.nextFloat();
                arrayList.add(new Vec3d((double) pos.getX() + d1, (double) pos.getY() + d2, (double) pos.getZ() + d3));
            }
        }
        return arrayList;
    }
    public static float distSqr(float... a) {
        float d = 0.0F;
        for (float f : a) {
            d += f * f;
        }
        return d;
    }

    public static float distance(float... a) {
        return sqrt(distSqr(a));
    }

    /**
     * {@code else do if}
     */
    public static <T> T findFirstMatching(T[] arr, Predicate<T> predicate, T orElse) {
        int i = 0;
        if (arr == null) {
            return orElse;
        } else do if (predicate.test(arr[i])) return arr[i]; while (arr.length > i++);
        return orElse;
    }
}
