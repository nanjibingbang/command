package com.liou.scriptcall.info;

import java.io.InputStream;

public interface InfoSyncable {

    InputStream getInputStream();

    InputStream getErrorStream();

    void setMessagePipeline(MessagePipeline messagePipeline);

    MessagePipeline getMessagePipeline();

    /**
     * Causes the current thread to wait
     */
    int currentWait();
    
    String syncName();

}