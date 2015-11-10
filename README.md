# Futile

Futile is a lightweight utility library for Java, aiming to add support for functional processing of Iterables. It
offers static utilities methods to work on Iterables, as well as an Iterable wrapper that can be used to build
processing pipelines from Iterables.

## Supported operations

+ each
+ filter
+ map
+ flatMap
+ fold
+ zip
+ groupBy

## Usage Example

### Static API

```java
List<String> result = Futile.filter(Arrays.asList("foo", "bar", "far", "baz"), 
                                    new Predicate<String>() {
                                        @Override
                                        public boolean apply(String it) {
                                            return it.contains("r");
                                        }
                                    });

```

### Wrapper API
```java
List<Integer> result = Futile.from(Arrays.asList(1, 2, 3))
                                .map(new Function1<Integer, Integer>() {
                                    @Override
                                    public Integer apply(Integer it) {
                                        return it + 2;
                                    })
                                .toLinkedList();
```


## Licence

    Copyright 2015 Thibaut Lorrain

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.