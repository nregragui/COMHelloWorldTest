package com.mjog.hello;

import com.sun.jna.platform.win32.COM.COMLateBindingObject;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.WTypes;

public class HelloWorldExample extends COMLateBindingObject {

    public HelloWorldExample() {
        super("HelloWorldCOM.HelloWorld", false);
    }

    public void HelloWorld() {
        // Initialize the in/out string variable as a BSTR reference
        WTypes.BSTRByReference bstrRef = new WTypes.BSTRByReference();

        String name = "Clark";
        WTypes.BSTR bstr = OleAuto.INSTANCE.SysAllocString(name);
        bstrRef.setValue(bstr);

        // Wrap the BSTR reference in a VARIANT
        Variant.VARIANT var1 = new Variant.VARIANT(bstrRef);

        // Prepare the parameters array
        Variant.VARIANT[] params = new Variant.VARIANT[] { var1 };

        // Invoke the COM method
        //this.invoke("HelloWorld", params);

        // Retrieve the modified value
        String modifiedValue = bstrRef.getValue().getValue();
        System.out.println("Modified Value: " + modifiedValue);
    }

    public static void main(String[] args) {

       String arch = System.getProperty("os.arch");
        System.out.println("JVM Architecture: " + arch);


             // Initialize the COM library
             Ole32.INSTANCE.CoInitializeEx(null, Ole32.COINIT_MULTITHREADED);


        // try {
        //     new HelloWorldExample().HelloWorld();
        // } finally {
        //     // Uninitialize COM library
        //     Ole32.INSTANCE.CoUninitialize();
        // }



        //      WTypes.BSTR bstr = OleAuto.INSTANCE.SysAllocString("Clark");
            
        //      // Create a BSTRByReference and set its value
        //      WTypes.BSTRByReference bstrRef = new WTypes.BSTRByReference();
        //      bstrRef.setValue(bstr);
             

        //      // Create a Variant with the initial string value
        //      //Variant.VARIANT inOutVariant = new Variant.VARIANT(bstrRef);     
        //      // Call the DLL method
        // try{
        //      MyCOMLibrary.INSTANCE.HelloWorld(bstrRef);
     
        //      // Retrieve the modified value
        //     //  String modifiedString = inOutVariant.stringValue();
        //     //  System.out.println("Modified String: " + modifiedString);

        //     String modifiedString = bstrRef.getValue().getValue();
        //     System.out.println("Modified String: " + modifiedString);
        // }finally{
        //      // Uninitialize the COM library
        //      Ole32.INSTANCE.CoUninitialize();
        // }
    }
}
