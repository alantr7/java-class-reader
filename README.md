# Lightweight .class parser
Allows you to easily read and parse a Java class, without actually loading it.

```java

// Reads a Java class file at the specified path
ClassReader reader = new ClassReader(new FileInputStream(new File("PATH TO YOUR FILE")));
RawClass rawClass = reader.read();

// Wraps the raw class (loads names, signatures, descriptors, etc. for fields, methods, etc.)
ClassWrapper wrapper = ClassWrapper.newBuilder().build();

// Get class information
ClassInfo klass = wrapper.wrap(rawClass);

// Get all fields in the loaded class
Field[] fields = klass.getFields();

// Get all methods in the loaded class
Method[] methods = klass.getMethods();
```

For more details please check the wiki:
https://github.com/alantr7/java-class-reader/wiki
