package com.mjog.hello.service;

import org.springframework.stereotype.Service;

import com.mjog.hello.HelloWorldLibrary;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.WTypes;

@Service
public class HelloWorldService {

    public String hello(String input) {

        Ole32.INSTANCE.CoInitializeEx(null, Ole32.COINIT_MULTITHREADED);

        WTypes.BSTR bstr = OleAuto.INSTANCE.SysAllocString(input);
        WTypes.BSTRByReference bstrRef = new WTypes.BSTRByReference();
        bstrRef.setValue(bstr);

        try {
            HelloWorldLibrary.INSTANCE.HelloWorld(bstrRef);
            return bstrRef.getValue().getValue();
        } finally {
            Ole32.INSTANCE.CoUninitialize();
        }
    }
}