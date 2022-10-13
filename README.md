# Lightweight .class parser
Allows you to easily read and parse a Java class file, without loading the class.

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
```java

for (Field field : fields) {
  
  // Gets information about the annotations, including types and parameters
  AnnotationInfo[] annotationInfos = field.getAnnotationInfos();
  
  // This method loads and creates instance of all the annotations
  // NOTE: Make sure all the annotations and their parameter values are accessible in the class path,
  //       otherwise this method would not work
  Annotation[] annotations = field.getAnnotations();
  
  // Get a specific annotation
  Deprecated deprecated = field.getAnnotation(Deprecated.class);
  boolean forRemoval = deprecated.forRemoval();
  
}
