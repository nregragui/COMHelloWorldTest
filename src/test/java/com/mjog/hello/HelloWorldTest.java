package com.mjog.hello;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;

import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.WTypes;

public class HelloWorldTest {

    @Test
    public void testHelloWorldInOutRef() {

        assertEquals("x86", System.getProperty("os.arch"));

        Ole32.INSTANCE.CoInitializeEx(null, Ole32.COINIT_MULTITHREADED);

        WTypes.BSTR bstr = OleAuto.INSTANCE.SysAllocString("Clark");

        WTypes.BSTRByReference bstrRef = new WTypes.BSTRByReference();
        bstrRef.setValue(bstr);

        try {
            HelloWorldLibrary.INSTANCE.HelloWorld(bstrRef);
            
            String modifiedString = bstrRef.getValue().getValue();

            assertEquals("Hello Clark", modifiedString);

        } finally {
            Ole32.INSTANCE.CoUninitialize();
        }
    }

    @Test
    public void testHelloWorldVarientVarientInOut() {


        assertEquals("x86", System.getProperty("os.arch"));

        Ole32.INSTANCE.CoInitializeEx(null, Ole32.COINIT_MULTITHREADED);

        Variant.VARIANT inOnlyVariant = new Variant.VARIANT("Clark");     

        try {
            HelloWorldLibrary.INSTANCE.HelloWorldVar(inOnlyVariant);
            
            String modifiedString = inOnlyVariant.stringValue();

            assertEquals("Hello Clark", modifiedString);

        } finally {

            Ole32.INSTANCE.CoUninitialize();
        }
    }

    @Test
    public void testHelloWorldVariantRefInOutNoChange() {

        assertEquals("x86", System.getProperty("os.arch"));

        Ole32.INSTANCE.CoInitializeEx(null, Ole32.COINIT_MULTITHREADED);

        WTypes.BSTR bstr = OleAuto.INSTANCE.SysAllocString("Clark");

        // Create a BSTRByReference and set its value
        WTypes.BSTRByReference bstrRef = new WTypes.BSTRByReference();
        bstrRef.setValue(bstr);

        Variant.VARIANT inOutVariant = new Variant.VARIANT(bstrRef);     
        // Call the DLL method
        try {
            HelloWorldLibrary.INSTANCE.HelloWorldVar(inOutVariant);
            
            String UNMODIFIEDString = bstrRef.getValue().getValue();

            assertNotEquals("Hello Clark", UNMODIFIEDString);

        } finally {
            
            Ole32.INSTANCE.CoUninitialize();
        }
    }
}
