package com.sunmi.scanner;

interface IScanInterface {
    void sendKeyEvent(in KeyEvent key);
    void scan();
    void stop();
    int getScannerModel();
}