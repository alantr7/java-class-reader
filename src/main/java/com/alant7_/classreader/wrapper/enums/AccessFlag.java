package com.alant7_.classreader.wrapper.enums;

import com.alant7_.classreader.io.ComponentType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum AccessFlag {


    /**
     * Declared public; may be accessed from outside its package
     */
    PUBLIC(0x0001, ComponentType.CLASS, ComponentType.FIELD, ComponentType.METHOD),

    /**
     * Declared private; accessible only within the defining class and other classes belonging to the same nest
     */
    PRIVATE(0x0002, ComponentType.CLASS, ComponentType.FIELD, ComponentType.METHOD),

    /**
     * Declared protected; may be accessed within subclasses.
     */
    PROTECTED(0x0004, ComponentType.CLASS, ComponentType.FIELD, ComponentType.METHOD),

    /**
     * Declared static.
     */
    STATIC(0x0008, ComponentType.CLASS, ComponentType.FIELD, ComponentType.METHOD),

    /**
     * Declared final, no subclasses allowed
     */
    FINAL(0x0010, ComponentType.CLASS, ComponentType.FIELD, ComponentType.METHOD),

    /**
     * Treat superclass methods specially when invoked by the "invokespecial" instruction
     */
    SUPER(0x0020, ComponentType.CLASS),

    /**
     * Declared synchronized; invocation is wrapped by a monitor use.
     */
    SYNCHRONIZED(0x0020, ComponentType.METHOD),

    /**
     * Declared volatile; cannot be cached.
     */
    VOLATILE(0x0040, ComponentType.FIELD),

    /**
     * A bridge method, generated by the compiler.
     */
    BRIDGE(0x0040, ComponentType.METHOD),

    /**
     * Declared transient; not written or read by a persistent object manager.
     */
    TRANSIENT(0x0080, ComponentType.FIELD),

    /**
     * Declared with variable number of arguments.
     */
    VARARGS(0x0080, ComponentType.METHOD),

    /**
     * Declared native; implemented in a language other than the Java programming language.
     */
    NATIVE(0x0100, ComponentType.METHOD),

    /**
     * Is an interface, not a class
     */
    INTERFACE(0x0200, ComponentType.CLASS),

    /**
     * Must not be instantiated
     */
    ABSTRACT(0x0400, ComponentType.CLASS, ComponentType.METHOD),

    /**
     * Declared strictfp; floating-point mode is FP-strict.
     */
    STRICT(0x0800, ComponentType.FIELD, ComponentType.METHOD),

    /**
     * Declared as synthetic, not present in the source code
     */
    SYNTHETIC(0x1000, ComponentType.CLASS, ComponentType.FIELD, ComponentType.METHOD),

    /**
     * Declared as an annotation type
     */
    ANNOTATION(0x2000, ComponentType.CLASS),

    /**
     * Declared as an enum type
     */
    ENUM(0x4000, ComponentType.CLASS, ComponentType.FIELD),

    /**
     * In a module, not a class or an interface
     */
    MODULE(0x8000, ComponentType.CLASS);

    @Getter
    private final int value;

    @Getter
    private final ComponentType[] targets;

    AccessFlag(int value, ComponentType... targets) {
        this.value = value;
        this.targets = targets;
    }

    private static final Map<ComponentType, AccessFlag[]> flagsGroupedByTarget = new HashMap<>();

    static {
        Map<ComponentType, List<AccessFlag>> flags = new HashMap<>();
        for (var flag : values()) {
            for (var target : flag.getTargets()) {
                flags.computeIfAbsent(target, v -> new ArrayList<>()).add(flag);
            }
        }

        flags.forEach((k, v) -> flagsGroupedByTarget.put(k, v.toArray(AccessFlag[]::new)));
    }

    public static AccessFlag[] values(ComponentType target) {
        return flagsGroupedByTarget.getOrDefault(target, new AccessFlag[0]);
    }

}
