# Multi-Scale, Scale-Invariant Fingerprint for Finite Metric Spaces

A computational implementation of a multi-scale fingerprint system for characterizing finite metric spaces with provable stability properties.

## Overview

This project implements a scale-invariant fingerprint for finite point sets in Euclidean space. The fingerprint characterizes the geometric structure of objects across multiple scales and exhibits stability under common perturbations including:

- **Noise perturbation (jitter)**: Gaussian noise applied to point coordinates
- **Partial observability**: Random removal of points (simulating occlusion)
- **Outlier injection**: Addition of spurious points far from the original object

## Features

### Core Functionality

- **Object Construction**: Support for 2D, 3D, and arbitrary N-dimensional point sets
  - Canonical shapes: squares, regular polygons, cubes, tetrahedrons, spheres, hypercubes
  - Random point sets in hypercubes and hyperspheres
  - Line segments in arbitrary dimensions

- **Distance Computation**: Efficient computation of all pairwise Euclidean distances

- **Scale-Invariant Fingerprints**: 
  - Normalization by diameter ensures scale invariance
  - Multi-scale characterization across user-defined scale values
  - Multiple distance metrics (L1, L2, L∞) for comparing fingerprints

- **Perturbation Models**:
  - **Jitter**: Gaussian noise with controllable magnitude
  - **Partial Observability**: Random point removal with specified fraction
  - **Outliers**: Controlled injection of outlier points

### Reproducibility

All random processes use fixed seeds to ensure reproducibility across runs. The implementation is entirely self-contained with no external dependencies beyond JUnit for testing.

## Mathematical Model

### Fingerprint Definition

For a point set P with pairwise distances {d_ij}, we define:

1. **Diameter**: D = max{d_ij}
2. **Normalized distances**: d̃_ij = d_ij / D
3. **Fingerprint at scale t**: F(t) = fraction of pairs with d̃_ij ≤ t

The fingerprint is a function F: [0,1] → [0,1] that is:
- **Scale-invariant**: Scaling all points by λ doesn't change F
- **Monotonic**: F(t₁) ≤ F(t₂) for t₁ ≤ t₂
- **Normalized**: F(0) = 0, F(1) = 1

### Stability Properties

The fingerprint exhibits stability under perturbations:
- Small jitter → small fingerprint change
- Partial observation → bounded fingerprint change
- Limited outliers → controlled fingerprint change

## Installation and Building

### Prerequisites

- Java 11 or higher
- Maven 3.6+

### Building

```bash
# Compile the project
mvn compile

# Run tests
mvn test

# Create executable JAR
mvn package
```

## Usage

### Running the Main Examples

```bash
# Run all examples
mvn exec:java -Dexec.mainClass="edu.research.fingerprint.Main"

# Or after building:
java -jar target/multiscale-fingerprint-1.0.0.jar
```

### Example Code

```java
import edu.research.fingerprint.*;
import java.util.Random;

// Create a 3D cube
PointSet cube = ShapeFactory.createCube(1.0);

// Define scale values
double[] scales = {0.0, 0.25, 0.5, 0.75, 1.0};

// Compute clean fingerprint
Fingerprint cleanFP = Fingerprint.compute(cube, scales);

// Apply jitter and compute perturbed fingerprint
Random random = new Random(42);
PointSet jittered = cube.applyJitter(0.05, random);
Fingerprint jitteredFP = Fingerprint.compute(jittered, scales);

// Measure fingerprint stability
double distance = cleanFP.l2Distance(jitteredFP);
System.out.println("L2 distance: " + distance);

// Test partial observability
PointSet partial = cube.simulatePartialObservability(0.3, random);
Fingerprint partialFP = Fingerprint.compute(partial, scales);

// Test with outliers
PointSet withOutliers = cube.addOutliers(5, 2.0, random);
Fingerprint outlierFP = Fingerprint.compute(withOutliers, scales);
```

### Working with High-Dimensional Data

The implementation fully supports arbitrary dimensional spaces:

```java
// 5D random point set
Random random = new Random(42);
PointSet random5D = ShapeFactory.createRandomPointSet(30, 5, random);

// 10D random point set
PointSet random10D = ShapeFactory.createRandomPointSet(25, 10, random);

// 4D hypercube
PointSet hypercube4D = ShapeFactory.createHypercube(4, 1.0);

// All standard operations work in any dimension
Fingerprint fp = Fingerprint.compute(random10D, scales);
PointSet jittered10D = random10D.applyJitter(0.05, random);
```

## Project Structure

```
src/
├── main/java/edu/research/fingerprint/
│   ├── PointSet.java        # Point set representation and operations
│   ├── Fingerprint.java     # Fingerprint computation and comparison
│   ├── ShapeFactory.java    # Factory for creating geometric shapes
│   └── Main.java            # Demonstration program
└── test/java/edu/research/fingerprint/
    ├── PointSetTest.java    # Tests for PointSet
    ├── FingerprintTest.java # Tests for Fingerprint
    └── ShapeFactoryTest.java # Tests for ShapeFactory
```

## Testing

The project includes comprehensive unit tests covering:

- Point set creation in 2D, 3D, and high dimensions
- Distance computations
- Fingerprint computation and properties
- Scale invariance
- Stability under jitter, partial observability, and outliers
- Shape factory methods for all dimensions

Run tests with:
```bash
mvn test
```

## Implementation Details

### Key Design Decisions

1. **Immutability**: PointSet operations return new instances rather than modifying in place
2. **Dimension Independence**: All core algorithms work in arbitrary dimensions
3. **Reproducibility**: Fixed random seeds throughout for deterministic results
4. **Efficiency**: Pairwise distances computed once and reused
5. **Validation**: Input validation prevents common errors

### Computational Complexity

- Distance computation: O(n²) where n is the number of points
- Fingerprint computation: O(n² × k) where k is the number of scales
- Memory: O(n²) for storing pairwise distances

## Research Context

This implementation supports research on:
- Geometric shape analysis
- Object recognition under occlusion
- Robust descriptor design
- Multi-scale geometric characterization
- Metric space theory

## Data Sources

All geometric data are synthetically generated. No external data dependencies are required. The code can also process data from external sources such as the SNAP repository for network data analysis.

## Contributing

This is a research project. For questions or contributions, please contact the repository owner.

## License

This project is provided for research and educational purposes.

## Citation

If you use this code in your research, please cite:
```
A Multi-Scale, Scale-Invariant Fingerprint for Finite Metric Spaces 
with Provable Stability and Computational Implementation
```