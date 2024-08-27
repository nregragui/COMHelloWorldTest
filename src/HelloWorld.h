// HelloWorld.h
#pragma once

#include <windows.h>

extern "C" __declspec(dllexport) void __stdcall HelloWorldVar(VARIANT* input);
extern "C" __declspec(dllexport) void __stdcall HelloWorld(BSTR* input);
extern "C" __declspec(dllexport) HRESULT __stdcall DllRegisterServer();
extern "C" __declspec(dllexport) HRESULT __stdcall DllUnregisterServer();