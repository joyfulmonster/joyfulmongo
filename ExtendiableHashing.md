# Requirements

Implement an in-memory hashmap that put and read Key Value pairs.  It should be highly scalable and safe.

# Analysis

## Components of HashMap

HashMap is a well known data structure that provides O(1) read/write performance.  There are three main components for a hashmap design:

* hash function
* collision resolution
* storage resizing

## Standard Implemenations

Java JDK provides three implemenations:

* java.util.HashMap
* java.util.Hashtable
* java.util.concurrent.ConcurrentHashMap

### Issues with these implemenations

#### Safety

* java.util.HashMap is not thread safe.  It can not be directly used in multiple threading scenario without external coordination.
* java.util.Hashtable is thread safe implemenation.  It provides Object wide lock when performing put/remove.  
* java.util.concurrent.ConcurrentHashMap is thread safe implementation and provides better concurrency.  It takes a parameter to configure the number of Segments when constructing the object.  Each Segment is concurrent access unit, will be locked during put/remove operation.  The get operation is unblocked.

#### Scalability and Availability

In the situation that there are huge amount of data and highly concurrent access, the above implementation may suffer from the following perspectives:

* java.util.Hashtable may suffers from two sides
** it uses global locker
** it uses dynamic resizing that doubles the memory storage whenever load factor is reached.   Dynamic resizing may cause two trouble
*** it needs to copy the whole old array into the new array, during the copy the whole table will be locked and not available to write.
*** memory waste.  The memory size is doubled from previous size for every resizing, the bigger the data set is, the more extra memory is needed during resizing.
* java.util.concurrent.ConcurrentHashMap has a static configuration of the number of Segments.  Each Segment is a sub hashtable and will also do dynamic resizing.   It needs a very careful upfront planning.

# Design 

There are a lot methmatical research on how to build a high efficient evenly distributed hash function, I will not touch that.  The focus of this design is to provide a implementation that meet following goals by tweaking the hashmap stoage resizing mechanism.

* dynamically scalable
* highly concurrent and thread safe
* highly available

The idea is to leverage the extendiable hashing algorithm (https://en.wikipedia.org/wiki/Extendible_hashing) to manage a collection of small buckets to provide:

1. fine-grained parallel locking 
2. incremental resizing

## Extendiable Hashing

Extendible hashing uses a Directory to manage a list of Buckets.  A Directory consists of an array of pointers to Buckets.  Its size must be in a power of 2 value.  The array index maps to the lower bits of the hashcode.  The number of bits called the depth of  bucket.  The actual data are stored on one of the buckets.   One bucket is a small hashtable.  When a Bucket is overflow, a Split needs to be done to resize the storage.

### Operation Steps

The following is the steps of put(K, V) operation:

```
1. get hashcode of K and apply hash function to the hashcode to ensure even distribution
2. ask Directory for the Bucket maps to hashcode
3. lock the Bucket
4. try to put K,V to the Bucket
4.1  if Bucket is overflow do the Bucket split
4.1.1  Allocate two new buckets.  Typically a bucket has small memory footprint, the allocation can be easily satisfied
4.1.2  Mark the old bucket invalid and disallow further write
4.1.3  Spread the old bucket entries into the two new buckets
4.1.4  put the new pair (K,V) into the one of the two new buckets.
4.1.5  Lock the Directory
4.1.6  If the new bucket local depth outgrow the depth of the Directory, then double the Directory size and rewire the existing bucket pointers to teh new Directory.
4.1.7  Register the two new buckets.
4.1.8  Unlock the Directory
5. unlock the Bucket
```

The following is the steps of get(K) operation:

```
1. get hashcode of K and apply hash function to the hashcode to ensure even distribution
2. ask Directory for the Bucket maps to hashcode
3. get entry of K from bucket
```

The following is the steps of remove(K) operation:

```
1. get hashcode of K and apply hash function to the hashcode to ensure even distribution
2. ask Directory for the Bucket maps to hashcode
3. lock the Bucket
4. try to find the entry of K
4.1 if found, set the value of K to null
4.2 if not found, ignore the operation
5. unlock the Bucket
```

# Implementation Details

*This implementation packaged in joyfulmonster.zip focus on implementing the core algorithm of the extendible hashing.  The org.joyfulmonster.util.ConcurrentExtendiableHashMap API definition was borrowed from java.util.concurrent.ConcurrentMap, but I didn't try to do full implementation of ConcurrentMap*

## package structure

Source Code:

src\main\java\org\joyfulmonster\util\ConcurrentExtendiableHashMap.java
src\main\java\org\joyfulmonster\util\internal\ConcurrentExtendiableHashMapImpl.java
src\main\java\org\joyfulmonster\util\internal\Bucket.java
src\main\java\org\joyfulmonster\util\internal\BucketFactory.java
src\main\java\org\joyfulmonster\util\internal\BucketOverflowError.java
src\main\java\org\joyfulmonster\util\internal\Directory.java
src\main\java\org\joyfulmonster\util\internal\HashEntry.java
src\main\java\org\joyfulmonster\util\internal\HashStrategy.java
src\main\java\org\joyfulmonster\util\internal\LinearProbingBucketImpl.java

Test Code:

src\test\java\org\joyfulmonster\util\BasicTest.java
src\test\java\org\joyfulmonster\util\ConcurrencyTest.java
src\test\java\org\joyfulmonster\util\RandomStringSet.java

# Future Improvement
