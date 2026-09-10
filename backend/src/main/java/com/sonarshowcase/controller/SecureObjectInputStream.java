package com.sonarshowcase.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.List;

/**
 * Secure ObjectInputStream that only allows deserialization of pre-approved classes.
 * This prevents insecure deserialization attacks by filtering classes during deserialization.
 */
public class SecureObjectInputStream extends ObjectInputStream {

    private final List<String> approvedClasses;

    public SecureObjectInputStream(InputStream in, List<String> approvedClasses) throws IOException {
        super(in);
        this.approvedClasses = approvedClasses;
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass osc) throws IOException, ClassNotFoundException {
        if (!approvedClasses.contains(osc.getName())) {
            throw new InvalidClassException("Unauthorized deserialization", osc.getName());
        }
        return super.resolveClass(osc);
    }
}
