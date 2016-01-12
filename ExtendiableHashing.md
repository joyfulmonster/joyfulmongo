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

There are a lot methmatical research on how to build a high efficient evenly distributed hash function, I will not touch that.  The focus of this design is to provide a implementation that meet following goals by tweaking the hashmap stoage resizing mechansim.

* dynamically scalable
* highly concurrent and thread safe
* highly available

## Extendiable Hashing

Extendible hashing uses a Directory to manage a list of Buckets.  A Directory consists of an array of pointers to Buckets.  Its size must be in a power of 2 value.  The array index maps to the lower bits of the hashcode.  The number of bits called the depth of  bucket.  The actual data are stored on one of the buckets.   One bucket is a small hashtable.

When a Bucket is overflow, a Split needs to be done to resize the storage.  The split operations do the following:

* Allocate two new buckets.  Typically a bucket has small memory footprint, the allocation can be easily satisfied.
* Mark the old bucket invalid and disallow further write
** Spread the old bucket entries into the two new buckets.
** Lock the Directory
*** If the new bucket local depth outgrow the depth of the Directory, then double the Directory size and rewire the existing bucket pointers to teh new Directory.
*** Register the two new buckets.
** Unlock the Directory



# Implementation Details

# Future
