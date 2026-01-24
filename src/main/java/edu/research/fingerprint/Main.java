package edu.research.fingerprint;

import java.util.Random;

/**
 * Main class demonstrating the multi-scale fingerprint system.
 * Includes examples of all functionality: clean fingerprints, jitter, partial observability, and outliers.
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("Multi-Scale Fingerprint System");
        System.out.println("A Scale-Invariant Fingerprint for Finite Metric Spaces");
        System.out.println("===========================================\n");
        
        // Fixed random seed for reproducibility
        long seed = 42;
        Random random = new Random(seed);
        
        // Define scale values for fingerprint computation
        double[] scales = createScaleValues(20);
        
        System.out.println("Scale values: " + formatArray(scales) + "\n");
        
        // Run experiments for different shapes and conditions
        runExperiments2D(scales, random);
        runExperiments3D(scales, random);
        runExperimentsHighDimensional(scales, random);
        
        System.out.println("\n===========================================");
        System.out.println("All experiments completed successfully!");
        System.out.println("===========================================");
    }
    
    /**
     * Creates an array of scale values uniformly distributed in [0, 1].
     */
    private static double[] createScaleValues(int numScales) {
        double[] scales = new double[numScales];
        for (int i = 0; i < numScales; i++) {
            scales[i] = (double) i / (numScales - 1);
        }
        return scales;
    }
    
    /**
     * Runs experiments on 2D shapes.
     */
    private static void runExperiments2D(double[] scales, Random random) {
        System.out.println("========== 2D Shape Experiments ==========\n");
        
        // Triangle
        PointSet triangle = ShapeFactory.createRegularPolygon(3, 1.0);
        runExperimentSet("Triangle (2D)", triangle, scales, random);
        
        // Square
        PointSet square = ShapeFactory.createSquare(1.0);
        runExperimentSet("Square (2D)", square, scales, random);
        
        // Pentagon
        PointSet pentagon = ShapeFactory.createRegularPolygon(5, 1.0);
        runExperimentSet("Pentagon (2D)", pentagon, scales, random);
        
        // Random 2D point set
        PointSet random2D = ShapeFactory.createRandomPointSet(20, 2, new Random(random.nextLong()));
        runExperimentSet("Random 2D (20 points)", random2D, scales, random);
    }
    
    /**
     * Runs experiments on 3D shapes.
     */
    private static void runExperiments3D(double[] scales, Random random) {
        System.out.println("\n========== 3D Shape Experiments ==========\n");
        
        // Tetrahedron
        PointSet tetrahedron = ShapeFactory.createTetrahedron(1.0);
        runExperimentSet("Tetrahedron (3D)", tetrahedron, scales, random);
        
        // Cube
        PointSet cube = ShapeFactory.createCube(1.0);
        runExperimentSet("Cube (3D)", cube, scales, random);
        
        // Sphere surface
        PointSet sphere = ShapeFactory.createSphereSurface(50, 1.0);
        runExperimentSet("Sphere Surface (3D, 50 points)", sphere, scales, random);
        
        // Random 3D point set
        PointSet random3D = ShapeFactory.createRandomPointSet(30, 3, new Random(random.nextLong()));
        runExperimentSet("Random 3D (30 points)", random3D, scales, random);
    }
    
    /**
     * Runs experiments on high-dimensional spaces.
     */
    private static void runExperimentsHighDimensional(double[] scales, Random random) {
        System.out.println("\n========== High-Dimensional Experiments ==========\n");
        
        // 4D hypercube
        PointSet hypercube4D = ShapeFactory.createHypercube(4, 1.0);
        runExperimentSet("4D Hypercube", hypercube4D, scales, random);
        
        // 5D random point set
        PointSet random5D = ShapeFactory.createRandomPointSet(25, 5, new Random(random.nextLong()));
        runExperimentSet("Random 5D (25 points)", random5D, scales, random);
        
        // 10D random point set
        PointSet random10D = ShapeFactory.createRandomPointSet(20, 10, new Random(random.nextLong()));
        runExperimentSet("Random 10D (20 points)", random10D, scales, random);
        
        // Line segment in 7D
        PointSet line7D = ShapeFactory.createLineSegment(15, 7, 1.0);
        runExperimentSet("Line Segment in 7D (15 points)", line7D, scales, random);
    }
    
    /**
     * Runs a complete set of experiments for a given shape.
     * Tests: clean, jitter, partial observability, and outliers.
     */
    private static void runExperimentSet(String name, PointSet pointSet, double[] scales, Random random) {
        System.out.println("--- " + name + " ---");
        System.out.println(pointSet);
        System.out.println("Diameter: " + String.format("%.4f", pointSet.computeDiameter()));
        
        // Compute clean fingerprint
        Fingerprint cleanFP = Fingerprint.compute(pointSet, scales);
        System.out.println("\nClean Fingerprint (first 5 scales):");
        printFirstNScales(cleanFP, 5);
        
        // Test with jitter
        double noiseMagnitude = 0.05;
        PointSet jitteredSet = pointSet.applyJitter(noiseMagnitude, new Random(random.nextLong()));
        Fingerprint jitteredFP = Fingerprint.compute(jitteredSet, scales);
        double jitterDistance = cleanFP.l2Distance(jitteredFP);
        System.out.println("\nJittered (noise=" + noiseMagnitude + "):");
        System.out.println("  L2 distance from clean: " + String.format("%.6f", jitterDistance));
        
        // Test with partial observability
        double removalFraction = 0.3;
        PointSet partialSet = pointSet.simulatePartialObservability(removalFraction, new Random(random.nextLong()));
        Fingerprint partialFP = Fingerprint.compute(partialSet, scales);
        double partialDistance = cleanFP.l2Distance(partialFP);
        System.out.println("\nPartial Observability (removed " + (int)(removalFraction * 100) + "%):");
        System.out.println("  Remaining points: " + partialSet.size() + " / " + pointSet.size());
        System.out.println("  L2 distance from clean: " + String.format("%.6f", partialDistance));
        
        // Test with outliers
        int numOutliers = Math.max(1, pointSet.size() / 5);
        double outlierScale = 2.0;
        PointSet outlierSet = pointSet.addOutliers(numOutliers, outlierScale, new Random(random.nextLong()));
        Fingerprint outlierFP = Fingerprint.compute(outlierSet, scales);
        double outlierDistance = cleanFP.l2Distance(outlierFP);
        System.out.println("\nWith Outliers (" + numOutliers + " outliers, scale=" + outlierScale + "):");
        System.out.println("  Total points: " + outlierSet.size());
        System.out.println("  L2 distance from clean: " + String.format("%.6f", outlierDistance));
        
        System.out.println();
    }
    
    /**
     * Prints the first N scales of a fingerprint.
     */
    private static void printFirstNScales(Fingerprint fp, int n) {
        double[] scales = fp.getScales();
        double[] values = fp.getValues();
        for (int i = 0; i < Math.min(n, scales.length); i++) {
            System.out.println(String.format("  scale=%.4f, value=%.4f", scales[i], values[i]));
        }
        if (n < scales.length) {
            System.out.println("  ...");
        }
    }
    
    /**
     * Formats an array for compact display.
     */
    private static String formatArray(double[] arr) {
        if (arr.length <= 5) {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < arr.length; i++) {
                if (i > 0) sb.append(", ");
                sb.append(String.format("%.2f", arr[i]));
            }
            sb.append("]");
            return sb.toString();
        } else {
            return String.format("[%.2f, %.2f, ..., %.2f, %.2f] (%d values)", 
                arr[0], arr[1], arr[arr.length-2], arr[arr.length-1], arr.length);
        }
    }
}
