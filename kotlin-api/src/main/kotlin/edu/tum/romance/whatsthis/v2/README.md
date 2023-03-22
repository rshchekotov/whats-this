# API v2
After this first version, that works formidably for the samples
we tested out by hand some major concerns came up, which I focused
on resolving in this version.

## Formal Concerns
Firstly the formal concerns regarding this change:
Why a `v2` package instead of a branch or tag?
Firstly, as I mentioned before, `v1` works, and for the cases
we used it for during testing, it works *well*.
I don't want the working version to be lost, so I intend to
keep both versions around and make it possible to switch
between them in application.
This has major benefits, one of the more significant ones
is that we can directly compare performance and accuracy
of both versions.

## Why is a v2 necessary?
Now that the formal concerns are out of the way, let's
get to the actual technical reason for this version:

The `v1` version of the API has a major flaw, in that
n-grams are supported by the tokenization algorithm, BUT
in all other parts of the code they were ignored.
This brings a huge avalanche of problems with it.
Starting from the fact that n-grams bring more unique
tokens to the table, which then implies that we'll run
out of index-space (32-bits) *very* quickly.
This issue can be resolved, but that would involve
reworking all existing data structures and algorithms
that we have in place - this is a huge undertaking
and is a lot simpler to achieve by starting from a =
clean slate.

## Goals of `v2`
Naturally `v2` should support all the features of `v1`,
but aside from that some new ones will also be added.

For the sake of 'accounting', here's a list of the major
features that `v2` will support (including existing ones
from `v1`):
- [ ] Tokenization
  - [ ] Unigram Support
  - [ ] N-Gram Support
- [ ] Token Manager (Data Structure*)
- [ ] Data Manager
- [ ] Vector Manager
- [ ] VectorSpace Manager

### Token Manager Data Structure
The token data structure is a major change from `v1`.
It is significantly more complex than a simple HashMap
or List.

Firstly I plan on deciding the operations such a 
data structure would require, then I would design
an interface for it and finally implement a concrete
class that implements that interface.
The interface would be there to allow for easy
switching between different data structures,
should we find that there's a better suitable one
for our needs.

I plan on using an octovigesimal tree, with 26 nodes
for the letters of the alphabet, 1 node for special
characters and the last node for special tokens.
Each level of said tree will correspond to a 'level
of n-gram'.
The idea of using such a tree is to grant a much
bigger index-space, which will allow us to store
a lot more tokens.
Given a huge database of text, we can easily expect
it to exceed 2 billion tokens (especially in the
case of n-grams), which is the maximum number of
tokens we can store given an array(-backed structure),
as those have 32-bit (signed int) indices.

Lastly, the leaves of that tree themselves will
be using TreeMaps to store the actual tokens.
[TreeMap](https://docs.oracle.com/javase/7/docs/api/java/util/TreeMap.html)s
are an implementation of a 
[Red-Black Tree](https://en.wikipedia.org/wiki/Red%E2%80%93black_tree),
which is wonderful for our purposes as it guarantees
fast insertion and look-up times.

As a last important note - this structure will exist
**twice**.
Once for the unigrams and once for the n-grams,
if n-grams have been enabled.
The unigram structure will use a TreeMap with the
text as an index and its hash as the value.
Then, the n-grams will reference the unigrams.
This is done as another optimization to save space.

As a simple example, imagine the following trigram `quick brown fox`.
On creation, it would first be inserted into the unigram-table:
```
hash(quick) -> quick
hash(brown) -> brown
hash(fox)   -> fox
```
Then, it would be inserted into the n-gram set:
```
hash(quick brown fox) -> [hash(quick), hash(2002), hash(7201)]
```

Being maps, the look-up for existing tokens is superfluous.
We can simply add the token to the map, as it doesn't
allow duplicate keys.

### Data Manager
The Data will still be stored in TextData<*> objects, but these
themselves will change.
From now on they do not store text, but provide methods to access
it. During the implementation of `v1` I noticed how WebSources
are rather slow to load compared to FileSources, hence I intend
to implement a local cache for web sources in a dedicated
user-folder, i.e. on Windows: `%AppData%\.whatsthis\cache`
on Linux and Mac: `~/.whatsthis/cache`.

Aside from not storing text (to save memory and off-load some
of it onto disk-space), the vectors will also not be stored
in the TextData<*> objects, but rather in the VectorManager.

The Data Manager is now exclusively responsible for managing
TextData<*> objects, while the VectorManager is responsible
for managing the vectors corresponding to those 
TextData<*> objects.

### Vector Manager
Thanks to the split of the Data Manager and the Vector Manager
into separate units, this one can now focus on dynamically
adjusting vectors as necessary.

In the new implementation I intend to use sparse vectors from
the get-go, as they are much more efficient than dense vectors
for our application - the vector manager will be responsible
to store the vectors and provide methods to access or manipulate
them.

On another note, in order to address the issue of multi-level
addressing, I intend to apply the concept of 'paging' and
virtual memory to the vector manager.
In particular, I want to implement a 'vector processing unit',
which will associate indices with paths in the n-gram tree.
This way, we can stil use a vector and do not need to implement
a variable size tensor.

This table will naturally have its limits, so what would
happen once it reaches the limit would be to off-load the
least used 20% of the pages to disk to allow for more
entries to be added.
Pages here, are a slightly different concept than in
the context of operating systems. Here, pages signify
a 'chunk' of the n-gram tree, which can be off-loaded
to disk, thus the concept of 'paging' has a directory-like
meaning.

The most expensive operation in the vector manager will
be a 'cache-miss', as it would include a disk read.

Perhaps, having a 'page'-index file would improve performance
in the sense that it would be reading a single file and knowing
which file to look into, instead of having to scan the entire
cache directory.

To visualize the problem and it's solution:

Currently, a tree may contain this data for a trigram model:
'quick brown fox'.
In the n-gram table, this would appear like:
```kt
ngrams['q']['b']['f'] += hash('quick brown fox') to arrayOf(hash('quick'), hash('brown'), hash('fox'))
```
This could be described as a file system with the following
structure:
```bash
echo "[hash('quick'), hash('brown'), hash('fox')]" > ngrams/q/b/f/$(hash 'quick brown fox')
```
Now, the issue with this is, to access the hash, in both cases you'd
need to know the path to the file, thus I'd introduce a concept
of a virtual address space, which would do something like:
```
hash('quick brown fox') -> q/b/f
```
So, whenever you find a vector with `hash('quick brown fox')` among the
indices, you could simply look up the path in the virtual address
space and read the file at that path, or if the path is already
in memory, read it from cache with an offset.

### VectorSpace Manager
The VectorSpace manager is intended to be similar to the current
`v1` implementation - some functionality, will once again
be extracted into interfaces to allow for more flexibility.

### API
The API is the centerpiece of this library - this time, it will
be instantiable and as a user you will need to specify some
parameters, such as the n-gram size and potentially things
like the cache size, in the future.

(this document is still a work in progress and may be adjusted
at any time)

- Doom (2023-03-22 4:10PM)