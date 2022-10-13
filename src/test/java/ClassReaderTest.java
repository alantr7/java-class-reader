import com.alant7_.classreader.ClassReader;
import com.alant7_.classreader.ClassWrapper;
import com.alant7_.classreader.io.ComponentType;
import com.alant7_.classreader.io.RawClass;
import com.alant7_.classreader.wrapper.component.type.Type;
import com.alant7_.classreader.wrapper.component.type.TypeImpl;
import com.alant7_.classreader.wrapper.enums.Attribute;
import com.alant7_.classreader.wrapper.enums.Filter;
import com.alant7_.classreader.wrapper.util.Array;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ClassReaderTest {

    private RawClass rawClass;

    @Before
    public void setupReader() {
        ClassReader reader = new ClassReader(new File("C:\\Users\\Alan\\Desktop\\TestingClass.class"));
        rawClass = reader.read();

        var array = Array.of(List.of("a", "b"), List.of("c", "d"));
        System.out.println(Arrays.toString(array.getArray()));
    }

    @Test
    public void wrapTest() {

        var klass = ClassWrapper.newBuilder()
                .filter(ComponentType.METHOD, Filter.ANNOTATIONS, Deprecated.class)
                .filter(ComponentType.METHOD, Filter.ANNOTATIONS, SuppressWarnings.class)
                .build()
                .wrap(rawClass);

        for (var field : klass.getFields()) {
            System.out.println(
                    "Field: " + field.getName()
                            + ", Type: " + field.getType().getSimpleName()
                            + ", Flags: " + Arrays.toString(field.getAccessFlags())
                            + ", Attributes: " + Arrays.toString(field.getAttributeTypes())
                            + ", Annotations: " + Arrays.toString(field.getAnnotationInfos())
            );

            for (var annotation : field.getAnnotations()) {
                System.out.println("Annotation: " + annotation);

            }
        }

        for (var method : klass.getMethods()) {
            var types = method.getParameterTypes();
            System.out.println(
                    "Method: " + method.getName() +
                            ", Return: " + method.getReturnType().getSimpleName() +
                            ", Params: " + Arrays.toString(Arrays.stream(method.getParameterTypes()).map(Type::getSimpleName).toArray(String[]::new)) +
                            ", Flags: " + Arrays.toString(method.getAccessFlags()) +
                            ", Attributes: " + Arrays.toString(method.getAttributeTypes()) +
                            ", Annotations: " + Arrays.toString(method.getAnnotationInfos())
            );

            for (var annotation : method.getAnnotations()) {
                System.out.println("Annotation: " + annotation);
            }
        }

    }

    @Test
    public void readLoadedClass() {
        var reader = new ClassReader(ClassLoader.getSystemClassLoader().getResourceAsStream(String.class.getName().replace(".", "/") + ".class"));
        var raw = reader.read();

        var wrap = ClassWrapper.newBuilder().build().wrap(raw);
        for (var method : wrap.getMethods()) {
            System.out.println(
                    "Method: " + method.getName() +
                            ", Return: " + method.getReturnType().getSimpleName() +
                            ", Params: " + Arrays.toString(Arrays.stream(method.getParameterTypes()).map(Type::getSimpleName).toArray(String[]::new)) +
                            ", Flags: " + Arrays.toString(method.getAccessFlags()) +
                            ", Attributes: " + Arrays.toString(method.getAttributeTypes()) +
                            ", Annotations: " + Arrays.toString(method.getAnnotationInfos())
            );
        }
    }

}
