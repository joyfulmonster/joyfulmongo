package com.joyfulmongo.custom;

import com.joyfulmongo.controller.JFSingleDataInput;

public interface CustomInputPlugin {
    JFSingleDataInput process(JFSingleDataInput input);
}
