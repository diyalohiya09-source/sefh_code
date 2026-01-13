# sefh_code

## FingerprintCore - N-Dimensional Metric Space Analysis

A Python implementation for modeling and analyzing high-dimensional metric spaces. Supports points of arbitrary dimensionality (2D, 3D, and beyond) with operations for computing pairwise distances and diameter.

### Features

- **N-Dimensional Point Support**: Handle points in 2D, 3D, or any higher dimensional space
- **Dimension Validation**: Ensures all points in a collection have consistent dimensions
- **Euclidean Distance Computation**: Calculates pairwise distances using the general N-dimensional Euclidean distance formula
- **Diameter Calculation**: Finds the maximum distance between any two points in the collection
- **Type Safety**: Validates input types and provides clear error messages

### Installation

No external dependencies required. Uses only Python standard library.

### Usage

#### Basic Example (2D Points)

```python
from fingerprint_core import FingerprintCore

# Create a new instance
core = FingerprintCore()

# Add 2D points
core.addPoint((0.0, 0.0))
core.addPoint((3.0, 4.0))

# Compute pairwise distances
distances = core.computePairwiseDistances()
print(distances[0][1])  # Output: 5.0

# Compute diameter
diameter = core.computeDiameter()
print(diameter)  # Output: 5.0
```

#### 3D Points

```python
core = FingerprintCore()

# Add 3D points
core.addPoint((0.0, 0.0, 0.0))
core.addPoint((1.0, 1.0, 1.0))
core.addPoint((2.0, 2.0, 2.0))

print(core.getDimension())  # Output: 3
print(core.computeDiameter())  # Output: 3.464... (sqrt(12))
```

#### High-Dimensional Points

```python
core = FingerprintCore()

# Add 5D points
core.addPoint((0.0, 0.0, 0.0, 0.0, 0.0))
core.addPoint((1.0, 1.0, 1.0, 1.0, 1.0))

distances = core.computePairwiseDistances()
print(distances[0][1])  # Output: 2.236... (sqrt(5))
```

### API Reference

#### `FingerprintCore()`

Creates a new FingerprintCore instance with an empty point collection.

#### `addPoint(point: Tuple[float, ...]) -> None`

Adds an N-dimensional point to the collection.

- **Parameters**: `point` - A tuple or list of coordinates
- **Raises**: 
  - `ValueError` if point dimension doesn't match existing points
  - `TypeError` if point is not a tuple or list

#### `computePairwiseDistances() -> List[List[float]]`

Computes pairwise Euclidean distances between all points.

- **Returns**: A symmetric 2D matrix where element [i][j] is the distance between points i and j

#### `computeDiameter() -> float`

Computes the diameter (maximum distance) of the point set.

- **Returns**: Maximum pairwise distance, or 0.0 if fewer than 2 points

#### `getPoints() -> List[Tuple[float, ...]]`

Returns a copy of all points in the collection.

#### `getDimension() -> Optional[int]`

Returns the dimension of points in the collection, or None if empty.

#### `clear() -> None`

Removes all points from the collection.

### Running Tests

```bash
python -m unittest test_fingerprint_core.py -v
```

### Running Demo

```bash
python demo.py
```

### Implementation Details

- **Distance Formula**: Uses the general N-dimensional Euclidean distance formula:
  ```
  d(p, q) = sqrt(sum((p_i - q_i)^2 for i in 1..N))
  ```
- **Validation**: All points must have the same dimensionality within a single FingerprintCore instance
- **Type Conversion**: Lists are automatically converted to tuples for internal consistency

### Mathematical Background

The implementation models metric spaces where:
- Points exist in N-dimensional Euclidean space
- Distance satisfies the metric axioms (non-negativity, symmetry, triangle inequality)
- Diameter represents the extent of the point cloud in the metric space

### License

This project is part of the sefh_code repository.