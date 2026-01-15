/**
 * Demo program for FingerprintCore class.
 * 
 * Demonstrates N-dimensional point support with examples in 2D, 3D, and higher dimensions.
 */
public class FingerprintCoreDemo {
    
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("FingerprintCore - N-Dimensional Point Support Demo");
        System.out.println("============================================================");
        System.out.println();
        
        demo2DPoints();
        demo3DPoints();
        demoHighDimensional();
        demoDimensionValidation();
        
        System.out.println("============================================================");
        System.out.println("All demonstrations completed successfully!");
        System.out.println("============================================================");
    }
    
    /**
     * Demonstrate 2D point operations.
     */
    private static void demo2DPoints() {
        System.out.println("============================================================");
        System.out.println("2D Point Operations (Backward Compatibility)");
        System.out.println("============================================================");
        
        FingerprintCore core = new FingerprintCore();
        
        // Add 2D points
        double[][] points2D = {
            {0.0, 0.0},
            {3.0, 4.0},
            {6.0, 0.0}
        };
        
        for (double[] point : points2D) {
            core.addPoint(point);
            System.out.println("Added point: " + arrayToString(point));
        }
        
        System.out.println("\nDimension: " + core.getDimension() + "D");
        System.out.println("Number of points: " + core.getPoints().size());
        
        // Compute pairwise distances
        double[][] distances = core.computePairwiseDistances();
        System.out.println("\nPairwise distances:");
        for (int i = 0; i < distances.length; i++) {
            System.out.print("  From point " + i + ": [");
            for (int j = 0; j < distances[i].length; j++) {
                System.out.printf("%.2f", distances[i][j]);
                if (j < distances[i].length - 1) System.out.print(", ");
            }
            System.out.println("]");
        }
        
        // Compute diameter
        double diameter = core.computeDiameter();
        System.out.printf("\nDiameter (maximum distance): %.2f\n", diameter);
        System.out.println();
    }
    
    /**
     * Demonstrate 3D point operations.
     */
    private static void demo3DPoints() {
        System.out.println("============================================================");
        System.out.println("3D Point Operations");
        System.out.println("============================================================");
        
        FingerprintCore core = new FingerprintCore();
        
        // Add 3D points (corners of a cube)
        double[][] points3D = {
            {0.0, 0.0, 0.0},
            {1.0, 0.0, 0.0},
            {0.0, 1.0, 0.0},
            {1.0, 1.0, 1.0}
        };
        
        for (double[] point : points3D) {
            core.addPoint(point);
            System.out.println("Added point: " + arrayToString(point));
        }
        
        System.out.println("\nDimension: " + core.getDimension() + "D");
        System.out.println("Number of points: " + core.getPoints().size());
        
        // Compute pairwise distances
        double[][] distances = core.computePairwiseDistances();
        System.out.println("\nPairwise distances:");
        for (int i = 0; i < distances.length; i++) {
            System.out.print("  From point " + i + ": [");
            for (int j = 0; j < distances[i].length; j++) {
                System.out.printf("%.2f", distances[i][j]);
                if (j < distances[i].length - 1) System.out.print(", ");
            }
            System.out.println("]");
        }
        
        // Compute diameter
        double diameter = core.computeDiameter();
        System.out.printf("\nDiameter (maximum distance): %.2f\n", diameter);
        System.out.printf("Expected: %.2f (distance from (0,0,0) to (1,1,1))\n", Math.sqrt(3));
        System.out.println();
    }
    
    /**
     * Demonstrate high-dimensional point operations.
     */
    private static void demoHighDimensional() {
        System.out.println("============================================================");
        System.out.println("High-Dimensional Point Operations (5D)");
        System.out.println("============================================================");
        
        FingerprintCore core = new FingerprintCore();
        
        // Add 5D points
        double[][] points5D = {
            {0.0, 0.0, 0.0, 0.0, 0.0},
            {1.0, 1.0, 1.0, 1.0, 1.0},
            {2.0, 2.0, 2.0, 2.0, 2.0}
        };
        
        for (double[] point : points5D) {
            core.addPoint(point);
            System.out.println("Added point: " + arrayToString(point));
        }
        
        System.out.println("\nDimension: " + core.getDimension() + "D");
        System.out.println("Number of points: " + core.getPoints().size());
        
        // Compute pairwise distances
        double[][] distances = core.computePairwiseDistances();
        System.out.println("\nPairwise distances:");
        for (int i = 0; i < distances.length; i++) {
            System.out.print("  From point " + i + ": [");
            for (int j = 0; j < distances[i].length; j++) {
                System.out.printf("%.2f", distances[i][j]);
                if (j < distances[i].length - 1) System.out.print(", ");
            }
            System.out.println("]");
        }
        
        // Compute diameter
        double diameter = core.computeDiameter();
        System.out.printf("\nDiameter (maximum distance): %.2f\n", diameter);
        System.out.printf("Expected: %.2f (distance from origin to (2,2,2,2,2))\n", Math.sqrt(20));
        System.out.println();
    }
    
    /**
     * Demonstrate dimension validation.
     */
    private static void demoDimensionValidation() {
        System.out.println("============================================================");
        System.out.println("Dimension Validation");
        System.out.println("============================================================");
        
        FingerprintCore core = new FingerprintCore();
        
        // Add a 2D point
        core.addPoint(new double[]{1.0, 2.0});
        System.out.println("Added 2D point: (1.0, 2.0)");
        System.out.println("Current dimension: " + core.getDimension() + "D");
        
        // Try to add a 3D point (should fail)
        try {
            core.addPoint(new double[]{3.0, 4.0, 5.0});
            System.out.println("ERROR: Should have raised IllegalArgumentException!");
        } catch (IllegalArgumentException e) {
            System.out.println("\n✓ Correctly rejected 3D point with error:");
            System.out.println("  " + e.getMessage());
        }
        
        // Add another 2D point (should succeed)
        core.addPoint(new double[]{6.0, 7.0});
        System.out.println("\n✓ Successfully added another 2D point: (6.0, 7.0)");
        System.out.println("Total points: " + core.getPoints().size());
        System.out.println();
    }
    
    /**
     * Helper method to convert array to string representation.
     */
    private static String arrayToString(double[] arr) {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
