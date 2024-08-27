package com.mjog.hello;


import com.sun.jna.Native;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.win32.StdCallLibrary;

public interface HelloWorldLibrary extends StdCallLibrary {
    
    HelloWorldLibrary INSTANCE = (HelloWorldLibrary)
    Native.load("C:\\gitrepo\\COMHelloWorldTest\\HelloWorldCOM.dll", HelloWorldLibrary.class);


    void HelloWorld(WTypes.BSTRByReference inOutBstr);

    void HelloWorldVar(Variant.VARIANT inOutVar);

}


