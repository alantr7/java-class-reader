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

### Annotations
There are 2 methods to get annotation data.<br>
This first method only partially loads the annotations. It allows you to find out the type and the path of an annotation and its parameters.
```java
AnnotationInfo[] annotationInfos = component.getAnnotationInfos();
```

This method fully loads the annotations. Make sure all the annotations, together with their parameter values are accessible in the class path, otherwise this method won't work.
```java
Annotation[] annotations = component.getAnnotations();
```
