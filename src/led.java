// led
// Jerred Shepherd

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class led {
    public static void main(String args[]) throws FileNotFoundException {
        LinkedList<Input> inputs = getInputs();
        LinkedList<String> solutions = solveInputs(inputs);
        outputSolutions(solutions);
    }

    private static LinkedList<Input> getInputs() throws FileNotFoundException {
        File inputFile = new File("led.in");
        Scanner scanner = new Scanner(inputFile);

        LinkedList<Input> inputs = new LinkedList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Set<Integer> brokenComponents = stringToIntegerSet(line);

            if (brokenComponents.size() == 1 && brokenComponents.contains(0)) {
                break;
            }

            brokenComponents.remove(0);

            Input input = new Input(brokenComponents);
            inputs.add(input);
        }
        return inputs;
    }

    private static void outputSolutions(LinkedList<String> solutions) throws FileNotFoundException {
        File outputFile = new File("led.out");
        PrintWriter printWriter = new PrintWriter(outputFile);
        solutions.forEach(solution -> {
            System.out.println(solution);
            printWriter.println(solution);
        });
        printWriter.close();
    }


    private static LinkedList<String> solveInputs(LinkedList<Input> inputs) {
        LinkedList<String> solutions = new LinkedList<>();
        inputs.forEach(input -> solutions.add(solveInput(input)));
        return solutions;
    }

    private static String solveInput(Input input) {
        Set<Integer> brokenComponents = input.brokenComponents;

        SevenSegmentDisplay.BrokenSevenSegmentDisplayFactory displayFactory = new SevenSegmentDisplay.BrokenSevenSegmentDisplayFactory(
                brokenComponents.contains(1),
                brokenComponents.contains(2),
                brokenComponents.contains(3),
                brokenComponents.contains(4),
                brokenComponents.contains(5),
                brokenComponents.contains(6),
                brokenComponents.contains(7)
        );

        Map<SevenSegmentDisplay, Set<Integer>> mapOfDisplaysToIntegers = new HashMap<>();
        Set<Set<Integer>> integersThatHaveDisplayCollisions = new HashSet<>();

        for (int i = 0; i < 10; i++) {
            SevenSegmentDisplay display = displayFactory.create(i);
            if (mapOfDisplaysToIntegers.containsKey(display)) {
                Set<Integer> set = mapOfDisplaysToIntegers.get(display);
                if (set.size() == 1) {
                    integersThatHaveDisplayCollisions.add(set);
                }
                set.add(i);
            } else {
                Set<Integer> set = new HashSet<>();
                set.add(i);
                mapOfDisplaysToIntegers.put(display, set);
            }
        }

        return createSolutionString(brokenComponents, integersThatHaveDisplayCollisions);
    }

    private static Set<Integer> stringToIntegerSet(String input) {
        return Stream
                .of(input)
                .map(s -> s.split(" "))
                .flatMap(Arrays::stream)
                .mapToInt(Integer::parseInt)
                .boxed()
                .collect(Collectors.toSet());
    }

    // this is terrible
    private static String createSolutionString(Set<Integer> brokenSegments, Set<Set<Integer>> collidedIntegers) {

        StringBuilder sb = new StringBuilder();
        sb.append(brokenSegments.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",")));

        String brokenSegmentsString = sb.toString();

        sb = new StringBuilder();
        String ns = collidedIntegers.stream()
                .map(integers -> integers.stream().map(String::valueOf).collect(Collectors.joining(",")))
                .map(s -> String.format("{%s}", s))
                .collect(Collectors.joining(" "));
        sb.append(ns);

        String collisionsString = sb.toString();


        return String.format("Case <%s>: %s", brokenSegmentsString, collisionsString);
    }

    private static class Input {
        Set<Integer> brokenComponents;

        public Input(Set<Integer> brokenComponents) {
            this.brokenComponents = brokenComponents;
        }
    }

    private static class SevenSegmentDisplay {
        boolean segmentOne;
        boolean segmentTwo;
        boolean segmentThree;
        boolean segmentFour;
        boolean segmentFive;
        boolean segmentSix;
        boolean segmentSeven;

        private SevenSegmentDisplay(boolean segmentOne, boolean segmentTwo, boolean segmentThree, boolean segmentFour, boolean segmentFive, boolean segmentSix, boolean segmentSeven) {
            this.segmentOne = segmentOne;
            this.segmentTwo = segmentTwo;
            this.segmentThree = segmentThree;
            this.segmentFour = segmentFour;
            this.segmentFive = segmentFive;
            this.segmentSix = segmentSix;
            this.segmentSeven = segmentSeven;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SevenSegmentDisplay display = (SevenSegmentDisplay) o;
            return segmentOne == display.segmentOne &&
                    segmentTwo == display.segmentTwo &&
                    segmentThree == display.segmentThree &&
                    segmentFour == display.segmentFour &&
                    segmentFive == display.segmentFive &&
                    segmentSix == display.segmentSix &&
                    segmentSeven == display.segmentSeven;
        }

        @Override
        public int hashCode() {
            return Objects.hash(segmentOne, segmentTwo, segmentThree, segmentFour, segmentFive, segmentSix, segmentSeven);
        }

        private static class Builder {
            boolean segmentOne = false;
            boolean segmentTwo = false;
            boolean segmentThree = false;
            boolean segmentFour = false;
            boolean segmentFive = false;
            boolean segmentSix = false;
            boolean segmentSeven = false;

            public Builder withSegmentOne(boolean b) {
                this.segmentOne = b;
                return this;
            }

            public Builder withSegmentTwo(boolean b) {
                this.segmentTwo = b;
                return this;
            }

            public Builder withSegmentThree(boolean b) {
                this.segmentThree = b;
                return this;
            }

            public Builder withSegmentFour(boolean b) {
                this.segmentFour = b;
                return this;
            }

            public Builder withSegmentFive(boolean b) {
                this.segmentFive = b;
                return this;
            }

            public Builder withSegmentSix(boolean b) {
                this.segmentSix = b;
                return this;
            }

            public Builder withSegmentSeven(boolean b) {
                this.segmentSeven = b;
                return this;
            }

            public SevenSegmentDisplay build() {
                return new SevenSegmentDisplay(
                        segmentOne,
                        segmentTwo,
                        segmentThree,
                        segmentFour,
                        segmentFive,
                        segmentSix,
                        segmentSeven
                );
            }
        }

        private static class BrokenSevenSegmentDisplayFactory {
            boolean isSegmentOneBroken;
            boolean isSegmentTwoBroken;
            boolean isSegmentThreeBroken;
            boolean isSegmentFourBroken;
            boolean isSegmentFiveBroken;
            boolean isSegmentSixBroken;
            boolean isSegmentSevenBroken;

            public BrokenSevenSegmentDisplayFactory(boolean isSegmentOneBroken, boolean isSegmentTwoBroken, boolean isSegmentThreeBroken, boolean isSegmentFourBroken, boolean isSegmentFiveBroken, boolean isSegmentSixBroken, boolean isSegmentSevenBroken) {
                this.isSegmentOneBroken = isSegmentOneBroken;
                this.isSegmentTwoBroken = isSegmentTwoBroken;
                this.isSegmentThreeBroken = isSegmentThreeBroken;
                this.isSegmentFourBroken = isSegmentFourBroken;
                this.isSegmentFiveBroken = isSegmentFiveBroken;
                this.isSegmentSixBroken = isSegmentSixBroken;
                this.isSegmentSevenBroken = isSegmentSevenBroken;
            }

            public SevenSegmentDisplay create(int i) {
                switch (i) {
                    case 0:
                        return new SevenSegmentDisplay.Builder()
                                .withSegmentOne(!isSegmentOneBroken)
                                .withSegmentTwo(!isSegmentTwoBroken)
                                .withSegmentThree(!isSegmentThreeBroken)
                                .withSegmentFive(!isSegmentFiveBroken)
                                .withSegmentSix(!isSegmentSixBroken)
                                .withSegmentSeven(!isSegmentSevenBroken)
                                .build();
                    case 1:
                        return new SevenSegmentDisplay.Builder()
                                .withSegmentThree(!isSegmentThreeBroken)
                                .withSegmentSix(!isSegmentSixBroken)
                                .build();
                    case 2:
                        return new SevenSegmentDisplay.Builder()
                                .withSegmentOne(!isSegmentOneBroken)
                                .withSegmentThree(!isSegmentThreeBroken)
                                .withSegmentFour(!isSegmentFourBroken)
                                .withSegmentFive(!isSegmentFiveBroken)
                                .withSegmentSix(!isSegmentSixBroken)
                                .build();
                    case 3:
                        return new SevenSegmentDisplay.Builder()
                                .withSegmentOne(!isSegmentOneBroken)
                                .withSegmentThree(!isSegmentThreeBroken)
                                .withSegmentFour(!isSegmentFourBroken)
                                .withSegmentSix(!isSegmentSixBroken)
                                .withSegmentSeven(!isSegmentSevenBroken)
                                .build();
                    case 4:
                        return new SevenSegmentDisplay.Builder()
                                .withSegmentTwo(!isSegmentTwoBroken)
                                .withSegmentThree(!isSegmentThreeBroken)
                                .withSegmentFour(!isSegmentFourBroken)
                                .withSegmentSix(!isSegmentSixBroken)
                                .build();
                    case 5:
                        return new SevenSegmentDisplay.Builder()
                                .withSegmentOne(!isSegmentOneBroken)
                                .withSegmentTwo(!isSegmentTwoBroken)
                                .withSegmentFour(!isSegmentFourBroken)
                                .withSegmentSix(!isSegmentSixBroken)
                                .withSegmentSeven(!isSegmentSevenBroken)
                                .build();
                    case 6:
                        return new SevenSegmentDisplay.Builder()
                                .withSegmentOne(!isSegmentOneBroken)
                                .withSegmentTwo(!isSegmentTwoBroken)
                                .withSegmentFour(!isSegmentFourBroken)
                                .withSegmentFive(!isSegmentFiveBroken)
                                .withSegmentSix(!isSegmentSixBroken)
                                .withSegmentSeven(!isSegmentSevenBroken)
                                .build();
                    case 7:
                        return new SevenSegmentDisplay.Builder()
                                .withSegmentOne(!isSegmentOneBroken)
                                .withSegmentThree(!isSegmentThreeBroken)
                                .withSegmentSix(!isSegmentSixBroken)
                                .build();
                    case 8:
                        return new SevenSegmentDisplay.Builder()
                                .withSegmentOne(!isSegmentOneBroken)
                                .withSegmentTwo(!isSegmentTwoBroken)
                                .withSegmentThree(!isSegmentThreeBroken)
                                .withSegmentFour(!isSegmentFourBroken)
                                .withSegmentFive(!isSegmentFiveBroken)
                                .withSegmentSix(!isSegmentSixBroken)
                                .withSegmentSeven(!isSegmentSevenBroken)
                                .build();
                    case 9:
                        return new SevenSegmentDisplay.Builder()
                                .withSegmentOne(!isSegmentOneBroken)
                                .withSegmentTwo(!isSegmentTwoBroken)
                                .withSegmentThree(!isSegmentThreeBroken)
                                .withSegmentFour(!isSegmentFourBroken)
                                .withSegmentSix(!isSegmentSixBroken)
                                .build();
                    default:
                        throw new IllegalArgumentException("Can't build for " + i);
                }
            }
        }
    }
}
