#include "HelloWorld.h"
#include <oleauto.h>
#include <string>
#include <windows.h>
#include <winerror.h> // Include this header for HRESULT

#ifndef SELFREG_E_CLASS
#define SELFREG_E_CLASS 0x80004005L
#endif

extern "C" __declspec(dllexport) HRESULT __stdcall DllRegisterServer();
extern "C" __declspec(dllexport) HRESULT __stdcall DllUnregisterServer();
extern "C" __declspec(dllexport) void __stdcall HelloWorld(BSTR* input);
extern "C" __declspec(dllexport) void __stdcall HelloWorldVar(VARIANT* input);


HMODULE g_hModule = NULL;

void __stdcall HelloWorld(BSTR* input) {
    // Convert BSTR to a wide string
    std::wstring original(*input, SysStringLen(*input));

    // Prepend "Hello " to the original string
    std::wstring result = L"Hello " + original;

    // Free the original BSTR
    SysFreeString(*input);

    // Allocate a new BSTR for the result
    *input = SysAllocString(result.c_str());
}

extern "C" __declspec(dllexport) void __stdcall HelloWorldVar(VARIANT* input) {
    if (input && input->vt == VT_BSTR) {
        // Convert BSTR to a wide string
        std::wstring original(input->bstrVal, SysStringLen(input->bstrVal));

        // Prepend "Hello " to the original string
        std::wstring result = L"Hello " + original;

        // Free the original BSTR
        SysFreeString(input->bstrVal);

        // Allocate a new BSTR for the result
        input->bstrVal = SysAllocString(result.c_str());
    }
}

BOOL APIENTRY DllMain(HMODULE hModule, DWORD  ul_reason_for_call, LPVOID lpReserved) {
    if (ul_reason_for_call == DLL_PROCESS_ATTACH) {
        g_hModule = hModule;
    }
    return TRUE;
}

HRESULT __stdcall DllRegisterServer() {
    HKEY hKey;
    LONG lResult;
    WCHAR szModule[MAX_PATH];
    DWORD dwPathLen = 0;

    // Get the DLL's file path
    dwPathLen = GetModuleFileNameW(g_hModule, szModule, MAX_PATH);
    if (dwPathLen == 0) {
        return SELFREG_E_CLASS;
    }

    // Create the CLSID key
    lResult = RegCreateKeyExW(HKEY_CLASSES_ROOT, L"CLSID\\{fde118c6-a6e6-433a-b572-be9c32b6f5b9}", 0, NULL, REG_OPTION_NON_VOLATILE, KEY_WRITE, NULL, &hKey, NULL);
    if (lResult != ERROR_SUCCESS) {
        return SELFREG_E_CLASS;
    }

    // Set the default value of the CLSID key
    lResult = RegSetValueExW(hKey, NULL, 0, REG_SZ, (const BYTE*)L"HelloWorldCOM Class", (DWORD)(wcslen(L"HelloWorldCOM Class") + 1) * sizeof(WCHAR));
    if (lResult != ERROR_SUCCESS) {
        RegCloseKey(hKey);
        return SELFREG_E_CLASS;
    }

    // Create the InProcServer32 key
    HKEY hSubKey;
    lResult = RegCreateKeyExW(hKey, L"InProcServer32", 0, NULL, REG_OPTION_NON_VOLATILE, KEY_WRITE, NULL, &hSubKey, NULL);
    if (lResult != ERROR_SUCCESS) {
        RegCloseKey(hKey);
        return SELFREG_E_CLASS;
    }

    // Set the default value of the InProcServer32 key to the DLL's path
    lResult = RegSetValueExW(hSubKey, NULL, 0, REG_SZ, (const BYTE*)szModule, (DWORD)(wcslen(szModule) + 1) * sizeof(WCHAR));
    if (lResult != ERROR_SUCCESS) {
        RegCloseKey(hSubKey);
        RegCloseKey(hKey);
        return SELFREG_E_CLASS;
    }

    // Set the threading model to "Apartment"
    lResult = RegSetValueExW(hSubKey, L"ThreadingModel", 0, REG_SZ, (const BYTE*)L"Apartment", (DWORD)(wcslen(L"Apartment") + 1) * sizeof(WCHAR));
    if (lResult != ERROR_SUCCESS) {
        RegCloseKey(hSubKey);
        RegCloseKey(hKey);
        return SELFREG_E_CLASS;
    }

    RegCloseKey(hSubKey);
    RegCloseKey(hKey);

    // Register the ProgID
    lResult = RegCreateKeyExW(HKEY_CLASSES_ROOT, L"HelloWorldCOM.HelloWorld", 0, NULL, REG_OPTION_NON_VOLATILE, KEY_WRITE, NULL, &hKey, NULL);
    if (lResult != ERROR_SUCCESS) {
        return SELFREG_E_CLASS;
    }

    lResult = RegSetValueExW(hKey, NULL, 0, REG_SZ, (const BYTE*)L"HelloWorldCOM Class", (DWORD)(wcslen(L"HelloWorldCOM Class") + 1) * sizeof(WCHAR));
    if (lResult != ERROR_SUCCESS) {
        RegCloseKey(hKey);
        return SELFREG_E_CLASS;
    }

    lResult = RegCreateKeyExW(hKey, L"CLSID", 0, NULL, REG_OPTION_NON_VOLATILE, KEY_WRITE, NULL, &hSubKey, NULL);
    if (lResult != ERROR_SUCCESS) {
        RegCloseKey(hKey);
        return SELFREG_E_CLASS;
    }

    lResult = RegSetValueExW(hSubKey, NULL, 0, REG_SZ, (const BYTE*)L"{fde118c6-a6e6-433a-b572-be9c32b6f5b9}", (DWORD)(wcslen(L"{fde118c6-a6e6-433a-b572-be9c32b6f5b9}") + 1) * sizeof(WCHAR));
    if (lResult != ERROR_SUCCESS) {
        RegCloseKey(hSubKey);
        RegCloseKey(hKey);
        return SELFREG_E_CLASS;
    }

    RegCloseKey(hSubKey);
    RegCloseKey(hKey);

    return S_OK;
}

HRESULT __stdcall DllUnregisterServer() {
    // Delete the CLSID key
    HKEY hKey;
    LONG lResult = RegOpenKeyExW(HKEY_CLASSES_ROOT, L"CLSID", 0, KEY_WRITE, &hKey);
    if (lResult == ERROR_SUCCESS) {
        RegDeleteKeyW(hKey, L"{fde118c6-a6e6-433a-b572-be9c32b6f5b9}\\InProcServer32");
        RegDeleteKeyW(hKey, L"{fde118c6-a6e6-433a-b572-be9c32b6f5b9}");
        RegCloseKey(hKey);
    }
    return S_OK;
}
