# sefh_code

## FingerprintCore - N-Dimensional Metric Space Analysis

A Java implementation for modeling and analyzing high-dimensional metric spaces. Supports points of arbitrary dimensionality (2D, 3D, and beyond) with operations for computing pairwise distances and diameter.

### Features

- **N-Dimensional Point Support**: Handle points in 2D, 3D, or any higher dimensional space
- **Dimension Validation**: Ensures all points in a collection have consistent dimensions
- **Euclidean Distance Computation**: Calculates pairwise distances using the general N-dimensional Euclidean distance formula
- **Diameter Calculation**: Finds the maximum distance between any two points in the collection
- **Type Safety**: Validates input and provides clear error messages

### Installation

No external dependencies required beyond JUnit for testing.

### Usage

#### Basic Example (2D Points)

```java
FingerprintCore core = new FingerprintCore();

// Add 2D points
core.addPoint(new double[]{0.0, 0.0});
core.addPoint(new double[]{3.0, 4.0});

// Compute pairwise distances
double[][] distances = core.computePairwiseDistances();
System.out.println(distances[0][1]);  // Output: 5.0

// Compute diameter
double diameter = core.computeDiameter();
System.out.println(diameter);  // Output: 5.0
```

#### 3D Points

```java
FingerprintCore core = new FingerprintCore();

// Add 3D points
core.addPoint(new double[]{0.0, 0.0, 0.0});
core.addPoint(new double[]{1.0, 1.0, 1.0});
core.addPoint(new double[]{2.0, 2.0, 2.0});

System.out.println(core.getDimension());  // Output: 3
System.out.println(core.computeDiameter());  // Output: 3.464... (sqrt(12))
```

#### High-Dimensional Points

```java
FingerprintCore core = new FingerprintCore();

// Add 5D points
core.addPoint(new double[]{0.0, 0.0, 0.0, 0.0, 0.0});
core.addPoint(new double[]{1.0, 1.0, 1.0, 1.0, 1.0});

double[][] distances = core.computePairwiseDistances();
System.out.println(distances[0][1]);  // Output: 2.236... (sqrt(5))
```

### API Reference

#### `FingerprintCore()`

Creates a new FingerprintCore instance with an empty point collection.

#### `addPoint(double[] point)`

Adds an N-dimensional point to the collection.

- **Parameters**: `point` - An array of coordinates
- **Throws**: 
  - `IllegalArgumentException` if point dimension doesn't match existing points
  - `IllegalArgumentException` if point is null or empty

#### `computePairwiseDistances()`

Computes pairwise Euclidean distances between all points.

- **Returns**: A symmetric 2D array where element [i][j] is the distance between points i and j

#### `computeDiameter()`

Computes the diameter (maximum distance) of the point set.

- **Returns**: Maximum pairwise distance, or 0.0 if fewer than 2 points

#### `getPoints()`

Returns a copy of all points in the collection.

#### `getDimension()`

Returns the dimension of points in the collection, or null if empty.

#### `clear()`

Removes all points from the collection.

### Building and Running

#### Compile the code:
```bash
javac FingerprintCore.java
```

#### Run the demo:
```bash
javac FingerprintCoreDemo.java FingerprintCore.java
java FingerprintCoreDemo
```

#### Run tests (requires JUnit):
```bash
javac -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar FingerprintCoreTest.java FingerprintCore.java
java -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore FingerprintCoreTest
```

### Implementation Details

- **Distance Formula**: Uses the general N-dimensional Euclidean distance formula:
  ```
  d(p, q) = sqrt(sum((p_i - q_i)^2 for i in 1..N))
  ```
- **Validation**: All points must have the same dimensionality within a single FingerprintCore instance
- **Immutability**: Points are copied on input and output to prevent external modifications

### Mathematical Background

The implementation models metric spaces where:
- Points exist in N-dimensional Euclidean space
- Distance satisfies the metric axioms (non-negativity, symmetry, triangle inequality)
- Diameter represents the extent of the point cloud in the metric space

### License

This project is part of the sefh_code repository.