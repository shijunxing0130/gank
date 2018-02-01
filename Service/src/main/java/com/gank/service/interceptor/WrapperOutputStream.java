package com.gank.service.interceptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;

public class WrapperOutputStream extends ServletOutputStream{
    private ByteArrayOutputStream bos;

    public WrapperOutputStream(ByteArrayOutputStream bos) {
        this.bos = bos;
    }

    @Override
    public void write(int b) throws IOException {
        bos.write(b);
    }
}
