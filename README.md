# Data Structures (Java + Gradle)

## Overview
This repository contains my solutions for the five tasks:
- a custom linked list
- a generic deque
- a binary search tree (BST)
- a generic hash table using separate chaining with my linked list
- an experiment that evaluates the hash function quality for vehicle registration numbers using the hash table from task 4

## Prerequisites
- Java 21 or higher
- Gradle 7.6 or higher

## Build and test
To build the project and run tests, use the following command:
```
./gradlew clean build
./gradlew clean test
```

## Run the Vehicle experiment (Task 5)
Run the experiment with N vehicles and optional CSV output
- args[0] = number of unique vehicles to insert
- args[1] = path to CSV file ("0" to skip)
```
./gradlew run -PmainClass=app.ExperimentVehicles --args="20000 buckets.csv"
```
Example without CSV export
```
./gradlew run -PmainClass=app.ExperimentVehicles --args="20000 0"
```

## Experiment results
Run A
```
Vehicles (unique): 15000
Capacity (buckets): 32768
Load factor:        0.457763671875
Collisions (put):   2924
Non-empty buckets:  12076 / 32768
Max chain length:   5
Avg chain length:   0.458
Stddev chain len:   0.673
```
Run B
```
Vehicles (unique): 20000
Capacity (buckets): 32768
Load factor:        0.6103515625
Collisions (put):   5004
Non-empty buckets:  14996 / 32768
Max chain length:   6
Avg chain length:   0.610
Stddev chain len:   0.779
```

## Interpretation of results
- Load factor stays under 0.75 (by design), so average chain length is modest (load factor).

- Non-empty buckets: 12–15k non-empty out of 32,768 shows a broad spread; not clustered.

- Max chain length of 5–6 is low for these sizes, suggesting good distribution with few long chains.

- Stddev / Avg ratio is reasonable ( 1.1–1.3 here), indicating no strong skew; the csv-file confirms distribution is close to Poisson-like for these loads.

- Collisions increase with n as expected; absolute counts are less important than how they grow vs. load factor. For the given hashCode() (Java’s String.hashCode() on normalized plates), behavior is solid.




