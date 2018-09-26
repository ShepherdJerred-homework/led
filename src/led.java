// led
// Jerred Shepherd

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class led {
    public static void main(String[] args) throws FileNotFoundException {
        File inputFile = new File("led.in");
        File outputFile = new File("led.out");

        Scanner scanner = new Scanner(inputFile);
        PrintWriter printWriter = new PrintWriter(outputFile);

        Map<Integer, BitSet> originalNumbers = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            originalNumbers.put(i, createBitSetForInt(i));
        }

        String line;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            if (line.equals("0")) {
                break;
            }

            int[] brokenSegmentInts = line
                    .chars()
                    .filter(Character::isDigit)
                    .map(i -> (char) i)
                    .map(Character::getNumericValue)
                    .filter(i -> i != 0)
                    .toArray();

            BitSet brokenSegmentBitSet = new BitSet(7);
            brokenSegmentBitSet.set(0, 7);

            for (int brokenSegment : brokenSegmentInts) {
                brokenSegmentBitSet.set(brokenSegment - 1, false);
            }

            Map<Integer, BitSet> numbersAfterBrokenSegments = new HashMap<>();
            for (int i = 0; i < 10; i++) {
                BitSet clonedBitSet = (BitSet) originalNumbers.get(i).clone();
                clonedBitSet.and(brokenSegmentBitSet);
                numbersAfterBrokenSegments.put(i, clonedBitSet);
            }

            Set<Set> identicalNumbers = new LinkedHashSet<>();

            for (int i = 0; i < 10; i++) {
                Set<Integer> set = new LinkedHashSet<>();
                set.add(i);
                for (int j = 0; j < 10; j++) {
                    if (i == j) {
                        continue;
                    }
                    if (numbersAfterBrokenSegments.get(i).equals(numbersAfterBrokenSegments.get(j))) {
                        set.add(j);
                    }
                }
                if (set.size() > 1) {
                    identicalNumbers.add(set);
                }
            }

            String outputString;
            if (identicalNumbers.isEmpty()) {
                outputString = String.format("Case <%s>:", Arrays.toString(brokenSegmentInts)
                        .replace("[", "")
                        .replace("]", "")
                        .replace(", ", ","));
            } else {
                outputString = String.format("Case <%s>: %s",
                        Arrays.toString(brokenSegmentInts)
                                .replace("[", "")
                                .replace("]", "")
                                .replace(", ", ","),
                        identicalNumbers.toString()
                                .replace("[[", "{")
                                .replace("]]", "}")
                                .replace("[", "{")
                                .replace("]", "}")
                                .replace(", ", ",")
                                .replace("},{", "} {"));
            }

            System.out.println(outputString);
            printWriter.println(outputString);

        }
        printWriter.close();
    }

    public static BitSet createBitSetForInt(int i) {
        BitSet bitSet = new BitSet(7);
        switch (i) {
            case 0:
                bitSet.set(0);
                bitSet.set(1);
                bitSet.set(2);
                bitSet.set(4);
                bitSet.set(5);
                bitSet.set(6);
                break;
            case 1:
                bitSet.set(2);
                bitSet.set(5);
                break;
            case 2:
                bitSet.set(0);
                bitSet.set(2);
                bitSet.set(3);
                bitSet.set(4);
                bitSet.set(6);
                break;
            case 3:
                bitSet.set(0);
                bitSet.set(2);
                bitSet.set(3);
                bitSet.set(5);
                bitSet.set(6);
                break;
            case 4:
                bitSet.set(1);
                bitSet.set(2);
                bitSet.set(3);
                bitSet.set(5);
                break;
            case 5:
                bitSet.set(0);
                bitSet.set(1);
                bitSet.set(3);
                bitSet.set(5);
                bitSet.set(6);
                break;
            case 6:
                bitSet.set(0);
                bitSet.set(1);
                bitSet.set(3);
                bitSet.set(4);
                bitSet.set(5);
                bitSet.set(6);
                break;
            case 7:
                bitSet.set(0);
                bitSet.set(2);
                bitSet.set(5);
                break;
            case 8:
                bitSet.set(0);
                bitSet.set(1);
                bitSet.set(2);
                bitSet.set(3);
                bitSet.set(4);
                bitSet.set(5);
                bitSet.set(6);
                break;
            case 9:
                bitSet.set(0);
                bitSet.set(1);
                bitSet.set(2);
                bitSet.set(3);
                bitSet.set(5);
                break;
            default:
                throw new IllegalArgumentException("No LED value for int " + i);
        }
        return bitSet;
    }

    public static String bitSetToString(BitSet bitSet) {
        return String.format("%s%s%s\n%s %s\n%s%s%s\n%s %s\n%s%s%s",
                booleanToSegment(bitSet.get(0)), booleanToSegment(bitSet.get(0)), booleanToSegment(bitSet.get(0)),
                booleanToSegment(bitSet.get(1)), booleanToSegment(bitSet.get(2)),
                booleanToSegment(bitSet.get(3)), booleanToSegment(bitSet.get(3)), booleanToSegment(bitSet.get(3)),
                booleanToSegment(bitSet.get(4)), booleanToSegment(bitSet.get(5)),
                booleanToSegment(bitSet.get(6)), booleanToSegment(bitSet.get(6)), booleanToSegment(bitSet.get(6)));
    }

    public static String booleanToSegment(boolean b) {
        if (b) {
            return "▓";
        } else {
            return " ";
        }
    }
}
